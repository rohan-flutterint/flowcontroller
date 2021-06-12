package com.sajith.flowcontroller.throttle.rule;

import com.sajith.flowcontroller.throttle.common.ThrottleDataEvent;
import com.sajith.flowcontroller.throttle.engine.ThrottleDecisionCache;
import com.sajith.flowcontroller.throttle.engine.publish.ThrottleDecisionPublisher;

import java.util.List;

public class PerSenderRuleEvaluator extends RuleEvaluator {

    public PerSenderRuleEvaluator(ThrottleDecisionPublisher publisher, List<Rule> rules){
        super(publisher, rules);
    }
    @Override
    public String getKey(ThrottleDataEvent event) {
        return event.getSenderId();
    }

    @Override
    public boolean isStateChanged(String key, boolean currentStatus) {
        return ThrottleDecisionCache.updateSenderStatus(key, currentStatus);
    }
}
