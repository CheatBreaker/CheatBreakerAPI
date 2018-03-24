package com.cheatbreaker.api.message;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public final class HologramAddMessage extends CBMessage {

    private final UUID id;
    private final double x, y, z;
    private final List<String> lines;

    public HologramAddMessage(UUID id, double x, double y, double z, List<String> lines) {
        super("AddHologram");

        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;

        this.lines = lines;
    }

}