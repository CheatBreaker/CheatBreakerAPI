package net.cheatbreaker.api;

import com.google.common.base.Preconditions;

import org.bukkit.Location;

import lombok.Getter;

public final class CBWaypoint {

    @Getter private final String name;
    @Getter private final String domain;
    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int z;
    @Getter private final boolean forced;

    public CBWaypoint(String name, Location location, boolean forced) {
        this(
            name,
            // when adding a waypoint from a Location we assume the user doesn't want
            // to do anything fancy and that they just want to bind it to the world
            // (this world UUID should be unique across all servers)
            "world." + location.getWorld().getUID().toString(),
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ(),
            forced
        );
    }

    public CBWaypoint(String name, String domain, int x, int y, int z, boolean forced) {
        this.name = Preconditions.checkNotNull(name, "name");
        this.domain = Preconditions.checkNotNull(domain, "domain");
        this.x = x;
        this.y = y;
        this.z = z;
        this.forced = forced;
    }

}