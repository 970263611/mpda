package com.dahuaboke.mpda.core.context;


import com.dahuaboke.mpda.core.agent.scene.strategy.SceneFinderStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/17 16:42
 */
public class CoreContext {

    private String query;
    private String conversationId;
    private String sceneName;
    private Map<String, Object> extend;
    @JsonIgnore
    private transient SceneFinderStrategy findStrategy;

    public CoreContext(String query, String conversationId) {
        this.query = query;
        this.conversationId = conversationId;
    }

    public CoreContext(String query, String conversationId, SceneFinderStrategy findStrategy) {
        this.query = query;
        this.conversationId = conversationId;
        this.findStrategy = findStrategy;
    }

    public CoreContext() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void copyExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    public void addExtend(String key, Object data) {
        if (this.extend == null) {
            this.extend = new LinkedHashMap<>();
        }
        this.extend.put(key, data);
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public SceneFinderStrategy getFindStrategy() {
        return findStrategy;
    }

    public void setFindStrategy(SceneFinderStrategy findStrategy) {
        this.findStrategy = findStrategy;
    }
}
