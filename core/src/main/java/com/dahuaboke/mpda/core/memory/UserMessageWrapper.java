package com.dahuaboke.mpda.core.memory;

import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;

import java.util.Map;

public class UserMessageWrapper extends AbstractMessage implements MessageWrapper {

    public UserMessageWrapper(String conversationId, String sceneId, String content) {
        super(MessageType.USER, content, Map.of());
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneId", sceneId);
        this.metadata.put("time", System.currentTimeMillis());
    }

    public static Builder builder() {
        return new Builder();
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

    public static class Builder {
        private String content;
        private String conversationId;
        private String sceneId;

        public Builder text(String content) {
            this.content = content;
            return this;
        }

        public Builder conversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public Builder sceneId(String sceneId) {
            this.sceneId = sceneId;
            return this;
        }

        public UserMessageWrapper build() {
            Assert.notNull(content, "Content can not null");
            Assert.notNull(conversationId, "ConversationId can not null");
            Assert.notNull(sceneId, "SceneId can not null");
            return new UserMessageWrapper(conversationId, sceneId, content);
        }
    }
}
