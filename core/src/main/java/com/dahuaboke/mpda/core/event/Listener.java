package com.dahuaboke.mpda.core.event;

/**
 * @author dahua
 * @time 2025/7/15
 */
public interface Listener<T extends Event> {

    void onEvent(T event);

}
