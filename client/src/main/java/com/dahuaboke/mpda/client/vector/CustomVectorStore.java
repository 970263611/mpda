package com.dahuaboke.mpda.client.vector;


import com.dahuaboke.mpda.client.convert.DocumentConverter;
import com.dahuaboke.mpda.client.entity.req.C014006Req;
import com.dahuaboke.mpda.client.entity.resp.C014001Resp;
import com.dahuaboke.mpda.client.entity.resp.C014006Resp;
import com.dahuaboke.mpda.client.entity.resp.C014008Resp;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptionsBuilder;
import org.springframework.ai.observation.conventions.VectorStoreProvider;
import org.springframework.ai.vectorstore.AbstractVectorStoreBuilder;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.observation.AbstractObservationVectorStore;
import org.springframework.ai.vectorstore.observation.VectorStoreObservationContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Desc: 新核心数据库调用实现
 * @Author：zhh
 * @Date：2025/9/04 17:32
 */
public class CustomVectorStore extends AbstractObservationVectorStore {

    private static final Logger logger = LoggerFactory.getLogger(CustomVectorStore.class);
    /**
     * 新核心RAG接口处理类
     */
    private final VectorStoreRequestHandle vectorStoreRequestHandle;
    /**
     * 索引名(表名)
     */
    private String collectionName = "vector_store";
    /**
     * 需要向量查询的字段
     */
    private String vectorFieldName = "embedding";
    /**
     * 转换实体对象
     */
    private DocumentConverter converter;

    public CustomVectorStore(Builder builder) {
        super(builder);
        this.collectionName = builder.collectionName;
        this.converter = builder.converter;
        this.vectorFieldName = builder.vectorFieldName;
        this.vectorStoreRequestHandle = builder.vectorStoreRequestHandle;
    }

    public static Builder builder(EmbeddingModel embeddingModel, VectorStoreRequestHandle vectorStoreRequestHandle) {
        return new Builder(embeddingModel, vectorStoreRequestHandle);
    }

    @Override
    protected void doDelete(@NotNull Filter.Expression filterExpression) {

    }

    @Override
    public void doAdd(@NotNull List<Document> documents) {
        C014001Resp txEntity;
        if (converter == null) {
            List<float[]> embeddings = this.embeddingModel.embed(documents, EmbeddingOptionsBuilder.builder().build(),
                    this.batchingStrategy);
            ArrayList<HashMap<String, Object>> entities = getInsertBatchEntity(documents, embeddings);
            txEntity = vectorStoreRequestHandle.sendC014001(collectionName, entities);
        } else {
            txEntity = vectorStoreRequestHandle.sendC014001(collectionName, converter.requestConvert(documents, embeddingModel));
        }
        List<Map<String, Object>> failedInsertList = txEntity.getFailedInsertList();
        if (!failedInsertList.isEmpty()) {
            throw new RuntimeException("批量插入接口失败条数： " + failedInsertList);
        }
    }

    private ArrayList<HashMap<String, Object>> getInsertBatchEntity(List<Document> documents, List<float[]> embeddings) {
        ArrayList<HashMap<String, Object>> entities = new ArrayList<>();
        for (int i = 0; i < documents.size(); i++) {
            Document document = documents.get(i);
            Map<String, Object> metadata = document.getMetadata();
            HashMap<String, Object> entity = new HashMap<>(metadata);
            String text = document.getText();
            entity.put("text", text);
            float[] embedding = embeddings.get(i);
            entity.put("embedding", embedding);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public void doDelete(@NotNull List<String> idList) {
        C014008Resp c014008Resp = vectorStoreRequestHandle.sendC014008(collectionName, idList);
        if (!c014008Resp.getFailedDelIdList().isEmpty()) {
            logger.error("fail Ids is{}", c014008Resp.getFailedDelIdList());
        }
    }

    @NotNull
    @Override
    public List<Document> doSimilaritySearch(SearchRequest request) {
        String query = request.getQuery();
        int topK = request.getTopK();
        double similarityThreshold = request.getSimilarityThreshold();

        float[] embedding = embeddingModel.embed(query);
        Filter.Expression filterExpression = request.getFilterExpression();
        EqAndFilterToMapConverter filterExpressionConverter = new EqAndFilterToMapConverter();
        Map<String, Object> conditionMap = filterExpressionConverter.convertToMap(filterExpression);

        C014006Req c014006Req = new C014006Req();
        c014006Req.setVector(embedding);
        c014006Req.setConditionMap(conditionMap);
        c014006Req.setIndexName(collectionName);
        c014006Req.setVectorFieldName(vectorFieldName);
        c014006Req.setTopK(topK);
        c014006Req.setNumCandidates(topK);
        c014006Req.setSize(topK);

        C014006Resp<LinkedHashMap<String, Object>> c014006Resp = vectorStoreRequestHandle.sendC014006(c014006Req);
        List<LinkedHashMap<String, Object>> resultMap = c014006Resp.getResultMap();
        List<Document> documents;
        if (converter == null) {
            documents = resultMap.stream().map(map -> {
                Double score = (Double) map.get("score");
                String id = (String) map.get("id");
                String text = (String) map.get("text");
                return Document.builder()
                        .id(id)
                        .text(text)
                        .score(score)
                        .metadata(map)
                        .build();
            }).collect(Collectors.toList());
        } else {
            documents = converter.resultConvert(resultMap);
        }
        return documents.stream().filter(document -> {
            Double score = document.getScore();
            return score >= similarityThreshold;
        }).collect(Collectors.toList());

    }

    @NotNull
    @Override
    public VectorStoreObservationContext.Builder createObservationContextBuilder(@NotNull String operationName) {
        return VectorStoreObservationContext.builder(VectorStoreProvider.ELASTICSEARCH.value(), operationName).collectionName(this.collectionName);
    }

    public static class Builder extends AbstractVectorStoreBuilder<CustomVectorStore.Builder> {

        private String collectionName = "vector_store";

        private String vectorFieldName = "embedding";

        private DocumentConverter converter;

        private VectorStoreRequestHandle vectorStoreRequestHandle;


        private Builder(EmbeddingModel embeddingModel, VectorStoreRequestHandle vectorStoreRequestHandle) {
            super(embeddingModel);
            this.vectorStoreRequestHandle = vectorStoreRequestHandle;

        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public Builder converter(DocumentConverter converter) {
            this.converter = converter;
            return this;
        }

        public Builder vectorFieldName(String vectorFieldName) {
            this.vectorFieldName = vectorFieldName;
            return this;
        }


        @NotNull
        @Override
        public CustomVectorStore build() {
            return new CustomVectorStore(this);
        }

    }

}
