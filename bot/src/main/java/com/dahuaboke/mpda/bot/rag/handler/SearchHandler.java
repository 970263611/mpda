package com.dahuaboke.mpda.bot.rag.handler;


import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/9/1 10:19
 */
@Component
public class SearchHandler {

    private final VectorStore vectorStore;

    public SearchHandler(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> requestKey(SearchRequest searchRequest, List<String> keys) {
        SearchRequest request = SearchRequest.builder()
                .query(searchRequest.getQuery())
                .topK(searchRequest.getTopK())
                .filterExpression(new Filter.Expression(
                        Filter.ExpressionType.EQ, new Filter.Key("excerpt_keywords"), new Filter.Value(keys.get(0))
                ))
                .similarityThreshold(searchRequest.getSimilarityThreshold())
                .build();
        return vectorStore.similaritySearch(request);
    }

    public List<Document> requestProduct(SearchRequest searchRequest, List<String> matchers, int topK) {
        SearchRequest request = SearchRequest.builder()
                .query(searchRequest.getQuery())
                .topK(topK)
                .filterExpression(new Filter.Expression(
                        Filter.ExpressionType.EQ, new Filter.Key("file_name_keywords"), new Filter.Value(matchers.get(0))
                ))
                .similarityThreshold(searchRequest.getSimilarityThreshold())
                .build();
        return vectorStore.similaritySearch(request);
    }

    public List<Document> requestProductAndKey(SearchRequest searchRequest, List<String> matchers, List<String> keys) {
        SearchRequest request = SearchRequest.builder()
                .query(searchRequest.getQuery())
                .topK(searchRequest.getTopK())
                .filterExpression(new Filter.Expression(
                        Filter.ExpressionType.AND,
                        new Filter.Expression(
                                Filter.ExpressionType.EQ, new Filter.Key("file_name_keywords"), new Filter.Value(matchers.get(0))
                        ),
                        new Filter.Expression(
                                Filter.ExpressionType.EQ, new Filter.Key("excerpt_keywords"), new Filter.Value(keys.get(0))
                        )
                ))
                .similarityThreshold(searchRequest.getSimilarityThreshold())
                .build();
        return vectorStore.similaritySearch(request);
    }
}
