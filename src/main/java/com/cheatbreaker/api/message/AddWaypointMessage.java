package com.cheatbreaker.api.message;

import com.cheatbreaker.api.object.CBWaypoint;
import lombok.Getter;

@Getter
public final class AddWaypointMessage extends CBMessage {

    private final String name;
    private final double posX, posY, posZ;
    private final int dimension;

    public AddWaypointMessage(CBWaypoint waypoint) {
        super("AddWaypoint");
        this.name = waypoint.getName();
        this.posX = waypoint.getX();
        this.posY = waypoint.getY();
        this.posZ = waypoint.getZ();
        this.dimension = waypoint.getDimension();
    }

}