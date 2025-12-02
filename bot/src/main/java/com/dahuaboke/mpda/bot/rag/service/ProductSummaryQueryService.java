package com.dahuaboke.mpda.bot.rag.service;

import com.dahuaboke.mpda.bot.rag.advisor.QuestionAnswerAdvisor;
import com.dahuaboke.mpda.bot.rag.handler.DocContextHandler;
import com.dahuaboke.mpda.bot.rag.handler.SearchHandler;
import com.dahuaboke.mpda.bot.rag.handler.SortHandler;
import com.dahuaboke.mpda.bot.rag.ProcessingMonitor;
import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.bot.tools.entity.BrPdfParseExceptions;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import com.dahuaboke.mpda.bot.tools.enums.PdfExceptionType;
import com.dahuaboke.mpda.bot.tools.service.BrPdfParseExceptionsService;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.dahuaboke.mpda.core.rag.handler.EmbeddingSearchHandler;
import com.dahuaboke.mpda.core.rag.rerank.Rerank;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
public class ProductSummaryQueryService {

    private static final Logger log = LoggerFactory.getLogger(ProductSummaryQueryService.class);

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

    @Autowired
    private BrPdfParseExceptionsService brPdfParseExceptionsService;



    public BrProductSummary queryFundProduct(BrProduct product) {
        String productCode = product.getFundCode();
        String productName = product.getFundProdtFullNm();
        String ancmTpBclsCd = product.getAncmTpBclsCd();


        // 初始化查询
        Map<String, String> queryMap = FundDocUtil.initAllQuery(BrProductSummary.class);

        // 将模型结果封装到基金对象
        FundFieldMapper mapper = new FundFieldMapper(BrProductSummary.class);
        BrProductSummary fdProductSummary = new BrProductSummary();
        // 处理所有查询问题
        for (Map.Entry<String, String> entry : queryMap.entrySet()) {
            String question = entry.getKey();
            String key = entry.getValue();
            String userQuery = "基于" + productCode + "基金代码," + "基金名称为" + productName + "的" + question;
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
                                .fileType(ancmTpBclsCd)
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

                String answer = "无数据";
                String prefix = "『RESULT』";
                String suffix = "『END』";
                if(content.contains(prefix) && content.contains(suffix)){
                    answer = content.split(prefix)[1].split(suffix)[0].trim();
                }

                mapper.setFieldByComment(fdProductSummary, question, StringUtils.isEmpty(answer)?"无数据":answer);
            } catch (Exception e) {
                log.error("基金{}查询失败问题为: {}", productName, userQuery, e);
                insertPdfExceptionsTable(productCode,ancmTpBclsCd,productName,question,e);
            }
        }
        return fdProductSummary;
    }

    public BrProductSummary queryFundProduct(BrProductSummary brSummaryReport, String ancmTpBclsCd , String question) throws Exception {
        // 将模型结果封装到基金对象
        FundFieldMapper mapper = new FundFieldMapper(BrProductSummary.class);
        String productCode = brSummaryReport.getFundCode();
        String productName = brSummaryReport.getFundFnm();
        String userQuery = "基于" + productCode + "基金代码," + "基金名称为" + productName + "的" + question;

        SearchRequest searchRequest = SearchRequest.builder()
                .topK(15)
                .query(userQuery)
                .similarityThreshold(0.25)
                .build();

        String content = chatClient.prompt()
                .advisors(QuestionAnswerAdvisor
                        .builder()
                        .keys(List.of())
                        .productName(List.of(productCode))
                        .fileType(ancmTpBclsCd)
                        .searchRequest(searchRequest)
                        .searchHandler(searchHandler)
                        .embeddingHandler(embeddingSearchHandler)
                        .docContextHandler(docContextHandler)
                        .rerankHandler(rerankHandler)
                        .sortHandler(sortHandler)
                        .build())
                .user(userQuery)
                .call().content();
        String answer = "无数据";
        String prefix = "『RESULT』";
        String suffix = "『END』";
        if(content.contains(prefix) && content.contains(suffix)){
            answer = content.split(prefix)[1].split(suffix)[0].trim();
        }

        mapper.setFieldByComment(brSummaryReport, question, StringUtils.isEmpty(answer)?"无数据":answer);
        return brSummaryReport;
    }



    public void insertPdfExceptionsTable(String productCode, String ancmTpBclsCd, String productName,String userQuery,Exception e) {
        try {
            BrPdfParseExceptions parseExceptions = new BrPdfParseExceptions();
            parseExceptions.setFundCode(productCode);
            parseExceptions.setAncmTpBclsCd(ancmTpBclsCd);
            parseExceptions.setFundProdtFullNm(productName);
            parseExceptions.setQuestion(userQuery);
            parseExceptions.setExceptionType(PdfExceptionType.QUESTION_LEVEL.getCode());
            if(e != null){
                parseExceptions.setDescription(e.getMessage());
                parseExceptions.setDetails(ExceptionUtils.getStackTrace(e));
            }
            parseExceptions.setCount(0);
            parseExceptions.setStatus(FileDealFlag.PROCESS_FAIL.getCode());
            brPdfParseExceptionsService.insertParseException(parseExceptions);
        } catch (Exception ex) {
            log.error("{}问题处理失败，插入异常表失败。", userQuery, ex);
        }
    }

}