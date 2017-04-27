package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeammatesMessage implements CBMessage {

    private final Map<UUID, Map<String, Double>> players = new HashMap<>(); // uuid -> location
    private final UUID leader; // can be null (leader is shown as blue while other teammates are green)
    private final long lastMs; // how long this should last on the player's screen

    public TeammatesMessage(Player leader, long lastMs) {
        this.leader = leader == null ? null : leader.getUniqueId();
        this.lastMs = lastMs;
    }

    public TeammatesMessage addPlayer(Player player) {
        players.put(player.getUniqueId(), ImmutableMap.of(
                "x", player.getLocation().getX(),
                "y", player.getLocation().getY(),
                "z", player.getLocation().getZ()
        ));
        return this;
    }

    public void validatePlayers(Player sendingTo) {
        players.entrySet().removeIf(entry -> Bukkit.getPlayer(entry.getKey()) != null && !Bukkit.getPlayer(entry.getKey()).getWorld().equals(sendingTo.getWorld()));
    }

    @Override
    public String getAction() {
        return "TeamUpdate";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("teamMembers", players);
        map.put("leaderUuid", leader);
        map.put("lastMs", lastMs);

        return map;
    }

}
