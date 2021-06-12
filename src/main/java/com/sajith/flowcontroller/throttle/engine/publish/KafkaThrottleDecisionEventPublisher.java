package com.sajith.flowcontroller.throttle.engine.publish;

import com.sajith.flowcontroller.kafka.KafkaPublisher;
import com.sajith.flowcontroller.message.flow.throttle.publising.ThrottleDataPublisher;
import com.sajith.flowcontroller.throttle.common.ThrottleDataEvent;
import com.sajith.flowcontroller.throttle.common.ThrottleDecisionEvent;
import org.json.JSONObject;

import java.util.Properties;
import java.util.function.Function;

public class KafkaThrottleDecisionEventPublisher extends KafkaPublisher<ThrottleDecisionEvent>
        implements ThrottleDecisionPublisher {

    public KafkaThrottleDecisionEventPublisher(Properties properties, String topic) {
        super(properties, topic);
        Function<ThrottleDecisionEvent, String> conversionLogic = (event) -> {
            JSONObject object = new JSONObject();
            object.put(ThrottleDecisionEvent.THROTTLE_KEY_FIELD_NAME, event.getThrottleKey());
            object.put(ThrottleDecisionEvent.THROTTLE_DECISION_FIELD_NAME, event.isBlocked());
            return object.toString();
        };
        super.setConversionLogic(conversionLogic);
    }
}
