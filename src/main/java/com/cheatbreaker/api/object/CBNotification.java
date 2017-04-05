package com.cheatbreaker.api.object;

import com.google.common.base.Preconditions;

import java.util.concurrent.TimeUnit;

import lombok.Getter;

public final class CBNotification {

    @Getter private final String message;
    @Getter private final long durationMs;
    @Getter private final Level level;

    public CBNotification(String message, long unitCount, TimeUnit unit) {
        this(message, unitCount, unit, Level.INFO);
    }

    public CBNotification(String message, long unitCount, TimeUnit unit, Level level) {
        this.message = Preconditions.checkNotNull(message, "message");
        this.durationMs = unit.toMillis(unitCount);
        this.level = Preconditions.checkNotNull(level, "level");
    }

    public enum Level {

        INFO, ERROR

    }

}