package com.dahuaboke.mpda.core.trace;


import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.LimitedListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * auth: dahua
 * time: 2025/8/21 09:02
 */
@Component
public class TraceManager implements SmartLifecycle {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    @Value("${mpda.trace.max:50}")
    private int maxTrace;
    @Value("${mpda.trace.timeout:5}") // minute
    private int traceTimeout;
    @Value("${mpda.trace.check:30}") // second
    private int traceCheck;
    @Autowired
    private CacheManager cacheManager;
    private volatile boolean isRunning;
    private Map<String, Long> traceTimer = new HashMap<>();

    public void addTrace(TraceMessage traceMessage) {
        Map<String, LimitedListWrapper<TraceMessage>> traces = cacheManager.getTraces();
        String conversationId = traceMessage.getConversationId();
        if (traces.containsKey(conversationId)) {
            List<TraceMessage> traceList = traces.get(conversationId);
            if (traceList == null) {
                traceList = new LimitedListWrapper<>(maxTrace);
            }
            traceList.add(traceMessage);
        } else {
            traces.put(conversationId, new LimitedListWrapper<>(maxTrace) {{
                add(traceMessage);
            }});
        }
    }

    public void removeTrace(String conversationId) {
        Map<String, LimitedListWrapper<TraceMessage>> traces = cacheManager.getTraces();
        if (traces.containsKey(conversationId)) {
            traces.remove(conversationId);
        }
    }

    public List<TraceMessage> getTrace(String conversationId) {
        try {
            Map<String, LimitedListWrapper<TraceMessage>> traces = cacheManager.getTraces();
            return traces.get(conversationId);
        } finally {
            removeTrace(conversationId);
        }
    }

    @Override
    public void start() {
        scheduler.scheduleWithFixedDelay(() -> {
            Iterator<Map.Entry<String, Long>> iterator = traceTimer.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                long value = entry.getValue();
                long now = System.currentTimeMillis();
                if (now - value > traceTimeout * 60 * 1000) {
                    removeTrace(entry.getKey());
                    iterator.remove();
                } else {
                    break; //按照时间排序，如果存在非超时情况，后续一定不超时，则可以直接跳出
                }
            }
        }, traceCheck, traceCheck, TimeUnit.SECONDS);
        isRunning = true;
    }

    @Override
    public void stop() {
        scheduler.shutdown();
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
