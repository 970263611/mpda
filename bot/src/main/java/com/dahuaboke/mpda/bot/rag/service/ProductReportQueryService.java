package com.dahuaboke.mpda.bot.rag.service;

import com.dahuaboke.mpda.bot.rag.advisor.QuestionAnswerAdvisor;
import com.dahuaboke.mpda.bot.rag.handler.DocContextHandler;
import com.dahuaboke.mpda.bot.rag.handler.SearchHandler;
import com.dahuaboke.mpda.bot.rag.handler.SortHandler;
import com.dahuaboke.mpda.bot.rag.ProcessingMonitor;
import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.dahuaboke.mpda.core.rag.handler.EmbeddingSearchHandler;
import com.dahuaboke.mpda.core.rag.rerank.Rerank;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Desc: 文档查询服务
 * @Author：zhh
 * @Date：2025/9/2 17:27
 */
@Service
public class ProductReportQueryService {

    private static final Logger log = LoggerFactory.getLogger(ProductReportQueryService.class);

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
    private ProcessingMonitor processingMonitor;



    public BrProductReport queryFundProduct(BrProduct product) {
        String productCode = product.getFundCode();
        String productName = product.getFundProdtFullNm();

        // 初始化查询
        Map<String, String> queryMap = FundDocUtil.initAllQuery(BrProductReport.class);

        // 将模型结果封装到基金对象
        FundFieldMapper mapper = new FundFieldMapper(BrProductReport.class);
        BrProductReport fdProductReport = new BrProductReport();
        // 处理所有查询问题
        for (Map.Entry<String, String> entry : queryMap.entrySet()) {
            String question = entry.getKey();
            String key = entry.getValue();
            String userQuery = "基于" + productCode + "基金代码," + productName + "的" + question;
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
                                .productName(List.of(productCode))
                                .searchRequest(searchRequest)
                                .searchHandler(searchHandler)
                                .embeddingHandler(embeddingSearchHandler)
                                .docContextHandler(docContextHandler)
                                .rerankHandler(rerankHandler)
                                .sortHandler(sortHandler)
                                .build())
                        .user(userQuery)
                        .call().content();
                String answer = "";
                String prefix = "『RESULT』";
                String suffix = "『END』";
                if(content.contains(prefix) && content.contains(suffix)){
                    answer = content.split(prefix)[1].split(suffix)[0].trim();
                }

                mapper.setFieldByComment(fdProductReport, question, answer);
            } catch (Exception e) {
                log.error("基金{}查询失败问题为: {}", productName, userQuery, e);
            }
        }
        return fdProductReport;
    }


}