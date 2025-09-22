package com.dahuaboke.mpda.core.monitor;

import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.event.Listener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * auth: dahua
 * time: 2025/9/21 20:50
 */
@Component
public class MonitorManager implements Listener {

    private final Sinks.Many<Event> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void onEvent(Event event) {
        sink.tryEmitNext(event); // TODO 完善监控
    }

    public Flux monitor() {
        return sink.asFlux()
                .doOnCancel(() -> System.out.println("Monitor disconnected from stream"))
                .doOnError(e -> System.err.println("Error occurred during streaming: " + e));
    }
}
