package com.dahuaboke.mpda.bot.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.monitor.entity.MonitorEventEntity;
import com.dahuaboke.mpda.bot.monitor.mapper.BrMonitorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class MonitorTask {

    private static final Logger log = LoggerFactory.getLogger(MonitorTask.class);

    @Autowired
    private BrMonitorMapper brMonitorMapper;

    @Scheduled(cron = "0 0 15 * * ?")
    public void MonitorDeleteTaskJob() {
        log.info("开始执行监控表清理任务...........");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LambdaQueryWrapper<MonitorEventEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.lt(MonitorEventEntity::getCreateTime, thirtyDaysAgo.format(formatter));
        int delete = brMonitorMapper.delete(lambdaQueryWrapper);
        log.info("删除{}条", delete);
        log.info("执行监控表清理任务结束...........");
    }

}
