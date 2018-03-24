package com.cheatbreaker.api.message;

import com.cheatbreaker.api.object.CBNotification;
import lombok.Getter;

@Getter
public final class SendNotificationMessage extends CBMessage {

    private final String message;
    private final long durationMs;
    private final String level;

    public SendNotificationMessage(CBNotification notification) {
        super("Notification");

        this.message = notification.getMessage();
        this.durationMs = notification.getDurationMs();
        this.level = notification.getLevel().name();
    }

}