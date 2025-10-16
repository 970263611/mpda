package com.dahuaboke.mpda.bot.rag;

import com.dahuaboke.mpda.core.rag.entity.FieldComment;

/**
 * @Desc: 基金信息实体类
 * @Author：zhh
 * @Date：2025/8/7 14:11
 */
public class FundProduct {

    /**
     * 基金ID
     **/

    private String fundId;

    /**
     * 基金代码
     **/
    @FieldComment(question = "基金代码(只要值,例如006473)", keyWord = "基金代码")
    private String fundCode;

    /**
     * 基金全称
     **/
    @FieldComment(question = "基金全称", keyWord = "基金全称")
    private String fundFullName;

    /**
     * 基金简称
     **/
    @FieldComment(question = "基金简称", keyWord = "基金全称")
    private String fundShortName;

    /**
     * 季报时间
     **/
    @FieldComment(question = "季报时间", keyWord = "季报时间")
    private String quarterlyReportDate;

    /**
     * 基金经理名字
     **/
    @FieldComment(question = "基金经理名字(所有且完整内容)", keyWord = "基金经理名字")
    private String fundManagerName;

    /**
     * 基金经理任职日期
     **/
    @FieldComment(question = "基金经理任职日期(所有且完整内容)", keyWord = "基金经理任职日期")
    private String managerAppointmentDate;

    /**
     * 基金经理证券从业年限
     **/
    @FieldComment(question = "基金经理证券从业年限(所有且完整内容)", keyWord = "基金经理证券从业年限")
    private String managerSecuritiesExperience;

    /**
     * 基金经理说明
     **/
    @FieldComment(question = "基金经理说明(所有且完整内容)", keyWord = "基金经理说明")
    private String managerDescription;

    /**
     * 基金经理开始担任本基金基金经理的日期
     **/
    @FieldComment(question = "基金经理开始担任本基金基金经理的日期(所有且完整内容)", keyWord = "基金经理开始担任本基金基金经理的日期")
    private String managerStartDate;

    /**
     * 基金经理证券从业日期
     **/
    @FieldComment(question = "基金经理证券从业日期(所有且完整内容)", keyWord = "基金经理证券从业日期")
    private String managerSecuritiesStartDate;

    /**
     * 报告期末按行业分类的境内股票投资组合
     **/
    @FieldComment(question = "报告期末按行业分类的境内股票投资组合(完整表格,且包含子项)", keyWord = "报告期末按行业分类的境内股票投资组合")
    private String industryStockPortfolio;

    /**
     * 报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细
     **/
    @FieldComment(question = "报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细(完整表格,且包含子项)", keyWord = "报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细")
    private String topTenStockInvestments;

    /**
     * 报告期末按债券品种分类的债券投资组合
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合(完整表格,包含政策性金融债)", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String bondPortfolio;

    /**
     * 报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String nationalBondRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String centralBankBillRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String financialBondRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String policyFinancialBondRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中企业债券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中企业债券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String corporateBondRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String shortTermFinancingBillRatio;

    /**
     * 报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例
     **/
    @FieldComment(question = "报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例.只要值,如果获取不到,返回0.00", keyWord = "报告期末按债券品种分类的债券投资组合")
    private String mediumTermNoteRatio;

    /**
     * 报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细
     **/
    @FieldComment(question = "报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细(完整表格,且包含子项)", keyWord = "报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细")
    private String topFiveBondInvestments;

    /**
     * 报告期末基金资产组合情况
     **/
    @FieldComment(question = "报告期末基金资产组合情况(完整表格,且包含子项)", keyWord = "报告期末基金资产组合情况")
    private String assetComposition;

    /**
     * 本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较
     **/
    @FieldComment(question = "本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较(完整表格,且包含子项)", keyWord = "本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较")
    private String performanceComparison;

    /**
     * 期末基金资产净值
     **/
    @FieldComment(question = "期末基金资产净值(只要值,如果获取不到,返回0.00)", keyWord = "期末基金资产净值")
    private String netAssetValue;

    /**
     * 报告期期末基金份额总额
     **/
    @FieldComment(question = "报告期期末基金份额总额", keyWord = "报告期期末基金份额总额")
    private String totalFundShares;

