package com.dahuaboke.mpda.core.context;

import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.exception.MpdaIllegalConfigException;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/21 13:30
 */
@Component
public class CacheManager {

    /**
     * scene
     */
    private final Map<String, SceneWrapper> sceneWrappers = new HashMap<>();
    /**
     * memory
     */
    private final Map<String, Map<String, LimitedListWrapper<Message>>> memories = new LinkedHashMap();
    /**
     * trace
     */
    private final Map<String, LimitedListWrapper<TraceMessage>> traces = new LinkedHashMap();
    /**
     * thread local
     */
    private ThreadLocal<CoreContext> contextThreadLocal = new ThreadLocal<>();
    private ThreadLocal<Map<String, Object>> attributeThreadLocal = new ThreadLocal<>();

    public CoreContext getContext() {
        return contextThreadLocal.get();
    }

    public void setContext(CoreContext context) {
        contextThreadLocal.set(context);
    }

    public void removeContext() {
        contextThreadLocal.remove();
    }

    public Map<String, Object> getAttribute() {
        try {
            return attributeThreadLocal.get();
        } finally {
            removeAttribute();
        }
    }

    public void setAttribute(Map<String, Object> attribute) {
        attributeThreadLocal.set(attribute);
    }

    public void removeAttribute() {
        attributeThreadLocal.remove();
    }

    public void addScenedWrapper(String sceneId, SceneWrapper sceneWrapper) {
        sceneWrappers.put(sceneId, sceneWrapper);
    }

    public Map<String, SceneWrapper> getSceneWrappers() {
        return sceneWrappers;
    }

    public String getSceneIdBySceneClass(Class<? extends Scene> clz) {
        return sceneWrappers.values().stream().filter(
                wrapper -> wrapper.getSceneClass().equals(clz)).findFirst().orElseThrow(MpdaIllegalConfigException::new).getSceneId();
    }

    public Map<String, Map<String, LimitedListWrapper<Message>>> getMemories() {
        return memories;
    }

    public Map<String, LimitedListWrapper<TraceMessage>> getTraces() {
        return traces;
    }
}
