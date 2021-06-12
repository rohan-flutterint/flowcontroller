package com.sajith.flowcontroller.message.flow.decoder;

public class MessageDecoderFactory {
    public static MessageDecoder createMessageDecoder(String type) {
        switch (type.toLowerCase()) {
            case "json":
                return new JsonMessageDecoder();
            default:
                throw new IllegalArgumentException("Unknown decoder type " + type);
        }
    }
}
