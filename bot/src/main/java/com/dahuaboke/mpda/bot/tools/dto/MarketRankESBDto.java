package com.dahuaboke.mpda.bot.tools.dto;


import java.util.List;

public class MarketRankESBDto {

    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 债券基金类型
     */
    private String finBondType;

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
     * 季度信息排名信息
     */
    private List<QuarterYieldRateInfoDto> quarterYieldRateInfoDtoList;

    /**
     * 各时间排名信息
     */
    private List<YieldRateInfoDto> yieldRateInfoDtoList;

    /**
     * 今年以来收益率（年化）
     */
    private String pftrtName;

    /**
     * 今年以来排名（年化）
     */
    private int busicmOybinpRank;

    public MarketRankESBDto(String fundCode, String finBondType, String fundFnm, String assetNval, String fundMgrName, String contractEffDate, String investMgrName, int curRenewDt, String unitNetVal, String curDate, String dldFlnm, List<QuarterYieldRateInfoDto> quarterYieldRateInfoDtoList, List<YieldRateInfoDto> yieldRateInfoDtoList, String pftrtName, int busicmOybinpRank) {
        this.fundCode = fundCode;
        this.finBondType = finBondType;
        this.fundFnm = fundFnm;
        this.assetNval = assetNval;
        this.fundMgrName = fundMgrName;
        this.contractEffDate = contractEffDate;
        this.investMgrName = investMgrName;
        this.curRenewDt = curRenewDt;
        this.unitNetVal = unitNetVal;
        this.curDate = curDate;
        this.dldFlnm = dldFlnm;
        this.quarterYieldRateInfoDtoList = quarterYieldRateInfoDtoList;
        this.yieldRateInfoDtoList = yieldRateInfoDtoList;
        this.pftrtName = pftrtName;
        this.busicmOybinpRank = busicmOybinpRank;
    }

    public MarketRankESBDto() {
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFinBondType() {
        return finBondType;
    }

    public void setFinBondType(String finBondType) {
        this.finBondType = finBondType;
    }

    public String getFundFnm() {
        return fundFnm;
    }

    public void setFundFnm(String fundFnm) {
        this.fundFnm = fundFnm;
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

    public List<QuarterYieldRateInfoDto> getQuarterYieldRateInfoDtoList() {
        return quarterYieldRateInfoDtoList;
    }

    public void setQuarterYieldRateInfoDtoList(List<QuarterYieldRateInfoDto> quarterYieldRateInfoDtoList) {
        this.quarterYieldRateInfoDtoList = quarterYieldRateInfoDtoList;
    }

    public List<YieldRateInfoDto> getYieldRateInfoDtoList() {
        return yieldRateInfoDtoList;
    }

    public void setYieldRateInfoDtoList(List<YieldRateInfoDto> yieldRateInfoDtoList) {
        this.yieldRateInfoDtoList = yieldRateInfoDtoList;
    }

    public String getPftrtName() {
        return pftrtName;
    }

    public void setPftrtName(String pftrtName) {
        this.pftrtName = pftrtName;
    }

    public int getBusicmOybinpRank() {
        return busicmOybinpRank;
    }

    public void setBusicmOybinpRank(int busicmOybinpRank) {
        this.busicmOybinpRank = busicmOybinpRank;
    }
}
