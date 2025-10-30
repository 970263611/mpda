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
                            1.分析用户的对话内容，调用以下工具：
                                根据年化利率/基金类型/月最大回撤率来查询匹配的产品信息

                            2.需要分析工具所需参数，传入需要的参数
                            3.如果不满足工具调用条件，需要引导用户补充。
                        """
                , "tool", """
                            如果字段内容不是md格式需要用md格式美化下输出结果。
                            如果查询出来的产品列表数量>5, 返回: 根据您的要求,我共找到XX款符合条件的基金。一下是为您按收益率从高到低排序的前5个产品。如果这份列表不完全符合您的预期，您可以告诉我更具体的偏好，我能为您精准筛选，如: 指定基金公司或基金经理，设定您期望的收益率或最大回撤的具体区间。
                            如果查出来的产品<=5,返回: 根据您的要求，邮小盈为您精准筛选出了【x】款非常符合的产品。
                            如果工具返回未查询到匹配的产品信息，直接回复：未找到匹配条件的基金，您可以提供期望的年化利率、具体的基金类型或可接受的最大回撤率吗？这些信息将帮助我们更精准地筛选出符合您需求的基金产品。
                        """);
        this.description = promptMap.get("guide");
    }
}
