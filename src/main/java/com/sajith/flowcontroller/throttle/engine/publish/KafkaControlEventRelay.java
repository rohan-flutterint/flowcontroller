package com.sajith.flowcontroller.throttle.engine.publish;

import com.sajith.flowcontroller.kafka.KafkaPublisher;
import com.sajith.flowcontroller.throttle.common.ThrottleDecisionEvent;
import org.json.JSONObject;

import java.util.Properties;
import java.util.function.Function;

public class KafkaControlEventRelay extends KafkaPublisher<String> implements ControlEventRelay {
    public KafkaControlEventRelay(Properties properties, String topic) {
        super(properties, topic);
        Function<String, String> conversionLogic = (payload) -> { return payload;};
        super.setConversionLogic(conversionLogic);
    }

    @Override
    public void forward(String event) {
        super.publish(event);
    }
}
