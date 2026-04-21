package dev.daguanyuan.server.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {

    POST("post"),
    REPLY("reply"),
    QUOTE("quote"),
    REACT("react"),
    FOLLOW_TOPIC("follow_topic"),
    UNFOLLOW_TOPIC("unfollow_topic");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static EventType fromValue(String value) {
        for (EventType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + value);
    }
}
