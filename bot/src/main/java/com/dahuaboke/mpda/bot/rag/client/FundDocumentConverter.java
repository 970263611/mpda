package com.dahuaboke.mpda.bot.rag.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dahuaboke.mpda.client.convert.DocumentConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Desc: document转换成基金对象, 以及基金对象转换成document
 * @Author：zhh
 * @Date：2025/9/04 17:32
 */
public class FundDocumentConverter implements DocumentConverter<FundEntity> {


    @Override
    public List<FundEntity> requestConvert(List<Document> documents, EmbeddingModel embeddingModel) {
        List<FundEntity> fundEntitys = new ArrayList<>();

        for (Document document : documents) {
            FundEntity fundEntity = new FundEntity();
            float[] embed = embeddingModel.embed(document.getText());
            fundEntity.setEmbedding(embed);
            fundEntity.setContent(document.getText());
            Map<String, Object> metadata = document.getMetadata();

            fundEntity.setFile_name((String) metadata.get("file_name"));
            fundEntity.setPage_number((int) metadata.get("page_number"));
            fundEntity.setExcerpt_keywords((List<String>) metadata.get("excerpt_keywords"));
            fundEntity.setFile_name_keywords((List<String>) metadata.get("file_name_keywords"));
            fundEntitys.add(fundEntity);
        }
        return fundEntitys;
    }


    @Override
    public List<Document> resultConvert(List<LinkedHashMap<String, Object>> result) {
        return result.stream().map(jsonObject -> {
            String jsonString = JSONObject.toJSONString(jsonObject);
            FundEntity fundEntity = JSON.parseObject(jsonString, FundEntity.class);

            return Document.builder()
                    .id(fundEntity.getId())
                    .text(fundEntity.getContent())
                    .metadata(Map.of("file_name", fundEntity.getFile_name(), "page_number", (double) fundEntity.getPage_number(), "excerpt_keywords", fundEntity.getExcerpt_keywords()))
                    .score(fundEntity.getScore().doubleValue())
                    .build();
        }).collect(Collectors.toList());
    }

}
