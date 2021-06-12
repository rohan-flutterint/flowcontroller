package com.sajith.flowcontroller.throttle.rule;

import com.sajith.flowcontroller.throttle.engine.control.ThrottleControlEventListener;

public interface Rule extends ThrottleControlEventListener {
    /**
     * Check if a given message result in blocking the entity with key
     * @param key Key against which the evaluvation is done (e.g., user, function)
     * @return true if blocked
     */
    public boolean evaluate(String key);
}
