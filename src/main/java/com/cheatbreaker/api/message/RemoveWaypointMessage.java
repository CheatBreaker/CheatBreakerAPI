package com.cheatbreaker.api.message;

import com.cheatbreaker.api.object.CBWaypoint;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class RemoveWaypointMessage implements CBMessage {

    private final CBWaypoint waypoint;

    public RemoveWaypointMessage(CBWaypoint waypoint) {
        this.waypoint = waypoint;
    }

    @Override
    public String getAction() {
        return "addwaypoint";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "name", waypoint.getName(),
                "dimension", waypoint.getDimension()
        );
    }

}