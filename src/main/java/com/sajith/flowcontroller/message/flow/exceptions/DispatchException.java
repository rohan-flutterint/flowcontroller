package com.sajith.flowcontroller.message.flow.exceptions;

public class DispatchException extends RuntimeException {
    public DispatchException(String reason, Throwable cause){
        super(reason, cause);
    }
}
