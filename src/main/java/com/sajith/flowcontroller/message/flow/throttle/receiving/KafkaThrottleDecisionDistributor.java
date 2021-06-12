package com.sajith.flowcontroller.message.flow.throttle.receiving;

import com.sajith.flowcontroller.kafka.KafkaEventListener;
import com.sajith.flowcontroller.kafka.KafkaReceiver;
import com.sajith.flowcontroller.throttle.common.ThrottleDecisionEvent;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class KafkaThrottleDecisionDistributor extends ThrottleDecisionDistributor implements KafkaEventListener {
    private KafkaReceiver kafkaReceiver;
    private Properties properties;
    private Logger log = LoggerFactory.getLogger(KafkaThrottleDecisionDistributor.class);

    public KafkaThrottleDecisionDistributor(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void init(Collection<String> topics) {
        kafkaReceiver = new KafkaReceiver(properties, topics, this);
        kafkaReceiver.start();
    }

    @Override
    public void onKafkaEvent(String topic, String value) {
        if (haveListeners(topic)){
            ThrottleDecisionEvent event = decodeKafkaMessage(value);
            if (null != event) {
                notifyListeners(topic, event);
            }
        }
    }

    private ThrottleDecisionEvent decodeKafkaMessage(String value) {
        ThrottleDecisionEvent event = null;
        try {
            JSONObject object = new JSONObject(value);
            String key = object.getString(ThrottleDecisionEvent.THROTTLE_KEY_FIELD_NAME);
            boolean decision = object.getBoolean(ThrottleDecisionEvent.THROTTLE_DECISION_FIELD_NAME);
            event = new ThrottleDecisionEvent(key, decision);
        } catch (JSONException e) {
            log.error("Throttle event format is invalid : " + value);
        }
        return event;
    }
}
