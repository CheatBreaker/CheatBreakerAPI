package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public final class HologramRemoveMessage implements CBMessage {

    private final UUID id;

    @Override
    public String getAction() {
        return "RemoveHologram";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "id", id
        );
    }

}