    /**
     * 基金管理人
     **/
    @FieldComment(question = "基金管理人", keyWord = "基金管理人")
    private String fundManagerCompany;

    /**
     * 基金托管人
     **/
    @FieldComment(question = "基金托管人", keyWord = "基金托管人")
    private String fundCustodian;

    /**
     * 运作方式
     **/
    @FieldComment(question = "运作方式", keyWord = "运作方式")
    private String operationMode;

    /**
     * 开放频率
     **/
    @FieldComment(question = "开放频率", keyWord = "开放频率")
    private String openFrequency;

    /**
     * 基金合同生效日
     **/
    @FieldComment(question = "基金合同生效日", keyWord = "基金合同生效日")
    private String contractEffectiveDate;

    /**
     * 投资目标
     **/
    @FieldComment(question = "投资目标", keyWord = "投资目标")
    private String investmentObjective;

    /**
     * 投资范围
     **/
    @FieldComment(question = "投资范围", keyWord = "投资范围")
    private String investmentScope;

    /**
     * 主要投资策略
     **/
    @FieldComment(question = "主要投资策略", keyWord = "主要投资策略")
    private String investmentStrategy;

    /**
     * 业绩比较基准
     **/
    @FieldComment(question = "业绩比较基准", keyWord = "业绩比较基准")
    private String performanceBenchmark;

    /**
     * 风险收益特征
     **/
    @FieldComment(question = "风险收益特征", keyWord = "风险收益特征")
    private String riskReturnCharacteristics;

    /**
     * 基金销售相关费用申购费
     **/
    @FieldComment(question = "基金销售相关费用申购费", keyWord = "基金销售相关费用申购费")
    private String purchaseFee;

    /**
     * 基金销售相关费用赎回费
     **/
    @FieldComment(question = "基金销售相关费用赎回费", keyWord = "基金销售相关费用赎回费")
    private String redemptionFee;

    /**
     * 基金运作相关费用管理费
     **/
    @FieldComment(question = "基金运作相关费用管理费", keyWord = "基金运作相关费用管理费")
    private String managementFee;

    /**
     * 基金运作相关费用托管费
     **/
    @FieldComment(question = "基金运作相关费用托管费", keyWord = "基金运作相关费用托管费")
    private String custodyFee;

    /**
     * 基金运作相关费用销售服务费
     **/
    @FieldComment(question = "基金运作相关费用销售服务费", keyWord = "基金运作相关费用销售服务费")
    private String salesServiceFee;

    /**
     * 基金运作相关费用审计费用
     **/
    @FieldComment(question = "基金运作相关费用审计费用", keyWord = "基金运作相关费用审计费用")
    private String auditFee;

    /**
     * 基金运作相关费用信息披露费
     **/
    @FieldComment(question = "基金运作相关费用信息披露费", keyWord = "基金运作相关费用信息披露费")
    private String disclosureFee;

    /**
     * 基金运作相关费用其他费用
     **/
    @FieldComment(question = "基金运作相关费用其他费用", keyWord = "基金运作相关费用其他费用")
    private String otherFees;

    /**
     * 基金运作综合费率（年化）
     **/
    @FieldComment(question = "基金运作综合费率（年化）", keyWord = "基金运作综合费率（年化）")
    private String totalExpenseRatio;

    /**
     * 基金分类(代码)
     **/
    private String fundClassificationCode;

    /**
     * 基金分类原因(代码)
     **/
    private String fundClassificationReasonCode;

    /**
     * 基金分类(模型)
     **/
    private String fundClassificationModel;

    /**
     * 基金分类原因(模型)
     **/
    private String fundClassificationReasonModel;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundFullName() {
        return fundFullName;
    }

    public void setFundFullName(String fundFullName) {
        this.fundFullName = fundFullName;
    }

    public String getFundShortName() {
        return fundShortName;
    }

    public void setFundShortName(String fundShortName) {
        this.fundShortName = fundShortName;
    }

    public String getQuarterlyReportDate() {
        return quarterlyReportDate;
    }

    public void setQuarterlyReportDate(String quarterlyReportDate) {
        this.quarterlyReportDate = quarterlyReportDate;
    }

    public String getFundManagerName() {
        return fundManagerName;
    }

