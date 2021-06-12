package com.sajith.flowcontroller.message.flow.decoder;

import com.sajith.flowcontroller.message.flow.MessageEntryPoint;
import com.sajith.flowcontroller.message.flow.MessageProcessor;
import com.sajith.flowcontroller.message.flow.exceptions.InvalidMessageFormatException;
import com.sajith.flowcontroller.message.flow.Message;

public abstract class MessageDecoder implements MessageEntryPoint {
    private MessageProcessor nextProcessor;

    public MessageProcessor messageProcessor(MessageProcessor processor){
        this.nextProcessor = processor;
        return nextProcessor;
    }

    public void onMessageEntry(String payload) {
        Message message = decode(payload);
        if (nextProcessor != null){
            nextProcessor.process(message);
        }
    }

    protected abstract Message decode(String payload) throws InvalidMessageFormatException;
}
