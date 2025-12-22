package com.dahuaboke.mpda.bot.scenes.product;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;

import java.util.HashMap;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:32
 */
public abstract class AbstractProductAgentPrompt implements AgentPrompt {

    protected Map<String, String> promptMap;
    protected String description;
    String informationTranslatePrompt = """
            以下是字段的名称和中文的对照翻译
                    \n
                     | 英文字段 | 中文描述 | 同义词 / 常用词 |
                     | :--- | :--- | :--- |
                     | fundCode | 基金代码 | 基金编码、产品代码、基金编号 |
                     | prodtClsCode | 基金类型 1-股票型基金，2-混合型基金，3-债券型基金，4-货币式基金，5-QDII，6-商品型基金，7-短期理财债券型基金，8-基础设施基金，9-基金中基金(FOF) | 基金大类、基金品种类型、基金种类、基金属于哪一类 |
                     | fundFnm | 基金全称 | 基金名称、基金完整名称、产品全称（基金）、基金全名、基金的完整名字 |
                     | prodtSname | 基金简称 | 基金缩写名称、产品简称（基金）、基金简称、基金短名 |
                     | investMgrName | 基金经理名字 | 基金经理姓名、管理人姓名、负责该基金的经理名字、基金经理是谁 |
                     | engageYearNum | 基金经理证券从业年限 | 基金经理从业时长、管理人证券行业工作年数、经理做证券多久了、证券从业年限 |
                     | engagePersonDesc | 基金经理说明 | 基金经理介绍、管理人情况说明、经理背景介绍、基金经理的相关说明 |
                     | relatBgnTime | 基金经理开始担任本基金基金经理的日期 | 基金经理管理本基金起始日、基金经理任职开始日、开始管这只基金的日期、接手本基金的时间、基金经理上任日期、管理人任职起始日、经理开始管这只基金的日期、任职开始日 |
                     | competBgnTime | 基金经理证券从业日期 | 基金经理证券从业起始日、管理人进入证券行业日期、经理开始做证券的时间、证券从业开始日 |
                     | stockBondIssueSituDesc | 报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细 |
                     | bondChrgIntInfo | 报告期末按债券品种分类的债券投资组合 | 债券品种投资组合、债券分类投资情况、债券持仓分类、债券投资明细 |
                     | firstIssueBondBillNo | 报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例 | 国债占净值比例、国债资产占比、国债持仓占比、国债占基金净值比例 |
                     | pbcBuyRtslPamt | 报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例 | 央行票据占净值比例、央行票据资产占比、央票占净值比例、央行票据持仓占比 |
                     | amtAppoRatio | 报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例 | 金融债占净值比例、金融债券资产占比、金融债持仓占比、金融债占基金净值比例 |
                     | plcyFdbtAmt | 报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例 | 政策性金融债占净值比例、政策性金融债资产占比、政金债持仓占比、政金债占基金净值比例 |
                     | beInvesCorpShrHoldRatio | 报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例 | 短期融资券占净值比例、企业短融资产占比、短融持仓占比、短融占基金净值比例 |
                     | docBillRatio | 报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例|
                     | bibTmOcqn | 报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例 | 中期票据占净值比例、中期票据资产占比、中票持仓占比、中期票据占基金净值比例 |
                     | bondiDtl | 报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细 | 前五大债券持仓、债券投资前 5 名明细、债券持仓 Top5、前 5 大债券投资详情 |
                     | assetInfo | 报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细 | 前十大股票持仓、股票投资前 10 名明细、股票持仓 Top10、前 10 大股票投资详情 |
                     | fundstgSumProfrat | 本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较 |
                     | assetNval | 基金规模 | 期末基金资产净值、期末净值、报告期末基金资产净值、季末净值、基金期末资产价值、规模、资产规模 |
                     | projTotLimt | 报告期期末基金份额总额 |
                     | fundMgrName | 基金管理人 | 管理机构、基金管理公司、管理人、负责管理的公司 |
                     | trusteePersName | 基金托管人 | 托管机构、基金托管银行 / 公司、托管方、负责托管的银行 / 公司 |
                     | fundOprModeCd | 运作方式 | 运作模式、基金运行方式、基金怎么运作的、运作类型、基金是开放式还是封闭式 |
                     | exgRateUpdFreq | 开放频率 | 
                     | contractEffDate | 基金合同生效日 | 合同生效日期、基金成立生效日、基金合同开始日期、基金正式生效日 |
                     | investTargetCode | 投资目标 | 投资宗旨、基金投资目的、基金的投资方向目标 |
                     | ivstStgyName | 主要投资策略 | 核心投资方法、基金投资策略、怎么投资的、投资思路、基金策略 |
                     | performCmpBmkTxtDesc | 业绩比较基准 | 业绩基准、基金业绩对比基准、对比的业绩标准、业绩参考基准 |
                     | riskProfitCoeff | 风险收益特征 | 风险收益属性、基金风险与收益特点、风险和收益怎么样、风险收益情况 |
                     | fundAplypchsFee | 基金销售相关费用申购费 | 申购手续费、购买基金费用、买基金的手续费、申购时收的费 |
                     | rdmFert | 基金销售相关费用赎回费 | 赎回手续费、卖出基金费用、卖基金的手续费、赎回时收的费 |
                     | excessMngFee | 基金运作相关费用管理费 | 基金管理费、运作管理费用、管理服务费、给管理人的费用 |
                     | fundTsfrt | 基金运作相关费用托管费 | 基金托管费、运作托管费用、托管服务费、基金托管成本 |
                     | saleServFee | 基金运作相关费用销售服务费 | 销售服务费、运作销售费用、销售服务成本、给销售渠道的服务费 |
                     | auditFe | 基金运作相关费用审计费用 | 基金审计费、运作审计费用、审计服务费、基金审计成本 |
                     | infdclNppSumm | 基金运作相关费用信息披露费 | 信息披露费用、运作信息公开费、信息披露成本、基金信息发布费用 |
                     | afadjOthfe | 基金运作相关费用其他费用 | 其他运作费用、运作杂费、杂项费用、除管理费托管费外的运作费用 |
                     | psbcChremCphsFert | 基金运作综合费率（年化） | 年化综合费率、基金总运作成本率（年化）、年化总费率、一年的总运作费用率 |  
                     | maxWithDrawal | 近3月最大回撤 |
                     | yearRita | 近1月收益率 | 近1月年化收益率
                     | year3MRita | 近3月收益率 | 近3月年化收益率
                     | year1YRita | 近1年收益率 | 近1年化收益率
                     | year7DRita | 7日年化收益率 | 7日收益率
                     | yieldPer10000 | 万份收益
                    \n
            """;
    String recommendationTranslatePrompt = """
            以下是字段的名称和中文的对照翻译
                    \n
                     | 英文字段 | 中文描述 | 同义词 / 常用词 |
                     | :--- | :--- | :--- |
                     | fundCode | 基金代码 | 基金编码、产品代码、基金编号 |
                     | prodtClsCode | 基金类型 1-股票型基金，2-混合型基金，3-债券型基金，4-货币式基金，5-QDII，6-商品型基金，7-短期理财债券型基金，8-基础设施基金，9-基金中基金(FOF) | 基金大类、基金品种类型、基金种类、基金属于哪一类 |
                     | fundFnm | 基金全称 | 基金名称、基金完整名称、产品全称（基金）、基金全名、基金的完整名字 |
                     | investMgrName | 基金经理名字 | 基金经理姓名、管理人姓名、负责该基金的经理名字、基金经理是谁 |
                     | assetNval | 基金规模 | 期末基金资产净值、期末净值、报告期末基金资产净值、季末净值、基金期末资产价值、规模、资产规模 |
                     | fundMgrName | 基金管理人 | 管理机构、基金管理公司、管理人、负责管理的公司 |
                     | trusteePersName | 基金托管人 | 托管机构、基金托管银行 / 公司、托管方、负责托管的银行 / 公司 |
                     | riskProfitCoeff | 风险收益特征 | 风险收益属性、基金风险与收益特点、风险和收益怎么样、风险收益情况 |
                     | rate | 收益率 |
                     | maxWithDraw | 最大回撤 |
                     | sevenDayYearlyProfrat | 七日年化 |
                     | thouCopFundUnitProfit | 万份收益 | 
                    \n
            """;

    public String changePrompt(String key) {
        this.description = promptMap.get(key);
        return this.description;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public void build(Map params) {

    }

    public String translate(String key) {
        return this.promptMap.get(key);
    }

    public AbstractProductAgentPrompt() {
        this.promptMap = new HashMap<String, String>();
        this.promptMap.put("recommendationTranslatePrompt", recommendationTranslatePrompt);
        this.promptMap.put("informationTranslatePrompt", informationTranslatePrompt);
    }
}
