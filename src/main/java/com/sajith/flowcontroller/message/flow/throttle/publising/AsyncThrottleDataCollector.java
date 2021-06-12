package com.sajith.flowcontroller.message.flow.throttle.publising;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.sajith.flowcontroller.message.flow.Message;
import com.sajith.flowcontroller.message.flow.MessageProcessor;
import com.sajith.flowcontroller.throttle.common.ThrottleDataEvent;

public class AsyncThrottleDataCollector extends MessageProcessor {

    private RingBuffer<ThrottleDataEvent> ringBuffer;
    private Disruptor<ThrottleDataEvent> disruptor;
    private ThrottleDataPublisher throttleDataPublisher;

    public AsyncThrottleDataCollector(ThrottleDataPublisher throttleDataPublisher) {
        this.throttleDataPublisher = throttleDataPublisher;
        disruptor = new Disruptor<>(ThrottleDataEvent::new, 1024, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            throttleDataPublisher.publish(event);
        });
        disruptor.start();

        ringBuffer = disruptor.getRingBuffer();
    }

    @Override
    public void handleMessage(Message message) {
        long sequence = ringBuffer.next();
        ThrottleDataEvent event = ringBuffer.get(sequence);
        event.setPayload(message.getMessagePayload());
        ringBuffer.publish(sequence);
    }
}
