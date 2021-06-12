package com.sajith.flowcontroller.message.flow.dispatcher;

import java.util.Properties;

public class DispatcherFactory {
    public static Dispatcher createDispatcher(String type, Properties properties) {
        switch (type.toLowerCase()) {
            case "http":
                Dispatcher httpDispatcher = new HTTPDispatcher();
                httpDispatcher.init(properties);
                return httpDispatcher;
            default:
                throw new IllegalArgumentException("Unknown dispatcher type " + type);
        }
    }
}
