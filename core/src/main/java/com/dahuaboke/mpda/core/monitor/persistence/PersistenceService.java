package com.dahuaboke.mpda.core.monitor.persistence;


import com.dahuaboke.mpda.core.context.LimitedList;
import com.dahuaboke.mpda.core.monitor.entity.MonitorEventEntity;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/12/10 10:48
 */
public class PersistenceService implements PersistenceHandler {

    private List<MonitorEventEntity> monitorEventEntities;

    public PersistenceService(int persistenceSize) {
        monitorEventEntities = new LimitedList<>(persistenceSize);
    }

    @Override
    public void save(MonitorEventEntity entity) {
        monitorEventEntities.add(entity);
    }

    @Override
    public Flux<MonitorEventEntity> stream(Long beginTime, Long endTime) {
        return Flux.fromIterable(monitorEventEntities)
                .filter(entity -> {
                    Long createTime = entity.getCreateTime();
                    return createTime >= beginTime && createTime <= endTime;
                });
    }
}
