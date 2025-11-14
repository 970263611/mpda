package com.dahuaboke.mpda.bot.scenes.product.marketRanking;

import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingAgentPrompt extends AbstractProductAgentPrompt {

    public MarketRankingAgentPrompt() {
        int currentYear = Year.now().getValue();
        int currentQuarter = getCurrentQuarter();
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        String toolPrompt = """
                根据工具执行结果，返回以下信息：
                                            
                **必须展示的基础字段**：
                - 基金代码
                - 债券基金类型
                - 基金名称
                - 期末基金资产净值（基金规模）
                - 基金管理人
                - 基金成立日
                - 基金经理
                - 存续天数
                - 单位净值
                       
                **时间范围定义与区分规则**：
                1. 近一年：指从当前日期（%s）往前推12个月的时间段（%s至%s）
                   - 包含完整的自然月滚动周期，不受年度边界限制
                2. 今年（%d年）：指自然年度的1月1日至当前日期（%s）
                   - 仅包含当前自然年内的时间段，跨年部分自动排除
                3. 两者核心区别：近一年是滚动12个月，今年是自然年区间，可能存在重叠但绝对不相同
                                           
                **时间范围字段处理规则**：
                1. 如果用户明确指定了时间范围（近一周、近一月、近三个月、近一年[滚动12个月]、今年、季度等），则只返回相关的时间段字段
                2. 如果用户没有指定时间范围，则默认返回：
                   - 今年（%d年）相关字段
                   - 当前季度（第%d季度）相关字段
                                            
                **可用时间范围字段**：
                - 近一年[滚动12个月]收益率(nyy1Profrat)、排名(reachStRankSeqNo)、昨日排名(chremMgrIntglRankSeqNo)、排名变动(centerCfmCurdayChgTnum)、最大回撤(nyy1Wdwrt)
                - 今年[%d年]收益率(drtPftrtTval)、排名(rtnRtRank)、收益率（年化）(pftrtName)、排名（年化）(busicmOybinpRank)
                - 近一周收益率(nwk1CombProfrat)、排名(indsRankSeqNo)、昨日排名(txamtRankNo)、排名变动(curdayBalChgTotalAccnum)、最大回撤(styoMaxWdwDesc)
                - 近一月收益率(nmm1CombProfrat)、排名(lblmRank)、昨日排名(busicmLmRank)、排名变动(curdayBalChgAccnum)、最大回撤(maxWdwrt)
                - 近三个月收益率(nmm3CombProfrat)、排名(rankScopeLowLmtVal)、昨日排名(intglRankSeqNo)、排名变动(supptranBalChgTotalAccnum)、最大回撤(fundstgMaxWdwrt)
                - %d年第1季度收益率(lastYrlyPftrt)、排名(custRaiseRateRankNo)
                - %d年第2季度收益率(nmm6CombProfrat)、排名(detainRateRankNo)
                - %d年第3季度收益率(nmm3YrlyPftrt)、排名(tmPontAsetRaiseTotRanknum)
                - %d年第4季度收益率(nmm1YearlyProfrat)、排名(addRepPurcProTotnumRankno)
                                            
                **年份处理规则**：
                - 所有"今年"字样必须替换为实际年份：%d年
                - 季度字段也要使用实际年份：%d年第%d季度
                                            
                **输出要求**：
                - 所有时间字段必须保留标识（如近一年[滚动12个月]、今年[%d年]）
                - 完全引用字段的实际内容，不要推测
                - 用markdown表格格式返回
                - 如果用户未指定时间范围，在结果开头说明："为您展示默认的排名数据"
                - 展示返回的10条数据，无值的位置用空字符串表示              
                """.formatted(today, oneYearAgo, today,
                currentYear, today,
                currentYear,
                currentQuarter,
                currentYear,
                currentYear, currentYear, currentYear, currentYear,
                currentYear,
                currentYear, currentQuarter,
                currentYear
        );

        this.promptMap = Map.of(
                "guide", """
                            请按照以下步骤处理用户请求：
                            1.首先验证用户请求的基金类型是否在支持的债券基金类型分类中
                                
                            2.如果产品类型不在上述范围内，直接回复：
                                "很抱歉，邮小盈还在努力开发中~目前支持生成六类债券基金产品的市场产品报告，您可以选择：
                                 1-信用债-指数型，
                                 2-信用债主动-开放式，
                                 3-利率债主动-开放式，
                                 4-利率债指数1-3年，
                                 5-利率债指数3-5年，
                                 6-利率债指数1-5年。"                                
                            3.如果基金类型正确，分析用户的对话内容，必须调用以下工具：
                                 查询产品市场排名
                            4.调用工具时需明确区分"近一年"（滚动12个月）和"今年"（自然年）的时间范围，避免混淆
                            5.如果不满足工具调用条件，需要引导用户补充。
                        """,
                "tool", "根据工具执行后的结果,需要进行字段翻译,字段关系映射如下:" + translate("marketRankingTranslatePrompt") + toolPrompt
        );
        this.description = promptMap.get("guide");
    }

    /**
     * 获取当前季度
     */
    private int getCurrentQuarter() {
        int month = java.time.LocalDate.now().getMonthValue();
        return (month - 1) / 3 + 1;
    }
}