    public void setFundManagerName(String fundManagerName) {
        this.fundManagerName = fundManagerName;
    }

    public String getManagerAppointmentDate() {
        return managerAppointmentDate;
    }

    public void setManagerAppointmentDate(String managerAppointmentDate) {
        this.managerAppointmentDate = managerAppointmentDate;
    }

    public String getManagerSecuritiesExperience() {
        return managerSecuritiesExperience;
    }

    public void setManagerSecuritiesExperience(String managerSecuritiesExperience) {
        this.managerSecuritiesExperience = managerSecuritiesExperience;
    }

    public String getManagerDescription() {
        return managerDescription;
    }

    public void setManagerDescription(String managerDescription) {
        this.managerDescription = managerDescription;
    }

    public String getManagerStartDate() {
        return managerStartDate;
    }

    public void setManagerStartDate(String managerStartDate) {
        this.managerStartDate = managerStartDate;
    }

    public String getManagerSecuritiesStartDate() {
        return managerSecuritiesStartDate;
    }

    public void setManagerSecuritiesStartDate(String managerSecuritiesStartDate) {
        this.managerSecuritiesStartDate = managerSecuritiesStartDate;
    }

    public String getIndustryStockPortfolio() {
        return industryStockPortfolio;
    }

    public void setIndustryStockPortfolio(String industryStockPortfolio) {
        this.industryStockPortfolio = industryStockPortfolio;
    }

    public String getTopTenStockInvestments() {
        return topTenStockInvestments;
    }

    public void setTopTenStockInvestments(String topTenStockInvestments) {
        this.topTenStockInvestments = topTenStockInvestments;
    }

    public String getBondPortfolio() {
        return bondPortfolio;
    }

    public void setBondPortfolio(String bondPortfolio) {
        this.bondPortfolio = bondPortfolio;
    }

    public String getNationalBondRatio() {
        return nationalBondRatio;
    }

    public void setNationalBondRatio(String nationalBondRatio) {
        this.nationalBondRatio = nationalBondRatio;
    }

    public String getCentralBankBillRatio() {
        return centralBankBillRatio;
    }

    public void setCentralBankBillRatio(String centralBankBillRatio) {
        this.centralBankBillRatio = centralBankBillRatio;
    }

    public String getFinancialBondRatio() {
        return financialBondRatio;
    }

    public void setFinancialBondRatio(String financialBondRatio) {
        this.financialBondRatio = financialBondRatio;
    }

    public String getPolicyFinancialBondRatio() {
        return policyFinancialBondRatio;
    }

    public void setPolicyFinancialBondRatio(String policyFinancialBondRatio) {
        this.policyFinancialBondRatio = policyFinancialBondRatio;
    }

    public String getCorporateBondRatio() {
        return corporateBondRatio;
    }

    public void setCorporateBondRatio(String corporateBondRatio) {
        this.corporateBondRatio = corporateBondRatio;
    }

    public String getShortTermFinancingBillRatio() {
        return shortTermFinancingBillRatio;
    }

    public void setShortTermFinancingBillRatio(String shortTermFinancingBillRatio) {
        this.shortTermFinancingBillRatio = shortTermFinancingBillRatio;
    }

    public String getMediumTermNoteRatio() {
        return mediumTermNoteRatio;
    }

    public void setMediumTermNoteRatio(String mediumTermNoteRatio) {
        this.mediumTermNoteRatio = mediumTermNoteRatio;
    }

    public String getTopFiveBondInvestments() {
        return topFiveBondInvestments;
    }

    public void setTopFiveBondInvestments(String topFiveBondInvestments) {
        this.topFiveBondInvestments = topFiveBondInvestments;
    }

    public String getAssetComposition() {
        return assetComposition;
    }

    public void setAssetComposition(String assetComposition) {
        this.assetComposition = assetComposition;
    }

    public String getPerformanceComparison() {
        return performanceComparison;
    }

    public void setPerformanceComparison(String performanceComparison) {
        this.performanceComparison = performanceComparison;
    }

    public String getNetAssetValue() {
        return netAssetValue;
    }

    public void setNetAssetValue(String netAssetValue) {
        this.netAssetValue = netAssetValue;
    }

    public String getTotalFundShares() {
        return totalFundShares;
    }

