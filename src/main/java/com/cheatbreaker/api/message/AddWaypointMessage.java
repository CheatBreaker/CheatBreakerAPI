package com.cheatbreaker.api.message;

import com.cheatbreaker.api.object.CBWaypoint;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class AddWaypointMessage implements CBMessage {

    private final CBWaypoint waypoint;

    public AddWaypointMessage(CBWaypoint waypoint) {
        this.waypoint = waypoint;
    }

    @Override
    public String getAction() {
        return "AddWaypoint";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "name", waypoint.getName(),
                "posX", waypoint.getX(),
                "posY", waypoint.getY(),
                "posZ", waypoint.getZ(),
                "dimension", waypoint.getDimension()
        );
    }

}