package com.dahuaboke.mpda.bot.scenes.product.information;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:10
 */
@Component
public class InformationAgentPrompt extends AbstractProductAgentPrompt {

    public InformationAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户的对话内容，调用以下工具：
                                通过产品编号查询产品的详细信息
                            2.需要分析工具所需参数，必须传入需要的参数
                            3.如果不满足工具调用条件，需要引导用户补充。
                        """
                , "tool", translate() + """
                            根据中英文对照翻译返回正确的字段中文结果。
                            对于字段本身内容不要修改，原样返回。
                            仅回答用户最后一个问题，不要重复回答记忆中的问题。
                            如果是全量信息查询，则仅返回以下字段内容：
                                基金名称
                                基金代码
                                基金公司
                                基金类型
                                基金经理
                                基金规模
                                近1月年化收益率
                                近3月最大回撤
                        """);
        this.description = promptMap.get("guide");
    }
}
