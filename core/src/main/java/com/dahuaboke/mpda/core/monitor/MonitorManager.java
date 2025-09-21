package com.dahuaboke.mpda.core.monitor;

import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.event.Listener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/21 20:50
 */
@Component
public class MonitorManager implements Listener {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onEvent(Event event) {
        try {
            System.out.println(objectMapper.writeValueAsString(event)); // TODO 完善监控
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
