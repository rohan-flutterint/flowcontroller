package com.sajith.flowcontroller.message.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MessageProcessor {
    private Logger log = LoggerFactory.getLogger(MessageProcessor.class);
    private MessageProcessor nextProcessor;

    public MessageProcessor nextProcessor(MessageProcessor processor) {
        this.nextProcessor = processor;
        return nextProcessor;
    }

    public void process(Message message){
        handleMessage(message);

        if (nextProcessor != null) {
            nextProcessor.process(message);
        }
    }

    public abstract void handleMessage(Message message);
}
