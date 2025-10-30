package com.dahuaboke.mpda.bot.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.monitor.entity.MonitorEventEntity;
import com.dahuaboke.mpda.bot.monitor.mapper.BrMonitorMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorTask {

    private static final Logger log = LoggerFactory.getLogger(MonitorTask.class);

    @Autowired
    private BrMonitorMapper brMonitorMapper;

    @Scheduled(cron = "0 0 15 * * ?")
    public void MonitorDeleteTaskJob() {
        log.info("开始执行监控表清理任务...........");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date oneWeekAgo = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        LambdaQueryWrapper<MonitorEventEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.lt(MonitorEventEntity::getCreateTime, simpleDateFormat.format(oneWeekAgo));
        brMonitorMapper.delete(lambdaQueryWrapper);
        log.info("执行监控表清理任务结束...........");
    }

}
