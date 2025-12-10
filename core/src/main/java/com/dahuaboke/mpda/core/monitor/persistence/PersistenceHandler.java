package com.dahuaboke.mpda.core.monitor.persistence;


import com.dahuaboke.mpda.core.monitor.entity.MonitorEventEntity;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/12/10 10:39
 */
public interface PersistenceHandler {

    void save(MonitorEventEntity monitorEventEntity);

    Flux<MonitorEventEntity> stream(Long begin, Long end);
}
