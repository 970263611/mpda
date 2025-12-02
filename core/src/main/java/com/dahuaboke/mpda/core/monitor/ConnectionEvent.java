package com.dahuaboke.mpda.core.monitor;

import com.dahuaboke.mpda.core.event.Event;

/**
 * auth: dahua
 * time: 2025/12/2 14:50
 */
public record ConnectionEvent(String connectionType) implements Event {
}