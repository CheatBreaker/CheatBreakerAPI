package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTeammatesMessage implements CBMessage {

    private final Map<UUID, Map<String, Double>> players = new HashMap<>(); // uuid string -> location
    private final UUID leader; // can be null (leader is shown as blue while other teammates are green)
    private final long lastMs; // how long this should last on the player's screen

    public AddTeammatesMessage(Player leader, long lastMs) {
        this.leader = leader == null ? null : leader.getUniqueId();
        this.lastMs = lastMs;
    }

    public AddTeammatesMessage addPlayer(Player player) {
        players.put(player.getUniqueId(), ImmutableMap.of(
                "x", player.getLocation().getX(),
                "y", player.getLocation().getY(),
                "z", player.getLocation().getZ()
        ));
        return this;
    }

    @Override
    public String getAction() {
        return "addteammates";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "uuid-list", players,
                "leader", leader,
                "lastMs", lastMs
        );
    }

}
