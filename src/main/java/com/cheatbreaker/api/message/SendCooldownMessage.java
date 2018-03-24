package com.cheatbreaker.api.message;

import com.cheatbreaker.api.object.CBCooldown;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.Map;

@Getter
public final class SendCooldownMessage extends CBMessage {

    private final String message;
    private final long durationMs;
    private final int icon;

    public SendCooldownMessage(CBCooldown cooldown) {
        super("Cooldown");

        this.message = cooldown.getMessage();
        this.durationMs = cooldown.getDurationMs();
        this.icon = cooldown.getIcon().getId();
    }

}