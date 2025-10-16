package com.dahuaboke.mpda.bot.tools.entity;

import com.dahuaboke.mpda.core.rag.entity.FieldComment;

/**
 * @Desc: 基金产品信息季报实体类
 * @Author：zhh
 * @Date：2025/8/7 14:11
 */
public class BrProductReport {

    private String id;


    /**
     * 基金代码
     **/
    private String fundCode;

    /**
     * 基金全称
     **/
    private String fundFnm;

    /**
     * 基金简称
     **/
    private String prodtSname;

    /**
     * 季报时间
     **/
    @FieldComment(question = "季报时间", keyWord = "季报时间")
    private String rptTime;

    /**
     * 基金经理名字
     **/
    @FieldComment(question = "基金经理名字(所有且完整内容)", keyWord = "基金经理名字")
    private String investMgrName;

    /**
     * 基金经理任职日期
     **/
    @FieldComment(question = "基金经理任职日期(所有且完整内容)", keyWord = "基金经理任职日期")
    private String offcDt;

    /**
     * 基金经理证券从业年限
     **/
    @FieldComment(question = "基金经理证券从业年限(所有且完整内容)", keyWord = "基金经理证券从业年限")
    private String engageYearNum;

    /**
     * 基金经理说明
     **/
    @FieldComment(question = "基金经理说明(所有且完整内容)", keyWord = "基金经理说明")
    private String engagePersonDesc;

    /**
     * 基金经理开始担任本基金基金经理的日期
     **/
    @FieldComment(question = "基金经理开始担任本基金基金经理的日期(所有且完整内容)", keyWord = "基金经理开始担任本基金基金经理的日期")
    private String relatBgnTime;

    /**
     * 基金经理证券从业日期
     **/
    @FieldComment(question = "基金经理证券从业日期(所有且完整内容)", keyWord = "基金经理证券从业日期")
    private String competBgnTime;

    /**
     * 报告期末按行业分类的境内股票投资组合
     **/
    @FieldComment(question = "报告期末按行业分类的境内股票投资组合(完整表格,且包含子项)", keyWord = "报告期末按行业分类的境内股票投资组合")
    private String ltsrIvstStockInfo;

    /**
     * 报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细
     **/
    @FieldComment(question = "报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细(完整表格,且包含子项)", keyWord = "报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细")
    private String stockBondIssueSituDesc;

    /**
     * 报告期末按债券品种分类的债券投资组合
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合(完整表格,包含政策性金融债)", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String bondChrgIntInfo;

    /**
     * 报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String firstIssueBondBillNo;

    /**
     * 报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String pbcBuyRtslPamt;

    /**
     * 报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String amtAppoRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String plcyFdbtAmt;

    /**
     * 报告期末按债券品种分类的债券投资组合中企业债券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中企业债券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String beInvesCorpShrHoldRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String docBillRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String bibTmOcqn;

    /**
     * 报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细
     **/
    @FieldComment(question = "报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细(完整表格,且包含子项)", keyWord = "报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细")
    private String bondiDtl;

    /**
     * 报告期末基金资产组合情况
     **/
    @FieldComment(question = "报告期末基金资产组合情况(完整表格,且包含子项)", keyWord = "报告期末基金资产组合情况")
    private String assetInfo;

    /**
     * 本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较
     **/
    @FieldComment(question = "本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较(完整表格,且包含子项)", keyWord = "本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较")
    private String fundstgSumProfrat;

    /**
     * 期末基金资产净值
     **/
    @FieldComment(question = "期末基金资产净值(只要值,如果获取不到,返回0.00)", keyWord = "期末基金资产净值")
    private String assetNval;

    /**
     * 报告期期末基金份额总额
     **/
    @FieldComment(question = "报告期期末基金份额总额", keyWord = "报告期期末基金份额总额")
    private String projTotLimt;

    /**
     * 基金分类(代码)
     **/
    private String pcrRptfClsRsnCd;

    /**
     * 基金分类原因(代码)
     **/
    private String adjReason;

    /**
     * 基金分类(模型)
     **/
    private String clsReasonCode;

    /**
     * 基金分类原因(模型)
     **/
    private String mainReason;



    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundFnm() {
        return fundFnm;
    }

    public void setFundFnm(String fundFnm) {
        this.fundFnm = fundFnm;
    }

    public String getProdtSname() {
        return prodtSname;
    }

    public void setProdtSname(String prodtSname) {
        this.prodtSname = prodtSname;
    }

    public String getRptTime() {
        return rptTime;
    }

    public void setRptTime(String rptTime) {
        this.rptTime = rptTime;
    }

    public String getInvestMgrName() {
        return investMgrName;
    }

    public void setInvestMgrName(String investMgrName) {
        this.investMgrName = investMgrName;
    }

    public String getOffcDt() {
        return offcDt;
    }

