package com.cheatbreaker.api;

import com.google.common.base.Preconditions;

import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

import lombok.Getter;

public final class CBCooldown {

    @Getter private final String message;
    @Getter private final long durationMs;
    @Getter private final Material icon;

    public CBCooldown(String message, long unitCount, TimeUnit unit, Material icon) {
        this.message = Preconditions.checkNotNull(message, "message");
        this.durationMs = unit.toMillis(unitCount);
        this.icon = Preconditions.checkNotNull(icon, "icon");
    }

}