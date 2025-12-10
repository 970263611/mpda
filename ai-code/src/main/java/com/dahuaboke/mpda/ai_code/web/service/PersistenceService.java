package com.dahuaboke.mpda.ai_code.web.service;


import com.dahuaboke.mpda.core.monitor.entity.MonitorEventEntity;
import com.dahuaboke.mpda.core.monitor.persistence.PersistenceHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

/**
 * auth: dahua
 * time: 2025/12/10 10:48
 */
@Component
public class PersistenceService implements PersistenceHandler {

    private List<MonitorEventEntity> monitorEventEntities = new ArrayList<>();

    @Override
    public void save(MonitorEventEntity entity) {
        monitorEventEntities.add(entity);
    }

    @Override
    public Flux<MonitorEventEntity> stream(Long beginTime, Long endTime) {
        Sinks.Many<MonitorEventEntity> sink = Sinks.many().unicast().onBackpressureBuffer();
        monitorEventEntities.stream()
                .filter(entity -> {
                    Long createTime = entity.getCreateTime();
                    return createTime >= beginTime && createTime <= endTime;
                })
                .forEach(entity -> {
                    sink.tryEmitNext(entity);
                });
        return sink.asFlux();
    }
}
