package com.dahuaboke.mpda.core.memory;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.content.Media;

import java.util.List;
import java.util.Map;

public class AssistantMessageWrapper extends AssistantMessage implements MessageWrapper {

    public AssistantMessageWrapper(String conversationId, String sceneId, String content) {
        super(content);
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneId", sceneId);
        this.metadata.put("time", System.currentTimeMillis());
    }

    public AssistantMessageWrapper(String conversationId, String sceneId, String content, Map<String, Object> properties) {
        super(content, properties);
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneId", sceneId);
        this.metadata.put("time", System.currentTimeMillis());
    }

    public AssistantMessageWrapper(String content, Map<String, Object> properties, List<ToolCall> toolCalls, String conversationId, String sceneId) {
        super(content, properties, toolCalls);
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneId", sceneId);
        this.metadata.put("time", System.currentTimeMillis());
    }

    public AssistantMessageWrapper(String conversationId, String sceneId, String content, Map<String, Object> properties, List<ToolCall> toolCalls, List<Media> media) {
        super(content, properties, toolCalls, media);
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneId", sceneId);
        this.metadata.put("time", System.currentTimeMillis());
    }

    @Override
    public long getTime() {
        return (long) this.metadata.get("time");
    }

    @Override
    public String getConversationId() {
        return (String) this.metadata.get("conversationId");
    }

    @Override
    public String getSceneId() {
        return (String) this.metadata.get("sceneId");
    }
}
