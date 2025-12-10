package com.dahuaboke.mpda.bot.monitor;


import com.dahuaboke.mpda.bot.monitor.mapper.BrMonitorMapper;
import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.monitor.entity.MonitorEventEntity;
import com.dahuaboke.mpda.core.monitor.persistence.PersistenceHandler;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * auth: dahua
 * time: 2025/12/10 10:48
 */
@Component
public class PersistenceService implements PersistenceHandler {

    @Autowired
    private BrMonitorMapper brMonitorMapper;

    @Override
    public void save(MonitorEventEntity entity) {
        String data = entity.getData();
        String conversationId = entity.getConversationId();
        String eventTypeName = entity.getEventType().toString();
        Long createTime = entity.getCreateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .withZone(ZoneId.of("Asia/Shanghai"));
        Instant instant = Instant.ofEpochMilli(createTime);
        String time = formatter.format(instant);
        var monitorEventEntity = new com.dahuaboke.mpda.bot.monitor.entity.MonitorEventEntity(conversationId, data, eventTypeName, time);
        brMonitorMapper.insert(monitorEventEntity);
    }

    @Override
    public Flux<MonitorEventEntity> stream(Long beginTime, Long endTime) {
        Sinks.Many<MonitorEventEntity> sink = Sinks.many().unicast().onBackpressureBuffer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
                .withZone(ZoneId.of("Asia/Shanghai"));
        Instant beginInstant = Instant.ofEpochMilli(beginTime);
        String begin = formatter.format(beginInstant);
        Instant endInstant = Instant.ofEpochMilli(endTime);
        String end = formatter.format(endInstant);
        Cursor<com.dahuaboke.mpda.bot.monitor.entity.MonitorEventEntity> monitorEventEntities = brMonitorMapper.streamMonitorEvent(begin, end);
        monitorEventEntities.forEach(monitorEventEntity -> {
            String data = monitorEventEntity.getBusiData();
            String conversationId = monitorEventEntity.getConversationId();
            String eventTypeName = monitorEventEntity.getEventTypeName();
            String createTime = monitorEventEntity.getCreateTime();
            Event.Type type;
            if ("ADDED".equals(eventTypeName)) {
                type = Event.Type.ADDED;
            } else if ("REMOVED".equals(eventTypeName)) {
                type = Event.Type.REMOVED;
            } else {
                type = null;
            }
            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime localDateTime = LocalDateTime.parse(createTime, newFormatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
            Long timestamp = zonedDateTime.toInstant().toEpochMilli();
            MonitorEventEntity mee = new MonitorEventEntity(conversationId, data, type, timestamp);
            sink.tryEmitNext(mee);
        });
        return sink.asFlux();
    }
}
