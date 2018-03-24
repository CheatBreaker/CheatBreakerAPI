package com.cheatbreaker.api.message;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public final class HologramUpdateMessage extends CBMessage {

    private final UUID id;
    private final List<String> lines;

    public HologramUpdateMessage(UUID id, List<String> lines) {
        super("UpdateHologram");

        this.id = id;
        this.lines = lines;
    }

}