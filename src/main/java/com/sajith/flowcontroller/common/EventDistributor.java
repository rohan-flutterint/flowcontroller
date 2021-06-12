package com.sajith.flowcontroller.common;

import java.util.*;

/**
 * Represents the entity which receives events from the external source.
 * Responsible for managing the listeners and notifying them upon receiving events
 * @param <E> Event type
 */
public abstract class EventDistributor<E> {
    private Map<String, List<EventListener<E>>> topicListeners = new HashMap<>();
    public abstract void init(Collection<String> topics);

    protected boolean haveListeners(String topic) {
        List<EventListener<E>> eventListeners = topicListeners.get(topic);
        return !(null == eventListeners || eventListeners.isEmpty());
    }

    protected void notifyListeners(String topic, E event){
        List<EventListener<E>> eventListeners = topicListeners.get(topic);
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.onEvent(event));
        }
    }

    public void registerListener(String topic, EventListener<E> listener) {
        List<EventListener<E>> listeners = topicListeners.get(topic);
        if (listeners == null) {
            listeners = new ArrayList<>();
            topicListeners.put(topic, listeners);
        }
        listeners.add(listener);
    }
}
