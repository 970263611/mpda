package com.dahuaboke.mpda.bot.tools.entity;

import com.dahuaboke.mpda.bot.utils.FieldTranslation;

/**
 * @Desc: 市场产品报告实体类
 * @Author：zhh
 * @Date：2025/10/9 17:09
 */
public class BrMarketProductReport {

    /**
     * 基金代码
     */
    @FieldTranslation(value = "基金代码", order = 1)
    private String fundCode;

    /**
     * 债券基金类型	1-信用债-指数型，2-信用债主动-开放式，3-利率债主动-开放式，4-利率债指数1-3年，5-利率债指数3-5年，6-利率债指数1-5年
     */
    @FieldTranslation(value = "债券基金类型", order = 2)
    private String finBondType;

    /**
     * 近一周收益率
     */
    @FieldTranslation(value = "近一周收益率", order = 10)
    private String nwk1CombProfrat;

    /**
     * 近一周收益率排名
     */
    @FieldTranslation(value = "近一周收益率排名", order = 11)
    private int indsRankSeqNo;

    /**
     * 近一周收益率昨日排名
     */
    @FieldTranslation(value = "近一周收益率昨日排名", order = 12)
    private int txamtRankNo;

    /**
     * 近一周收益率排名变动
     */
    @FieldTranslation(value = "近一周收益率排名变动", order = 13)
    private int curdayBalChgTotalAccnum;

    /**
     * 近一周最大回撤
     */
    @FieldTranslation(value = "近一周最大回撤", order = 14)
    private String styoMaxWdwDesc;

    /**
     * 近一月收益率
     */
    @FieldTranslation(value = "近一月收益率", order = 15)
    private String nmm1CombProfrat;

    /**
     * 近一月收益率排名
     */
    @FieldTranslation(value = "近一月收益率排名", order = 16)
    private int lblmRank;

    /**
     * 近一月收益率昨日排名
     */
    @FieldTranslation(value = "近一月收益率昨日排名", order = 17)
    private int busicmLmRank;

    /**
     * 近一月收益率排名变动
     */
    @FieldTranslation(value = "近一月收益率排名变动", order = 18)
    private int curdayBalChgAccnum;

    /**
     * 近一月最大回撤
     */
    @FieldTranslation(value = "近一月最大回撤", order = 19)
    private String maxWdwrt;

    /**
     * 近三月收益率
     */
    @FieldTranslation(value = "近三月收益率", order = 20)
    private String nmm3CombProfrat;

    /**
     * 近三月收益率排名
     */
    @FieldTranslation(value = "近三月收益率排名", order = 21)
    private int rankScopeLowLmtVal;

    /**
     * 近三月收益率昨日排名
     */
    @FieldTranslation(value = "近三月收益率昨日排名", order = 22)
    private int intglRankSeqNo;

    /**
     * 近三月收益率排名变动
     */
    @FieldTranslation(value = "近三月收益率排名变动", order = 23)
    private int supptranBalChgTotalAccnum;

    /**
     * 近三月最大回撤
     */
    @FieldTranslation(value = "近三月最大回撤", order = 24)
    private String fundstgMaxWdwrt;

    /**
     * 近一年收益率
     */
    @FieldTranslation(value = "近一年收益率", order = 25)
    private String nyy1Profrat;

    /**
     * 近一年收益率排名
     */
    @FieldTranslation(value = "近一年收益率排名", order = 26)
    private int reachStRankSeqNo;

    /**
     * 近一年收益率昨日排名
     */
    @FieldTranslation(value = "近一年收益率昨日排名", order = 27)
    private int chremMgrIntglRankSeqNo;

    /**
     * 近一年收益率排名变动
     */
    @FieldTranslation(value = "近一年收益率排名变动", order = 28)
    private int centerCfmCurdayChgTnum;

    /**
     * 近一年最大回撤
     */
    @FieldTranslation(value = "近一年最大回撤", order = 29)
    private String nyy1Wdwrt;

    /**
     * 今年以来收益率
     */
    @FieldTranslation(value = "今年以来收益率", order = 30)
    private String drtPftrtTval;

    /**
     * 今年以来收益率排名
     */
    @FieldTranslation(value = "今年以来收益率排名", order = 31)
    private int rtnRtRank;

    /**
     * 今年以来收益率(年化)
     */
    @FieldTranslation(value = "今年以来收益率(年化)", order = 32)
    private String pftrtName;

    /**
     * 今年以来收益率排名（年化）
     */
    @FieldTranslation(value = "今年以来收益率排名（年化）", order = 33)
    private int busicmOybinpRank;

    /**
     * xxxx年第1季度收益率
     */
    @FieldTranslation(value = "第1季度收益率", order = 34)
    private String lastYrlyPftrt;

    /**
     * xxxx年第1季度收益率排名
     */
    @FieldTranslation(value = "第1季度收益率排名", order = 35)
    private int custRaiseRateRankNo;

