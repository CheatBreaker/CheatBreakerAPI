package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public final class TeammatesMessage extends CBMessage {

    private final Map<UUID, Map<String, Double>> teamMembers = new HashMap<>(); // uuid -> location
    private final UUID leaderUuid; // can be null (leader is shown as blue while other teammates are green)
    private final long lastMs; // how long this should last on the player's screen

    public TeammatesMessage(Player leader, long lastMs) {
        super("TeamUpdate");

        this.leaderUuid = leader == null ? null : leader.getUniqueId();
        this.lastMs = lastMs;
    }

    public TeammatesMessage addPlayer(Player player) {
        teamMembers.put(player.getUniqueId(), ImmutableMap.of(
                "x", player.getLocation().getX(),
                "y", player.getLocation().getY(),
                "z", player.getLocation().getZ()
        ));
        return this;
    }

    public void validatePlayers(Player sendingTo) {
        teamMembers.entrySet().removeIf(entry -> Bukkit.getPlayer(entry.getKey()) != null && !Bukkit.getPlayer(entry.getKey()).getWorld().equals(sendingTo.getWorld()));
    }

}
