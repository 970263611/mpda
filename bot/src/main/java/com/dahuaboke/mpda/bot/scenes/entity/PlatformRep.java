package com.dahuaboke.mpda.bot.scenes.entity;

import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;

import java.util.List;

/**
 * @description: 返回平台报文实体
 * @author: ZHANGSHUHAN
 * @date: 2025/10/23
 */
public class PlatformRep {

    private List<MarketRankDto> marketRankDtoList;

    public List<MarketRankDto> getMarketRankDtoList() {
        return marketRankDtoList;
    }


    public void setMarketRankDtoList(List<MarketRankDto> marketRankDtoList) {
        this.marketRankDtoList = marketRankDtoList;
    }

    public PlatformRep(List<MarketRankDto> marketRankDtoList) {
        this.marketRankDtoList = marketRankDtoList;
    }

    public PlatformRep() {
    }

}
