package com.dahuaboke.mpda.core.event.monitor;

import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.memory.MessageWrapper;

/**
 * auth: dahua
 * time: 2025/9/21 15:23
 */
public record MessageChangeEvent(MessageWrapper messageWrapper, Event.Type eventType) implements MonitorEvent {

}
