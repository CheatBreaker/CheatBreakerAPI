package com.cheatbreaker.api.message;

import com.cheatbreaker.api.object.CBWaypoint;
import lombok.Getter;

@Getter
public final class RemoveWaypointMessage extends CBMessage {

    private final String name;
    private final int dimension;

    public RemoveWaypointMessage(CBWaypoint waypoint) {
        super("DeleteWaypoint");

        this.name = waypoint.getName();
        this.dimension = waypoint.getDimension();
    }

}