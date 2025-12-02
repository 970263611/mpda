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
                            2.如果基金超过三个,返回:邮小盈仅支持三个以内的产品对比。
                            3.如果满足条件，直接进行工具查询，不要和用户确认。
                            4.你返回的内容只能是以下两种情况：
                                (1)工具查询到有效内容：得出结论
                                (2)工具无法查询到有效内容：告知用户无法获取哪些产品信息，提示用户更换对比产品
                        """
                , "tool", "1.根据工具执行后的结果，需要进行字段翻译，字段关系映射如下:\n" + translate("informationTranslatePrompt") + "\n" + """
                            2.如果有产品无法查询到信息，则直接返回并明确告知用户，反之返回以下信息的对比结果：
                                基金名称
                                基金代码
                                基金公司
                                基金类型
                                基金经理
                                基金规模（提取其中的数字信息）
                                近1月年化收益率
                                近3月最大回撤
                            3.不要返回任何的字段翻译前信息，你的职责是需要总结完全翻译后的结果。
                            4.返回的内容需要有格式，有段落，需要用人类能理解的方式返回，不要出现报文字段等辅助信息。
                            5.最后需用一段连贯文字给出综合投资建议，且必须包含以下维度的分析：
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
