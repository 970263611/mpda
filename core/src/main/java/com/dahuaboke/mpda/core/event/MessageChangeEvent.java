package com.dahuaboke.mpda.core.event;

import com.dahuaboke.mpda.core.memory.MessageWrapper;

/**
 * auth: dahua
 * time: 2025/9/21 15:23
 */
public record MessageChangeEvent(MessageWrapper messageWrapper, Type changeEventType) implements ChangeEvent {

}
