package com.cheatbreaker.api.message;

import com.cheatbreaker.api.object.CBNotification;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class SendNotificationMessage implements CBMessage {

    private final CBNotification notification;

    public SendNotificationMessage(CBNotification notification) {
        this.notification = Preconditions.checkNotNull(notification, "notification");
    }

    @Override
    public String getAction() {
        return "notification";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
            "message", notification.getMessage(),
            "durationMs", notification.getDurationMs(),
            "level", notification.getLevel().name()
        );
    }

}