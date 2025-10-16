package com.dahuaboke.mpda.bot.tools.entity;

import com.dahuaboke.mpda.core.rag.entity.FieldComment;

/**
 * @Desc: 基金产品信息概要实体类
 * @Author：zhh
 * @Date：2025/8/7 14:11
 */
public class BrProductSummary {


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
     * 基金管理人
     **/
    @FieldComment(question = "基金管理人", keyWord = "基金管理人")
    private String fundMgrName;

    /**
     * 基金托管人
     **/
    @FieldComment(question = "基金托管人", keyWord = "基金托管人")
    private String trusteePersName;

    /**
     * 运作方式
     **/
    @FieldComment(question = "运作方式", keyWord = "运作方式")
    private String fundOprModeCd;

    /**
     * 开放频率
     **/
    @FieldComment(question = "开放频率", keyWord = "开放频率")
    private String exgRateUpdFreq;

    /**
     * 基金合同生效日
     **/
    @FieldComment(question = "基金合同生效日", keyWord = "基金合同生效日")
    private String contractEffDate;

    /**
     * 投资目标
     **/
    @FieldComment(question = "投资目标", keyWord = "投资目标")
    private String investTargetCode;

    /**
     * 投资范围
     **/
    @FieldComment(question = "投资范围", keyWord = "投资范围")
    private String investScope;

    /**
     * 主要投资策略
     **/
    @FieldComment(question = "主要投资策略", keyWord = "主要投资策略")
    private String ivstStgyName;

    /**
     * 业绩比较基准
     **/
    @FieldComment(question = "业绩比较基准", keyWord = "业绩比较基准")
    private String performCmpBmkTxtDesc;

    /**
     * 风险收益特征
     **/
    @FieldComment(question = "风险收益特征", keyWord = "风险收益特征")
    private String riskProfitCoeff;

    /**
     * 基金销售相关费用申购费
     **/
    @FieldComment(question = "基金销售相关费用申购费", keyWord = "基金销售相关费用申购费")
    private String fundAplypchsFee;

    /**
     * 基金销售相关费用赎回费
     **/
    @FieldComment(question = "基金销售相关费用赎回费", keyWord = "基金销售相关费用赎回费")
    private String rdmFert;

    /**
     * 基金运作相关费用管理费
     **/
    @FieldComment(question = "基金运作相关费用管理费", keyWord = "基金运作相关费用管理费")
    private String excessMngFee;

    /**
     * 基金运作相关费用托管费
     **/
    @FieldComment(question = "基金运作相关费用托管费", keyWord = "基金运作相关费用托管费")
    private String fundTsfrt;

    /**
     * 基金运作相关费用销售服务费
     **/
    @FieldComment(question = "基金运作相关费用销售服务费", keyWord = "基金运作相关费用销售服务费")
    private String saleServFee;

    /**
     * 基金运作相关费用审计费用
     **/
    @FieldComment(question = "基金运作相关费用审计费用", keyWord = "基金运作相关费用审计费用")
    private String auditFe;

    /**
     * 基金运作相关费用信息披露费
     **/
    @FieldComment(question = "基金运作相关费用信息披露费", keyWord = "基金运作相关费用信息披露费")
    private String infdclNppSumm;

    /**
     * 基金运作相关费用其他费用
     **/
    @FieldComment(question = "基金运作相关费用其他费用", keyWord = "基金运作相关费用其他费用")
    private String afadjOthfe;

    /**
     * 基金运作综合费率（年化）
     **/
    @FieldComment(question = "基金运作综合费率（年化）", keyWord = "基金运作综合费率（年化）")
    private String psbcChremCphsFert;


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

}
