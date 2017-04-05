package com.cheatbreaker.api.message;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import com.cheatbreaker.api.object.CBCooldown;

import java.util.Map;

public final class SendCooldownMessage implements CBMessage {

    private final CBCooldown cooldown;

    public SendCooldownMessage(CBCooldown cooldown) {
        this.cooldown = Preconditions.checkNotNull(cooldown, "cooldown");
    }

    @Override
    public String getAction() {
        return "cooldown";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
            "message", cooldown.getMessage(),
            "durationMs", cooldown.getDurationMs(),
            "icon", cooldown.getIcon().getId()
        );
    }

}