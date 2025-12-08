package com.dahuaboke.mpda.core.event;


import com.dahuaboke.mpda.core.memory.MemoryWrapper;

/**
 * auth: dahua
 * time: 2025/12/8 15:01
 */
public record MemoryAppendEvent(MemoryWrapper memoryWrapper,Event.Type eventType) implements Event {
}
