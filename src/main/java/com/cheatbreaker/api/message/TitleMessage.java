package com.cheatbreaker.api.message;

import lombok.Getter;

import java.time.Duration;

@Getter
public final class TitleMessage extends CBMessage {

    private final String type;
    private final String message;
    private final int fadeInTime;
    private final int displayTime;
    private final int fadeOutTime;

    public TitleMessage(Type type, String message, Duration fadeInTime, Duration displayTime, Duration fadeOutTime) {
        super("Title");

        this.type = type.name().toLowerCase();
        this.message = message;
        this.fadeInTime = (int) fadeInTime.toMillis();
        this.displayTime = (int) displayTime.toMillis();
        this.fadeOutTime = (int) fadeOutTime.toMillis();
    }

    public enum Type {
        TITLE, SUBTITLE, TOOLTIP
    }

}