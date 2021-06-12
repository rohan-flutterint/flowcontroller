package com.sajith.flowcontroller.throttle;

import com.sajith.flowcontroller.ApplicationContext;
import com.sajith.flowcontroller.FlowControllerApplication;
import com.sajith.flowcontroller.kafka.KafkaUtil;
import com.sajith.flowcontroller.throttle.common.ThrottleConstants;
import com.sajith.flowcontroller.throttle.engine.publish.KafkaThrottleDecisionEventPublisher;
import com.sajith.flowcontroller.throttle.engine.publish.ThrottleDecisionPublisher;
import com.sajith.flowcontroller.throttle.rule.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ThrottleEngineBuilderHelper {
    public static void createThrottleEngine() {

        // Creating Per User Rules
        Properties kafkaPublisherProperties = KafkaUtil.createDefaultPublisherProperties();
        ThrottleDecisionPublisher perSenderDecisionPublisher =
                new KafkaThrottleDecisionEventPublisher(kafkaPublisherProperties,
                        ThrottleConstants.PER_SENDER_THROTTLE_EVENT_KAFKA_TOPIC);

        QuotaBasedRule perSenderQuotaRule = new QuotaBasedRule();
        SenderAllowDenyRule senderAllowDenyRule = new SenderAllowDenyRule();
        List<Rule> rules = new ArrayList<>();
        rules.add(perSenderQuotaRule);
        rules.add(senderAllowDenyRule);
        RuleEvaluator perSenderEvaluator = new PerSenderRuleEvaluator(perSenderDecisionPublisher, rules);

        ApplicationContext.getThrottleDataDistributor()
                .registerListener(ThrottleConstants.THROTTLE_DATA_KAFKA_TOPIC, perSenderEvaluator);

        ApplicationContext.getThrottleControlEventDistributor()
                .registerListener(ThrottleConstants.CONTROL_EVENT_KAFKA_TOPIC, perSenderQuotaRule);
        ApplicationContext.getThrottleControlEventDistributor()
                .registerListener(ThrottleConstants.CONTROL_EVENT_KAFKA_TOPIC, senderAllowDenyRule);

    }
}
