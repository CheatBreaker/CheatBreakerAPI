package com.cheatbreaker.api.object;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@EqualsAndHashCode
public final class CBWaypoint {

    @Getter private final String name;
    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int z;
    @Getter private final String world;
    @Getter private final int color;
    @Getter private final boolean forced;

    public CBWaypoint(String name, Location location, int color, boolean forced) {
        this(
            name,
            // when adding a waypoint from a Location we assume the user doesn't want
            // to do anything fancy and that they just want to bind it to the world
            // (this world UUID should be unique across all servers)
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ(),
            CheatBreakerAPI.getInstance().getWorldIdentifier(location.getWorld()),
            color,
            forced
        );
    }

    public CBWaypoint(String name, int x, int y, int z, String world, int color, boolean forced) {
        this.name = Preconditions.checkNotNull(name, "name");
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.color = color;
        this.forced = forced;
    }

}