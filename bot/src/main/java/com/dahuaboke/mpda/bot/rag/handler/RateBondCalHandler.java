package com.dahuaboke.mpda.bot.rag.handler;


import com.dahuaboke.mpda.bot.rag.RagPrompt;
import com.dahuaboke.mpda.bot.rag.utils.FundClassifierUtil;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.BondFundType;
import com.dahuaboke.mpda.bot.tools.service.BrMarketProductReportService;
import com.dahuaboke.mpda.bot.tools.service.BrProductReportService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(RateBondCalHandler.class);

    private static final Double MIN_NVAL = 5000000000.0;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private BrProductReportService brProductReportService;

    @Autowired
    private BrMarketProductReportService brMarketProductReportService;

    public boolean calculateRateBond(String fundCode, BrProductReport report, BrProductSummary summary) {
        if(report.getAssetNval() == null || report.getAssetNval().isEmpty()){
            return false;
        }
        Double assetNval = FundClassifierUtil.findDouble(report.getAssetNval());
        report.setAssetNval(assetNval.toString());
        log.info("{}该基金期末资产净值是{}", fundCode, assetNval);

        if (assetNval < MIN_NVAL) {
            return false;
        }
        log.info("{}开始发送模型计算利率债", fundCode);
        String[] strings = this.callModel(report, summary);
        String type = strings[0];
        String reason = strings[1];

        //转换数字是为了判断模型返回内容是否正确
        String code = type.trim();
        int codeNum = 0;
        try {
            codeNum = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            log.error("type{} parse fail ", code);
        }
        code = String.valueOf(codeNum);

        report.setClsReasonCode(code);
        report.setMainReason(reason);
        //更新季报
        brProductReportService.updateProductReport(report);
        if(BondFundType.NONE.getCode().equals(code)){
            return false;
        }
        log.info("{}计算完成利率债是{},开始插入市场报告表", fundCode, code);
        //插入市场报告表
        BrMarketProductReport brMarketProductReport = new BrMarketProductReport();
        brMarketProductReport.setFundCode(fundCode);
        brMarketProductReport.setFinBondType(code);
        brMarketProductReportService.insertMarketProductReport(brMarketProductReport);
        return true;
    }

    private String[] callModel(BrProductReport fdProductReport, BrProductSummary fdProductSummary) {
        String prodtSname = fdProductReport.getProdtSname();
        String assetNval = fdProductReport.getAssetNval();
        String ivstStgyName = fdProductSummary.getIvstStgyName();
        String bondChrgIntInfo = fdProductReport.getBondChrgIntInfo();
        if (StringUtils.isEmpty(prodtSname) || StringUtils.isEmpty(assetNval) || StringUtils.isEmpty(ivstStgyName) || StringUtils.isEmpty(bondChrgIntInfo)) {
            return new String[]{"无", "基金投资策略、基金债券投资组合表格、基金规模、基金简称不能为空"};
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
        String answer = "";
        String prefix = "『RESULT』";
        String suffix = "『END』";
        if(content.contains(prefix) && content.contains(suffix)){
            answer = content.split(prefix)[1].split(suffix)[0].trim();
        }
        if(answer.contains("||")){
            return answer.split("\\|\\|");
        }
        log.error("基金{} 模型返回内容有误,识别失败",fdProductReport.getFundCode());
        return new String[]{BondFundType.NONE.getCode(),"模型返回内容有误,识别失败"};
    }

}
