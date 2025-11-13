package com.dahuaboke.mpda.bot.scheduler;

import com.dahuaboke.mpda.bot.tools.service.BrProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ResetTimeOutTask {

    private static final Logger log = LoggerFactory.getLogger(ResetTimeOutTask.class);

    @Autowired
    BrProductService brProductService;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 12)
    public void ragSearchJob() {
        log.info("开始执行超时扫描任务...........");
        int number = brProductService.resetTimeout();
        if(number > 0){
            log.info("扫描到{} 条超时任务",number);
        }
    }

}
