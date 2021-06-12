package com.sajith.flowcontroller.message.flow.filter;

import com.sajith.flowcontroller.message.flow.Message;
import com.sajith.flowcontroller.spring.rest.FlowControlController;
import com.sajith.flowcontroller.throttle.common.ThrottleDecisionEvent;
import com.sajith.flowcontroller.message.flow.throttle.receiving.ThrottleDecisionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PerSenderThrottleFilter implements Filter, ThrottleDecisionListener {
    private Map<String, Boolean> blockedUsers = new HashMap();
    Logger log = LoggerFactory.getLogger(PerSenderThrottleFilter.class);


    @Override
    public boolean isAllowed(Message message) {
        return (!blockedUsers.containsKey(message.getSenderId()));
    }

    @Override
    public String getFilteredOutReason() {
        return "Filtered out at Per Sender Throttle Filter";
    }

    @Override
    public void onEvent(ThrottleDecisionEvent event) {
        log.info("Applying new throttle Event Decision Received[senderId: " + event.getThrottleKey()
                + ", status:" + event.isBlocked() + "]");

        if (event.isBlocked()) {
            blockedUsers.put(event.getThrottleKey(), true);
        } else {
            blockedUsers.remove(event.getThrottleKey());
        }
    }
}
