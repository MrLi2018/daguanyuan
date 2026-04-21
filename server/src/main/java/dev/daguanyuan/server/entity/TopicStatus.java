package dev.daguanyuan.server.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TopicStatus {

    ACTIVE("active"),
    ARCHIVED("archived");

    private final String value;

    TopicStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static TopicStatus fromValue(String value) {
        for (TopicStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown topic status: " + value);
    }
}
