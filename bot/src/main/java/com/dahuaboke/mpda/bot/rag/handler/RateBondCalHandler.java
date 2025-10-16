package com.dahuaboke.mpda.bot.rag.handler;


import com.dahuaboke.mpda.bot.rag.RagPrompt;
import com.dahuaboke.mpda.bot.rag.utils.FundClassifierUtil;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Desc: 利率债计算
 * @Author：zhh
 * @Date：2025/9/23 16:52
 */
@Component
public class RateBondCalHandler {

    @Autowired
    private ChatClient chatClient;

    public String[] callModel(BrProductReport fdProductReport , BrProductSummary fdProductSummary) {
        String prodtSname = fdProductReport.getProdtSname();
        String assetNval = fdProductReport.getAssetNval();
        String ivstStgyName = fdProductSummary.getIvstStgyName();
        String bondChrgIntInfo = fdProductReport.getBondChrgIntInfo();
        if(StringUtils.isEmpty(prodtSname) || StringUtils.isEmpty(assetNval) || StringUtils.isEmpty(ivstStgyName) || StringUtils.isEmpty(bondChrgIntInfo)){
            return new String[]{"无","基金投资策略、基金债券投资组合表格、基金规模、基金简称不能为空"};
        }
        String content = chatClient.prompt()
                .user("基金简称是" + prodtSname
                        + "，基金规模是" + FundClassifierUtil.findDouble(assetNval)
                        + "，基金投资策略是" + ivstStgyName
                        + "，基金季度报告中提取的基金债券投资组合表格是" + bondChrgIntInfo
                        + "\n"
                        + RagPrompt.FUND_CLASSIFIER
                )
                .call().content();
        String trim = content.split("『RESULT』")[1].split("『END』")[0].trim();
        return trim.split("\\|\\|");
    }
}
