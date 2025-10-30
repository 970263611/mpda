package com.dahuaboke.mpda.bot.tools.dto;


public class MarketRankReq {

    /**
     * 债券基金类型	1-信用债-指数型，2-信用债主动-开放式，3-利率债主动-开放式，4-利率债指数1-3年，5-利率债指数3-5年，6-利率债指数1-5年
     */
    private String finBondType;

    /**
     * 时间范围
     * 0-无时间
     * 1-近1周
     * 2-近1月
     * 3-近3月
     * 4-近一年
     * 5-当前年第1季度
     * 6-当前年第2季度
     * 7-当前年第3季度
     * 8-当前年第4季度
     * 9-今年以来
     */
    private String period;

    public String getFinBondType() {
        return finBondType;
    }

    public void setFinBondType(String finBondType) {
        this.finBondType = finBondType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
