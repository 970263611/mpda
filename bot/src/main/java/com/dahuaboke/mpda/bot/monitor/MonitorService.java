package com.dahuaboke.mpda.bot.monitor;

import com.dahuaboke.mpda.bot.monitor.entity.MonitorEventEntity;
import com.dahuaboke.mpda.bot.monitor.mapper.BrMonitorMapper;
import com.dahuaboke.mpda.core.event.*;
import com.dahuaboke.mpda.core.memory.MemoryWrapper;
import com.dahuaboke.mpda.core.memory.MessageWrapper;
import com.dahuaboke.mpda.core.monitor.ConnectionEvent;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.cursor.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MonitorService implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(MonitorService.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BrMonitorMapper brMonitorMapper;
    private Sinks.Many<Event> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void onEvent(Event event) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            MonitorEventEntity monitorEventEntity = null;
            if (event instanceof MessageChangeEvent messageChangeEvent) {
                LocalDateTime now = LocalDateTime.now();
                MessageWrapper messageWrapper = messageChangeEvent.messageWrapper();
                monitorEventEntity = new MonitorEventEntity(
                        messageWrapper.getConversationId(), objectMapper.writeValueAsString(messageWrapper)
                        , messageChangeEvent.eventType().toString(), now.format(dateTimeFormatter));
            }
            if (event instanceof TraceChangeEvent traceChangeEvent) {
                LocalDateTime now = LocalDateTime.now();
                TraceMessage traceMessage = traceChangeEvent.traceMessage();
                monitorEventEntity = new MonitorEventEntity(
                        traceMessage.getConversationId(), objectMapper.writeValueAsString(traceMessage)
                        , traceChangeEvent.eventType().toString(), now.format(dateTimeFormatter));
            }
            if (event instanceof MemoryAppendEvent memoryAppendEvent) {
                LocalDateTime now = LocalDateTime.now();
                MemoryWrapper memoryWrapper = memoryAppendEvent.memoryWrapper();
                monitorEventEntity = new MonitorEventEntity(
                        memoryWrapper.getConversationId(), objectMapper.writeValueAsString(memoryWrapper)
                        , memoryAppendEvent.eventType().toString(), now.format(dateTimeFormatter));
            }
            if (monitorEventEntity != null) {
                brMonitorMapper.insert(monitorEventEntity);
            }
        } catch (JsonProcessingException e) {
        }
    }

    @Transactional
    public Flux<Event> getMonitorEvent(String begin, String end) {
        try {
            Cursor<MonitorEventEntity> monitorEventEntities = brMonitorMapper.streamMonitorEvent(begin, end);
            monitorEventEntities.forEach(monitorEventEntity -> {
                try {
                    System.out.println(objectMapper.writeValueAsString(monitorEventEntity));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
