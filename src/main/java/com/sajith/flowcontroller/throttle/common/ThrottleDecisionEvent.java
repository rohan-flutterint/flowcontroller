package com.sajith.flowcontroller.throttle.common;

import org.apache.kafka.common.protocol.types.Field;

public class ThrottleDecisionEvent {
    public static String THROTTLE_KEY_FIELD_NAME = "throttleKey";
    public static String THROTTLE_DECISION_FIELD_NAME = "isBlocked";

    private String throttleKey;
    private boolean isBlocked;

    public ThrottleDecisionEvent() {
    }

    public ThrottleDecisionEvent(String throttleKey, boolean isBlocked) {
        this.throttleKey = throttleKey;
        this.isBlocked = isBlocked;
    }

    public String getThrottleKey() {
        return throttleKey;
    }

    public void setThrottleKey(String throttleKey) {
        this.throttleKey = throttleKey;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }
}