    public void setOffcDt(String offcDt) {
        this.offcDt = offcDt;
    }

    public String getEngageYearNum() {
        return engageYearNum;
    }

    public void setEngageYearNum(String engageYearNum) {
        this.engageYearNum = engageYearNum;
    }

    public String getEngagePersonDesc() {
        return engagePersonDesc;
    }

    public void setEngagePersonDesc(String engagePersonDesc) {
        this.engagePersonDesc = engagePersonDesc;
    }

    public String getRelatBgnTime() {
        return relatBgnTime;
    }

    public void setRelatBgnTime(String relatBgnTime) {
        this.relatBgnTime = relatBgnTime;
    }

    public String getCompetBgnTime() {
        return competBgnTime;
    }

    public void setCompetBgnTime(String competBgnTime) {
        this.competBgnTime = competBgnTime;
    }

    public String getLtsrIvstStockInfo() {
        return ltsrIvstStockInfo;
    }

    public void setLtsrIvstStockInfo(String ltsrIvstStockInfo) {
        this.ltsrIvstStockInfo = ltsrIvstStockInfo;
    }

    public String getStockBondIssueSituDesc() {
        return stockBondIssueSituDesc;
    }

    public void setStockBondIssueSituDesc(String stockBondIssueSituDesc) {
        this.stockBondIssueSituDesc = stockBondIssueSituDesc;
    }

    public String getBondChrgIntInfo() {
        return bondChrgIntInfo;
    }

    public void setBondChrgIntInfo(String bondChrgIntInfo) {
        this.bondChrgIntInfo = bondChrgIntInfo;
    }

    public String getFirstIssueBondBillNo() {
        return firstIssueBondBillNo;
    }

    public void setFirstIssueBondBillNo(String firstIssueBondBillNo) {
        this.firstIssueBondBillNo = firstIssueBondBillNo;
    }

    public String getPbcBuyRtslPamt() {
        return pbcBuyRtslPamt;
    }

    public void setPbcBuyRtslPamt(String pbcBuyRtslPamt) {
        this.pbcBuyRtslPamt = pbcBuyRtslPamt;
    }

    public String getAmtAppoRatio() {
        return amtAppoRatio;
    }

    public void setAmtAppoRatio(String amtAppoRatio) {
        this.amtAppoRatio = amtAppoRatio;
    }

    public String getPlcyFdbtAmt() {
        return plcyFdbtAmt;
    }

    public void setPlcyFdbtAmt(String plcyFdbtAmt) {
        this.plcyFdbtAmt = plcyFdbtAmt;
    }

    public String getBeInvesCorpShrHoldRatio() {
        return beInvesCorpShrHoldRatio;
    }

    public void setBeInvesCorpShrHoldRatio(String beInvesCorpShrHoldRatio) {
        this.beInvesCorpShrHoldRatio = beInvesCorpShrHoldRatio;
    }

    public String getDocBillRatio() {
        return docBillRatio;
    }

    public void setDocBillRatio(String docBillRatio) {
        this.docBillRatio = docBillRatio;
    }

    public String getBibTmOcqn() {
        return bibTmOcqn;
    }

    public void setBibTmOcqn(String bibTmOcqn) {
        this.bibTmOcqn = bibTmOcqn;
    }

    public String getBondiDtl() {
        return bondiDtl;
    }

    public void setBondiDtl(String bondiDtl) {
        this.bondiDtl = bondiDtl;
    }

    public String getAssetInfo() {
        return assetInfo;
    }

    public void setAssetInfo(String assetInfo) {
        this.assetInfo = assetInfo;
    }

    public String getFundstgSumProfrat() {
        return fundstgSumProfrat;
    }

    public void setFundstgSumProfrat(String fundstgSumProfrat) {
        this.fundstgSumProfrat = fundstgSumProfrat;
    }

    public String getAssetNval() {
        return assetNval;
    }

    public void setAssetNval(String assetNval) {
        this.assetNval = assetNval;
    }

    public String getProjTotLimt() {
        return projTotLimt;
    }

    public void setProjTotLimt(String projTotLimt) {
        this.projTotLimt = projTotLimt;
    }

    public String getPcrRptfClsRsnCd() {
        return pcrRptfClsRsnCd;
    }

    public void setPcrRptfClsRsnCd(String pcrRptfClsRsnCd) {
        this.pcrRptfClsRsnCd = pcrRptfClsRsnCd;
    }

    public String getAdjReason() {
        return adjReason;
    }

    public void setAdjReason(String adjReason) {
        this.adjReason = adjReason;
    }

    public String getClsReasonCode() {
        return clsReasonCode;
    }

    public void setClsReasonCode(String clsReasonCode) {
        this.clsReasonCode = clsReasonCode;
    }

    public String getMainReason() {
        return mainReason;
    }

    public void setMainReason(String mainReason) {
        this.mainReason = mainReason;
    }

}
