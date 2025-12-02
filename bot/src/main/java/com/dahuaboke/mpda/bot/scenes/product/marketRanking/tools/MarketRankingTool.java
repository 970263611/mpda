package com.dahuaboke.mpda.bot.scenes.product.marketRanking.tools;


import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.QuarterYieldRateInfoDto;
import com.dahuaboke.mpda.bot.tools.enums.TimeType;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:39
 */
@Component
public class MarketRankingTool extends ProductTool<MarketRankingTool.Input> {

    @Override
    public String getDescription() {
        return "查询产品市场排名/定制市场分析报告/市场排名报告";
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType(), "finBondType", "year");
    }

    @Override
    public Class<MarketRankingTool.Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(MarketRankingTool.Input input) {
        String period = input.period + "";
        String finBondType = input.finBondType + "";
        if ("0".equals(finBondType)) {
            finBondType = "1";
        }
        //特殊处理场景:用户查询时间为季度
        if (TimeType.CURRENT_YEAR_Q1.getCode().equals(period) || TimeType.CURRENT_YEAR_Q2.getCode().equals(period) || TimeType.CURRENT_YEAR_Q3.getCode().equals(period) || TimeType.CURRENT_YEAR_Q4.getCode().equals(period)) {
            if (input.period >= 5 && input.period <= 8) {
                String periodTemp = String.valueOf(input.period - 4);
                Map<String, String> supportYearQuartergMap = supportYearQuarter(Integer.parseInt(input.year() + periodTemp));
                if ("false".equals(supportYearQuartergMap.get("flag"))) {
                    return ToolResult.success("查询成功", "您查询的时间不在可查询时间范围内~" + "支持查询的季度范围为" + supportYearQuartergMap.get("cue"));
                }
            }
        }

        List<MarketRankDto> marketRankList = productToolHandler.getMarketRank(finBondType, period);
        List<Map<String, Object>> result = new ArrayList<>();
        if (marketRankList == null || marketRankList.isEmpty()) {
            return ToolResult.success("查询成功", "未查询到满足条件的数据，请换条件进行查询呢~");
        }
        if (TimeType.LAST_WEEK.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("nwk1CombProfrat", m.getNwk1CombProfrat());
                fieldMap.put("indsRankSeqNo", m.getIndsRankSeqNo());
                fieldMap.put("txamtRankNo", m.getTxamtRankNo());
                fieldMap.put("curdayBalChgTotalAccnum", m.getCurdayBalChgTotalAccnum());
                fieldMap.put("styoMaxWdwDesc", m.getStyoMaxWdwDesc());
                result.add(fieldMap);
            }
        } else if (TimeType.LAST_MONTH.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("nmm1CombProfrat", m.getNmm1CombProfrat());
                fieldMap.put("lblmRank", m.getLblmRank());
                fieldMap.put("busicmLmRank", m.getBusicmLmRank());
                fieldMap.put("curdayBalChgAccnum", m.getCurdayBalChgAccnum());
                fieldMap.put("maxWdwrt", m.getMaxWdwrt());
                result.add(fieldMap);
            }
        } else if (TimeType.LAST_3_MONTH.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("nmm3CombProfrat", m.getNmm3CombProfrat());
                fieldMap.put("rankScopeLowLmtVal", m.getRankScopeLowLmtVal());
                fieldMap.put("intglRankSeqNo", m.getIntglRankSeqNo());
                fieldMap.put("supptranBalChgTotalAccnum", m.getSupptranBalChgTotalAccnum());
                fieldMap.put("fundstgMaxWdwrt", m.getFundstgMaxWdwrt());
                result.add(fieldMap);
            }
        } else if (TimeType.LAST_YEAR.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("nyy1Profrat", m.getNyy1Profrat());
                fieldMap.put("reachStRankSeqNo", m.getReachStRankSeqNo());
                fieldMap.put("chremMgrIntglRankSeqNo", m.getChremMgrIntglRankSeqNo());
                fieldMap.put("centerCfmCurdayChgTnum", m.getCenterCfmCurdayChgTnum());
                fieldMap.put("nyy1Wdwrt", m.getNyy1Wdwrt());
                result.add(fieldMap);
            }
        } else if (TimeType.CURRENT_YEAR_Q1.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("lastYrlyPftrt", m.getLastYrlyPftrt());
                fieldMap.put("custRaiseRateRankNo", m.getCustRaiseRateRankNo());
                result.add(fieldMap);
            }
        } else if (TimeType.CURRENT_YEAR_Q2.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("nmm6CombProfrat", m.getNmm6CombProfrat());
                fieldMap.put("detainRateRankNo", m.getDetainRateRankNo());
                result.add(fieldMap);
            }
        } else if (TimeType.CURRENT_YEAR_Q3.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("nmm3YrlyPftrt", m.getNmm3YrlyPftrt());
                fieldMap.put("tmPontAsetRaiseTotRanknum", m.getTmPontAsetRaiseTotRanknum());
                result.add(fieldMap);
            }
        } else if (TimeType.CURRENT_YEAR_Q4.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("nmm1YearlyProfrat", m.getNmm1YearlyProfrat());
                fieldMap.put("addRepPurcProTotnumRankno", m.getAddRepPurcProTotnumRankno());
                result.add(fieldMap);
            }
        } else if (TimeType.CURRENT_YEAR.getCode().equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("drtPftrtTval", m.getDrtPftrtTval());
                fieldMap.put("rtnRtRank", m.getRtnRtRank());
                fieldMap.put("pftrtName", m.getPftrtName());
                fieldMap.put("busicmOybinpRank", m.getBusicmOybinpRank());
                result.add(fieldMap);
            }
        } else if (TimeType.NONE.getCode().equals(period) || "".equals(period)) {
            for (MarketRankDto m : marketRankList) {
                Map<String, Object> fieldMap = buildCommonFieldMap(m);
                fieldMap.put("drtPftrtTval", m.getDrtPftrtTval());
                fieldMap.put("rtnRtRank", m.getRtnRtRank());
                fieldMap.put("pftrtName", m.getPftrtName());
                fieldMap.put("busicmOybinpRank", m.getBusicmOybinpRank());

                fieldMap.put("lastYrlyPftrt", m.getLastYrlyPftrt());
                fieldMap.put("custRaiseRateRankNo", m.getCustRaiseRateRankNo());
                fieldMap.put("nmm6CombProfrat", m.getNmm6CombProfrat());
                fieldMap.put("detainRateRankNo", m.getDetainRateRankNo());
                fieldMap.put("nmm3YrlyPftrt", m.getNmm3YrlyPftrt());
                fieldMap.put("tmPontAsetRaiseTotRanknum", m.getTmPontAsetRaiseTotRanknum());
                fieldMap.put("nmm1YearlyProfrat", m.getNmm1YearlyProfrat());
                fieldMap.put("addRepPurcProTotnumRankno", m.getAddRepPurcProTotnumRankno());
                result.add(fieldMap);
            }
        }
        //TODO delete
