package com.cheatbreaker.api.message;

import lombok.Getter;

@Getter
public final class StaffModuleStateMessage extends CBMessage {

    private final String name;
    private final boolean state;

    public StaffModuleStateMessage(StaffModule module, boolean state) {
        super("StaffPermission");

        this.name = module.name().toLowerCase().replace("_", "");
        this.state = state;
    }

    public enum StaffModule {

        XRAY,
        NAME_TAGS,
        BUNNY_HOP

    }

}
