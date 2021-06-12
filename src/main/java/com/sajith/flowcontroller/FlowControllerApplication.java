package com.sajith.flowcontroller;

import com.sajith.flowcontroller.kafka.KafkaUtil;
import com.sajith.flowcontroller.message.flow.MessageEntryPoint;
import com.sajith.flowcontroller.message.flow.MessageFlowBuilderHelper;
import com.sajith.flowcontroller.message.flow.throttle.receiving.KafkaThrottleDecisionDistributor;
import com.sajith.flowcontroller.throttle.ThrottleEngineBuilderHelper;
import com.sajith.flowcontroller.throttle.common.ThrottleConstants;
import com.sajith.flowcontroller.throttle.engine.control.KafkaThrottleControlEventDistributor;
import com.sajith.flowcontroller.throttle.engine.publish.ControlEventRelay;
import com.sajith.flowcontroller.throttle.engine.publish.KafkaControlEventRelay;
import com.sajith.flowcontroller.throttle.engine.receive.KafkaThrottleDataDistributor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collection;
import java.util.Properties;

@SpringBootApplication
public class FlowControllerApplication {

	public static void main(String[] args) {
		String serverId = "";
		if (args.length > 0){
			serverId = args[0];
			System.out.println("Setting server id to" + serverId);
		}

		Collection topics = KafkaUtil.createTopicCollection(new String[] {
				ThrottleConstants.PER_SENDER_THROTTLE_EVENT_KAFKA_TOPIC,
				ThrottleConstants.PER_FUNCTION_THROTTLE_EVENT_KAFKA_TOPIC});

		Properties properties = KafkaUtil.createDefaultConsumerProperties("EventFlow" + serverId);
		KafkaThrottleDecisionDistributor throttleDecisionDistributor = new KafkaThrottleDecisionDistributor(properties);
		throttleDecisionDistributor.init(topics);
		ApplicationContext.setThrottleDecisionDistributor(throttleDecisionDistributor);

		MessageEntryPoint entryPoint = MessageFlowBuilderHelper.buildMessageFlow();
		ApplicationContext.setEntryPoint(entryPoint);

		if (args.length > 1 && args[1].equals("throttle")){
			// In throttle engine mode the process will listen to throttle data from
			// flow controller and any control messages to change the flow
			Properties throttleEngineProperties = KafkaUtil.createDefaultConsumerProperties("ThrottleEngine" + serverId);
			Collection<String> throttleDataTopic = KafkaUtil.createTopicCollection(new
					String[] {ThrottleConstants.THROTTLE_DATA_KAFKA_TOPIC});
			KafkaThrottleDataDistributor throttleDataDistributor = new KafkaThrottleDataDistributor(throttleEngineProperties);
			throttleDataDistributor.init(throttleDataTopic);
			ApplicationContext.setThrottleDataDistributor(throttleDataDistributor);

			// Creating control event distributor to listen to control events sent by user
			Collection<String> throttleControlEventTopic = KafkaUtil.createTopicCollection(new
					String[] {ThrottleConstants.CONTROL_EVENT_KAFKA_TOPIC});
			KafkaThrottleControlEventDistributor throttleControlEventDistributor
					= new KafkaThrottleControlEventDistributor(throttleEngineProperties);
			throttleControlEventDistributor.init(throttleControlEventTopic);
			ApplicationContext.setThrottleControlEventDistributor(throttleControlEventDistributor);

			 // Control event relay is used to publish the control events to kafka so that all throttle engines will
			 // know the changes to the control
			 Properties relayProperties = KafkaUtil.createDefaultPublisherProperties();
			 ControlEventRelay controlEventRelay =
					 new KafkaControlEventRelay(relayProperties, ThrottleConstants.CONTROL_EVENT_KAFKA_TOPIC);
			 ApplicationContext.setControlEventRelay(controlEventRelay);
			 ApplicationContext.setIsThrottleEngineEnabled(true);

			 ThrottleEngineBuilderHelper.createThrottleEngine();
		}

		SpringApplication.run(FlowControllerApplication.class, args);
	}
}
