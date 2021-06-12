package com.sajith.flowcontroller.message.flow.exceptions;

public class MessageFilteredOutException extends RuntimeException {
    public MessageFilteredOutException(String reason){
        super(reason);
    }
}
