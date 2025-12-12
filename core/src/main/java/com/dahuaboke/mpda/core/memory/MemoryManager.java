package com.dahuaboke.mpda.core.memory;


import com.dahuaboke.mpda.core.config.MpdaMemoryProperties;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.LimitedListWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.messages.Message;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * auth: dahua
 * time: 2025/9/17 17:29
 */
@Component
public class MemoryManager implements SmartLifecycle {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private int maxMemory;
    private int memoryTimeout;
    private int memoryCheck;
    private CacheManager cacheManager;
    private volatile boolean isRunning;
    private Map<String, Long> memoryTimer = new HashMap<>();

    public MemoryManager(CacheManager cacheManager, MpdaMemoryProperties properties) {
        this.cacheManager = cacheManager;
        this.maxMemory = properties.getMax();
        this.memoryTimeout = properties.getTimeout();
        this.memoryCheck = properties.getCheck();
    }

    private static Stream<Message> getMessageStream(Set<Class<? extends Message>> memoryExclude, List<Message> finalMessages) {
        Stream<Message> stream = finalMessages.stream().sorted((m1, m2) -> {
            if (m1 instanceof MessageWrapper w1 && m2 instanceof MessageWrapper w2) {
                return Long.valueOf(w1.getTime() - w2.getTime()).intValue();
            }
            return 0;
        });
        if (!memoryExclude.isEmpty()) {
            stream = stream.filter(
                    m3 -> !memoryExclude.stream().anyMatch(me -> me.isAssignableFrom(m3.getClass())));
        }
        return stream;
    }

    public void addMemory(Message message) {
        String conversationId = cacheManager.getContext().getConversationId();
        String sceneName = cacheManager.getContext().getSceneName();
        addMemory(conversationId, sceneName, message);
    }

    public void addMemory(String conversationId, String sceneName, Message message) {
        Map<String, Map<String, LimitedListWrapper<Message>>> memories = cacheManager.getMemories();
        if (memories.containsKey(conversationId)) {
            Map<String, LimitedListWrapper<Message>> sceneMessages = memories.get(conversationId);
            if (sceneMessages.containsKey(sceneName)) {
                sceneMessages.get(sceneName).add(message);
            } else {
                sceneMessages.put(sceneName, new LimitedListWrapper(maxMemory) {{
                    add(message);
                }});
            }
        } else {
            memories.put(conversationId, new LinkedHashMap<>() {{
                put(sceneName, new LimitedListWrapper(maxMemory) {{
                    add(message);
                }});
            }});
        }
        memoryTimer.put(conversationId, System.currentTimeMillis());
    }

    public List<Message> getMemory() {
        return getMemory(cacheManager.getContext().getConversationId(), cacheManager.getContext().getSceneName());
    }

    public List<Message> getMemory(String conversationId, String sceneName) {
        Map<String, Map<String, LimitedListWrapper<Message>>> memories = cacheManager.getMemories();
        if (memories.containsKey(conversationId)) {
            return memories.get(conversationId).get(sceneName);
        }
        return List.of();
    }

    public List<Message> getMemory(String conversationId, String sceneName, List<String> sceneMerge, Set<Class<? extends Message>> memoryExclude) {
        Map<String, Map<String, LimitedListWrapper<Message>>> memories = cacheManager.getMemories();
        if (memories.containsKey(conversationId)) {
            List<Message> messages = memories.get(conversationId).get(sceneName);
            if (messages == null) {
                messages = new ArrayList<>();
            }
            List<Message> finalMessages = new ArrayList<>(messages);
            if (!CollectionUtils.isEmpty(sceneMerge)) {
                sceneMerge.stream().forEach(merge -> {
                    List<Message> memory = getMemory(conversationId, merge);
                    if (memory != null) {
                        finalMessages.addAll(memory);
                    }
                });
            }
            return getMessageStream(memoryExclude, finalMessages).limit(maxMemory).toList();
        }
        return List.of();
    }

    public void removeMemory(String conversationId) {
        Map<String, Map<String, LimitedListWrapper<Message>>> memories = cacheManager.getMemories();
        if (memories.containsKey(conversationId)) {
            memories.remove(conversationId);
        }
    }

    @Override
    public void start() {
        scheduler.scheduleWithFixedDelay(() -> {
            Iterator<Map.Entry<String, Long>> iterator = memoryTimer.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                long value = entry.getValue();
                long now = System.currentTimeMillis();
                if (now - value > memoryTimeout * 60 * 1000) {
                    removeMemory(entry.getKey());
                    iterator.remove();
                }
            }
        }, memoryCheck, memoryCheck, TimeUnit.SECONDS);
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
