package com.cheatbreaker.api.message;

import lombok.Getter;
import org.bukkit.entity.Entity;

import java.util.List;

@Getter
public final class OverrideNametagMessage extends CBMessage {

    private final int entityId;
    private final List<String> nametag;

    public OverrideNametagMessage(Entity entity, List<String> nametag) {
        super("OverrideNametag");

        this.entityId = entity.getEntityId();
        this.nametag = nametag;
    }

}