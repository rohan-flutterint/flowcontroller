package com.sajith.flowcontroller;

import com.sajith.flowcontroller.message.flow.MessageEntryPoint;
import com.sajith.flowcontroller.message.flow.throttle.receiving.ThrottleDecisionDistributor;
import com.sajith.flowcontroller.throttle.engine.control.ThrottleControlEventDistributor;
import com.sajith.flowcontroller.throttle.engine.publish.ControlEventRelay;
import com.sajith.flowcontroller.throttle.engine.receive.ThrottleDataDistributor;

public class ApplicationContext {
    private static ThrottleDecisionDistributor throttleDecisionDistributor;
    private static ThrottleDataDistributor throttleDataDistributor;
    private static ThrottleControlEventDistributor throttleControlEventDistributor;
    private static boolean isThrottleEngineEnabled = false;
    private static MessageEntryPoint entryPoint;
    private static ControlEventRelay controlEventRelay;

    public static void setThrottleDecisionDistributor(ThrottleDecisionDistributor throttleDecisionDistributor) {
        ApplicationContext.throttleDecisionDistributor = throttleDecisionDistributor;
    }

    public static void setThrottleDataDistributor(ThrottleDataDistributor throttleDataDistributor) {
        ApplicationContext.throttleDataDistributor = throttleDataDistributor;
    }

    public static void setThrottleControlEventDistributor(ThrottleControlEventDistributor throttleControlEventDistributor) {
        ApplicationContext.throttleControlEventDistributor = throttleControlEventDistributor;
    }

    public static void setIsThrottleEngineEnabled(boolean isThrottleEngineEnabled) {
        ApplicationContext.isThrottleEngineEnabled = isThrottleEngineEnabled;
    }

    public static void setEntryPoint(MessageEntryPoint entryPoint) {
        ApplicationContext.entryPoint = entryPoint;
    }

    public static void setControlEventRelay(ControlEventRelay controlEventRelay) {
        ApplicationContext.controlEventRelay = controlEventRelay;
    }

    public static ThrottleDecisionDistributor getThrottleEventDistributor() {
        return throttleDecisionDistributor;
    }

    public static ThrottleDataDistributor getThrottleDataDistributor() {
        return throttleDataDistributor;
    }

    public static boolean getIsThrottledEngineEnabled() {
        return isThrottleEngineEnabled;
    }

    public static ControlEventRelay getControlEventRelay() {
        return controlEventRelay;
    }

    public static MessageEntryPoint getEntryPoint() {
        return entryPoint;
    }

    public static ThrottleControlEventDistributor getThrottleControlEventDistributor() {
        return throttleControlEventDistributor;
    }
}
