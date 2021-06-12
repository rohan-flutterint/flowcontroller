package com.sajith.flowcontroller.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.function.Function;

public class KafkaPublisher<T> {
    private KafkaProducer<String, String> producer;
    private String topic;
    Function<T, String> payloadGenerationLogic;

    public KafkaPublisher(Properties properties, String topic) {
        this.topic = topic;
        producer = new KafkaProducer<String, String>(properties);
    }

    public void setConversionLogic(Function<T, String> payloadGenerationLogic) {
        this.payloadGenerationLogic = payloadGenerationLogic;
    }

    public void publish(T event) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, payloadGenerationLogic.apply(event));
        producer.send(record);
    }
}
