package com.dahuaboke.mpda.core.memory;

import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;

import java.util.Map;

public class UserMessageWrapper extends AbstractMessage implements MessageWrapper {

    public UserMessageWrapper(String conversationId, String sceneName, String content) {
        this(conversationId, sceneName, content, null);
    }

    public UserMessageWrapper(String conversationId, String sceneName, String content, Long time) {
        super(MessageType.USER, content, Map.of());
        time = time == null ? System.currentTimeMillis() : time;
        this.metadata.put("conversationId", conversationId);
        this.metadata.put("sceneName", sceneName);
        this.metadata.put("time", time);
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
    public String getSceneName() {
        return (String) this.metadata.get("sceneName");
    }

    public static class Builder {
        private String content;
        private String conversationId;
        private String sceneName;
        private Long time;

        public Builder text(String content) {
            this.content = content;
            return this;
        }

        public Builder conversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public Builder sceneName(String sceneName) {
            this.sceneName = sceneName;
            return this;
        }

        public Builder time(Long time) {
            this.time = time;
            return this;
        }

        public UserMessageWrapper build() {
            Assert.notNull(content, "Content can not null");
            Assert.notNull(conversationId, "ConversationId can not null");
            Assert.notNull(sceneName, "SceneName can not null");
            return new UserMessageWrapper(conversationId, sceneName, content, time);
        }
    }
}
