package com.sajith.flowcontroller.common;

/**
 * Objects which intends to receive events of Type E has to implement this interface and register with the
 * Event distributor
 */
public interface EventListener<E> {
    public void onEvent(E event);
}
