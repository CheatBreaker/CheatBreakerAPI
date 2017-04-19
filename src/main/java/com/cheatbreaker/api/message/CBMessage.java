package com.cheatbreaker.api.message;

import java.util.Map;

public interface CBMessage {

    /**
     * Messages have actions.
     * The client knows what to do with the data in {@link #toMap()}
     * because of the action provided.
     */
    String getAction();

    /**
     * The data contained in this Message. Will be sent to the Client.
     */
    Map<String, Object> toMap();

}