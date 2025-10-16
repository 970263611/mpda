package com.dahuaboke.mpda.bot.tools.dto;


public class MarketRankDto {

    /**
     * 基金代码
     **/
    private String fundCode;

    /**
     * 期末基金资产净值
     **/
    private String assetNval;

    /**
     * 基金管理人
     **/
    private String fundMgrName;

    /**
     * 季报时间
     **/
    private String rptTime;

    /**
     * 近一年收益率
     */
    private String nyy1Profrat;

    /**
     * 近一年收益率排名
     */
    private int reachStRankSeqNo;

    /**
     * 近一年收益率昨日排名
     */
    private int chremMgrIntglRankSeqNo;

    /**
     * 近一年收益率排名变动
     */
    private int centerCfmCurdayChgTnum;

    /**
     * 近一年最大回撤
     */
    private String nyy1Wdwrt;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getAssetNval() {
        return assetNval;
    }

    public void setAssetNval(String assetNval) {
        this.assetNval = assetNval;
    }

    public String getFundMgrName() {
        return fundMgrName;
    }

    public void setFundMgrName(String fundMgrName) {
        this.fundMgrName = fundMgrName;
    }

    public String getRptTime() {
        return rptTime;
    }

    public void setRptTime(String rptTime) {
        this.rptTime = rptTime;
    }

    public String getNyy1Profrat() {
        return nyy1Profrat;
    }

    public void setNyy1Profrat(String nyy1Profrat) {
        this.nyy1Profrat = nyy1Profrat;
    }

    public int getReachStRankSeqNo() {
        return reachStRankSeqNo;
    }

    public void setReachStRankSeqNo(int reachStRankSeqNo) {
        this.reachStRankSeqNo = reachStRankSeqNo;
    }

    public int getChremMgrIntglRankSeqNo() {
        return chremMgrIntglRankSeqNo;
    }

    public void setChremMgrIntglRankSeqNo(int chremMgrIntglRankSeqNo) {
        this.chremMgrIntglRankSeqNo = chremMgrIntglRankSeqNo;
    }

    public int getCenterCfmCurdayChgTnum() {
        return centerCfmCurdayChgTnum;
    }

    public void setCenterCfmCurdayChgTnum(int centerCfmCurdayChgTnum) {
        this.centerCfmCurdayChgTnum = centerCfmCurdayChgTnum;
    }

    public String getNyy1Wdwrt() {
        return nyy1Wdwrt;
    }

    public void setNyy1Wdwrt(String nyy1Wdwrt) {
        this.nyy1Wdwrt = nyy1Wdwrt;
    }

}
