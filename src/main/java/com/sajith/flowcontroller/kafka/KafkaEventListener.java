package com.sajith.flowcontroller.kafka;

public interface KafkaEventListener {
    void onKafkaEvent(String topic, String payload);
}
