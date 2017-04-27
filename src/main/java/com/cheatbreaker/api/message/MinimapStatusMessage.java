package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class MinimapStatusMessage implements CBMessage {

    private MinimapStatus status;

    public MinimapStatusMessage(MinimapStatus status) {
        this.status = status;
    }

    @Override
    public String getAction() {
        return "MinimapStatus";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "state", status
        );
    }

    public enum MinimapStatus {

        FORCED_OFF,
        NEUTRAL

    }

}
