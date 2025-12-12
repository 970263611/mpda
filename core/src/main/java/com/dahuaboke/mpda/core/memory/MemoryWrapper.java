package com.dahuaboke.mpda.core.memory;


import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/12/8 15:35
 */
public class MemoryWrapper {

    private String conversationId;
    private String sceneName;
    private List<Message> messages;
    private Long time;

    public MemoryWrapper() {
    }

    public MemoryWrapper(String conversationId, String sceneName, List<Message> messages) {
        this.conversationId = conversationId;
        this.sceneName = sceneName;
        this.messages = messages;
        this.time = System.currentTimeMillis();
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
