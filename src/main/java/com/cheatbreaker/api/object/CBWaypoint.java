package com.cheatbreaker.api.object;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class CBWaypoint {

    @Getter private final String name;
    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int z;
    @Getter private final int dimension;
    @Getter private final boolean forced;

    public CBWaypoint(String name, Location location, boolean forced) {
        this(
            name,
            // when adding a waypoint from a Location we assume the user doesn't want
            // to do anything fancy and that they just want to bind it to the world
            // (this world UUID should be unique across all servers)
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ(),
            Bukkit.getWorlds().indexOf(location.getWorld()),
            forced
        );
    }

    public CBWaypoint(String name, int x, int y, int z, int dimension, boolean forced) {
        this.name = Preconditions.checkNotNull(name, "name");
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.forced = forced;
    }

}