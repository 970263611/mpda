package com.dahuaboke.mpda.core.context;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/17 16:42
 */
public class CoreContext {

    private String query;
    private String conversationId;
    private String sceneId;
    private Map<String, Object> metadata;

    public CoreContext(String query, String conversationId) {
        this.query = query;
        this.conversationId = conversationId;
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

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void copyMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public void addMetadata(String key, Object data) {
        if (this.metadata == null) {
            this.metadata = new LinkedHashMap<>();
        }
        this.metadata.put(key,data);
    }
}
