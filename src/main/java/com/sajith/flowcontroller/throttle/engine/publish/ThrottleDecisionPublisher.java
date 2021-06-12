package com.sajith.flowcontroller.throttle.engine.publish;

import com.sajith.flowcontroller.throttle.common.ThrottleDecisionEvent;

public interface ThrottleDecisionPublisher {
    void publish(ThrottleDecisionEvent event);
}
