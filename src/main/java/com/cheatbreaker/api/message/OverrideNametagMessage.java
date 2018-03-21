package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Entity;

import java.util.Map;

public final class OverrideNametagMessage implements CBMessage {

    private final int entityId;
    private final String nametag;

    public OverrideNametagMessage(Entity entity, String nametag) {
        this.entityId = entity.getEntityId();
        this.nametag = nametag;
    }

    @Override
    public String getAction() {
        return "OverrideNametag";
    }

    @Override
    public Map<String, Object> toMap() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("entityId", entityId);

        if (nametag != null) {
            builder.put("nametag", nametag);
        }

        return builder.build();
    }

}