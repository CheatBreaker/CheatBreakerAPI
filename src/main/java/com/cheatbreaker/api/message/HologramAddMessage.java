package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class HologramAddMessage implements CBMessage {

    private final UUID id;
    private final double x, y, z;
    private final List<String> lines;

    @Override
    public String getAction() {
        return "AddHologram";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "id", id,
                "x", x,
                "y", y,
                "z", z,
                "lines", lines
        );
    }

}