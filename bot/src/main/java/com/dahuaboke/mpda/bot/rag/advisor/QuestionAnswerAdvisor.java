/*
 * Copyright 2023-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dahuaboke.mpda.bot.rag.advisor;

import com.dahuaboke.mpda.bot.rag.handler.DocContextHandler;
import com.dahuaboke.mpda.bot.rag.handler.SearchHandler;
import com.dahuaboke.mpda.bot.rag.handler.SortHandler;
import com.dahuaboke.mpda.core.rag.handler.EmbeddingSearchHandler;
import com.dahuaboke.mpda.core.rag.rerank.Rerank;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.lang.Nullable;
import reactor.core.scheduler.Scheduler;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Desc: 自定义rag问答增强器
 * @Author：zhh
 * @Date：2025/7/25 1:32
 */
public class QuestionAnswerAdvisor implements BaseAdvisor {

    public static final String RETRIEVED_DOCUMENTS = "qa_retrieved_documents";

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
                 请严格根据用户查询问题，从提供的参考内容中提取最精确的答案：
            
             **用户查询（核心焦点，必须优先匹配）** \s
             "{query}"
            
             **参考内容（仅作为检索来源）** \s
             {question_answer_context}
            
             **硬性要求** \s
             1. **答案必须100%来自参考内容**，禁止任何形式的编造、推断或外部知识 \s
             2. **匹配逻辑**： \s
                - 先筛选参考内容中所有与用户查询直接相关的片段 \s
                - 选择其中语义匹配度最高的一段作为最终答案 \s
                - 输出结果只包含答案即可，不需要包含问题 \s
                - 若存在有多个结果(匹配度未达到绝对唯一),必须完整保留所有相关结果,按语义匹配度从高到底排序,整理输出                   
                - 根据最终答案和用户查询问题,分析结果直接输出原文格式 \s
            
             3. 根据最终答案和用户查询问题,分析结果和整理格式 ,并将最终结果放在『RESULT』和『END』之间：
                  <think>思考过程...</think>
                  『RESULT』
                    制造业占比60% ,金融业是...
                  『END』
                 \s
             4. **无答案情况**：如果参考内容中无任何相关片段，不输出内容"      
            
