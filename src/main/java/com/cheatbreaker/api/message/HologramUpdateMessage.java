package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public final class HologramUpdateMessage implements CBMessage {

    private final UUID id;
    private final String[] lines;

    @Override
    public String getAction() {
        return "UpdateHologram";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "id", id,
                "lines", lines
        );
    }

}