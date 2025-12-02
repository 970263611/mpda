package com.dahuaboke.mpda.bot.tools.dto;


public class YieldRateInfoDto {

    /**
     * 【】收益率
     */
    private String yieldRate;

    /**
     * 【】收益率排名
     **/
    private int yieldRateRank;

    /**
     * 【】收益率昨日排名
     **/
    private int yieldRateRankYes;

    /**
     * 【】收益率排名变动
     **/
    private int yieldRateRankChange;

    /**
     * 【】最大回撤
     **/
    private String yieldWdwrt;

    /**
     * 时间
     * 1-近1周
     * 2-近1月
     * 3-近3月
     * 4-近1年
     * （展示上面1 2 3 4 四个字段）
     * 9-今年以来
     * （展示1 2 两个字段）
     **/
    private String timeRules;

    public YieldRateInfoDto(String yieldRate, int yieldRateRank, int yieldRateRankYes, int yieldRateRankChange, String yieldWdwrt, String timeRules) {
        this.yieldRate = yieldRate;
        this.yieldRateRank = yieldRateRank;
        this.yieldRateRankYes = yieldRateRankYes;
        this.yieldRateRankChange = yieldRateRankChange;
        this.yieldWdwrt = yieldWdwrt;
        this.timeRules = timeRules;
    }

    public YieldRateInfoDto() {
    }

    public String getYieldRate() {
        return yieldRate;
    }

    public void setYieldRate(String yieldRate) {
        this.yieldRate = yieldRate;
    }

    public int getYieldRateRank() {
        return yieldRateRank;
    }

    public void setYieldRateRank(int yieldRateRank) {
        this.yieldRateRank = yieldRateRank;
    }

    public int getYieldRateRankYes() {
        return yieldRateRankYes;
    }

    public void setYieldRateRankYes(int yieldRateRankYes) {
        this.yieldRateRankYes = yieldRateRankYes;
    }

    public int getYieldRateRankChange() {
        return yieldRateRankChange;
    }

    public void setYieldRateRankChange(int yieldRateRankChange) {
        this.yieldRateRankChange = yieldRateRankChange;
    }

    public String getTimeRules() {
        return timeRules;
    }

    public void setTimeRules(String timeRules) {
        this.timeRules = timeRules;
    }

    public String getYieldWdwrt() {
        return yieldWdwrt;
    }

    public void setYieldWdwrt(String yieldWdwrt) {
        this.yieldWdwrt = yieldWdwrt;
    }
}
