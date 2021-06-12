package com.sajith.flowcontroller.message.flow;

public class Message {
    public static final String SENDER_ID_FIELD_NAME = "senderId";
    public static final String FUNCTION_ID_FIELD_NAME = "functionId";
    public static final String MESSAGE_ID_FIELD_NAME = "messageId";

    private String senderId;
    private String functionId;
    private String messageId;
    private String messagePayload;

    public Message(String senderId, String functionId, String messageId, String messagePayload) {
        this.senderId = senderId;
        this.functionId = functionId;
        this.messageId = messageId;
        this.messagePayload = messagePayload;
    }

    public String getSenderId() { return senderId; }

    public String getFunctionId() {
        return functionId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

}
