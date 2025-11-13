package com.dahuaboke.mpda.bot.monitor;

import com.dahuaboke.mpda.bot.monitor.entity.MonitorEventEntity;
import com.dahuaboke.mpda.bot.monitor.mapper.BrMonitorMapper;
import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.event.Listener;
import com.dahuaboke.mpda.core.event.MessageChangeEvent;
import com.dahuaboke.mpda.core.event.TraceChangeEvent;
import com.dahuaboke.mpda.core.memory.MessageWrapper;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitorSaver implements Listener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrMonitorMapper brMonitorMapper;

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
                        , messageChangeEvent.changeEventType().toString(), now.format(dateTimeFormatter));
            }
            if (event instanceof TraceChangeEvent traceChangeEvent) {
                LocalDateTime now = LocalDateTime.now();
                TraceMessage traceMessage = traceChangeEvent.traceMessage();
                monitorEventEntity = new MonitorEventEntity(
                        traceMessage.getConversationId(), objectMapper.writeValueAsString(traceMessage)
                        , traceChangeEvent.changeEventType().toString(), now.format(dateTimeFormatter));
            }
            if (monitorEventEntity != null) {
                brMonitorMapper.insert(monitorEventEntity);
            }
        } catch (JsonProcessingException e) {
        }
    }

}
