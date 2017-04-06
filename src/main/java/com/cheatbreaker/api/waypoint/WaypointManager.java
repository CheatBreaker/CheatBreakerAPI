package com.cheatbreaker.api.waypoint;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.message.AddWaypointMessage;
import com.cheatbreaker.api.message.RemoveWaypointMessage;
import com.cheatbreaker.api.object.CBWaypoint;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public final class WaypointManager {

    private final Map<UUID, Set<CBWaypoint>> waypoints = new HashMap<>();

    public void sendWaypoint(Player player, String displayName, Location location) {
        CBWaypoint waypoint = new CBWaypoint(displayName, location, false);

        CheatBreakerAPI.getInstance().sendMessage(player, new AddWaypointMessage(waypoint));

        waypoints.putIfAbsent(player.getUniqueId(), new HashSet<>());
        waypoints.get(player.getUniqueId()).add(waypoint);
    }

    public void sendForcedWaypoint(Player player, String displayName, Location location) {
        CBWaypoint waypoint = new CBWaypoint(displayName, location, true);

        CheatBreakerAPI.getInstance().sendMessage(player, new AddWaypointMessage(waypoint));

        waypoints.putIfAbsent(player.getUniqueId(), new HashSet<>());
        waypoints.get(player.getUniqueId()).add(waypoint);
    }

    public Set<CBWaypoint> getWaypoints(Player player) {
        if (!waypoints.containsKey(player.getUniqueId())) {
            return ImmutableSet.of();
        }

        return ImmutableSet.copyOf(waypoints.get(player.getUniqueId()));
    }

    public void deleteWaypoint(Player player, CBWaypoint waypoint) {
        CheatBreakerAPI.getInstance().sendMessage(player, new RemoveWaypointMessage(waypoint));

        waypoints.get(player.getUniqueId()).remove(waypoint);
    }

}
