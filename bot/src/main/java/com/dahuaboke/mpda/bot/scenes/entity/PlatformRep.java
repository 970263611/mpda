package com.dahuaboke.mpda.bot.scenes.entity;

import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankESBDto;

import java.util.List;

/**
 * @description: 返回平台报文实体
 * @author: ZHANGSHUHAN
 * @date: 2025/10/23
 */
public class PlatformRep {

    private List<MarketRankESBDto> marketRankESBDtoList;

    public PlatformRep(List<MarketRankESBDto> marketRankESBDtoList) {
        this.marketRankESBDtoList = marketRankESBDtoList;
    }

    public PlatformRep() {
    }

    public List<MarketRankESBDto> getMarketRankESBDtoList() {
        return marketRankESBDtoList;
    }

    public void setMarketRankESBDtoList(List<MarketRankESBDto> marketRankESBDtoList) {
        this.marketRankESBDtoList = marketRankESBDtoList;
    }
}