    public void setTotalFundShares(String totalFundShares) {
        this.totalFundShares = totalFundShares;
    }

    public String getFundManagerCompany() {
        return fundManagerCompany;
    }

    public void setFundManagerCompany(String fundManagerCompany) {
        this.fundManagerCompany = fundManagerCompany;
    }

    public String getFundCustodian() {
        return fundCustodian;
    }

    public void setFundCustodian(String fundCustodian) {
        this.fundCustodian = fundCustodian;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getOpenFrequency() {
        return openFrequency;
    }

    public void setOpenFrequency(String openFrequency) {
        this.openFrequency = openFrequency;
    }

    public String getContractEffectiveDate() {
        return contractEffectiveDate;
    }

    public void setContractEffectiveDate(String contractEffectiveDate) {
        this.contractEffectiveDate = contractEffectiveDate;
    }

    public String getInvestmentObjective() {
        return investmentObjective;
    }

    public void setInvestmentObjective(String investmentObjective) {
        this.investmentObjective = investmentObjective;
    }

    public String getInvestmentScope() {
        return investmentScope;
    }

    public void setInvestmentScope(String investmentScope) {
        this.investmentScope = investmentScope;
    }

    public String getInvestmentStrategy() {
        return investmentStrategy;
    }

    public void setInvestmentStrategy(String investmentStrategy) {
        this.investmentStrategy = investmentStrategy;
    }

    public String getPerformanceBenchmark() {
        return performanceBenchmark;
    }

    public void setPerformanceBenchmark(String performanceBenchmark) {
        this.performanceBenchmark = performanceBenchmark;
    }

    public String getRiskReturnCharacteristics() {
        return riskReturnCharacteristics;
    }

    public void setRiskReturnCharacteristics(String riskReturnCharacteristics) {
        this.riskReturnCharacteristics = riskReturnCharacteristics;
    }

    public String getPurchaseFee() {
        return purchaseFee;
    }

    public void setPurchaseFee(String purchaseFee) {
        this.purchaseFee = purchaseFee;
    }

    public String getRedemptionFee() {
        return redemptionFee;
    }

    public void setRedemptionFee(String redemptionFee) {
        this.redemptionFee = redemptionFee;
    }

    public String getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(String managementFee) {
        this.managementFee = managementFee;
    }

    public String getCustodyFee() {
        return custodyFee;
    }

    public void setCustodyFee(String custodyFee) {
        this.custodyFee = custodyFee;
    }

    public String getSalesServiceFee() {
        return salesServiceFee;
    }

    public void setSalesServiceFee(String salesServiceFee) {
        this.salesServiceFee = salesServiceFee;
    }

    public String getAuditFee() {
        return auditFee;
    }

    public void setAuditFee(String auditFee) {
        this.auditFee = auditFee;
    }

    public String getDisclosureFee() {
        return disclosureFee;
    }

    public void setDisclosureFee(String disclosureFee) {
        this.disclosureFee = disclosureFee;
    }

    public String getOtherFees() {
        return otherFees;
    }

    public void setOtherFees(String otherFees) {
        this.otherFees = otherFees;
    }

    public String getTotalExpenseRatio() {
        return totalExpenseRatio;
    }

    public void setTotalExpenseRatio(String totalExpenseRatio) {
        this.totalExpenseRatio = totalExpenseRatio;
    }

    public String getFundClassificationCode() {
        return fundClassificationCode;
    }

    public void setFundClassificationCode(String fundClassificationCode) {
        this.fundClassificationCode = fundClassificationCode;
    }

    public String getFundClassificationReasonCode() {
        return fundClassificationReasonCode;
    }

    public void setFundClassificationReasonCode(String fundClassificationReasonCode) {
        this.fundClassificationReasonCode = fundClassificationReasonCode;
    }

    public String getFundClassificationModel() {
        return fundClassificationModel;
    }

    public void setFundClassificationModel(String fundClassificationModel) {
        this.fundClassificationModel = fundClassificationModel;
    }

    public String getFundClassificationReasonModel() {
        return fundClassificationReasonModel;
    }

    public void setFundClassificationReasonModel(String fundClassificationReasonModel) {
        this.fundClassificationReasonModel = fundClassificationReasonModel;
    }
}
