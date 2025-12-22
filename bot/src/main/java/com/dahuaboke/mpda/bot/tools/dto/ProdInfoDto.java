package com.dahuaboke.mpda.bot.tools.dto;

import com.dahuaboke.mpda.bot.utils.FieldTranslation;

public class ProdInfoDto {


    /**
     * 基金代码
     **/
    @FieldTranslation(value = "基金代码", order = -5)
    private String fundCode;

    /**
     * 基金类型
     */
    @FieldTranslation(value = "基金类型", order = -3)
    private String prodtClsCode;

    /**
     * 基金全称
     **/
    @FieldTranslation(value = "基金全称", order = -6)
    private String fundFnm;

    /**
     * 基金简称
     **/
    @FieldTranslation(value = "基金简称")
    private String prodtSname;

    /**
     * 季报时间
     **/
    private String rptTime;

    /**
     * 基金经理名字
     **/
    @FieldTranslation(value = "基金经理", order = -2)
    private String investMgrName;

    /**
     * 基金经理任职日期
     **/
    @FieldTranslation(value = "基金经理任职日期")
    private String offcDt;

    /**
     * 基金经理证券从业年限
     **/
    @FieldTranslation(value = "基金经理证券从业年限")
    private String engageYearNum;

    /**
     * 基金经理说明
     **/
    @FieldTranslation(value = "基金经理说明")
    private String engagePersonDesc;

    /**
     * 基金经理开始担任本基金基金经理的日期
     **/
    @FieldTranslation(value = "基金经理开始担任本基金基金经理的日期")
    private String relatBgnTime;

    /**
     * 基金经理证券从业日期
     **/
    @FieldTranslation(value = "基金经理证券从业日期")
    private String competBgnTime;

    /**
     * 报告期末按行业分类的境内股票投资组合
     **/
    @FieldTranslation(value = "报告期末按行业分类的境内股票投资组合")
    private String ltsrIvstStockInfo;

    /**
     * 报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细
     **/
    @FieldTranslation(value = "报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细")
    private String stockBondIssueSituDesc;

    /**
     * 报告期末按债券品种分类的债券投资组合
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合")
    private String bondChrgIntInfo;

    /**
     * 报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例")
    private String firstIssueBondBillNo;

    /**
     * 报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例")
    private String pbcBuyRtslPamt;

    /**
     * 报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例")
    private String amtAppoRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例")
    private String plcyFdbtAmt;

    /**
     * 报告期末按债券品种分类的债券投资组合中企业债券占基金资产净值比例
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合中企业债券占基金资产净值比例")
    private String beInvesCorpShrHoldRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例")
    private String docBillRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例
     **/
    @FieldTranslation(value = "报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例")
    private String bibTmOcqn;

    /**
     * 报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细
     **/
    @FieldTranslation(value = "报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细")
    private String bondiDtl;

    /**
     * 报告期末基金资产组合情况
     **/
    @FieldTranslation(value = "报告期末基金资产组合情况")
    private String assetInfo;

    /**
     * 本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较
     **/
    @FieldTranslation(value = "本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较")
    private String fundstgSumProfrat;

    /**
     * 期末基金资产净值
     **/
    @FieldTranslation(value = "基金规模", order = -1)
    private String assetNval;

    /**
     * 报告期期末基金份额总额
     **/
    @FieldTranslation(value = "报告期期末基金份额总额")
    private String projTotLimt;


    /**
     * 基金管理人
     **/
    @FieldTranslation(value = "基金公司", order = -4)
    private String fundMgrName;

    /**
     * 基金托管人
     **/
    @FieldTranslation(value = "基金托管人")
    private String trusteePersName;

    /**
     * 运作方式
     **/
    @FieldTranslation(value = "运作方式")
    private String fundOprModeCd;

    /**
     * 开放频率
     **/
    @FieldTranslation(value = "开放频率")
    private String exgRateUpdFreq;

    /**
     * 基金合同生效日
     **/
    @FieldTranslation(value = "基金合同生效日")
    private String contractEffDate;

    /**
     * 投资目标
     **/
    @FieldTranslation(value = "投资目标")
    private String investTargetCode;

    /**
     * 投资范围
     **/
    @FieldTranslation(value = "投资范围")
    private String investScope;

    /**
     * 主要投资策略
     **/
    @FieldTranslation(value = "主要投资策略")
    private String ivstStgyName;

