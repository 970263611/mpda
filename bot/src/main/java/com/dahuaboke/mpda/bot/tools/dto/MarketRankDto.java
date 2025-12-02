package com.dahuaboke.mpda.bot.tools.dto;


import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;

public class MarketRankDto extends BrMarketProductReport {

    /**
     * 基金名称
     */
    private String fundFnm;

    /**
     * 期末基金资产净值（基金规模）
     **/
    private String assetNval;

    /**
     * 基金管理人
     **/
    private String fundMgrName;

    /**
     * 基金成立日
     **/
    private String contractEffDate;

    /**
     * 基金经理
     */
    private String investMgrName;

    /**
     * 存续天数
     */
    private int curRenewDt;

    /**
     * 单位净值
     */
    private String unitNetVal;

    /**
     * 当前日期   {2025年5月1日}
     */
    private String curDate;

    /**
     * 下载文件名   全市场{信用债-指数型}债基排名{2025}{Q1}
     */
    private String dldFlnm;


    /**
     * 时间范围
     */
    private String period;

    /**
     * 支持查询的最新季度-逻辑=文件名中季度
     */
    private String quarterRule;


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

    public String getFundFnm() {
        return fundFnm;
    }

    public void setFundFnm(String fundFnm) {
        this.fundFnm = fundFnm;
    }

    public String getContractEffDate() {
        return contractEffDate;
    }

    public void setContractEffDate(String contractEffDate) {
        this.contractEffDate = contractEffDate;
    }

    public String getInvestMgrName() {
        return investMgrName;
    }

    public void setInvestMgrName(String investMgrName) {
        this.investMgrName = investMgrName;
    }

    public int getCurRenewDt() {
        return curRenewDt;
    }

    public void setCurRenewDt(int curRenewDt) {
        this.curRenewDt = curRenewDt;
    }

    public String getUnitNetVal() {
        return unitNetVal;
    }

    public void setUnitNetVal(String unitNetVal) {
        this.unitNetVal = unitNetVal;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getDldFlnm() {
        return dldFlnm;
    }

    public void setDldFlnm(String dldFlnm) {
        this.dldFlnm = dldFlnm;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getQuarterRule() {
        return quarterRule;
    }

    public void setQuarterRule(String quarterRule) {
        this.quarterRule = quarterRule;
    }

    @Override
    public String toString() {
        String s = super.toString();
        String s1 = "MarketRankDto{" +
                "fundFnm='" + fundFnm + '\'' +
                ", assetNval='" + assetNval + '\'' +
                ", fundMgrName='" + fundMgrName + '\'' +
                ", contractEffDate='" + contractEffDate + '\'' +
                ", investMgrName='" + investMgrName + '\'' +
                ", curRenewDt=" + curRenewDt +
                ", unitNetVal='" + unitNetVal + '\'' +
                ", curDate='" + curDate + '\'' +
                ", dldFlnm='" + dldFlnm + '\'' +
                ", period='" + period + '\'' +
                ", quarterRule='" + quarterRule + '\'' +
                '}';
        return s1 + s;
    }
}
