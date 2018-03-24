package com.cheatbreaker.api.message;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class HologramRemoveMessage extends CBMessage {

    private final UUID id;

    public HologramRemoveMessage(UUID id) {
        super("RemoveHologram");

        this.id = id;
    }

}
