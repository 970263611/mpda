package com.dahuaboke.mpda.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final Map<Class<? extends Event>, List<Listener<? super Event>>> listeners = new HashMap<>();

    public void publish(Event event) {
        Class<? extends Event> eventClass = event.getClass();
        for (Map.Entry<Class<? extends Event>, List<Listener<? super Event>>> entry : listeners
                .entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass)) {
                for (Listener<? super Event> listener : entry.getValue()) {
                    try {
                        listener.onEvent(event);
                    } catch (Exception e) {
                        logger.error("Error occurred while processing event: {}", e.getMessage(), e);
                    }
                }
            }
        }
    }

    void registerListener(Class<? extends Event> eventClass, Listener<? super Event> listener) {
        List<Listener<? super Event>> listeners = this.listeners.get(eventClass);
        if (listeners == null) {
            List<Listener<? super Event>> list = new ArrayList<>();
            list.add(listener);
            this.listeners.put(eventClass, list);
        } else {
            listeners.add(listener);
        }
    }

}
