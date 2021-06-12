package com.sajith.flowcontroller.throttle.common;

public class ThrottleDataEvent {
    private String payload;
    String senderId;
    String functionId;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload(){
        return payload;
    }
}
