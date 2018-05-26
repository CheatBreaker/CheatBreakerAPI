package com.cheatbreaker.api.voice;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.nethandler.server.CBPacketDeleteVoiceChannel;
import com.cheatbreaker.nethandler.server.CBPacketVoiceChannelUpdate;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class VoiceChannel
{
    private final String name;

    private final UUID uuid;

    private final List<Player> playersInChannel = new ArrayList<>();

    private final List<Player> playersListening = new ArrayList<>();

    public VoiceChannel(String name)
    {
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public void addPlayer(Player player)
    {
        if (hasPlayer(player)) return;

        for (Player player1 : playersInChannel)
        {
            CheatBreakerAPI.getInstance().sendMessage(player1, new CBPacketVoiceChannelUpdate(0, uuid, player.getUniqueId(), player.getDisplayName()));
        }

        playersInChannel.add(player);
        CheatBreakerAPI.getInstance().sendVoiceChannel(player, this);
    }

    public boolean removePlayer(Player player)
    {
        if (!hasPlayer(player)) return false;

        for (Player player1 : playersInChannel)
        {
            if (player1 == player) continue;
            CheatBreakerAPI.getInstance().sendMessage(player1, new CBPacketVoiceChannelUpdate(1, uuid, player.getUniqueId(), player.getDisplayName()));
        }

        CheatBreakerAPI.getInstance().sendMessage(player, new CBPacketDeleteVoiceChannel(uuid));
        CheatBreakerAPI.getInstance().getPlayerActiveChannels().remove(player.getUniqueId());

        playersListening.removeIf(player1 -> player1 == player);
        return playersInChannel.removeIf(player1 -> player1 == player);
    }

    private boolean addListening(Player player)
    {
        if (!hasPlayer(player) || isListening(player)) return false;

        playersListening.add(player);

        for (Player player1 : playersInChannel)
        {
            CheatBreakerAPI.getInstance().sendMessage(player1, new CBPacketVoiceChannelUpdate(2, uuid, player.getUniqueId(), player.getDisplayName()));
        }

        return true;
    }

    private boolean removeListening(Player player)
    {
        if (!isListening(player)) return false;

        for (Player player1 : playersInChannel)
        {
            if (player1 == player) continue;
            CheatBreakerAPI.getInstance().sendMessage(player1, new CBPacketVoiceChannelUpdate(3, uuid, player.getUniqueId(), player.getDisplayName()));
        }

        return playersListening.removeIf(player1 -> player1 == player);
    }

    public void setActive(Player player)
    {
        CheatBreakerAPI api = CheatBreakerAPI.getInstance();
        Optional.ofNullable(api.getPlayerActiveChannels().get(player.getUniqueId())).ifPresent(c -> {
            if (c != this) c.removeListening(player);
        });
        if (addListening(player)) {
            api.getPlayerActiveChannels().put(player.getUniqueId(), this);
        }
    }

    public boolean validatePlayers()
    {
        return playersInChannel.removeIf(Objects::isNull) || playersListening.removeIf(player -> !playersInChannel.contains(player));
    }

    public boolean hasPlayer(Player player)
    {
        return playersInChannel.contains(player);
    }

    public boolean isListening(Player player)
    {
        return playersListening.contains(player);
    }

    /**
     * Convert this to the map that will be sent over the net channel
     */
    public Map<UUID, String> toPlayersMap()
    {
        return playersInChannel.stream()
                .collect(Collectors.toMap(
                        Player::getUniqueId,
                        Player::getDisplayName
                ));
    }

    /**
     * Convert this to the map that will be sent over the net channel
     */
    public Map<UUID, String> toListeningMap()
    {
        return playersListening.stream()
                .collect(Collectors.toMap(
                        Player::getUniqueId,
                        Player::getDisplayName
                ));
    }
}
