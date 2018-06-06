package com.cheatbreaker.api;

import com.cheatbreaker.api.event.PlayerRegisterCBEvent;
import com.cheatbreaker.api.event.PlayerUnregisterCBEvent;
import com.cheatbreaker.api.net.CBNetHandler;
import com.cheatbreaker.api.net.CBNetHandlerImpl;
import com.cheatbreaker.api.net.event.CBPacketReceivedEvent;
import com.cheatbreaker.api.net.event.CBPacketSentEvent;
import com.cheatbreaker.api.object.*;
import com.cheatbreaker.api.voice.VoiceChannel;
import com.cheatbreaker.nethandler.CBPacket;
import com.cheatbreaker.nethandler.obj.ServerRule;
import com.cheatbreaker.nethandler.server.*;
import com.cheatbreaker.nethandler.shared.CBPacketAddWaypoint;
import com.cheatbreaker.nethandler.shared.CBPacketRemoveWaypoint;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CheatBreakerAPI extends JavaPlugin implements Listener {

    private static final String MESSAGE_CHANNEL = "CB-Client";

    @Getter private static CheatBreakerAPI instance;
    private final Set<UUID> playersRunningCheatBreaker = new HashSet<>();

    private final Set<UUID> playersNotRegistered = new HashSet<>();

    @Setter private CBNetHandler netHandlerServer = new CBNetHandlerImpl();

    private boolean voiceEnabled;

    @Getter private List<VoiceChannel> voiceChannels = new ArrayList<>();

    @Getter private final Map<UUID, VoiceChannel> playerActiveChannels = new HashMap<>();

    private final Map<UUID, List<CBPacket>> packetQueue = new HashMap<>();

    private final Map<UUID, List<UUID>> muteMap = new HashMap<>();

    private final Map<UUID, Function<World, String>> worldIdentifiers = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Messenger messenger = getServer().getMessenger();

        messenger.registerOutgoingPluginChannel(this, MESSAGE_CHANNEL);
        messenger.registerIncomingPluginChannel(this, MESSAGE_CHANNEL, (channel, player, bytes) -> {
            CBPacket packet = CBPacket.handle(netHandlerServer, bytes, player);
            CBPacketReceivedEvent event;
            Bukkit.getPluginManager().callEvent(event = new CBPacketReceivedEvent(player, packet));
            if (!event.isCancelled()) {
                packet.process(netHandlerServer);
            }
        });

        getServer().getPluginManager().registerEvents(
                new Listener() {

                    @EventHandler
                    public void onRegister(PlayerRegisterChannelEvent event) {
                        if (!event.getChannel().equals(MESSAGE_CHANNEL)) {
                            return;
                        }

                        playersNotRegistered.remove(event.getPlayer().getUniqueId());
                        playersRunningCheatBreaker.add(event.getPlayer().getUniqueId());

                        muteMap.put(event.getPlayer().getUniqueId(), new ArrayList<>());

                        if (voiceEnabled) {
                            sendPacket(event.getPlayer(), new CBPacketServerRule(ServerRule.VOICE_ENABLED, true));
                        }

                        if (packetQueue.containsKey(event.getPlayer().getUniqueId())) {
                            packetQueue.get(event.getPlayer().getUniqueId()).forEach(p -> {
                                sendPacket(event.getPlayer(), p);
                            });

                            packetQueue.remove(event.getPlayer().getUniqueId());
                        }

                        getServer().getPluginManager().callEvent(new PlayerRegisterCBEvent(event.getPlayer()));
                        updateWorld(event.getPlayer());
                    }

                    @EventHandler
                    public void onUnregister(PlayerUnregisterChannelEvent event) {
                        if (event.getChannel().equals(MESSAGE_CHANNEL)) {
                            playersRunningCheatBreaker.remove(event.getPlayer().getUniqueId());
                            playerActiveChannels.remove(event.getPlayer().getUniqueId());
                            muteMap.remove(event.getPlayer().getUniqueId());

                            getServer().getPluginManager().callEvent(new PlayerUnregisterCBEvent(event.getPlayer()));
                        }
                    }

                    @EventHandler
                    public void onUnregister(PlayerQuitEvent event) {
                        getPlayerChannels(event.getPlayer()).forEach(channel -> channel.removePlayer(event.getPlayer()));

                        playersRunningCheatBreaker.remove(event.getPlayer().getUniqueId());
                        playersNotRegistered.remove(event.getPlayer().getUniqueId());
                        playerActiveChannels.remove(event.getPlayer().getUniqueId());
                        muteMap.remove(event.getPlayer().getUniqueId());
                    }

                    @EventHandler(priority = EventPriority.LOWEST)
                    public void onJoin(PlayerJoinEvent event) {
                        Bukkit.getScheduler().runTaskLater(instance, () -> {
                            if (!isRunningCheatBreaker(event.getPlayer())) {
                                playersNotRegistered.add(event.getPlayer().getUniqueId());
                                packetQueue.remove(event.getPlayer().getUniqueId());
                            }
                        }, 2 * 20L);
                    }

                    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
                    public void onWorldChange(PlayerChangedWorldEvent event) {
                        updateWorld(event.getPlayer());
                    }

                    private void updateWorld(Player player) {
                        String worldIdentifier = getWorldIdentifier(player.getWorld());

                        sendPacket(player, new CBPacketUpdateWorld(worldIdentifier));
                    }

                }
        , this);
    }

    public String getWorldIdentifier(World world) {
        String worldIdentifier = world.getUID().toString();

        if (worldIdentifiers.containsKey(world.getUID())) {
            worldIdentifier = worldIdentifiers.get(world.getUID()).apply(world);
        }

        return worldIdentifier;
    }

    public void registerWorldIdentifier(World world, Function<World, String> identifier) {
        worldIdentifiers.put(world.getUID(), identifier);
    }

    public boolean isRunningCheatBreaker(Player player) {
        return isRunningCheatBreaker(player.getUniqueId());
    }

    public boolean isRunningCheatBreaker(UUID playerUuid) {
        return playersRunningCheatBreaker.contains(playerUuid);
    }

    public Set<Player> getPlayersRunningCheatBreaker() {
        return ImmutableSet.copyOf(playersRunningCheatBreaker.stream().map(Bukkit::getPlayer).collect(Collectors.toSet()));
    }

    public void isCheatBreakerBanned(UUID playerUuid, Consumer<Boolean> resultListener) {
        resultListener.accept(false);
    }

    public void sendNotification(Player player, CBNotification notification) {
        sendPacket(player, new CBPacketNotification(
                notification.getMessage(),
                notification.getDurationMs(),
                notification.getLevel().name()
        ));
    }

    public void sendNotificationOrFallback(Player player, CBNotification notification, Runnable fallback) {
        if (isRunningCheatBreaker(player)) {
            sendNotification(player, notification);
        } else {
            fallback.run();
        }
    }

    public void setStaffModuleState(Player player, StaffModule module, boolean state) {
        sendPacket(player, new CBPacketStaffModState(module.name(), state));
    }

    public void setMinimapStatus(Player player, MinimapStatus status) {
        sendPacket(player, new CBPacketServerRule(ServerRule.MINIMAP_STATUS, status.name()));
    }

    public void setCompetitiveGame(Player player, boolean isCompetitive) {
        sendPacket(player, new CBPacketServerRule(ServerRule.COMPETITIVE_GAMEMODE, isCompetitive));
    }

    public void giveAllStaffModules(Player player) {
        for (StaffModule module : StaffModule.values()) {
            CheatBreakerAPI.getInstance().setStaffModuleState(player, module, true);
        }

        sendNotification(player, new CBNotification("Staff modules enabled", 3, TimeUnit.SECONDS));
    }

    public void disableAllStaffModules(Player player) {
        for (StaffModule module : StaffModule.values()) {
            CheatBreakerAPI.getInstance().setStaffModuleState(player, module, false);
        }

        sendNotification(player, new CBNotification("Staff modules disabled", 3, TimeUnit.SECONDS));
    }

    public void sendTeammates(Player player, CBPacketTeammates packet) {
        validatePlayers(player, packet);
        sendPacket(player, packet);
    }

    public void validatePlayers(Player sendingTo, CBPacketTeammates packet) {
        packet.getPlayers().entrySet().removeIf(entry -> Bukkit.getPlayer(entry.getKey()) != null && !Bukkit.getPlayer(entry.getKey()).getWorld().equals(sendingTo.getWorld()));
    }

    public void addHologram(Player player, UUID id, Vector position, String[] lines) {
        sendPacket(player, new CBPacketAddHologram(id, position.getX(), position.getY(), position.getZ(), Arrays.asList(lines)));
    }

    public void updateHologram(Player player, UUID id, String[] lines) {
        sendPacket(player, new CBPacketUpdateHologram(id, Arrays.asList(lines)));
    }

    public void removeHologram(Player player, UUID id) {
        sendPacket(player, new CBPacketRemoveHologram(id));
    }

    public void overrideNametag(LivingEntity target, List<String> nametag, Player viewer) {
        sendPacket(viewer, new CBPacketOverrideNametags(target.getEntityId(), nametag));
    }

    public void resetNametag(LivingEntity target, Player viewer) {
        sendPacket(viewer, new CBPacketOverrideNametags(target.getEntityId(), null));
    }

    public void hideNametag(LivingEntity target, Player viewer) {
        sendPacket(viewer, new CBPacketOverrideNametags(target.getEntityId(), ImmutableList.of()));
    }

    public void sendTitle(Player player, TitleType type, String message, Duration displayTime) {
        sendTitle(player, type, message, Duration.ofMillis(500), displayTime, Duration.ofMillis(500));
    }

    public void sendTitle(Player player, TitleType type, String message, Duration displayTime, float scale) {
        sendTitle(player, type, message, Duration.ofMillis(500), displayTime, Duration.ofMillis(500), scale);
    }

    public void sendTitle(Player player, TitleType type, String message, Duration fadeInTime, Duration displayTime, Duration fadeOutTime) {
        sendTitle(player, type, message, fadeInTime, displayTime, fadeOutTime, 1F);
    }

    public void sendTitle(Player player, TitleType type, String message, Duration fadeInTime, Duration displayTime, Duration fadeOutTime, float scale) {
        sendPacket(player, new CBPacketTitle(type.name().toLowerCase(), message, scale, displayTime.toMillis(), fadeInTime.toMillis(), fadeOutTime.toMillis()));
    }

    public void sendWaypoint(Player player, CBWaypoint waypoint) {
        sendPacket(player, new CBPacketAddWaypoint(
                waypoint.getName(),
                waypoint.getWorld(),
                waypoint.getColor(),
                waypoint.getX(),
                waypoint.getY(),
                waypoint.getZ(),
                waypoint.isForced(),
                waypoint.isVisible()
        ));
    }

    public void removeWaypoint(Player player, CBWaypoint waypoint) {
        sendPacket(player, new CBPacketRemoveWaypoint(
                waypoint.getName(),
                waypoint.getWorld()
        ));
    }

    public void sendCooldown(Player player, CBCooldown cooldown) {
        sendPacket(player, new CBPacketCooldown(cooldown.getMessage(), cooldown.getDurationMs(), cooldown.getIcon().getId()));
    }

    public void clearCooldown(Player player, CBCooldown cooldown) {
        sendPacket(player, new CBPacketCooldown(cooldown.getMessage(), 0L, cooldown.getIcon().getId()));
    }

    public void voiceEnabled(boolean enabled) {
        voiceEnabled = enabled;
    }

    public void createVoiceChannels(VoiceChannel... voiceChannels) {
        this.voiceChannels.addAll(Arrays.asList(voiceChannels));
        for (VoiceChannel channel : voiceChannels) {
            for (Player player : channel.getPlayersInChannel()) {
                sendVoiceChannel(player, channel);
            }
        }
    }

    public void deleteVoiceChannel(VoiceChannel channel) {
        this.voiceChannels.removeIf(c -> {
            boolean remove = c == channel;
            if (remove) {
                channel.validatePlayers();
                for (Player player : channel.getPlayersInChannel()) {
                    sendPacket(player, new CBPacketDeleteVoiceChannel(channel.getUuid()));
                    if (getPlayerActiveChannels().get(player.getUniqueId()) == channel) {
                        getPlayerActiveChannels().remove(player);
                    }
                }
            }
            return remove;
        });
    }

    public void deleteVoiceChannel(UUID channelUUID) {
        getChannel(channelUUID).ifPresent(c -> deleteVoiceChannel(c));
    }

    public List<VoiceChannel> getPlayerChannels(Player player) {
        return this.voiceChannels.stream().filter(channel -> channel.hasPlayer(player)).collect(Collectors.toList());
    }

    public void sendVoiceChannel(Player player, VoiceChannel channel) {
        channel.validatePlayers();
        sendPacket(player, new CBPacketVoiceChannel(channel.getUuid(), channel.getName(), channel.toPlayersMap(), channel.toListeningMap()));
    }

    public void setActiveChannel(Player player, UUID uuid) {
        getChannel(uuid).ifPresent(channel -> setActiveChannel(player, channel));
    }

    public Optional<VoiceChannel> getChannel(UUID uuid) {
        return voiceChannels.stream().filter(channel -> channel.getUuid().equals(uuid)).findFirst();
    }

    public void setActiveChannel(Player player, VoiceChannel channel) {
        channel.setActive(player);
    }

    public void toggleVoiceMute(Player player, UUID other) {
        if (!muteMap.get(player.getUniqueId()).removeIf(uuid -> uuid.equals(other))) {
            muteMap.get(player.getUniqueId()).add(other);
        }
    }

    public boolean playerHasPlayerMuted(Player player, Player other) {
        return muteMap.get(other.getUniqueId()).contains(player.getUniqueId());
    }

    /*
    *  This is a boolean to indicate whether or not a CB message was sent.
    *  An example use-case is when you want to send a CheatBreaker
    *  notification if a player is running CheatBreaker, and a chat
    *  message if not.
    */
    public boolean sendPacket(Player player, CBPacket packet) {
        if (isRunningCheatBreaker(player)) {
            player.sendPluginMessage(this, MESSAGE_CHANNEL, CBPacket.getPacketData(packet));
            Bukkit.getPluginManager().callEvent(new CBPacketSentEvent(player, packet));
            return true;
        } else if (!playersNotRegistered.contains(player.getUniqueId())) {
            packetQueue.putIfAbsent(player.getUniqueId(), new ArrayList<>());
            packetQueue.get(player.getUniqueId()).add(packet);
            return false;
        }
        return false;
    }

}