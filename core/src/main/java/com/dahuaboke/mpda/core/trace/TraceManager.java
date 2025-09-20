package com.dahuaboke.mpda.core.trace;


import com.dahuaboke.mpda.core.context.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.*;
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
    @Value("${mpda.scene.traceTimeout:5}") // minute
    private int traceTimeout;
    @Autowired
    private CacheManager cacheManager;
    private volatile boolean isRunning;
    private Map<String, Long> traceTimer = new HashMap<>();

    public void addTrace(String key, String trace) {
        Map<String, List<String>> traces = cacheManager.getTraces();
        if (traces.containsKey(key)) {
            List<String> traceList = traces.get(key);
            if (traceList == null) {
                traceList = new LinkedList<>();
            }
            traceList.add(trace);
        } else {
            traces.put(key, new LinkedList<>() {{
                add(trace);
            }});
        }
    }

    public void removeTrace(String key) {
        Map<String, List<String>> traces = cacheManager.getTraces();
        if (traces.containsKey(key)) {
            traces.remove(key);
        }
    }

    public List<String> getTrace(String key) {
        try {
            Map<String, List<String>> traces = cacheManager.getTraces();
            return traces.get(key);
        } finally {
            removeTrace(key);
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
        }, traceTimeout, traceTimeout, TimeUnit.SECONDS);
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