    /**
     * 业绩比较基准
     **/
    @FieldTranslation(value = "业绩比较基准")
    private String performCmpBmkTxtDesc;

    /**
     * 风险收益特征
     **/
    @FieldTranslation(value = "风险收益特征")
    private String riskProfitCoeff;

    /**
     * 基金销售相关费用申购费
     **/
    @FieldTranslation(value = "基金销售相关费用申购费")
    private String fundAplypchsFee;

    /**
     * 基金销售相关费用赎回费
     **/
    @FieldTranslation(value = "基金销售相关费用赎回费")
    private String rdmFert;

    /**
     * 基金运作相关费用管理费
     **/
    @FieldTranslation(value = "基金运作相关费用管理费")
    private String excessMngFee;

    /**
     * 基金运作相关费用托管费
     **/
    @FieldTranslation(value = "基金运作相关费用托管费")
    private String fundTsfrt;

    /**
     * 基金运作相关费用销售服务费
     **/
    @FieldTranslation(value = "基金运作相关费用销售服务费")
    private String saleServFee;

    /**
     * 基金运作相关费用审计费用
     **/
    @FieldTranslation(value = "基金运作相关费用审计费用")
    private String auditFe;

    /**
     * 基金运作相关费用信息披露费
     **/
    @FieldTranslation(value = "基金运作相关费用信息披露费")
    private String infdclNppSumm;

    /**
     * 基金运作相关费用其他费用
     **/
    @FieldTranslation(value = "基金运作相关费用其他费用")
    private String afadjOthfe;

    /**
     * 基金运作综合费率（年化）
     **/
    @FieldTranslation(value = "基金运作综合费率（年化）")
    private String psbcChremCphsFert;

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


    public ProdInfoDto() {
    }

