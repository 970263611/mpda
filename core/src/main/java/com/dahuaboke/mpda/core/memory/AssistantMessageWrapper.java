package com.dahuaboke.mpda.core.memory;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.content.Media;

import java.util.List;
import java.util.Map;

public class AssistantMessageWrapper extends AssistantMessage implements MessageWrapper {

    private final long time;

    public AssistantMessageWrapper(String content) {
        super(content);
        this.time = System.currentTimeMillis();
    }

    public AssistantMessageWrapper(String content, Map<String, Object> properties) {
        super(content, properties);
        this.time = System.currentTimeMillis();
    }

    public AssistantMessageWrapper(String content, Map<String, Object> properties, List<ToolCall> toolCalls) {
        super(content, properties, toolCalls);
        this.time = System.currentTimeMillis();
    }

    public AssistantMessageWrapper(String content, Map<String, Object> properties, List<ToolCall> toolCalls, List<Media> media) {
        super(content, properties, toolCalls, media);
        this.time = System.currentTimeMillis();
    }

    @Override
    public long getTime() {
        return time;
    }
}
