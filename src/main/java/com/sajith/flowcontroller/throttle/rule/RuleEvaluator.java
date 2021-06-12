package com.sajith.flowcontroller.throttle.rule;

import com.sajith.flowcontroller.spring.rest.FlowControlController;
import com.sajith.flowcontroller.throttle.common.ThrottleDataEvent;
import com.sajith.flowcontroller.throttle.common.ThrottleDecisionEvent;
import com.sajith.flowcontroller.throttle.engine.ThrottleDecisionCache;
import com.sajith.flowcontroller.throttle.engine.publish.ThrottleDecisionPublisher;
import com.sajith.flowcontroller.throttle.engine.receive.ThrottleDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class RuleEvaluator implements ThrottleDataListener {
    private List<Rule> rules;
    private ThrottleDecisionPublisher decisionPublisher;
    Logger log = LoggerFactory.getLogger(RuleEvaluator.class);

    public RuleEvaluator(ThrottleDecisionPublisher publisher, List<Rule> rules){
        this.decisionPublisher = publisher;
        this.rules = rules;
    }

    @Override
    public void onEvent(ThrottleDataEvent event) {
        boolean isStateChanged = false;
        boolean result = false;
        String key = getKey(event);
        for (Rule rule: rules) {
            if (rule.evaluate(key) == true){
                result = true;
                break;
            }
        }
        isStateChanged = isStateChanged(key, result);
        if (isStateChanged) {
            decisionPublisher.publish(new ThrottleDecisionEvent(key, result));
            log.info("Publishing throttling state change " +
                    "[SenderId: " + key + ", status:" + result + "]");
        }
    }

    public abstract String getKey(ThrottleDataEvent event);

    public abstract boolean isStateChanged(String key, boolean currentStatus);
}
