package com.dahuaboke.mpda.bot.scenes.product.information.code;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:10
 */
@Component
public class InformationByIdAgentPrompt extends AbstractProductAgentPrompt {

    public InformationByIdAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户的对话内容，调用以下工具：
                                通过产品编号查询产品的详细信息
                            2.需要分析工具所需参数，必须传入需要的参数
                            3.如果不满足工具调用条件，需要引导用户补充。
                        """
                , "tool", "根据工具执行后的结果,需要进行字段翻译,字段关系映射如下:" + translate() + """
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
                            如果查询的字段不在中英文对照翻译字段中，则返回；"很抱歉，邮小盈暂未收录该指标，我会继续覆盖各类指标~您可以关注该产品的其他信息，如：产品类型、风险水平、基金规模、基金经理、基金收费方式、基金购买限额、基金收费方式、收益率、最大回撤"
                        """);
        this.description = promptMap.get("guide");
    }
}
