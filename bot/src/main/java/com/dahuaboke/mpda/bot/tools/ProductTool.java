package com.dahuaboke.mpda.bot.tools;


import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.core.agent.tools.AbstractBaseTool;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * auth: dahua
 * time: 2025/9/1 09:32
 */
public abstract class ProductTool<I> extends AbstractBaseTool<I> {

    @Autowired
    @Lazy
    protected ProductToolHandler productToolHandler;

    protected String getMaxWithDrawal(String productNo) {
        return productToolHandler.maxWithDrawal(new NetValReq(productNo));
    }

    protected String getYearRita(String productNo) {
        return productToolHandler.yearRita(new NetValReq(productNo));
    }

    protected String getYearRita(String productNo, LocalDateTime begTime, LocalDateTime endTime) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        return productToolHandler.yearRita(new NetValReq(productNo, begTime.format(yyyyMMdd), endTime.format(yyyyMMdd)));
    }


    protected List<ProdInfoDto> getFundByType(String fundType) {
        return productToolHandler.getFundByType(fundType);
    }



}