/*        System.out.println("----" + result.size());
        for (int i = 0; i < result.size(); i++) {
            Map<String, Object> stringObjectMap = result.get(i);
            for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue() + ",");
            }
            System.out.println();
        }*/
        return ToolResult.success("查询成功", result);
    }

    public record Input(
            @JsonPropertyDescription("""
                    债基类型（必填）
                    请严格按照以下规则匹配用户语义到数字参数(1-6) 
                    1:信用债-指数型        -包含"信用债"且明确"指数型" "被动跟踪" 等关键字
                    2:信用债主动-开放式    -包含"信用债"且有"主动管理" "主动型" "开放式" "可申赎"等关键字
                    3:利率债主动-开放式    -包含"利率债"且有"主动管理" "主动型" "开放式" "可申赎"等关键字
                    4:利率债指数1-3年     -包含"利率债" "指数型"且明确年限"1-3年" "一年到三年"
                    5:利率债指数3-5年     -包含"利率债" "指数型"且明确年限"3-5年" "三年到五年"
                    6:利率债指数1-5年     -包含"利率债" "指数型"且明确年限"1-5年" "一年到五年"                     
                     """) int finBondType,
            @JsonPropertyDescription("""
                    可用时间范围（非必填）
                    请严格按照以下规则匹配用户语义到数字参数(0-9)
                    0:无
                    1:近1周
                    2:近1月
                    3:近3月
                    4:近一年/（滚动12个月）
                    5:第一季度
                    6:第二季度
                    7:第三季度
                    8:第四季度(8)
                    9:当前年
                     """) int period,
            @JsonPropertyDescription("""
                    查询年份（默认为当前年，时间格式为yyyy）
                     """) int year
    ) {

    }

    private Map<String, Object> buildCommonFieldMap(MarketRankDto m) {
        Map<String, Object> fielCommonMap = new HashMap<>();
        fielCommonMap.put("fundFnm", m.getFundFnm());
        fielCommonMap.put("assetNval", m.getAssetNval());
        fielCommonMap.put("fundMgrName", m.getFundMgrName());
        fielCommonMap.put("contractEffDate", m.getContractEffDate());
        fielCommonMap.put("investMgrName", m.getInvestMgrName());
        fielCommonMap.put("curRenewDt", m.getCurRenewDt());
        fielCommonMap.put("unitNetVal", m.getUnitNetVal());
        fielCommonMap.put("period", m.getPeriod());
        fielCommonMap.put("fundCode", m.getFundCode());
        fielCommonMap.put("finBondType", m.getFinBondType());
        return fielCommonMap;
    }

    /**
     * 返回是否支持查询
     * 如果不支持 返回支持查询的季度范围
     * @param yearAndQuarter
     * @return
     */
    private Map<String, String> supportYearQuarter(int yearAndQuarter) {
        Map<String, String> supportYearQuarterMap = new HashMap<>();
        String lastQuarter = productToolHandler.dealDldFlnm(LocalDate.now());
        int currentYear = Integer.parseInt(lastQuarter.substring(0, 4));
        int currentQuarter = Integer.parseInt(lastQuarter.substring(5));
        List<QuarterYieldRateInfoDto> quarterYieldRateInfoDtos = new ArrayList<>();
        // j=0 → 当前季度-3（最早），j=1 → 当前季度-2，j=2 → 当前季度-1，j=3 → 当前季度（最晚）
        List<Integer> supportYearQuarter = new ArrayList<>();
        String cue = "";
        for (int j = 3; j >= 0; j--) { // 调整循环变量:从3递减到0
            int targetQuarter = currentQuarter - j;
            int targetYear = currentYear;
            // 处理跨年份（如2025Q1 - 3 = 2024Q2）
            while (targetQuarter < 1) {
                targetQuarter += 4;
                targetYear -= 1;
            }
            // 生成年份季度字符串（如2024Q2）
            supportYearQuarter.add(Integer.parseInt(String.valueOf(targetYear) + targetQuarter));
            cue += targetYear + "年第" + targetQuarter + "季度 ";
        }
        Integer begin = supportYearQuarter.get(0);
        Integer end = supportYearQuarter.get(3);
        if (begin <= yearAndQuarter && yearAndQuarter <= end) {
            supportYearQuarterMap.put("flag", "true");
        } else {
            supportYearQuarterMap.put("flag", "false");
            supportYearQuarterMap.put("cue", cue);
        }
        return supportYearQuarterMap;
    }
}
