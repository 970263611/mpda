package com.dahuaboke.mpda.core.event;

import com.dahuaboke.mpda.core.trace.TraceMessage;

/**
 * auth: dahua
 * time: 2025/9/21 15:23
 */
public record TraceChangeEvent(TraceMessage traceMessage, Type changeEventType) implements ChangeEvent {

}
