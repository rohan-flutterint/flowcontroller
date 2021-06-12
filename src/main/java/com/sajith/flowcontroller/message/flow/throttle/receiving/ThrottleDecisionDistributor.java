package com.sajith.flowcontroller.message.flow.throttle.receiving;

import com.sajith.flowcontroller.common.EventDistributor;
import com.sajith.flowcontroller.throttle.common.ThrottleDecisionEvent;

/**
 * Receiving throttle events from the throttle engine and notify the interested listeners
 */
public abstract class ThrottleDecisionDistributor extends EventDistributor<ThrottleDecisionEvent> { }
