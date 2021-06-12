package com.sajith.flowcontroller.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collection;
import java.util.Properties;

public class KafkaReceiver {
    private Consumer<String, String> consumer;
    private KafkaEventListener listener;

    public KafkaReceiver(Properties properties, Collection<String> topics, KafkaEventListener listener) {
        this.consumer = new KafkaConsumer<>(properties);
        this.listener = listener;
        consumer.subscribe(topics);
    }

    public void start(){
        new Thread(new KafkaReceiver.ReceiverThread()).start();
    }

    public void onKafkaEvent(String topic, String payload){
        listener.onKafkaEvent(topic, payload);
    }

    class ReceiverThread implements Runnable {
        Duration pollDuration = Duration.ofMillis(300);
        @Override
        public void run() {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(pollDuration);
                consumerRecords.forEach(record -> onKafkaEvent(record.topic(), record.value()));
                consumer.commitAsync();
            }
        }
    }
}
