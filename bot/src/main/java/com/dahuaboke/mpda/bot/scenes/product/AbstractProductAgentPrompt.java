package com.dahuaboke.mpda.bot.scenes.product;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:32
 */
public abstract class AbstractProductAgentPrompt implements AgentPrompt {

    protected Map<String, String> promptMap;
    protected String description;

    String translatePrompt = """
                   以下是字段的名称和中文的对照翻译
                   \n
                    | 英文字段 | 中文描述 | 同义词 / 常用词 |
                    | :--- | :--- | :--- |
                    | totLimt | 总额度 | 总限额、产品总额度上限、总的额度限制、最多能买多少（总额） |
                    | tranTrusteeStatus | 转托管状态代码（0 - 允许所有转托管等） | 转托管状态、转托管权限、能转托管吗、转托管允许情况 |
                    | tranTrusteeMode | 转托管方式代码（0 - 一步转托管 1 - 两步转托管 2 - 禁止） | 转托管模式、转托管方式、转托管怎么操作、一步 / 两步转托管 |
                    | ivstStgyName | 主要投资策略 | 核心投资方法、基金投资策略、怎么投资的、投资思路、基金策略 |
                    | fundOprModeCd | 运作方式 | 运作模式、基金运行方式、基金怎么运作的、运作类型、基金是开放式还是封闭式 |
                    | performCmpBmkTxtDesc | 业绩比较基准 | 业绩基准、基金业绩对比基准、对比的业绩标准、业绩参考基准 |
                    | agedFundCloseYearNum | 养老基金封闭年数 | 养老基金封闭年限、养老基金锁定期年数、养老基金封闭时长、养老基金封闭期（年） |
                    | agedFundCd | 养老基金代码（0 - 目标日期基金，1 - 目标风险基金） | 养老基金类型代码、养老基金分类编码、养老基金品种代码、目标养老基金标识号 |
                    | investTargetCode | 投资目标 | 投资宗旨、基金投资目的、基金的投资方向目标 |
                    | investScope | 投资范围 | 投资领域、基金可投资品种范围、基金能投什么、投资的品种/领域限制 |
                    | belongInstNo | 所属机构号（基金所属机构） | 基金归属机构、所属机构、机构归属、基金所属公司、基金管理人 |
                    | ctfeeModeCd | 收费方式（0 - 前端收费 1 - 后端收费） | 收费模式、前端 / 后端收费、收费类型、买的时候收费 / 卖的时候收费 |
                    | permitTranfmFlag | 是否允许转换标志（1. 关闭，2. 转入，3. 转出，4. 转入转出） | 转换权限、基金能转进/转出吗、转换权限状态 |
                    | dividModeCode | 是否允许分红方式变更（0 - 允许；1 - 不允许） | 分红方式变更权限、能否修改分红方式、可以改分红方式吗、分红方式修改权限 |
                    | agedTargetFundFlag | 是否养老目标基金（0 - 否 1 - 是） | 养老目标基金标识、是否为养老目标基金、养老目标基金标记、属于养老目标基金吗 |
                    | hkMutRecoFundFlag | 是否香港互认基金标志（0 - 否，1 - 是） | 香港互认基金标识、是否为港基互认产品、港股互认基金标记、能和香港互认的基金吗 |
                    | psbcTrusteeFlag | 是否我行托管标志（0 - 否，1 - 是） | 邮储银行（中国邮政储蓄银行）托管、是否由邮储银行托管基金、邮储银行托管吗 |
                    | spcaccChmprdtFlag | 是否是专户理财产品（0 - 否；1 - 是） | 专户理财、是否为特定客户资产管理产品、专户产品、属于专户理财吗 |
                    | lofFundFlag | 是否是 lof 基金标志（0 - 非 lof 基金 1-lof 基金） | LOF 基金、是否为上市开放式基金、能在交易所买卖的开放式基金吗 |
                    | revealProdtDocOutlFlag | 是否披露产品资料概要标志（0 - 否 1 - 是） | 产品概要披露、是否公开产品资料概要、会公示产品概要吗、资料概要披露状态 |
                    | ctrlLimitFlag | 是否控制额度（0 - 允许；1 - 不允许） | 额度控制、是否限额、是否限制购买额度 |
                    | openFixQuotaBusiFlag | 是否开办定期定额业务（0–开办 1–不开办） | 定投业务、是否开通定期定额申购、能做定投吗、支持每月固定投吗 |
                    | structurProdtFlag | 是否结构化产品标志（0 - 否，1 - 是） | 结构化产品、是否为分层设计产品、分层产品、有结构化设计吗 |
                    | setsChmprdtFlag | 是否集合理财产品标志（0 - 否 1 - 是） | 集合理财、是否为集合资产管理产品、集合类理财、属于集合理财产品吗 |
                    | relPtyManageProdtFlag | 是否关联方管理产品标志（0 - 否，1 - 是） | 关联方管理、是否由关联机构管理基金、关联公司管理吗、关联管理状态 |
                    | byquitFlag | 是否被退出标志（0 - 正常 1 - 已退出） | 退出状态、是否退出、产品退出状态 |
                    | hotSaleFundFlag | 热销基金标志（0 - 否 1 - 是） | 热销产品、是否为热门基金、爆款基金、现在卖得好的基金吗 |
                    | cfmTermVal | 确认时限 | 确认周期、到账确认时间限制、确认时长、申请后确认时间 |
                    | enableDate | 启用日期（启用日期） | / |
                    | assetNval | 期末基金资产净值 | 期末净值、报告期末基金资产净值、季末净值、基金期末资产价值、基金规模、规模、资产规模 |
                    | enPyName | 拼音名称（拼音名称全拼） | 全拼名称、拼音全称、产品拼音全名、中文名称拼音 |
                    | enPySname | 拼音简称（基金拼音首字母） | 拼音首字母简称、首字母拼音缩写、拼音缩写、产品首字母拼音 |
                    | collectDaynum | 募集天数（募集天数） | 募集周期天数、基金募集时长、募集期天数、从开始募集到结束要几天 |
                    | raisBgnDate | 募集起始日期（募集起始日期） | 募集开始日、基金募集启动日期、开始认购的日期、募集第一天 |
                    | collectEndDt | 募集结束日期（募集结束日期） | 募集截止日、基金募集终止日期、募集到期日、最后认购日期 |
                    | partcYrHoldHvFundFlag | 某年持有基金标志（0 - 否，1 - 是） | 年度持有基金标识、是否在某年度持有基金标记、某一年有没有持有这只基金、年度持仓标记 |
                    | defauDividMode | 默认分红方式（0 - 红利转股 1 - 现金分红） | 默认分红类型、自动分红方式、默认分红选择、系统自动分红方式 |
                    | chremProdtRiskLvlCode | 理财产品风险等级代码（1 - 低 2 - 中低 3 - 中 4 - 中高 5 - 高） | 产品风险等级、理财风险等级、风险等级、产品风险程度 |
                    | exgRateUpdFreq | 开放频率 | 开放周期、申购赎回开放频次、多久开放一次、什么时候能买 / 卖 |
                    | rptTime | 季报时间 | 季度报告日期、季报发布时间、季报什么时候出、季度报告的时间 |
                    | fundStatusCode | 基金状态代码（0 - 可申购赎回等） | 基金状态编码、申购赎回状态代码、基金当前状态代码、基金能否买 / 卖的代码 |
                    | fundTranfmLowLmtAmt | 基金转换下限金额 | 转换最低金额、基金转换最低限额、转基金最少要多少钱、转换起步金额 |
                    | psbcChremCphsFert | 基金运作综合费率（年化） | 年化综合费率、基金总运作成本率（年化）、年化总费率、一年的总运作费用率 |
                    | infdclNppSumm | 基金运作相关费用信息披露费 | 信息披露费用、运作信息公开费、信息披露成本、基金信息发布费用 |
                    | saleServFee | 基金运作相关费用销售服务费 | 销售服务费、运作销售费用、销售服务成本、给销售渠道的服务费 |
                    | fundTsfrt | 基金运作相关费用托管费 | 基金托管费、运作托管费用、托管服务费、基金托管成本 |
                    | auditFe | 基金运作相关费用审计费用 | 基金审计费、运作审计费用、审计服务费、基金审计成本 |
                    | afadjOthfe | 基金运作相关费用其他费用 | 其他运作费用、运作杂费、杂项费用、除管理费托管费外的运作费用 |
                    | excessMngFee | 基金运作相关费用管理费 | 基金管理费、运作管理费用、管理服务费、给管理人的费用 |
                    | rdmFert | 基金销售相关费用赎回费 | 赎回手续费、卖出基金费用、卖基金的手续费、赎回时收的费 |
                    | fundAplypchsFee | 基金销售相关费用申购费 | 申购手续费、购买基金费用、买基金的手续费、申购时收的费 |
                    | fundTrusteeBankName | 基金托管银行 | 托管银行名称、基金托管机构名称、负责托管的银行名字、托管银行全称 |
                    | trusteePersName | 基金托管人 | 托管机构、基金托管银行 / 公司、托管方、负责托管的银行 / 公司 |
                    | fundFnm | 基金全称 | 基金完整名称、产品全称（基金）、基金全名、基金的完整名字 |
                    | fundType | 基金类型（0 - 货币型基金等） | 基金大类、基金品种类型、基金种类、基金属于哪一类 |
                    | competBgnTime | 基金经理证券从业日期 | 基金经理证券从业起始日、管理人进入证券行业日期、经理开始做证券的时间、证券从业开始日 |
                    | engageYearNum | 基金经理证券从业年限 | 基金经理从业时长、管理人证券行业工作年数、经理做证券多久了、证券从业年限 |
                    | engagePersonDesc | 基金经理说明 | 基金经理介绍、管理人情况说明、经理背景介绍、基金经理的相关说明 |
                    | offcDt | 基金经理任职日期 | 基金经理管理本基金起始日、基金经理任职开始日、开始管这只基金的日期、接手本基金的时间、基金经理上任日期、管理人任职起始日、经理开始管这只基金的日期、任职开始日 |
                    | investMgrName | 基金经理名字 | 基金经理姓名、管理人姓名、负责该基金的经理名字、基金经理是谁 |
                    | relatBgnTime | 基金经理开始担任本基金基金经理的日期 | 基金经理管理本基金起始日、基金经理任职开始日、开始管这只基金的日期、接手本基金的时间、基金经理上任日期、管理人任职起始日、经理开始管这只基金的日期、任职开始日 |
                    | prodtSname | 基金简称 | 基金缩写名称、产品简称（基金）、基金简称、基金短名 |
                    | contractEffDate | 基金合同生效日 | 合同生效日期、基金成立生效日、基金合同开始日期、基金正式生效日 |
                    | fundMgrName | 基金管理人 | 管理机构、基金管理公司、管理人、负责管理的公司 |
                    | mainReason | 基金分类原因 (模型) | 模型分类原因、基金模型分类依据、按模型分类的原因、模型分类依据说明 |
                    | adjReason | 基金分类原因 (代码) | 分类原因编码、基金类别判定原因代码、分类依据代码、为什么这么分类 |
                    | clsReasonCode | 基金分类 (模型) | 基金模型分类、模型化基金分类、按模型分的基金类别、基金模型分类方式 |
                    | pcrRptfClsRsnCd | 基金分类 (代码) | 基金分类编码、基金类别代码、基金分类编号、基金类型编码 |
                    | fundLvl2ClsCd | 基金二级分类（01 - 普通股票型等） | 基金细化分类、细分基金类型、基金小类、具体基金类型 |
                    | fundCode | 基金代码 | 基金编码、产品代码、基金编号 |
                    | instHighestRedemLmtAmt | 机构最高赎回限额 | 机构赎回上限金额、机构最大赎回额度、机构最多能赎回多少钱、机构赎回封顶金额 |
                    | instHighestAplypchsLmtAmt | 机构最高申购限额 | 机构申购上限金额、机构最大申购额度、机构最多能申购多少钱、机构申购封顶金额 |
                    | instHighestSubscriLmtAmt | 机构最高认购限额 | 机构认购上限金额、机构最大认购额度、机构最多能认购多少钱、机构认购封顶金额 |
                    | instLowestRedemLmtAmt | 机构最低赎回限额 | 机构赎回最低金额、机构单次赎回下限、机构卖基金最少要卖多少钱、机构赎回起步金额 |
                    | instLowestAplypchsLmtAmt | 机构最低申购限额 | 机构申购最低金额、机构首次申购下限、机构买基金最少要多少钱、机构申购起步金额 |
                    | instLowestSubscriLmtAmt | 机构最低认购限额 | 机构认购最低金额、机构首次认购下限、机构买新基金最少要多少钱、机构认购起步金额 |
                    | instLowestHoldHaveLmtAmt | 机构最低持有限额 | 机构持有最低金额、机构持仓下限额度、机构最少要持有多少钱、机构持仓起步金额 |
                    | instaddtoLoweAplypchsLmamt | 机构追加最低申购限额 | 机构追加申购最低金额、机构二次申购下限、机构补购最低额度、机构加购最少多少钱 |
                    | instaddtoLowestSubscriLmamt | 机构追加最低认购限额 | 机构追加认购最低金额、机构二次认购下限、机构补认购最低额度、机构加认购最少多少钱 |
                    | instPerHighestAplypLmtAmt | 机构单笔最高申购限额 | 机构单笔申购上限、机构单次申购最大金额、机构一次最多能申购多少钱、单笔申购封顶 |
                    | busiContractName | 合同名称 | 业务合同全称、基金业务合同名称、基金合同全称、业务合作合同名称 |
                    | riskProfitCoeff | 风险收益特征 | 风险收益属性、基金风险与收益特点、风险和收益怎么样、风险收益情况 |
                    | chremFlag | 短期理财标志（0 - 否 1 - 是） | 短期理财产品、是否为短期理财基金、短期理财、属于短期理财类基金吗 |
                    | fixQuotaAplypchsLowLmtAmt | 定期定额申购下限金额 | 定投最低金额、定期定额申购最低限额、定投起步金额、每月定投最少要多少钱 |
                    | enrollInstNo | 登记机构号（TA 登记机构） | TA 机构、登记机构、TA 登记公司、基金TA账户登记机构、TA账号所属的基金公司 |
                    | roleCompatDesc | 产品特色额度（角色兼容说明） | / |
                    | prodtAname | 基金名称 | 基金产品完整名称、产品全称、基金产品全名、完整的基金产品名字 |
                    | prodtName | 基金名称 | 产品全称、基金产品名称、产品名字、基金的产品名 |
                    | prodtDesc | 基金描述 | 产品介绍、基金产品说明、这只基金的介绍、产品详情说明 |
                    | labelNo | 标签编号 | / |
                    | fundstgSumProfrat | 本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较 | 业绩对比、期净值增长与基准收益率比较、净值涨幅和基准比怎么样、业绩和基准对比 |
                    | projTotLimt | 报告期期末基金份额总额 | 总份额、基金总份数、总份额、基金现有份额数、基金份额 |
                    | assetComposition | 报告期末基金资产组合情况 | 基金资产构成、基金资产配置情况、基金资产分布、资产组合明细 |
                    | bibTmOcqn | 报告期末按债券品种分类的债券投资组合中中期票据占基金资产净值比例 | 中期票据占净值比例、中期票据资产占比、中票持仓占比、中期票据占基金净值比例 |
                    | plcyFdbtAmt | 报告期末按债券品种分类的债券投资组合中政策性金融债占基金资产净值比例 | 政策性金融债占净值比例、政策性金融债资产占比、政金债持仓占比、政金债占基金净值比例 |
                    | pbcBuyRtslPamt | 报告期末按债券品种分类的债券投资组合中央行票据占基金资产净值比例 | 央行票据占净值比例、央行票据资产占比、央票占净值比例、央行票据持仓占比 |
                    | corporateBondRatio | 报告期末按债券品种分类的债券投资组合中企业债券占基金资产净值比例 | 企业债占净值比例、企业债券资产占比、企业债持仓占比、企业债占基金净值比例 |
                    | beInvesCorpShrHoldRatio | 报告期末按债券品种分类的债券投资组合中企业短期融资券占基金资产净值比例 | 短期融资券占净值比例、企业短融资产占比、短融持仓占比、短融占基金净值比例 |
                    | amtAppoRatio | 报告期末按债券品种分类的债券投资组合中金融债券占基金资产净值比例 | 金融债占净值比例、金融债券资产占比、金融债持仓占比、金融债占基金净值比例 |
                    | firstIssueBondBillNo | 报告期末按债券品种分类的债券投资组合中国家债券占基金资产净值比例 | 国债占净值比例、国债资产占比、国债持仓占比、国债占基金净值比例 |
                    | bondChrgIntInfo | 报告期末按债券品种分类的债券投资组合 | 债券品种投资组合、债券分类投资情况、债券持仓分类、债券投资明细 |
                    | ltsrIvstStockInfo | 报告期末按行业分类的境内股票投资组合 | 行业股票配置、股票行业分布、股票行业持仓、股票投了哪些行业 |
                    | bondiDtl | 报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细 | 前五大债券持仓、债券投资前 5 名明细、债券持仓 Top5、前 5 大债券投资详情 |
                    | assetInfo | 报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细 | 前十大股票持仓、股票投资前 10 名明细、股票持仓 Top10、前 10 大股票投资详情 |
                    | nwk1CombProfrat | 近1周年化收益率 | 近1周收益率、 近1周年化收益 |
                    | indsRankSeqNo | 近一周收益率排名 |
                    | txamtRankNo | 近一周收益率昨日排名 |
                    | curdayBalChgTotalAccnum | 近一周收益率排名变动 |
                    | nmm1CombProfrat | 近1月年化收益率 | 近1月收益率、 近1月年化收益 |
                    | lblmRank | 近一月收益率排名 |
                    | busicmLmRank | 近一月收益率昨日排名 |
                    | curdayBalChgAccnum | 近一月收益率排名变动 |
                    | nmm3CombProfrat | 近3月年化收益率 | 近3月收益率、 近3月年化收益 |
                    | rankScopeLowLmtVal | 近三月收益率排名 |
                    | intglRankSeqNo | 近三月收益率昨日排名 |
                    | supptranBalChgTotalAccnum | 近三月收益率排名变动 |
                    | nyy1Profrat | 近一年收益率 | 近一年[滚动12个月]收益率 |
                    | reachStRankSeqNo | 近一年收益率排名 | 近一年[滚动12个月]收益率 |
                    | chremMgrIntglRankSeqNo | 近一年收益率昨日排名 | 近一年[滚动12个月]收益率昨日排名 |
                    | centerCfmCurdayChgTnum | 近一年收益率排名变动 | 近一年[滚动12个月]收益率排名变动 |
                    | styoMaxWdwDesc | 近一周最大回撤 |
                    | maxWdwrt | 近一月最大回撤 |
                    | fundstgMaxWdwrt | 近三月最大回撤 |
                    | nyy1Wdwrt | 近一年最大回撤 |
                    | drtPftrtTval | 今年以来收益率 | 当前年收益率、今年收益率
                    | rtnRtRank | 今年以来收益率排名 | 当前年收益率排名、今年收益率排名
                    | pftrtName | 今年以来收益率(年化) | 当前年收益率(年化)、今年收益率(年化)
                    | busicmOybinpRank | 今年以来收益率排名（年化） | 当前年收益率排名（年化）、今年收益率排名（年化）
                    | lastYrlyPftrt | 第1季度收益率 |
                    | custRaiseRateRankNo | 第1季度收益率排名 |
                    | nmm6CombProfrat | 第2季度收益率 |
                    | detainRateRankNo | 第2季度收益率排名 |
                    | nmm3YrlyPftrt | 第3季度收益率 |
                    | tmPontAsetRaiseTotRanknum | 第3季度收益率排名 |
                    | nmm1YearlyProfrat | 第4季度收益率 |
                    | addRepPurcProTotnumRankno | 第4季度收益率排名 |
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

    public String translate() {
        return translatePrompt;
    }
}
