package com.dahuaboke.mpda.core.memory;


import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/12/8 15:35
 */
public class MemoryWrapper {

    private String conversationId;
    private String sceneId;
    private List<Message> messages;
    private Long time;

    public MemoryWrapper(String conversationId, String sceneId, List<Message> messages) {
        this.conversationId = conversationId;
        this.sceneId = sceneId;
        this.messages = messages;
        this.time = System.currentTimeMillis();
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Long getTime() {
        return time;
    }
}
