/*
package com.cheatbreaker.api.message;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Set;

public final class SetWaypointDomainsMessage implements CBMessage {

    private final String primary;
    private final Set<String> remaining;

    public SetWaypointDomainsMessage(String primary, Set<String> remaining) {
        this.primary = Preconditions.checkNotNull(primary, "primary");
        this.remaining = Preconditions.checkNotNull(remaining, "remaining");
    }

    @Override
    public String getAction() {
        return null;
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
            "primary", primary,
            "remaining", remaining
        );
    }

}*/
