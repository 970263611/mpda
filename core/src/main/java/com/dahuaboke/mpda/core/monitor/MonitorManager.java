package com.dahuaboke.mpda.core.monitor;

import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * auth: dahua
 * time: 2025/9/21 20:50
 */
@Component
public class MonitorManager implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(MonitorManager.class);
    private final Sinks.Many<Event> sink = Sinks.many().multicast().directAllOrNothing();

    @Override
    public void onEvent(Event event) {
        if (sink.currentSubscriberCount() > 0) {
            sink.tryEmitNext(event);
        }
    }

    public Flux monitor() {
        return sink.asFlux()
                .doOnSubscribe(sub -> {
                    Flux.interval(Duration.ofSeconds(3))
                            .subscribeOn(Schedulers.boundedElastic())
                            .take(1)
                            .subscribe(
                                    v -> {
                                        Sinks.EmitResult result = sink.tryEmitNext(new ConnectionEvent("connected"));
                                        if (result.isFailure()) {
                                            logger.error("Connection send failed:{}", result);
                                        }
                                    },
                                    e -> logger.error("Connection subscribe failed:{}", e.getMessage()),
                                    () -> logger.debug("Connection send success")
                            );
                })
                .doOnCancel(() -> logger.debug("Monitor disconnected from stream"))
                .doOnError(e -> logger.error("Error occurred during streaming: {}", String.valueOf(e)));
    }
}