    /**
     * xxxx年第2季度收益率
     */
    @FieldTranslation(value = "第2季度收益率", order = 36)
    private String nmm6CombProfrat;

    /**
     * xxxx年第2季度收益率排名
     */
    @FieldTranslation(value = "第2季度收益率排名", order = 37)
    private int detainRateRankNo;

    /**
     * xxxx年第3季度收益率
     */
    @FieldTranslation(value = "第3季度收益率", order = 38)
    private String nmm3YrlyPftrt;

    /**
     * xxxx年第3季度收益率排名
     */
    @FieldTranslation(value = "第3季度收益率排名", order = 39)
    private int tmPontAsetRaiseTotRanknum;

    /**
     * xxxx年第4季度收益率
     */
    @FieldTranslation(value = "第4季度收益率", order = 40)
    private String nmm1YearlyProfrat;

    /**
     * xxxx年第4季度收益率排名
     */
    @FieldTranslation(value = "第4季度收益率排名", order = 41)
    private int addRepPurcProTotnumRankno;

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

    public String getNwk1CombProfrat() {
        return nwk1CombProfrat;
    }

    public void setNwk1CombProfrat(String nwk1CombProfrat) {
        this.nwk1CombProfrat = nwk1CombProfrat;
    }

    public int getIndsRankSeqNo() {
        return indsRankSeqNo;
    }

    public void setIndsRankSeqNo(int indsRankSeqNo) {
        this.indsRankSeqNo = indsRankSeqNo;
    }

    public int getTxamtRankNo() {
        return txamtRankNo;
    }

    public void setTxamtRankNo(int txamtRankNo) {
        this.txamtRankNo = txamtRankNo;
    }

    public int getCurdayBalChgTotalAccnum() {
        return curdayBalChgTotalAccnum;
    }

    public void setCurdayBalChgTotalAccnum(int curdayBalChgTotalAccnum) {
        this.curdayBalChgTotalAccnum = curdayBalChgTotalAccnum;
    }

    public String getNmm1CombProfrat() {
        return nmm1CombProfrat;
    }

    public void setNmm1CombProfrat(String nmm1CombProfrat) {
        this.nmm1CombProfrat = nmm1CombProfrat;
    }

    public int getLblmRank() {
        return lblmRank;
    }

    public void setLblmRank(int lblmRank) {
        this.lblmRank = lblmRank;
    }

    public int getBusicmLmRank() {
        return busicmLmRank;
    }

    public void setBusicmLmRank(int busicmLmRank) {
        this.busicmLmRank = busicmLmRank;
    }

    public int getCurdayBalChgAccnum() {
        return curdayBalChgAccnum;
    }

    public void setCurdayBalChgAccnum(int curdayBalChgAccnum) {
        this.curdayBalChgAccnum = curdayBalChgAccnum;
    }

    public String getNmm3CombProfrat() {
        return nmm3CombProfrat;
    }

    public void setNmm3CombProfrat(String nmm3CombProfrat) {
        this.nmm3CombProfrat = nmm3CombProfrat;
    }

    public int getRankScopeLowLmtVal() {
        return rankScopeLowLmtVal;
    }

    public void setRankScopeLowLmtVal(int rankScopeLowLmtVal) {
        this.rankScopeLowLmtVal = rankScopeLowLmtVal;
    }

    public int getIntglRankSeqNo() {
        return intglRankSeqNo;
    }

    public void setIntglRankSeqNo(int intglRankSeqNo) {
        this.intglRankSeqNo = intglRankSeqNo;
    }

    public int getSupptranBalChgTotalAccnum() {
        return supptranBalChgTotalAccnum;
    }

