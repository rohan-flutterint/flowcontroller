package com.sajith.flowcontroller.throttle.engine.receive;

import com.sajith.flowcontroller.kafka.KafkaEventListener;
import com.sajith.flowcontroller.kafka.KafkaReceiver;
import com.sajith.flowcontroller.message.flow.Message;
import com.sajith.flowcontroller.message.flow.exceptions.InvalidMessageFormatException;
import com.sajith.flowcontroller.message.flow.throttle.receiving.KafkaThrottleDecisionDistributor;
import com.sajith.flowcontroller.throttle.common.ThrottleDataEvent;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Properties;

public class KafkaThrottleDataDistributor extends ThrottleDataDistributor implements KafkaEventListener {
    private KafkaReceiver kafkaReceiver;
    private Properties properties;
    private Logger log = LoggerFactory.getLogger(KafkaThrottleDecisionDistributor.class);

    public KafkaThrottleDataDistributor(Properties properties) {
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
            ThrottleDataEvent event = decodeKafkaMessage(value);
            if (null != event) {
                notifyListeners(topic, event);
            }
        }
    }

    private ThrottleDataEvent decodeKafkaMessage(String value) {
        ThrottleDataEvent event = null;
        try {
            JSONObject jsonObject = new JSONObject(value);
            String senderId = jsonObject.getString(Message.SENDER_ID_FIELD_NAME);
            String functionId = jsonObject.getString(Message.FUNCTION_ID_FIELD_NAME);

            event = new ThrottleDataEvent();
            event.setFunctionId(functionId);
            event.setSenderId(senderId);
        } catch (JSONException e) {
            throw new InvalidMessageFormatException("JSON message is invalid", e);
        }
        return event;
    }
}
