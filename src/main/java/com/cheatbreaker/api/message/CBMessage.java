package com.cheatbreaker.api.message;

import java.util.Map;

public interface CBMessage {

    String getAction();

    Map<String, Object> toMap();

}