    public void setSupptranBalChgTotalAccnum(int supptranBalChgTotalAccnum) {
        this.supptranBalChgTotalAccnum = supptranBalChgTotalAccnum;
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

    public String getLastYrlyPftrt() {
        return lastYrlyPftrt;
    }

    public void setLastYrlyPftrt(String lastYrlyPftrt) {
        this.lastYrlyPftrt = lastYrlyPftrt;
    }

    public int getCustRaiseRateRankNo() {
        return custRaiseRateRankNo;
    }

    public void setCustRaiseRateRankNo(int custRaiseRateRankNo) {
        this.custRaiseRateRankNo = custRaiseRateRankNo;
    }

    public String getNmm6CombProfrat() {
        return nmm6CombProfrat;
    }

    public void setNmm6CombProfrat(String nmm6CombProfrat) {
        this.nmm6CombProfrat = nmm6CombProfrat;
    }

    public int getDetainRateRankNo() {
        return detainRateRankNo;
    }

    public void setDetainRateRankNo(int detainRateRankNo) {
        this.detainRateRankNo = detainRateRankNo;
    }

    public String getNmm3YrlyPftrt() {
        return nmm3YrlyPftrt;
    }

    public void setNmm3YrlyPftrt(String nmm3YrlyPftrt) {
        this.nmm3YrlyPftrt = nmm3YrlyPftrt;
    }

    public int getTmPontAsetRaiseTotRanknum() {
        return tmPontAsetRaiseTotRanknum;
    }

    public void setTmPontAsetRaiseTotRanknum(int tmPontAsetRaiseTotRanknum) {
        this.tmPontAsetRaiseTotRanknum = tmPontAsetRaiseTotRanknum;
    }

    public String getNmm1YearlyProfrat() {
        return nmm1YearlyProfrat;
    }

    public void setNmm1YearlyProfrat(String nmm1YearlyProfrat) {
        this.nmm1YearlyProfrat = nmm1YearlyProfrat;
    }

    public int getAddRepPurcProTotnumRankno() {
        return addRepPurcProTotnumRankno;
    }

    public void setAddRepPurcProTotnumRankno(int addRepPurcProTotnumRankno) {
        this.addRepPurcProTotnumRankno = addRepPurcProTotnumRankno;
    }

    public String getStyoMaxWdwDesc() {
        return styoMaxWdwDesc;
    }

    public void setStyoMaxWdwDesc(String styoMaxWdwDesc) {
        this.styoMaxWdwDesc = styoMaxWdwDesc;
    }

    public String getMaxWdwrt() {
        return maxWdwrt;
    }

    public void setMaxWdwrt(String maxWdwrt) {
        this.maxWdwrt = maxWdwrt;
    }

    public String getFundstgMaxWdwrt() {
        return fundstgMaxWdwrt;
    }

    public void setFundstgMaxWdwrt(String fundstgMaxWdwrt) {
        this.fundstgMaxWdwrt = fundstgMaxWdwrt;
    }

    public String getNyy1Wdwrt() {
        return nyy1Wdwrt;
    }

    public void setNyy1Wdwrt(String nyy1Wdwrt) {
        this.nyy1Wdwrt = nyy1Wdwrt;
    }

    public String getDrtPftrtTval() {
        return drtPftrtTval;
    }

    public void setDrtPftrtTval(String drtPftrtTval) {
        this.drtPftrtTval = drtPftrtTval;
    }

    public int getRtnRtRank() {
        return rtnRtRank;
    }

    public void setRtnRtRank(int rtnRtRank) {
        this.rtnRtRank = rtnRtRank;
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

    @Override
    public String toString() {
        return "BrMarketProductReport{" +
                "fundCode='" + fundCode + '\'' +
                ", finBondType='" + finBondType + '\'' +
                ", nwk1CombProfrat='" + nwk1CombProfrat + '\'' +
                ", indsRankSeqNo=" + indsRankSeqNo +
                ", txamtRankNo=" + txamtRankNo +
                ", curdayBalChgTotalAccnum=" + curdayBalChgTotalAccnum +
                ", nmm1CombProfrat='" + nmm1CombProfrat + '\'' +
                ", lblmRank=" + lblmRank +
                ", busicmLmRank=" + busicmLmRank +
                ", curdayBalChgAccnum=" + curdayBalChgAccnum +
                ", nmm3CombProfrat='" + nmm3CombProfrat + '\'' +
                ", rankScopeLowLmtVal=" + rankScopeLowLmtVal +
                ", intglRankSeqNo=" + intglRankSeqNo +
                ", supptranBalChgTotalAccnum=" + supptranBalChgTotalAccnum +
                ", drtPftrtTval='" + drtPftrtTval + '\'' +
                ", rtnRtRank=" + rtnRtRank +
                ", pftrtName='" + pftrtName + '\'' +
                ", busicmOybinpRank=" + busicmOybinpRank +
                ", nyy1Profrat='" + nyy1Profrat + '\'' +
                ", reachStRankSeqNo=" + reachStRankSeqNo +
                ", chremMgrIntglRankSeqNo=" + chremMgrIntglRankSeqNo +
                ", centerCfmCurdayChgTnum=" + centerCfmCurdayChgTnum +
                ", lastYrlyPftrt='" + lastYrlyPftrt + '\'' +
                ", custRaiseRateRankNo=" + custRaiseRateRankNo +
                ", nmm6CombProfrat='" + nmm6CombProfrat + '\'' +
                ", detainRateRankNo=" + detainRateRankNo +
                ", nmm3YrlyPftrt='" + nmm3YrlyPftrt + '\'' +
                ", tmPontAsetRaiseTotRanknum=" + tmPontAsetRaiseTotRanknum +
                ", nmm1YearlyProfrat='" + nmm1YearlyProfrat + '\'' +
                ", addRepPurcProTotnumRankno=" + addRepPurcProTotnumRankno +
                ", styoMaxWdwDesc='" + styoMaxWdwDesc + '\'' +
                ", maxWdwrt='" + maxWdwrt + '\'' +
                ", fundstgMaxWdwrt='" + fundstgMaxWdwrt + '\'' +
                ", nyy1Wdwrt='" + nyy1Wdwrt + '\'' +
                '}';
    }
}
