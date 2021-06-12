package com.sajith.flowcontroller.message.flow.throttle.publising;

import com.sajith.flowcontroller.throttle.common.ThrottleDataEvent;

/**
 * Publishing throttling events to the throttle engine
 */
public interface ThrottleDataPublisher {
    void publish(ThrottleDataEvent event);
}