    public ProdInfoDto(String fundCode, String fundFnm, String prodtSname, String rptTime, String investMgrName, String offcDt, String engageYearNum, String engagePersonDesc, String relatBgnTime, String competBgnTime, String ltsrIvstStockInfo, String stockBondIssueSituDesc, String bondChrgIntInfo, String firstIssueBondBillNo, String pbcBuyRtslPamt, String amtAppoRatio, String plcyFdbtAmt, String beInvesCorpShrHoldRatio, String docBillRatio, String bibTmOcqn, String bondiDtl, String assetInfo, String fundstgSumProfrat, String assetNval, String projTotLimt, String fundMgrName, String trusteePersName, String fundOprModeCd, String exgRateUpdFreq, String contractEffDate, String investTargetCode, String investScope, String ivstStgyName, String performCmpBmkTxtDesc, String riskProfitCoeff, String fundAplypchsFee, String rdmFert, String excessMngFee, String fundTsfrt, String saleServFee, String auditFe, String infdclNppSumm, String afadjOthfe, String psbcChremCphsFert, String pcrRptfClsRsnCd, String adjReason, String clsReasonCode, String mainReason) {
        this.fundCode = fundCode;
        this.fundFnm = fundFnm;
        this.prodtSname = prodtSname;
        this.rptTime = rptTime;
        this.investMgrName = investMgrName;
        this.offcDt = offcDt;
        this.engageYearNum = engageYearNum;
        this.engagePersonDesc = engagePersonDesc;
        this.relatBgnTime = relatBgnTime;
        this.competBgnTime = competBgnTime;
        this.ltsrIvstStockInfo = ltsrIvstStockInfo;
        this.stockBondIssueSituDesc = stockBondIssueSituDesc;
        this.bondChrgIntInfo = bondChrgIntInfo;
        this.firstIssueBondBillNo = firstIssueBondBillNo;
        this.pbcBuyRtslPamt = pbcBuyRtslPamt;
        this.amtAppoRatio = amtAppoRatio;
        this.plcyFdbtAmt = plcyFdbtAmt;
        this.beInvesCorpShrHoldRatio = beInvesCorpShrHoldRatio;
        this.docBillRatio = docBillRatio;
        this.bibTmOcqn = bibTmOcqn;
        this.bondiDtl = bondiDtl;
        this.assetInfo = assetInfo;
        this.fundstgSumProfrat = fundstgSumProfrat;
        this.assetNval = assetNval;
        this.projTotLimt = projTotLimt;
        this.fundMgrName = fundMgrName;
        this.trusteePersName = trusteePersName;
        this.fundOprModeCd = fundOprModeCd;
        this.exgRateUpdFreq = exgRateUpdFreq;
        this.contractEffDate = contractEffDate;
        this.investTargetCode = investTargetCode;
        this.investScope = investScope;
        this.ivstStgyName = ivstStgyName;
        this.performCmpBmkTxtDesc = performCmpBmkTxtDesc;
        this.riskProfitCoeff = riskProfitCoeff;
        this.fundAplypchsFee = fundAplypchsFee;
        this.rdmFert = rdmFert;
        this.excessMngFee = excessMngFee;
        this.fundTsfrt = fundTsfrt;
        this.saleServFee = saleServFee;
        this.auditFe = auditFe;
        this.infdclNppSumm = infdclNppSumm;
        this.afadjOthfe = afadjOthfe;
        this.psbcChremCphsFert = psbcChremCphsFert;
        this.pcrRptfClsRsnCd = pcrRptfClsRsnCd;
        this.adjReason = adjReason;
        this.clsReasonCode = clsReasonCode;
        this.mainReason = mainReason;
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

    public String getFundOprModeCd() {
        return fundOprModeCd;
    }

    public void setFundOprModeCd(String fundOprModeCd) {
        this.fundOprModeCd = fundOprModeCd;
    }

    public String getExgRateUpdFreq() {
        return exgRateUpdFreq;
    }

    public void setExgRateUpdFreq(String exgRateUpdFreq) {
        this.exgRateUpdFreq = exgRateUpdFreq;
    }

    public String getContractEffDate() {
        return contractEffDate;
    }

    public void setContractEffDate(String contractEffDate) {
        this.contractEffDate = contractEffDate;
    }

    public String getInvestTargetCode() {
        return investTargetCode;
    }

    public void setInvestTargetCode(String investTargetCode) {
        this.investTargetCode = investTargetCode;
    }

    public String getInvestScope() {
        return investScope;
    }

    public void setInvestScope(String investScope) {
        this.investScope = investScope;
    }

    public String getIvstStgyName() {
        return ivstStgyName;
    }

    public void setIvstStgyName(String ivstStgyName) {
        this.ivstStgyName = ivstStgyName;
    }

    public String getPerformCmpBmkTxtDesc() {
        return performCmpBmkTxtDesc;
    }

    public void setPerformCmpBmkTxtDesc(String performCmpBmkTxtDesc) {
        this.performCmpBmkTxtDesc = performCmpBmkTxtDesc;
    }

    public String getRiskProfitCoeff() {
        return riskProfitCoeff;
    }

    public void setRiskProfitCoeff(String riskProfitCoeff) {
        this.riskProfitCoeff = riskProfitCoeff;
    }

    public String getFundAplypchsFee() {
        return fundAplypchsFee;
    }

    public void setFundAplypchsFee(String fundAplypchsFee) {
        this.fundAplypchsFee = fundAplypchsFee;
    }

    public String getRdmFert() {
        return rdmFert;
    }

    public void setRdmFert(String rdmFert) {
        this.rdmFert = rdmFert;
    }

    public String getExcessMngFee() {
        return excessMngFee;
    }

    public void setExcessMngFee(String excessMngFee) {
        this.excessMngFee = excessMngFee;
    }

    public String getFundTsfrt() {
        return fundTsfrt;
    }

    public void setFundTsfrt(String fundTsfrt) {
        this.fundTsfrt = fundTsfrt;
    }

    public String getSaleServFee() {
        return saleServFee;
    }

    public void setSaleServFee(String saleServFee) {
        this.saleServFee = saleServFee;
    }

    public String getAuditFe() {
        return auditFe;
    }

    public void setAuditFe(String auditFe) {
        this.auditFe = auditFe;
    }

    public String getInfdclNppSumm() {
        return infdclNppSumm;
    }

    public void setInfdclNppSumm(String infdclNppSumm) {
        this.infdclNppSumm = infdclNppSumm;
    }

    public String getAfadjOthfe() {
        return afadjOthfe;
    }

    public void setAfadjOthfe(String afadjOthfe) {
        this.afadjOthfe = afadjOthfe;
    }

    public String getPsbcChremCphsFert() {
        return psbcChremCphsFert;
    }

    public void setPsbcChremCphsFert(String psbcChremCphsFert) {
        this.psbcChremCphsFert = psbcChremCphsFert;
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
