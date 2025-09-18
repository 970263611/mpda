package com.dahuaboke.mpda.bot.rag.service;

import com.dahuaboke.mpda.bot.rag.FundProduct;
import com.dahuaboke.mpda.bot.rag.RagPrompt;
import com.dahuaboke.mpda.bot.rag.advisor.QuestionAnswerAdvisor;
import com.dahuaboke.mpda.bot.rag.handler.DocContextHandler;
import com.dahuaboke.mpda.bot.rag.handler.SearchHandler;
import com.dahuaboke.mpda.bot.rag.handler.SortHandler;
import com.dahuaboke.mpda.bot.rag.monitor.ProcessingMonitor;
import com.dahuaboke.mpda.bot.rag.utils.FundClassifierUtil;
import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.entity.YwjqrProduct;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.dahuaboke.mpda.core.rag.handler.EmbeddingSearchHandler;
import com.dahuaboke.mpda.core.rag.rerank.Rerank;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Desc: 文档查询服务
 * @Author：zhh
 * @Date：2025/9/2 17:27
 */
@Service
public class DocumentQueryService {

    private static final Logger log = LoggerFactory.getLogger(DocumentQueryService.class);

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private SearchHandler searchHandler;

    @Autowired
    private EmbeddingSearchHandler embeddingSearchHandler;

    @Autowired
    private DocContextHandler docContextHandler;

    @Autowired
    private Rerank rerankHandler;

    @Autowired
    private SortHandler sortHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductToolHandler productToolHandler;

    @Autowired
    private ProcessingMonitor processingMonitor;

    private static boolean checkFundType(String fundCode) {
        // TODO 合并书涵编写的基金类型查询代码
        return true;
    }

    private static void writeTxt(FundProduct data, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of(fileName, data));
            Files.write(Paths.get("D:/jsonDir06/" + fileName + ".json"), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 处理单个产品
     */
    private boolean processProduct(Map.Entry<String, String> productEntry) {
        // 初始化查询
        Map<String, String> queryMap = initAllQuery();

        String productCode = productEntry.getKey();
        String productName = productEntry.getValue();

        // 将模型结果封装到基金对象
        FundFieldMapper mapper = new FundFieldMapper(FundProduct.class);
        FundProduct fund = new FundProduct();

        // 处理所有查询问题
        for (Map.Entry<String, String> entry : queryMap.entrySet()) {
            String question = entry.getKey();
            String key = entry.getValue();
            String userQuery = "基于" + productCode + "产品代码," + productName + "的" + question;
            try {
                SearchRequest searchRequest = SearchRequest.builder()
                        .topK(15)
                        .query(userQuery)
                        .similarityThreshold(0.25)
                        .build();

                String content = chatClient.prompt()
                        .advisors(QuestionAnswerAdvisor
                                .builder()
                                .keys(List.of(key))
                                .productName(List.of(productName))
                                .searchRequest(searchRequest)
                                .searchHandler(searchHandler)
                                .embeddingHandler(embeddingSearchHandler)
                                .docContextHandler(docContextHandler)
                                .rerankHandler(rerankHandler)
                                .sortHandler(sortHandler)
                                .build())
                        .user(userQuery)
                        .call().content();

                String answer = content.split("『RESULT』")[1].split("『END』")[0].trim();

                mapper.setFieldByComment(fund, question, answer);
            } catch (Exception e) {
                log.error("产品{}查询失败问题为: {}", productName, userQuery, e);
                return false;
            }
        }

        // 获取基金分类
        if (checkFundType(fund.getFundCode())) {
            Map<String, String> map = FundClassifierUtil.classifyFund(fund);
            String[] modelResult = callModel(fund);

            // 封装对象
            fund.setFundClassificationCode(map.get("final_category"));
            fund.setFundClassificationReasonCode(map.get("reason"));
            fund.setFundClassificationModel(modelResult[0]);
            fund.setFundClassificationReasonModel(modelResult[1]);
        }

        // 写入数据库
        //writeDb(fund);
        writeTxt(fund, productName);
        return true;
    }

    /**
     * 观测处理产品
     */
    public void processProducts(Map<String, String> productMap) {
        // 转换为列表处理
        List<Map.Entry<String, String>> productEntries = new ArrayList<>(productMap.entrySet());

        ProcessingMonitor.ProcessingResult<Map.Entry<String, String>> result = processingMonitor.processBatch(
                productEntries,
                this::processProduct,
                Map.Entry::getValue,
                "产品文档查询"
        );

        // 将失败记录写入文件
        processingMonitor.writeFailuresToFile(result.getFailures(), "product_query_processing");
    }

    private Map<String, String> initAllQuery() {
        FundFieldMapper fundFieldMapper = new FundFieldMapper(FundProduct.class);
        return fundFieldMapper.getQuestionKeyWordMap();
    }

    private void writeDb(FundProduct data) {
        YwjqrProduct ywjqrProduct = objectMapper.convertValue(data, YwjqrProduct.class);
        productToolHandler.fundProduct(ywjqrProduct);
    }

    private String[] callModel(FundProduct fundData) {
        String content = chatClient.prompt()
                .user("基金简称是" + fundData.getFundShortName()
                        + "，基金规模是" + FundClassifierUtil.findDouble(fundData.getNetAssetValue())
                        + "，基金投资策略是" + fundData.getInvestmentStrategy()
                        + "，基金季度报告中提取的基金债券投资组合表格是" + fundData.getBondPortfolio()
                        + "\n"
                        + RagPrompt.FUND_CLASSIFIER
                )
                .call().content();
        String trim = content.split("『RESULT』")[1].split("『END』")[0].trim();
        return trim.split("\\|\\|");
    }
}