package com.cheatbreaker.api.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class CBMessage {

    private final String action;

    public final String getAction() {
        return action;
    }

}