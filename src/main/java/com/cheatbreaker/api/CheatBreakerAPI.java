package com.cheatbreaker.api;

import com.cheatbreaker.api.message.*;
import com.cheatbreaker.api.object.CBNotification;
import com.cheatbreaker.api.waypoint.WaypointManager;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class CheatBreakerAPI extends JavaPlugin implements Listener {

    private static final String MESSAGE_CHANNEL = "CB-Client";

    @Getter private static CheatBreakerAPI instance;
    private final Set<UUID> playersRunningCheatBreaker = new HashSet<>();

    @Getter private WaypointManager waypointManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.waypointManager = new WaypointManager();

        Messenger messenger = getServer().getMessenger();

        messenger.registerOutgoingPluginChannel(this, MESSAGE_CHANNEL);
        messenger.registerIncomingPluginChannel(this, MESSAGE_CHANNEL, (channel, player, bytes) -> {

        });

        getServer().getPluginManager().registerEvents(
                new Listener() {

                    @EventHandler
                    public void onRegister(PlayerRegisterChannelEvent event) {
                        if (event.getChannel().equals(MESSAGE_CHANNEL)) {
                            playersRunningCheatBreaker.add(event.getPlayer().getUniqueId());
                        }
                    }

                    @EventHandler
                    public void onUnregister(PlayerUnregisterChannelEvent event) {
                        if (event.getChannel().equals(MESSAGE_CHANNEL)) {
                            playersRunningCheatBreaker.remove(event.getPlayer().getUniqueId());
                        }
                    }

                    @EventHandler
                    public void onUnregister(PlayerQuitEvent event) {
                        playersRunningCheatBreaker.remove(event.getPlayer().getUniqueId());
                    }

                }
        , this);
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
        sendMessage(player, new SendNotificationMessage(notification));
    }

    public void sendNotificationOrFallback(Player player, CBNotification notification, Runnable fallback) {
        if (isRunningCheatBreaker(player)) {
            sendNotification(player, notification);
        } else {
            fallback.run();
        }
    }

    public void setStaffModuleState(Player player, StaffModuleStateMessage.StaffModule module, boolean state) {
        sendMessage(player, new StaffModuleStateMessage(module, state));
    }

    public void setMinimapStatus(Player player, MinimapStatusMessage.MinimapStatus status) {
        sendMessage(player, new MinimapStatusMessage(status));
    }

    public void giveAllStaffModules(Player player) {
        for (StaffModuleStateMessage.StaffModule module : StaffModuleStateMessage.StaffModule.values()) {
            CheatBreakerAPI.getInstance().setStaffModuleState(player, module, true);
        }

        sendNotification(player, new CBNotification("Staff modules enabled", 3, TimeUnit.SECONDS));
    }

    public void disableAllStaffModules(Player player) {
        for (StaffModuleStateMessage.StaffModule module : StaffModuleStateMessage.StaffModule.values()) {
            CheatBreakerAPI.getInstance().setStaffModuleState(player, module, false);
        }

        sendNotification(player, new CBNotification("Staff modules disabled", 3, TimeUnit.SECONDS));
    }

    public void sendTeammates(Player player, TeammatesMessage message) {
        message.validatePlayers(player);
        sendMessage(player, message);
    }

    public void sendMessage(Player player, CBMessage message) {
        Map<Object, Object> data = new HashMap<>();
        data.put("action", message.getAction());
        data.putAll(message.toMap());

        player.sendPluginMessage(this, MESSAGE_CHANNEL, toJson(data).getBytes());
    }

    @SuppressWarnings("unchecked")
    private String toJson(Map<Object, Object> map) {
        String json = "";

        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            if (json.length() != 0) json += ",";

            if (entry.getValue() instanceof Map) {
                json += entry.getKey() + ":" + toJson((Map) entry.getValue());
            } else {
                json += entry.getKey() + ":\"" + entry.getValue() + "\"";
            }
        }

        return "{" + json + "}";
    }

}