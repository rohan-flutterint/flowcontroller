package com.sajith.flowcontroller.message.flow;

import com.sajith.flowcontroller.ApplicationContext;
import com.sajith.flowcontroller.FlowControllerApplication;
import com.sajith.flowcontroller.kafka.KafkaUtil;
import com.sajith.flowcontroller.message.flow.filter.Filter;
import com.sajith.flowcontroller.message.flow.filter.PerSenderThrottleFilter;
import com.sajith.flowcontroller.message.flow.throttle.publising.KafkaThrottleDataPublisher;
import com.sajith.flowcontroller.message.flow.decoder.MessageDecoder;
import com.sajith.flowcontroller.message.flow.decoder.MessageDecoderFactory;
import com.sajith.flowcontroller.message.flow.dispatcher.Dispatcher;
import com.sajith.flowcontroller.message.flow.dispatcher.DispatcherFactory;
import com.sajith.flowcontroller.message.flow.filter.FilterChain;
import com.sajith.flowcontroller.message.flow.throttle.publising.AsyncThrottleDataCollector;
import com.sajith.flowcontroller.message.flow.throttle.receiving.ThrottleDecisionListener;
import com.sajith.flowcontroller.throttle.common.ThrottleConstants;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * This class represents a complete flow a message
 */
public class MessageFlowBuilderHelper {

    public static MessageEntryPoint buildMessageFlow() {
        //Creating message decoder
        MessageDecoder decoder = MessageDecoderFactory.createMessageDecoder("json");

        //Creating Throttle data publisher
        Properties kafkaPublisherProperties = KafkaUtil.createDefaultPublisherProperties();
        MessageProcessor throttleEventReporter =
                new AsyncThrottleDataCollector(new KafkaThrottleDataPublisher(kafkaPublisherProperties,
                        ThrottleConstants.THROTTLE_DATA_KAFKA_TOPIC));

        // Creating Filter chain
        Filter perSenderThrottleFilter = new PerSenderThrottleFilter();
        MessageProcessor filterChain = new FilterChain.FilterChainBuilder()
                .filter(perSenderThrottleFilter)
                .build();

        //Registering for throttle decision events
        ApplicationContext.getThrottleEventDistributor()
                .registerListener(ThrottleConstants.PER_SENDER_THROTTLE_EVENT_KAFKA_TOPIC,
                        (ThrottleDecisionListener) perSenderThrottleFilter);

        // Creating Dispatcher
        Properties dispatcherProperties = new Properties();
        dispatcherProperties.setProperty("URL", "http://localhost:9090/backend");

        Dispatcher dispatcher = DispatcherFactory.createDispatcher("http", dispatcherProperties);
        dispatcher.init(dispatcherProperties);

        // create the flow
        decoder.messageProcessor(throttleEventReporter)
                .nextProcessor(filterChain)
                .nextProcessor((MessageProcessor) dispatcher);

        return decoder;
    }
}
