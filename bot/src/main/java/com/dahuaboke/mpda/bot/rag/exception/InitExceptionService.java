package com.dahuaboke.mpda.bot.rag.exception;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.bot.rag.service.ProductReportQueryService;
import com.dahuaboke.mpda.bot.rag.service.ProductSummaryQueryService;
import com.dahuaboke.mpda.bot.tools.dao.BrProductReportMapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductSummaryMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.FundInfoType;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Desc: 初始化数据库中为空的数据，重新提取。
 * @Author：zhh
 * @Date：2025/11/21 17:22
 */
@Component
public class InitExceptionService {

    private static final Logger log = LoggerFactory.getLogger(InitExceptionService.class);

    @Autowired
    private BrProductReportMapper reportMapper;

    @Autowired
    private BrProductSummaryMapper summaryMapper;

    @Autowired
    private ProductReportQueryService productReportQueryService;

    @Autowired
    private ProductSummaryQueryService productSummaryQueryService;



    public void initEmpty() {
        Class<BrProductReport> brProductReportClass = BrProductReport.class;
        Class<BrProductSummary> brProductSummaryClass = BrProductSummary.class;
        FundFieldMapper fundFieldMapper = new FundFieldMapper();
        //构建fieldGetters Map
        Map<String, String> reportMap = fundFieldMapper.getQuestionByName(brProductReportClass);
        Map<String, Function<BrProductReport, Object>> reportFiledGetters = this.getFieldGetters(reportMap, brProductReportClass);

        Map<String, String> summaryMap = fundFieldMapper.getQuestionByName(brProductSummaryClass);
        Map<String, Function<BrProductSummary, Object>> summaryFieldGetters = this.getFieldGetters(summaryMap, brProductSummaryClass);

        //找到季报中的空字段数据
        Map<BrProductReport, List<String>> reportResult = this.generateEmptyFieldsMapWithMapper(
                reportMapper,
                reportFiledGetters,
                1000
        );
        //找到概要中的空字段数据
        Map<BrProductSummary, List<String>> summaryResult = this.generateEmptyFieldsMapWithMapper(
                summaryMapper,
                summaryFieldGetters,
                1000
        );

        //3. 插入到异常表中
        reportResult.entrySet().forEach(entry -> this.insertReportException(entry,reportMap));
        summaryResult.entrySet().forEach(entry -> this.insertSummaryException(entry,summaryMap));
    }



    private <T> Map<String, Function<T, Object>> getFieldGetters(Map<String, String> map,Class<T> clazz){
        Map<String, Function<T, Object>> fieldGetters = new HashMap<>();
        for (String fieldName: map.keySet()){
            try{
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                fieldGetters.put(fieldName, (report -> {
                    try {
                        return field.get(report);
                    } catch (IllegalAccessException ignored) {
                    }
                    return null;
                }));
            }catch (Exception ignored){

            }
        }
        return fieldGetters;
    }



    /**
     * 使用BaseMapper进行分页分析空字段（带查询条件）
     */
    public <T> Map<T, List<String>> generateEmptyFieldsMapWithMapper(
            BaseMapper<T> mapper,
            Map<String, Function<T, Object>> fieldGetters,
            int pageSize) {

        Map<T, List<String>> resultMap = new HashMap<>();
        int offset = 0;
        boolean hasMore = true;

        log.info("开始手动分页分析空字段，每页大小: {}", pageSize);

        while (hasMore) {
            // 克隆或创建查询条件
            LambdaQueryWrapper<T> currentWrapper =  new LambdaQueryWrapper<>();

            // 手动分页：使用 LIMIT 和 OFFSET
            currentWrapper.last("LIMIT " + pageSize + " OFFSET " + offset);

            // 执行查询
            List<T> entityList = mapper.selectList(currentWrapper);

            if (entityList.isEmpty()) {
                break;
            }

            log.info("处理偏移量 {}，共 {} 条记录", offset, entityList.size());

            // 处理当前页数据
            Map<T, List<String>> pageResultMap = processPageData(
                    entityList, fieldGetters);

            resultMap.putAll(pageResultMap);

            // 如果返回的记录数小于 pageSize，说明是最后一页
            if (entityList.size() < pageSize) {
                hasMore = false;
            }

            offset += pageSize;

            // 安全限制，防止无限循环
            if (offset > 100000) {
                log.warn("已达到最大偏移量限制，强制退出循环");
                break;
            }
        }

        log.info("手动分页分析完成，共发现 {} 条记录存在空字段", resultMap.size());
        return resultMap;
    }

    /**
     * 处理单页数据
     */
    private <T> Map<T, List<String>> processPageData(
            List<T> entityList,
            Map<String, Function<T, Object>> fieldGetters) {

        return entityList.stream()
                .collect(Collectors.toMap(
                        entity -> entity,
                        entity -> getEmptyFields(entity, fieldGetters)
                ))
                .entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 获取实体对象的空字段列表
     */
    private <T> List<String> getEmptyFields(T entity, Map<String, Function<T, Object>> fieldGetters) {
        return fieldGetters.entrySet().stream()
                .filter(entry -> {
                    try {
                        Object value = entry.getValue().apply(entity);
                        return value == null;
                    } catch (Exception e) {
                        log.warn("获取字段 {} 值时发生异常: {}", entry.getKey(), e.getMessage());
                        return false;
                    }
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void insertReportException(Map.Entry<BrProductReport, List<String>> entry ,Map<String, String> reportMap){
        BrProductReport reportEntity = entry.getKey();
        List<String> filedList = entry.getValue();
        List<String> questionList = filedList.stream().map(reportMap::get).toList();
        questionList.forEach(
                question->{
                    productReportQueryService.insertPdfExceptionsTable(
                            reportEntity.getFundCode(),
                            FundInfoType.REPORT.getCode(),
                            reportEntity.getFundFnm(),
                            question, null);
                }
        );

    }

    private void insertSummaryException(Map.Entry<BrProductSummary, List<String>> entry,Map<String, String> summaryMap){
        BrProductSummary summaryEntity = entry.getKey();
        List<String> filedList = entry.getValue();
        List<String> questionList = filedList.stream().map(summaryMap::get).toList();
        questionList.forEach(
                question->{
                    productSummaryQueryService.insertPdfExceptionsTable(
                            summaryEntity.getFundCode(),
                            FundInfoType.SUMMARY.getCode(),
                            summaryEntity.getFundFnm(),
                            question, null);
                }
        );
    }
}
