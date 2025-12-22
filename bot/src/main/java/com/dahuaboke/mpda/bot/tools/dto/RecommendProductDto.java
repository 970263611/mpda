package com.dahuaboke.mpda.bot.tools.dto;

public class RecommendProductDto {

    /**
     * 基金代码
     **/
    private String fundCode;

    /**
     * 基金类型
     **/
    private String prodtClsCode;

    /**
     * 基金全称
     **/
    private String fundFnm;

    /**
     * 基金经理名字
     **/
    private String investMgrName;

    /**
     * 期末基金资产净值
     **/
    private String assetNval;

    /**
     * 基金管理人
     **/
    private String fundMgrName;

    /**
     * 基金托管人
     **/
    private String trusteePersName;

    /**
     * 风险收益特征
     **/
    private String riskProfitCoeff;

    /**
     * 收益率
     */
    private Double rate;

    /**
     * 收益率时间周期
     */
    private String rateTimePeriod;

    /**
     * 最大回撤
     */
    private Double maxWithDraw;

    /**
     * 最大回撤时间周期
     */
    private String drawTimePeriod;

    /**
     * 七日年化 只有货基有
     */
    private Double sevenDayYearlyProfrat;


    /**
     * 万份收益 只有货基有
     */
    private Double thouCopFundUnitProfit;


    public void setThouCopFundUnitProfit(Double thouCopFundUnitProfit) {
        this.thouCopFundUnitProfit = thouCopFundUnitProfit;
    }

    public RecommendProductDto() {
    }

    public RecommendProductDto(String fundCode, String prodtClsCode, String fundFnm, String investMgrName, String assetNval, String fundMgrName, String trusteePersName, String riskProfitCoeff, Double rate, String rateTimePeriod, Double maxWithDraw, String drawTimePeriod, Double sevenDayYearlyProfrat, Double thouCopFundUnitProfit) {
        this.fundCode = fundCode;
        this.prodtClsCode = prodtClsCode;
        this.fundFnm = fundFnm;
        this.investMgrName = investMgrName;
        this.assetNval = assetNval;
        this.fundMgrName = fundMgrName;
        this.trusteePersName = trusteePersName;
        this.riskProfitCoeff = riskProfitCoeff;
        this.rate = rate;
        this.rateTimePeriod = rateTimePeriod;
        this.maxWithDraw = maxWithDraw;
        this.drawTimePeriod = drawTimePeriod;
        this.sevenDayYearlyProfrat = sevenDayYearlyProfrat;
        this.thouCopFundUnitProfit = thouCopFundUnitProfit;
    }

    @Override
    public String toString() {
        return "RecommendProductDto{" +
                "fundCode='" + fundCode + '\'' +
                ", prodtClsCode='" + prodtClsCode + '\'' +
                ", fundFnm='" + fundFnm + '\'' +
                ", investMgrName='" + investMgrName + '\'' +
                ", assetNval='" + assetNval + '\'' +
                ", fundMgrName='" + fundMgrName + '\'' +
                ", trusteePersName='" + trusteePersName + '\'' +
                ", riskProfitCoeff='" + riskProfitCoeff + '\'' +
                ", rate=" + rate +
                ", rateTimePeriod='" + rateTimePeriod + '\'' +
                ", maxWithDraw=" + maxWithDraw +
                ", drawTimePeriod='" + drawTimePeriod + '\'' +
                ", sevenDayYearlyProfrat='" + sevenDayYearlyProfrat + '\'' +
                ", thouCopFundUnitProfit=" + thouCopFundUnitProfit +
                '}';
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getProdtClsCode() {
        return prodtClsCode;
    }

    public void setProdtClsCode(String prodtClsCode) {
        this.prodtClsCode = prodtClsCode;
    }

    public String getFundFnm() {
        return fundFnm;
    }

    public void setFundFnm(String fundFnm) {
        this.fundFnm = fundFnm;
    }

    public String getInvestMgrName() {
        return investMgrName;
    }

    public void setInvestMgrName(String investMgrName) {
        this.investMgrName = investMgrName;
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

    public String getTrusteePersName() {
        return trusteePersName;
    }

    public void setTrusteePersName(String trusteePersName) {
        this.trusteePersName = trusteePersName;
    }

    public String getRiskProfitCoeff() {
        return riskProfitCoeff;
    }

    public void setRiskProfitCoeff(String riskProfitCoeff) {
        this.riskProfitCoeff = riskProfitCoeff;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getMaxWithDraw() {
        return maxWithDraw;
    }

    public void setMaxWithDraw(Double maxWithDraw) {
        this.maxWithDraw = maxWithDraw;
    }

    public String getRateTimePeriod() {
        return rateTimePeriod;
    }

    public void setRateTimePeriod(String rateTimePeriod) {
        this.rateTimePeriod = rateTimePeriod;
    }

    public String getDrawTimePeriod() {
        return drawTimePeriod;
    }

    public void setDrawTimePeriod(String drawTimePeriod) {
        this.drawTimePeriod = drawTimePeriod;
    }

    public Double getThouCopFundUnitProfit() {
        return thouCopFundUnitProfit;
    }

    public Double getSevenDayYearlyProfrat() {
        return sevenDayYearlyProfrat;
    }

    public void setSevenDayYearlyProfrat(Double sevenDayYearlyProfrat) {
        this.sevenDayYearlyProfrat = sevenDayYearlyProfrat;
    }

}
