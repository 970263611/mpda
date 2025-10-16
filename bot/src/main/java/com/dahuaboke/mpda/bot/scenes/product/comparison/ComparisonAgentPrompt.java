package com.dahuaboke.mpda.bot.scenes.product.comparison;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 09:13
 */
@Component
public class ComparisonAgentPrompt extends AbstractProductAgentPrompt {

    public ComparisonAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户的对话内容，必须调用以下工具：
                                通过两个基金的基金编号，查询两个基金信息，用于对比
                            2.如果不满足工具调用条件，需要引导用户补充。
                        """
                , "tool", translate() + """
                            根据工具执行结果，返回以下信息的对比结果：
                                基金名称
                                基金代码
                                基金公司
                                基金类型
                                基金经理
                                基金规模（提取其中的数字信息）
                                近3月收益率
                                近3月最大回撤
                            需要完全引用字段的实际内容不要推测。
                            最后需用一段连贯文字给出综合投资建议，且必须包含以下维度的分析：
                                1. 基金经理和基金管理人情况
                                2. 收益表现对比
                                3. 稳定性特征分析
                                4. 资产构成差异
                                5. 回撤风险比较
                                6. 适合的风险偏好类型
                                7. 申购成本对比
                                8. 投资策略灵活性
                            如果字段内容不是md格式需要用md格式美化下输出结果。
                        """);
        this.description = promptMap.get("guide");
    }
}
