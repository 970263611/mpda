package com.dahuaboke.mpda.bot.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductMapper;
import com.dahuaboke.mpda.bot.tools.dao.FdIntbankKindparaMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateProductValidFlagTask {

    private static final Logger log = LoggerFactory.getLogger(UpdateProductValidFlagTask.class);

    @Autowired
    private FdIntbankKindparaMapper fdIntbankKindparaMapper;

    @Autowired
    private BrProductMapper brProductMapper;

    @Scheduled(cron = "0 0 9,13 * * ?")
    public void productRecommend() {
        log.info("开始同步上架产品！");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now();
        String today = now.format(formatter);
        int count = 1000;
        int start = 0;
        List<String> fundCodes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            List<String> list = fdIntbankKindparaMapper.selectFundCodes(today, start, count);
            start += 1000;
            fundCodes.addAll(list);
            if (list.size() < 1000) {
                break;
            }
        }
        BrProduct brProduct = new BrProduct();
        brProduct.setValidFlag("0");//是否解析标志 0-未上架 1-已上架
        brProductMapper.update(brProduct, new LambdaQueryWrapper<BrProduct>());
        if (fundCodes.size() > 0) {
            brProduct.setValidFlag("1");
            int update = brProductMapper.update(brProduct, new LambdaQueryWrapper<BrProduct>()
                    .in(BrProduct::getFundCode, fundCodes));
            log.info("上架产品{}条", update);
        }
    }


}
