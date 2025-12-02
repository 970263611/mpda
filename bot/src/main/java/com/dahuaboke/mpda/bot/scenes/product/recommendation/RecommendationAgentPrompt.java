package com.dahuaboke.mpda.bot.scenes.product.recommendation;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:47
 */
@Component
public class RecommendationAgentPrompt extends AbstractProductAgentPrompt {

    public RecommendationAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户问题,判断是否需要调用工具获取最新/详细数据。
                            2.满足调用工具时，无须和用户反馈，直接调用工具获取数据。
                            3.如果需要调用工具:
                                (1)需要分析工具所需参数，传入需要的参数
                                (2)如果不满足工具调用条件，需要引导用户补充。
                                (3)用户补充条件后，无须和用户反馈，直接调用工具。
                                最后调用以下工具： 根据年化利率/基金类型/月最大回撤率来查询匹配的产品信息
                            4.如果不需要调用工具,直接基于已有信息分析回答用户问题。
                           
                        """
                , "tool", "。 根据工具执行后的结果,需要遵循以下规则" +
                        "1. 对于基金的任何信息都需要在工具执行结果中，不可以进行胡乱编造，出现不存在的基金代码和基金信息。" +
                        "2. 工具返回的原始查询结果(如列表，实体类)，禁止直接呈现给用户；需要你自主整理，分析，优化为清晰易懂的MarkDown表格格式。如果没有返回任何结果,禁止胡编乱造。无需下述流程,直接返回。" +
                        "3. 如果工具未返回任何结果, 无需考虑字段翻译,直接返回即可" +
                        "4. 对工具返回数据需要进行字段翻译,字段关系映射如下:" +
                        "" + translate("recommendationTranslatePrompt") + """
                            如果是全量信息查询，则仅返回以下字段内容：
                                基金类型(必须有)
                                基金代码
                                基金名称
                                基金管理人
                                基金托管人
                                基金经理
                                基金规模（单位元）
                                收益率(工具返回的rateTimePeriod字段值)
                                最大回撤(工具返回的drawTimePeriod字段值)
                            如果是【配置】问题:
                                - 直接返回查询结果,模型基于工具返回的结果分析配置基金给用户
                            如果是【推荐】问题:
                                如果【工具返回的count】 >= 10， 只给用户展示前10条产品。 返回: 根据您的要求,我共找到【工具返回的count】款符合条件的基金。以下是为您按收益率从高到低排序的前10个产品。如果这份列表不完全符合您的预期，您可以告诉我更具体的偏好，我能为您精准筛选，如: 指定基金公司或基金经理，设定您期望的收益率或最大回撤的具体区间。
                                如果【工具返回的count】 <  10， 返回: 根据您的要求,我共找到【工具返回的count】款符合条件的基金。以下是为您按收益率从高到低排序的前【工具返回的count】个产品。如果这份列表不完全符合您的预期，您可以告诉我更具体的偏好，我能为您精准筛选，如: 指定基金公司或基金经理，设定您期望的收益率或最大回撤的具体区间。
                            如果工具未返回任何信息，直接回复：未找到匹配条件的基金，您可以提供期望的年化利率、具体的基金类型或可接受的最大回撤率吗？这些信息将帮助我们更精准地筛选出符合您需求的基金产品。
                        """);
        this.description = promptMap.get("guide");
    }

}
