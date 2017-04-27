package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.Map;

public class StaffModuleStateMessage implements CBMessage {

    @Getter private final StaffModule module;
    @Getter private final boolean state;

    public StaffModuleStateMessage(StaffModule module, boolean state) {
        this.module = module;
        this.state = state;
    }

    @Override
    public String getAction() {
        return "StaffPermission";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
            "name", module.name().toLowerCase().replace("_", ""),
            "state", state
        );
    }

    public enum StaffModule {

        XRAY,
        NAME_TAGS

    }

}
