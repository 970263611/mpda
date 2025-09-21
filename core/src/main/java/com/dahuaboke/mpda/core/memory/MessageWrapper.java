package com.dahuaboke.mpda.core.memory;

import org.springframework.ai.chat.messages.Message;

/**
 * auth: dahua
 * time: 2025/9/20 23:29
 */
public interface MessageWrapper extends Message {

    long getTime();

    String getConversationId();

    String getSceneId();
}
