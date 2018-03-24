package com.cheatbreaker.api.message;

import lombok.Getter;

@Getter
public final class MinimapStatusMessage extends CBMessage {

    private MinimapStatus status;

    public MinimapStatusMessage(MinimapStatus status) {
        super("MinimapStatus");

        this.status = status;
    }

    public enum MinimapStatus {

        FORCED_OFF,
        NEUTRAL

    }

}
