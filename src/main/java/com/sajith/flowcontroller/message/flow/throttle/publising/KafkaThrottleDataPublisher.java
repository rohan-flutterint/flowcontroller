package com.sajith.flowcontroller.message.flow.throttle.publising;

import com.sajith.flowcontroller.kafka.KafkaPublisher;
import com.sajith.flowcontroller.throttle.common.ThrottleDataEvent;


import java.util.Properties;
import java.util.function.Function;

public class KafkaThrottleDataPublisher extends KafkaPublisher<ThrottleDataEvent> implements ThrottleDataPublisher {

    public KafkaThrottleDataPublisher(Properties properties, String topic) {
        super(properties, topic);
        Function<ThrottleDataEvent, String> conversionLogic = (event) -> {
            return event.getPayload();
        };
        super.setConversionLogic(conversionLogic);
    }

}
