package com.sajith.flowcontroller.throttle.common;

public class ThrottleConstants {
    // Kafka Topics
    public static final String THROTTLE_DATA_KAFKA_TOPIC = "ThrottleData";
    public static String PER_SENDER_THROTTLE_EVENT_KAFKA_TOPIC = "perSenderThrottleDecisions";
    public static String PER_FUNCTION_THROTTLE_EVENT_KAFKA_TOPIC = "perFunctionThrottleDecisions";
    public static String CONTROL_EVENT_KAFKA_TOPIC = "ControlEvents";

}
