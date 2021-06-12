## Testing

### Prerequisite
* Java8 or above
* Apache Kafka (tested with kafka_2.13-2.8.0)
* maven

### Bringing up the setup
* Bring up kafka server
* Build the code with 'mvn clean install'
* Go to project home and run `java -jar target/flowcontroller-0.0.1-SNAPSHOT.jar <server-id>`
  * This will bring up the flow controller.
  * server-id has to be unique
* Go to project home and run `java -jar target/flowcontroller-0.0.1-SNAPSHOT.jar <server-id> throttle --server.port=8081`
  * This will bring up the throttle engine
* You can bring up any number of above process with unique server-ids
* Bring up a dummy HTTP receives that listens on `http://localhost:9090/backend` 

### Testing
* To send a message
```
curl -X POST -H "Content-Type: application/json" \
    -d '{"senderId": "1", "functionId": "2", "messageId": "3"}' \
    http://localhost:8080/message
```
* To send a control message to block/unblock a user
```
curl -X POST -H "Content-Type: application/json" \
    -d '{"type": "AllowDeny", "key": "1", "quota": "0", "isBlocked" : "true"}' \
    http://localhost:8081/control
```
* To send a control message to set per user throttle limit
```
curl -X POST -H "Content-Type: application/json" \
    -d '{"type": "Quota", "key": "1", "quota": "10", "isBlocked" : "false"}' \
    http://localhost:8081/control
```
* There will be an exception printed on the console when a message is throttled out. This exception is thrown to send back the correct HTTP status code the user. Please not that it's not an application error 
## Design
There are mainly two components,
- FlowController : Acts as the proxy that intercepts the message and apply the filtering
- ThrottleEngine : Takes all throttling decision 

These two components are communicating with each other via Kafka. Flow controllers keep reporting the events it gets by publishing them to a kafka topic. For each event throttle engine evaluates is the user has to be blocked. When the user is blocked, this is notified to the flow controllers by publishing an event. All flow controllers will receive the event ordering them to block/unblock a user and accordingly update their state

Flow Controller is a chain of message processor which has the following flow
`MessageDecoder->ThrottleEventPublisher->[FilterChain:f1->f2->f3]->Dispatcher`

Throttle Engine consists of set of Rules and Rule Evaluators. When it receives throttle data event from a flow controller, the rull evaluator will evaluate all rules to see if the subsequent events has to be blocked. And if So, it will be notified to the flow controller via kafka

Throttle engine also has the control API that can be used to update the rules. 

This can be extended to add new throttling polices by 
- Adding a new filter to the filter chain and
- Adding a rule to the throttle engine

## Assumptions
- The rules are evaluated lazily, that's upon receiving a request. Hence, one message can slip before the rule comes in effect at the FlowController. This is done to avoid excessive background checks
