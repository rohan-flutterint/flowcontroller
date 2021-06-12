package com.sajith.flowcontroller.throttle.engine.control;

import com.sajith.flowcontroller.kafka.KafkaEventListener;
import com.sajith.flowcontroller.kafka.KafkaReceiver;
import com.sajith.flowcontroller.message.flow.exceptions.InvalidMessageFormatException;
import com.sajith.flowcontroller.message.flow.throttle.receiving.KafkaThrottleDecisionDistributor;
import com.sajith.flowcontroller.throttle.common.ControlEvent;
import com.sajith.flowcontroller.throttle.common.ThrottleConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class KafkaThrottleControlEventDistributor extends ThrottleControlEventDistributor implements KafkaEventListener {
    private KafkaReceiver kafkaReceiver;
    private Properties properties;
    private Logger log = LoggerFactory.getLogger(KafkaThrottleDecisionDistributor.class);

    public KafkaThrottleControlEventDistributor(Properties properties) {
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
            ControlEvent event = decodeKafkaMessage(value);
            if (null != event) {
                notifyListeners(topic, event);
            }
        }
    }

    private ControlEvent decodeKafkaMessage(String value) {
        ControlEvent event = null;
        try {
            JSONObject jsonObject = new JSONObject(value);
            String type = jsonObject.getString(ControlEvent.TYPE_FIELD_NAME);
            String key = jsonObject.getString(ControlEvent.KEY_FIELD_NAME);
            if (type.equals(ControlEvent.ALLOW_DENNY_MESSAGE_TYPE)){
                boolean isBlocked = jsonObject.getBoolean(ControlEvent.IS_BLOCKED_FIELD_NAME);
                event = ControlEvent.createAllowDenyControlEvent(key, isBlocked);
            } else {
                int quota = jsonObject.getInt(ControlEvent.QUOTA_FIELD_NAME);
                event = ControlEvent.createQuotaSetControlEvent(key, quota);
            }
        } catch (JSONException e) {
            throw new InvalidMessageFormatException("JSON message is invalid", e);
        }
        return event;
    }
}
