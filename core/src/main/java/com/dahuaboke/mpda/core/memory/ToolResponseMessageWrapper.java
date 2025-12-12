package com.dahuaboke.mpda.core.memory;


import org.springframework.ai.chat.messages.ToolResponseMessage;

/**
 * auth: dahua
 * time: 2025/9/11 13:19
 */
public class ToolResponseMessageWrapper extends ToolResponseMessage implements MessageWrapper {

    public ToolResponseMessageWrapper(String conversationId, String sceneName, ToolResponseMessage toolResponseMessage) {
        super(toolResponseMessage.getResponses(), toolResponseMessage.getMetadata());
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneName", sceneName);
        this.metadata.put("time", System.currentTimeMillis());
    }

    public ToolResponseMessageWrapper(String conversationId, String sceneName, ToolResponseMessage toolResponseMessage, Long time) {
        super(toolResponseMessage.getResponses(), toolResponseMessage.getMetadata());
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneName", sceneName);
        this.metadata.put("time", time);
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
    public String getSceneName() {
        return (String) this.metadata.get("sceneName");
    }
}
