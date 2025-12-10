package com.dahuaboke.mpda.core.monitor.persistence;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.serializer.plain_text.jackson.SpringAIJacksonStateSerializer;
import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.event.Listener;
import com.dahuaboke.mpda.core.event.monitor.MemoryAppendEvent;
import com.dahuaboke.mpda.core.event.monitor.MessageChangeEvent;
import com.dahuaboke.mpda.core.event.monitor.MonitorEvent;
import com.dahuaboke.mpda.core.event.monitor.TraceChangeEvent;
import com.dahuaboke.mpda.core.memory.*;
import com.dahuaboke.mpda.core.monitor.ConnectionEvent;
import com.dahuaboke.mpda.core.monitor.entity.MonitorEventEntity;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/12/10 10:27
 */
public class PersistenceManager implements Listener<MonitorEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceManager.class);
    private ObjectMapper objectMapper;
    private PersistenceHandler persistenceHandler;

    public PersistenceManager(ObjectMapper objectMapper, PersistenceHandler persistenceHandler) {
        this.objectMapper = objectMapper;
        this.persistenceHandler = persistenceHandler;
    }

    @Override
    public void onEvent(MonitorEvent event) {
        try {
            Long time = System.currentTimeMillis();
            MonitorEventEntity monitorEventEntity = null;
            if (event instanceof MessageChangeEvent messageChangeEvent) {
                MessageWrapper messageWrapper = messageChangeEvent.messageWrapper();
                monitorEventEntity = new MonitorEventEntity(
                        messageWrapper.getConversationId(), objectMapper.writeValueAsString(messageWrapper)
                        , messageChangeEvent.eventType(), time);
            }
            if (event instanceof TraceChangeEvent traceChangeEvent) {
                TraceMessage traceMessage = traceChangeEvent.traceMessage();
                monitorEventEntity = new MonitorEventEntity(
                        traceMessage.getConversationId(), objectMapper.writeValueAsString(traceMessage)
                        , traceChangeEvent.eventType(), time);
            }
            if (event instanceof MemoryAppendEvent memoryAppendEvent) {
                MemoryWrapper memoryWrapper = memoryAppendEvent.memoryWrapper();
                monitorEventEntity = new MonitorEventEntity(
                        memoryWrapper.getConversationId(), objectMapper.writeValueAsString(memoryWrapper)
                        , memoryAppendEvent.eventType(), time);
            }
            if (monitorEventEntity != null) {
                persistenceHandler.save(monitorEventEntity);
            }
        } catch (JsonProcessingException e) {
        }
    }

    public Flux<Event> monitorStream(Long begin, Long end) {
        Sinks.Many<Event> sink = Sinks.many().unicast().onBackpressureBuffer();
        try {
            Flux<MonitorEventEntity> stream = persistenceHandler.stream(begin, end);
            stream.doOnNext(monitorEventEntity -> {
                try {
                    Event event;
                    Event.Type type = monitorEventEntity.getEventType();
                    ObjectMapper newObjectMapper = new SpringAIJacksonStateSerializer(OverAllState::new).objectMapper();
                    Map eventMap = objectMapper.readValue(monitorEventEntity.getData(), Map.class);
                    if (eventMap.containsKey("messages")) {
                        var memoryWrapper = objectMapper.convertValue(eventMap, MemoryWrapper.class);
                        event = new MemoryAppendEvent(memoryWrapper, type);
                    } else if (eventMap.containsKey("traceType")) {
                        var traceMessage = objectMapper.convertValue(eventMap, TraceMessage.class);
                        event = new TraceChangeEvent(traceMessage, type);
                    } else {
                        MessageWrapper messageWrapper;
                        String messageType = (String) eventMap.get("messageType");
                        String conversationId = (String) eventMap.get("conversationId");
                        String sceneId = (String) eventMap.get("sceneId");
                        Long time = (Long) eventMap.get("time");
                        if ("ASSISTANT".equals(messageType)) {
                            eventMap.put("@class", AssistantMessage.class.getName());
                            AssistantMessage assistantMessage = newObjectMapper.readValue(
                                    objectMapper.writeValueAsString(eventMap), AssistantMessage.class);
                            messageWrapper = new AssistantMessageWrapper(conversationId, sceneId, assistantMessage.getText(), assistantMessage.getMetadata()
                                    , assistantMessage.getToolCalls(), assistantMessage.getMedia(), time);
                        } else if ("USER".equals(messageType)) {
                            eventMap.put("@class", UserMessage.class.getName());
                            UserMessage userMessage = newObjectMapper.readValue(
                                    objectMapper.writeValueAsString(eventMap), UserMessage.class);
                            messageWrapper = new UserMessageWrapper(conversationId, sceneId, userMessage.getText(), time);
                        } else if ("TOOL".equals(messageType)) {
                            eventMap.put("@class", ToolResponseMessage.class.getName());
                            ToolResponseMessage toolResponseMessage = newObjectMapper.readValue(
                                    objectMapper.writeValueAsString(eventMap), ToolResponseMessage.class);
                            messageWrapper = new ToolResponseMessageWrapper(conversationId, sceneId, toolResponseMessage, time);
                        } else {
                            messageWrapper = null;
                        }
                        event = new MessageChangeEvent(messageWrapper, type);
                    }
                    sink.tryEmitNext(event);
                } catch (JsonProcessingException e) {
                    logger.error("Monitor message change failed", e);
                }
            });
        } catch (Exception e) {
            logger.error("Monitor message search failed", e);
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
                .doOnCancel(() -> {
                    logger.debug("Monitor disconnected from stream");
                })
                .doOnError(e -> logger.error("Error occurred during streaming: {}", String.valueOf(e)));
    }
}
