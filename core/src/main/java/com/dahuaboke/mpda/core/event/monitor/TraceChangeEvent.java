package com.dahuaboke.mpda.core.event.monitor;

import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.trace.TraceMessage;

/**
 * auth: dahua
 * time: 2025/9/21 15:23
 */
public record TraceChangeEvent(TraceMessage traceMessage, Event.Type eventType) implements MonitorEvent {

}
