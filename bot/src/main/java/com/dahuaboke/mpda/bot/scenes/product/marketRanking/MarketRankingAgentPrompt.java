package com.dahuaboke.mpda.bot.scenes.product.marketRanking;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingAgentPrompt extends AbstractProductAgentPrompt {

    public MarketRankingAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户的对话内容，必须调用以下工具：
                                 查询产品市场排名
                            2.如果不满足工具调用条件，需要引导用户补充。
                        """
                , "tool", translate() +"""
                            根据工具执行结果，返回以下信息的结果：
                                基金代码
                                基金规模
                                基金管理人
                                成立日期
                                近一年收益（%）
                                近一年回撤（%）
                                排名
                                昨日排名
                                排名变动
                            需要完全引用字段的实际内容不要推测。
                            
                            输出结尾需要添加以下内容：
                                对话框内为您展示前十名产品详情，全市场排名情况您可以在Excel文件中查看。如需其他指标，您可以直接告诉邮小盈。
                                Excel链接：https://www.psbc.com/excel
                            用md格式返回。
                        """);
        this.description = promptMap.get("guide");
    }
}
