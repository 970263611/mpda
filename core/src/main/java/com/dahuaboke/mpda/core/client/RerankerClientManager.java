package com.dahuaboke.mpda.core.client;


import com.alibaba.cloud.ai.document.DocumentWithScore;
import com.alibaba.cloud.ai.model.RerankRequest;
import com.alibaba.cloud.ai.model.RerankResponse;
import com.dahuaboke.mpda.core.client.entity.RerankerRequest;
import com.dahuaboke.mpda.core.client.entity.RerankerResponse;
import com.dahuaboke.mpda.core.config.MpdaRerankProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * auth: dahua
 * time: 2025/8/27 10:27
 */
@Component
public class RerankerClientManager {

    private String baseUrl;
    private String apiKey;
    private String rerankPath;
    private String model;

    private RetryTemplate retryTemplate;
    private RestClient restClient;

    public RerankerClientManager(MpdaRerankProperties properties) {
        this.baseUrl = properties.getBaseUrl();
        this.apiKey = properties.getApiKey();
        this.rerankPath = properties.getRerankPath();
        this.model = properties.getModel();
    }

    @PostConstruct
    public void init() {
        Consumer<HttpHeaders> headers = (h) -> {
            h.setContentType(MediaType.APPLICATION_JSON);
            h.setBearerAuth(apiKey);
        };
        this.restClient = RestClient.builder().clone().baseUrl(baseUrl)
                .defaultHeaders(headers)
                .defaultStatusHandler(RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER)
                .build();
        this.retryTemplate = RetryUtils.DEFAULT_RETRY_TEMPLATE;
    }

    public RerankResponse call(RerankRequest request) {
        Assert.notNull(request.getQuery(), "query must not be null");
        Assert.notNull(request.getInstructions(), "documents must not be null");
        RerankerRequest rerankInput = this.createRequest(request);
        ResponseEntity<List<RerankerResponse>> customRerankOutPut = this.retryTemplate.execute((ctx) -> rerankEntity(rerankInput));
        List<RerankerResponse> response = customRerankOutPut.getBody();
        if (response == null) {
            return new RerankResponse(Collections.emptyList());
        } else {
            List<DocumentWithScore> documentWithScores = response.stream().map((data) ->
                    DocumentWithScore.builder().withScore(data.getRelevanceScore()).withDocument(request.getInstructions().get(data.getIndex())).build()).toList();
            return new RerankResponse(documentWithScores);
        }
    }

    private RerankerRequest createRequest(RerankRequest request) {
        List<String> docs = request.getInstructions().stream().map(Document::getText).toList();
        return new RerankerRequest(model, request.getQuery(), docs);
    }

    private ResponseEntity<List<RerankerResponse>> rerankEntity(RerankerRequest rerankInput) {
        Assert.notNull(rerankInput, "The request body can not be null.");
        ResponseEntity<List<RerankerResponse>> entity = this.restClient.post().uri(rerankPath)
                .body(rerankInput).retrieve().toEntity(new ParameterizedTypeReference<>() {
                    @Override
                    public Type getType() {
                        return super.getType();
                    }
                });
        return entity;
    }
}
