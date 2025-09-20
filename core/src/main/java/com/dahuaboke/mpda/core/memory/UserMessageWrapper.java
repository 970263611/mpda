package com.dahuaboke.mpda.core.memory;

import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;

import java.util.Map;

public class UserMessageWrapper extends AbstractMessage implements MessageWrapper {

    private final long time;

    public UserMessageWrapper(String content, long time) {
        super(MessageType.USER, content, Map.of());
        this.time = time;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public long getTime() {
        return time;
    }

    public static class Builder {
        private String content;
        private long time;

        public Builder text(String content) {
            this.content = content;
            return this;
        }

        public UserMessageWrapper build() {
            Assert.notNull(content, "Content can not null");
            time = System.currentTimeMillis();
            return new UserMessageWrapper(content, time);
        }
    }
}
