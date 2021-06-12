package com.sajith.flowcontroller.throttle.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThrottleDecisionCache {
    private static Map<String, Boolean> senderStatus = new ConcurrentHashMap<>();
    private static Map<String, Boolean> functionStatus = new ConcurrentHashMap<>();

    public static boolean updateSenderStatus(String senderId, boolean newStatus){
        Boolean currentStatus = senderStatus.get(senderId);
        senderStatus.put(senderId, newStatus);
        return (currentStatus == null || (currentStatus.booleanValue() != newStatus));
    }

    public static boolean updateFunctionStatus(String functionId, boolean newStatus) {
        Boolean currentStatus = senderStatus.get(functionId);
        senderStatus.put(functionId, newStatus);
        return (currentStatus == null || (currentStatus.booleanValue() != newStatus));
    }
}
