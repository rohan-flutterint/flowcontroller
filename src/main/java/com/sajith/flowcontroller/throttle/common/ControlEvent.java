package com.sajith.flowcontroller.throttle.common;

public class ControlEvent {
    public static final String ALLOW_DENNY_MESSAGE_TYPE = "AllowDeny";
    public static final String QUOTA_SET_MESSAGE_TYPE = "Quota";
    public static final String TYPE_FIELD_NAME = "type";
    public static final String KEY_FIELD_NAME = "key";
    public static final String QUOTA_FIELD_NAME = "quota";
    public static final String IS_BLOCKED_FIELD_NAME = "isBlocked";

    private String type;
    private String key;
    private int quota;
    private boolean isBlocked;

    private ControlEvent(String type, String key, int quota, boolean isBlocked) {
        this.type = type;
        this.key = key;
        this.quota = quota;
        this.isBlocked = isBlocked;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public int getQuota() {
        return quota;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public static ControlEvent createAllowDenyControlEvent(String key, boolean isBlocked) {
        return new ControlEvent(ALLOW_DENNY_MESSAGE_TYPE, key, -1, isBlocked);
    }

    public static ControlEvent createQuotaSetControlEvent(String key, int quota) {
        return new ControlEvent(QUOTA_SET_MESSAGE_TYPE, key, quota, false);
    }
}
