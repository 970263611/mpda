package com.dahuaboke.mpda.bot.rag.utils;


import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Desc: 基金分类器（使用基金数据对象）
 * @Author：zhh
 * @Date：2025/7/30 10:37
 */
public class FundClassifierUtil {

    //基金规模
    private static final double FUND_SIZE = 10_0000_0000;

    //利率债比例
    private static final double INTEREST_RATE_RATIO = 80;

    //信用债比例
    private static final double CREDIT_RATIO = 80;

    //检查主动型关键词
    private static final String[] ACTIVE_KEYWORDS = {"灵活", "超额收益", "动态", "高于基准", "积极", "主动", "增强"};

    //检查被动型关键词
    private static final String[] PASSIVE_KEYWORDS = {"复制", "复刻", "跟踪", "成份券"};


    public static Map<String, String> classifyFund(BrProductReport fdProductReport , BrProductSummary fdProductSummary) {
        Map<String, String> result = new HashMap<>();
        result.put("final_category", "无");
        // 1. 检查基金资产净值是否 >= 10亿元
        if (!checkNetAssetValue(findDouble(fdProductReport.getAssetNval()))) {
            result.put("reason", "基金资产净值小于10亿元");
            return result;
        }

        // 2. 计算债券比例
        BondRatios ratios = calculateBondRatios(fdProductReport);

        if (ratios.interestRateRatio < INTEREST_RATE_RATIO && ratios.creditRatio < CREDIT_RATIO) {
            result.put("reason", "利率债和信用债持仓占比均小于80%");
            return result;
        }

        // 3. 判断基金类型
        if (ratios.creditRatio >= CREDIT_RATIO) {
            String style = determineInvestmentStyle(fdProductSummary.getIvstStgyName());
            if (style == null) {
                result.put("final_category", "信用债");
                result.put("reason", "不符合信用债基金分类条件");
                return result;
            }
            if ("被动型".equals(style)) {
                result.put("final_category", "信用债/信用债-指数型");
            } else {
                result.put("final_category", "信用债/信用债主动-开放式");
            }
            return result;
        }

        if (ratios.interestRateRatio >= INTEREST_RATE_RATIO) {
            // 4. 判断投资风格
            String style = determineInvestmentStyle(fdProductSummary.getIvstStgyName());
            if (style == null) {
                result.put("final_category", "利率债");
                result.put("reason", "不符合利率债基金分类条件");
                return result;
            }

            // 5. 如果是被动型，进一步分类期限
            if ("被动型".equals(style)) {
                String durationType = classifyPassiveDuration(fdProductReport.getProdtSname());
                if (durationType == null) {
                    result.put("final_category", "利率债/利率债-被动式");
                    result.put("reason", "被动型利率债基金但无法确定期限");
                    return result;
                }
                result.put("final_category", "利率债/利率债-被动式/利率债指数-" + durationType);
            } else {
                result.put("final_category", "利率债/利率债主动-开放式");
            }
        }

        return result;
    }


    private static boolean checkNetAssetValue(double netAssetValue) {
        return netAssetValue >= FUND_SIZE;
    }


    private static BondRatios calculateBondRatios(BrProductReport fundData) {
        BondRatios ratios = new BondRatios();

        // 利率债持仓占比 = 国家债券 + 央行票据 + 政策性金融债
        ratios.interestRateRatio = findDouble(fundData.getFirstIssueBondBillNo())
                + findDouble(fundData.getPbcBuyRtslPamt())
                + findDouble(fundData.getPlcyFdbtAmt());

        // 信用债持仓占比 = 企业债券 + 企业短期融资券 + 中期票据 + (金融债券 - 政策性金融债)
        ratios.creditRatio = findDouble(fundData.getBeInvesCorpShrHoldRatio())
                + findDouble(fundData.getDocBillRatio())
                + findDouble(fundData.getBibTmOcqn())
                + (findDouble(fundData.getAmtAppoRatio()) - findDouble(fundData.getPlcyFdbtAmt()));

        return ratios;
    }

    // 判断投资风格
    private static String determineInvestmentStyle(String fundStrategy) {
        if (fundStrategy == null) {
            return null;
        }
        boolean activeFlag = false;
        for (String keyword : ACTIVE_KEYWORDS) {
            if (fundStrategy.contains(keyword)) {
                activeFlag = true;
                break;
            }
        }
        boolean passiveFlag = false;
        for (String keyword : PASSIVE_KEYWORDS) {
            if (fundStrategy.contains(keyword)) {
                passiveFlag = true;
                break;
            }
        }

        if (activeFlag) {
            return "主动型";
        }
        if (passiveFlag) {
            return "被动型";
        }

        return null;
    }

    // 分类被动型利率债的期限
    private static String classifyPassiveDuration(String fundName) {
        if (fundName.contains("1-3")) {
            return "1-3年";
        } else if (fundName.contains("3-5")) {
            return "3-5年";
        } else if (fundName.contains("1-5")) {
            return "1-5年";
        }
        return null;
    }

    public static Double findDouble(String answer) {
        String replace = answer.replace(",", "");
        Pattern pattern = Pattern.compile("[+-]?\\d+\\.?\\d*([eE][+-]?\\d+)?");
        Matcher matcher = pattern.matcher(replace);
        double value = 0.00;
        while (matcher.find()) {
            value = Double.parseDouble(matcher.group());
        }
        return value;
    }

    // 债券比例内部类
    private static class BondRatios {
        // 利率债比例
        double interestRateRatio;
        // 信用债比例
        double creditRatio;
    }
}