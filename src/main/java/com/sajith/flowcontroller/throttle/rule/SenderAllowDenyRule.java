package com.sajith.flowcontroller.throttle.rule;

import com.sajith.flowcontroller.throttle.common.ControlEvent;
import java.util.ArrayList;
import java.util.List;

public class SenderAllowDenyRule implements Rule {
    private List<String> blockedUsers;

    public SenderAllowDenyRule() {
        blockedUsers = new ArrayList<>();
    }
    @Override
    public boolean evaluate(String key) {
        return blockedUsers.contains(key);
    }

    @Override
    public void onEvent(ControlEvent event) {
        if (event.getType().equals(ControlEvent.ALLOW_DENNY_MESSAGE_TYPE)) {
            String key = event.getKey();
            boolean isBlocked = event.isBlocked();
            if (isBlocked) {
                blockedUsers.add(key);
            } else {
                blockedUsers.remove(key);
            }
        }
    }
}
