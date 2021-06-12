package com.sajith.flowcontroller.spring.rest;

import com.sajith.flowcontroller.ApplicationContext;
import com.sajith.flowcontroller.FlowControllerApplication;
import com.sajith.flowcontroller.kafka.KafkaPublisher;
import com.sajith.flowcontroller.message.flow.MessageEntryPoint;
import com.sajith.flowcontroller.message.flow.exceptions.DispatchException;
import com.sajith.flowcontroller.message.flow.exceptions.InvalidMessageFormatException;
import com.sajith.flowcontroller.message.flow.exceptions.MessageFilteredOutException;
import com.sajith.flowcontroller.throttle.engine.publish.ControlEventRelay;
import com.sajith.flowcontroller.throttle.engine.publish.KafkaControlEventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;

@RestController
public class FlowControlController {
    Logger log = LoggerFactory.getLogger(FlowControlController.class);
    MessageEntryPoint entryPoint;
    ControlEventRelay controlEventRelay;

    @PostConstruct
    public void init() {
         entryPoint = ApplicationContext.getEntryPoint();
         if (ApplicationContext.getIsThrottledEngineEnabled()){
            controlEventRelay = ApplicationContext.getControlEventRelay();
         }
    }

    @PostMapping("/message")
    public void message(@RequestBody String payload) {
        try {
            entryPoint.onMessageEntry(payload);
        } catch (MessageFilteredOutException e) {
            log.error("Message Filtered out", e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (InvalidMessageFormatException e) {
            log.error("Message is invalid", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DispatchException e) {
            log.error("Message dispatching failed out", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @PostMapping("/control")
    public void control(@RequestBody String payload) {
        if (!ApplicationContext.getIsThrottledEngineEnabled()) {
            log.error("Throttling engine is not enabled...");
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);

        }
        controlEventRelay.forward(payload);
    }
}
