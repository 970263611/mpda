package com.dahuaboke.mpda.bot.tools.dto;


public class QuarterYieldRateInfoDto {

    /**
     * 【】年第【】季度收益率
     */
    private String quarterYieldRate;

    /**
     * 【】年第【】季度收益率排名
     **/
    private int quarterYieldRateRank;

    /**
     * 年份季度
     **/
    private String yearQuarter;

    public QuarterYieldRateInfoDto(String quarterYieldRate, int quarterYieldRateRank, String yearQuarter) {
        this.quarterYieldRate = quarterYieldRate;
        this.quarterYieldRateRank = quarterYieldRateRank;
        this.yearQuarter = yearQuarter;
    }

    public QuarterYieldRateInfoDto() {
    }

    public String getQuarterYieldRate() {
        return quarterYieldRate;
    }

    public void setQuarterYieldRate(String quarterYieldRate) {
        this.quarterYieldRate = quarterYieldRate;
    }

    public int getQuarterYieldRateRank() {
        return quarterYieldRateRank;
    }

    public void setQuarterYieldRateRank(int quarterYieldRateRank) {
        this.quarterYieldRateRank = quarterYieldRateRank;
    }

    public String getYearQuarter() {
        return yearQuarter;
    }

    public void setYearQuarter(String yearQuarter) {
        this.yearQuarter = yearQuarter;
    }
}
