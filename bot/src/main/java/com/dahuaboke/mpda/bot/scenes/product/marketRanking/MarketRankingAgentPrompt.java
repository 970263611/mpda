package com.dahuaboke.mpda.bot.scenes.product.marketRanking;

import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingAgentPrompt extends AbstractProductAgentPrompt {

    // 定义支持的基金类型（固定可选值）
    private static final List<String> SUPPORTED_FUND_TYPES = Arrays.asList(
            "信用债-指数型",
            "信用债主动-开放式",
            "利率债主动-开放式",
            "利率债指数1-3年",
            "利率债指数3-5年",
            "利率债指数1-5年"
    );
    // 定义支持的时间范围（固定可选值）
    private static final List<String> SUPPORTED_TIME_RANGES = Arrays.asList(
            "近一周", "近一月", "近三月", "近一年", "今年",
            "第一季度", "第二季度", "第三季度", "第四季度"
    );

    public MarketRankingAgentPrompt() {
        int currentYear = Year.now().getValue();
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        // 构建时间范围选项文本（便于用户选择）
        // 构建基金类型选项文本（便于用户选择）
        String fundTypeOptions = String.join("\n  ", SUPPORTED_FUND_TYPES);
        // 构建时间范围选项文本（便于用户选择）
        String timeRangeOptions = String.join("、", SUPPORTED_TIME_RANGES);

        String guidePrompt = """
                您好！我可以为您查询债券基金的市场排名报告，请严格按照以下规则提供查询条件，**不符合规则的查询将无法调用工具查询数据**：
                【核心规则（必须遵守）】
                1. 每次查询只能选择[1个基金类型 + 1个时间范围]的组合，禁止同时输入多个基金类型或多个时间范围
                2. 必须完成提供两个条件，缺一不可
                3。请直接使用上述列出的原始名称进行查询，避免自定义类型或时间范围
                4. 如果必输的2个条件用户提供了，直接调用工具：marketRankingTool
                                        
                【支持查询的基金类型（仅可选其一）】
                %s
                                        
                【支持查询的时间范围（仅可选其一）】
                %s
                                
                【今年】
                %d
                                
                【正确查询示例】
                1. 查询利率债主动-开放式的近一月的市场排名
                2. 信用债-指数型 今年
                3. 第二季度 利率债指数1-5年
                                
                【错误查询示例（禁止输入）】
                1. 同时选多个类型：查询信用债-指数型和信用债主动-开放式的近一周排名
                2. 同时选多个时间：查询利率债主动-开放式的近一月和近三月排名
                3. 自定义名称：查询信用债指数型的近30天排名
                4. 缺少条件：查询近一年市场排名
                                        
                【校验规则（必须遵守）】
                在调用查询工具前，请先校验用户输入：
                1. 若用户未提供基金类型 → 提示："请补充选择一个支持的基金类型（可选：%s），仅可选择其一"
                2. 若用户未提供时间范围 → 提示："请补充选择一个支持的时间范围（可选：%s），仅可选择其一"
                3. 重要：若用户提供多个基金类型 → 提示："每次仅可选择一个基金类型，请勿同时选择多个，请重新选择（可选：%s）"
                4. 重要：若用户提供多个时间范围 → 提示："每次仅可选择一个时间范围，请勿同时选择多个，请重新选择（可选：%s）"
                5. 重要：若用户提供多个基金类型+多个时间范围 → 提示："每次仅可选择一个基金类型+一个时间范围，请勿同时选择多个，请重新选择"
                6. 若用户输入的基金类型/时间范围不在支持列表 → 提示："您输入的条件不在支持范围内，基金类型仅支持：%s；时间范围仅支持：%s，请重新选择"
                """.formatted(fundTypeOptions, timeRangeOptions,
                currentYear,
                fundTypeOptions, timeRangeOptions,
                fundTypeOptions, timeRangeOptions,
                fundTypeOptions, timeRangeOptions);
        String toolPrompt = """
                根据工具执行结果，返回以下信息：
                                            
                **必须展示的基础字段**：
                - 基金代码
                - 债券基金类型
                - 基金名称
                - 期末基金资产净值（基金规模）
                - 基金管理人
                - 基金成立日
                - 基金经理
                - 存续天数
                - 单位净值
                       
                **时间范围定义与区分规则**：
                1. 近一年：指从当前日期（%s）往前推12个月的时间段（%s至%s）
                   - 包含完整的自然月滚动周期，不受年度边界限制
                2. 今年（%d年）：指自然年度的1月1日至当前日期（%s）
                   - 仅包含当前自然年内的时间段，跨年部分自动排除
                3. 两者核心区别：近一年是滚动12个月，今年是自然年区间，可能存在重叠但绝对不相同
                                           
                **时间范围字段处理规则**：
                1. 如果用户明确指定了时间范围（近一周、近一月、近三个月、近一年[滚动12个月]、今年、季度等），则只返回相关的时间段字段
                2. 如果用户没有指定时间范围，则默认返回：
                   - 今年（%d年）相关字段
                                            
                **可用时间范围字段**：
                - 近一年[滚动12个月]收益率、排名、昨日排名、排名变动、最大回撤
                - 今年[%d年]收益率、收益率排名、收益率（年化）、收益率排名（年化）
                - 近一周收益率、排名、昨日排名、排名变动、最大回撤
                - 近一月收益率、排名、昨日排名、排名变动、最大回撤
                - 近三个月收益率、排名、昨日排名、排名变动、最大回撤
                - 第1季度收益率、排名
                - 第2季度收益率、排名
                - 第3季度收益率、排名
                - 第4季度收益率、排名
                                            
                **年份、季度处理规则**：
                - 所有"今年"字样必须替换为实际年份：%d年
                                            
                **输出要求**：
                - 如果工具返回包含“未查询到满足条件的数据，请换条件进行查询呢~”：直接返回提示“未查询到满足条件的数据，请换条件进行查询呢~”，禁止做其他操作，禁止构建空的无实际数据的表格
                - 如果工具返回包含“您查询的时间不在可查询时间范围内~”：直接返回提示“您查询的时间不在可查询时间范围内，请换条件进行查询呢~”，禁止做其他操作，禁止构建空的无实际数据的表格
                - 如果工具返回包含“请重新输入查询条件”：直接返回提示“每次仅可选择一个基金类型+一个时间范围，请勿同时选择多个，请重新选择”，禁止做其他操作，禁止构建空的无实际数据的表格
                - 如果工具返回正确结果，按照返回条数展示表格，无值的位置用空字符串表示
                - 所有时间字段必须保留标识（如近一年[滚动12个月]、今年[%d年]）
                - 完全引用字段的实际内容，不要推测
                - 用markdown表格格式返回                                                           
                """.formatted(today, oneYearAgo, today,
                currentYear, today,
                currentYear,
                currentYear,
                currentYear,
                currentYear
        );

        this.promptMap = Map.of(
                "guide", guidePrompt,
                "tool", toolPrompt
        );
        this.description = promptMap.get("guide");
    }

}