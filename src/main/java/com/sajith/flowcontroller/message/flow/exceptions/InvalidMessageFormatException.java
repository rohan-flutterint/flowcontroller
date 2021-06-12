package com.sajith.flowcontroller.message.flow.exceptions;

public class InvalidMessageFormatException extends RuntimeException {
    public InvalidMessageFormatException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