             5.处理示例
                  输入页面内容："报告期末按行业分类的境内股票投资组合：制造业 (60%)、金融业 (20%)..."
                 \s
                  <think>思考过程...</think>
                  『RESULT』
                     制造业占比60% ,金融业是...
                  『END』 ""\";
            注：用户查询是判断相关性的唯一标准，参考内容仅用于检索，不可影响问题理解。
            """);

    private final SearchRequest searchRequest;

    private final PromptTemplate promptTemplate;

    private final Scheduler scheduler;

    private final int order;

    private final List<String> productName;

    private final List<String> keys;

    private final String fileType;

    private final SearchHandler searchHandler;

    private final EmbeddingSearchHandler embeddingSearchHandler;

    private final SortHandler sortHandler;

    private final DocContextHandler docContextHandler;

    private final Rerank rerankHandler;

    public QuestionAnswerAdvisor(SearchRequest searchRequest, @Nullable PromptTemplate promptTemplate,
                                 @Nullable Scheduler scheduler, int order, List<String> productName, List<String> keys,
                                 String fileType, SearchHandler searchHandler, EmbeddingSearchHandler embeddingSearchHandler, SortHandler sortHandler
            , DocContextHandler docContextHandler, Rerank rerankHandler) {
        this.searchRequest = searchRequest;
        this.promptTemplate = promptTemplate != null ? promptTemplate : DEFAULT_PROMPT_TEMPLATE;
        this.scheduler = scheduler != null ? scheduler : BaseAdvisor.DEFAULT_SCHEDULER;
        this.order = order;
        this.productName = productName;
        this.keys = keys;
        this.fileType = fileType;
        this.searchHandler = searchHandler;
        this.embeddingSearchHandler = embeddingSearchHandler;
        this.docContextHandler = docContextHandler;
        this.sortHandler = sortHandler;
        this.rerankHandler = rerankHandler;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        //处理请求
        List<Document> topKDocuments = processRequest(searchRequest);
        //封装返回
        String documentContext = topKDocuments == null ? ""
                : topKDocuments.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));

        UserMessage userMessage = chatClientRequest.prompt().getUserMessage(); // TODO 替换为wrapper包装类

        String augmentedUserText = this.promptTemplate
                .render(Map.of("query", userMessage.getText(), "question_answer_context", documentContext));

        Map<String, Object> context = new HashMap<>(chatClientRequest.context());
        context.put(RETRIEVED_DOCUMENTS, documentContext);

        return chatClientRequest.mutate()
                .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
                .context(context)
                .build();
    }

    private List<Document> processRequest(SearchRequest searchRequest) {
        int topK = searchRequest.getTopK();
        //1. 通过关键字做精准查询
        List<Document> documents = new ArrayList<>();
        if (productName.isEmpty() && !keys.isEmpty()) {
            documents.addAll(searchHandler.requestKey(searchRequest, keys,fileType));
        } else if (!productName.isEmpty() && !keys.isEmpty()) {
            documents.addAll(searchHandler.requestProductAndKey(searchRequest, productName, keys,fileType));
        }

        //2. 通过关键字并没有匹配到数据,或者匹配到的数据过少, 通过基金名称提取出来
        if (!productName.isEmpty() && (documents.isEmpty() || documents.size() < topK)) {
            List<Document> productDocs = searchHandler.requestProduct(searchRequest, productName, topK * 2,fileType);
            documents.addAll(productDocs);
        }

        //3. 兜底向量查询
        if (documents.isEmpty() || productName.isEmpty()) {
            documents.addAll(embeddingSearchHandler.handler(searchRequest, topK * 2));
        }

        //4. 查询的结果,有可能出现pdf分页情况,获取上下页防止分页场景出现
        List<Document> docContext = new ArrayList<>();
        if (!documents.isEmpty()) {
            docContext = docContextHandler.handler(searchRequest, distinctById(documents),fileType);
        }

        //5. 根据Document ID去重
        ArrayList<Document> allDocs = new ArrayList<>(documents);
        allDocs.addAll(docContext);
        List<Document> uniqueDocs = distinctById(allDocs);

        //6. 按文件,页码整理有序,并返回
        return sortHandler.handler(uniqueDocs);
    }

    private List<Document> distinctById(List<Document> documents){
        Set<String> seenIds = new HashSet<>();
        return documents.stream().filter(doc -> seenIds.add(doc.getId())).toList();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        ChatResponse.Builder chatResponseBuilder;
        if (chatClientResponse.chatResponse() == null) {
            chatResponseBuilder = ChatResponse.builder();
        } else {
            chatResponseBuilder = ChatResponse.builder().from(chatClientResponse.chatResponse());
        }
        chatResponseBuilder.metadata(RETRIEVED_DOCUMENTS, chatClientResponse.context().get(RETRIEVED_DOCUMENTS));
        return ChatClientResponse.builder()
                .chatResponse(chatResponseBuilder.build())
                .context(chatClientResponse.context())
                .build();
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public static final class Builder {
        private Rerank rerankHandler;
        private DocContextHandler docContextHandler;
        private SortHandler sortHandler;
        private EmbeddingSearchHandler embeddingSearchHandler;
        private SearchHandler searchHandler;
        private List<String> keys;
        private List<String> productName;
        private String fileType;
        private SearchRequest searchRequest;

        public Builder() {
        }

        public Builder rerankHandler(Rerank rerankHandler) {
            this.rerankHandler = rerankHandler;
            return this;
        }

        public Builder docContextHandler(DocContextHandler docContextHandler) {
            this.docContextHandler = docContextHandler;
            return this;
        }

        public Builder sortHandler(SortHandler sortHandler) {
            this.sortHandler = sortHandler;
            return this;
        }

        public Builder embeddingHandler(EmbeddingSearchHandler embeddingSearchHandler) {
            this.embeddingSearchHandler = embeddingSearchHandler;
            return this;
        }

        public Builder searchHandler(SearchHandler searchHandler) {
            this.searchHandler = searchHandler;
            return this;
        }

        public Builder keys(List<String> keys) {
            this.keys = keys;
            return this;
        }

        public Builder productName(List<String> productName) {
            this.productName = productName;
            return this;
        }

        public Builder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder searchRequest(SearchRequest searchRequest) {
            this.searchRequest = searchRequest;
            return this;
        }

        public QuestionAnswerAdvisor build() {
            QuestionAnswerAdvisor questionAnswerAdvisor = new QuestionAnswerAdvisor(
                    searchRequest, DEFAULT_PROMPT_TEMPLATE, null, 0, productName, keys,fileType,
                    searchHandler, embeddingSearchHandler, sortHandler, docContextHandler, rerankHandler);
            return questionAnswerAdvisor;
        }
    }
}
