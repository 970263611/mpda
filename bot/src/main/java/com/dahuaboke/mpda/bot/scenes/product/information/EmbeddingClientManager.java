package com.dahuaboke.mpda.bot.scenes.product.information;


import com.dahuaboke.mpda.bot.constants.FundConstant;
import com.dahuaboke.mpda.client.entity.req.C014006Req;
import com.dahuaboke.mpda.client.entity.resp.C014006Resp;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import com.dahuaboke.mpda.core.client.entity.EmbeddingResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/1 11:16
 */
@Component
public class EmbeddingClientManager {

    @Autowired
    EmbeddingModel embeddingModel;

    @Autowired
    VectorStoreRequestHandle vectorStoreRequestHandle;

    public EmbeddingResponse embed(String text) {
        if (text.isEmpty()) {
            return new EmbeddingResponse(Map.of());
        }
        float[] embedding = embeddingModel.embed(text);
        C014006Req c014006Req = new C014006Req();
        c014006Req.setVector(embedding);
        c014006Req.setIndexName(FundConstant.CODE_INDEX_NAME);
        c014006Req.setVectorFieldName(FundConstant.EMBEDDING);
        c014006Req.setTopK(10);
        c014006Req.setNumCandidates(10);
        c014006Req.setSize(10);
        C014006Resp c014006Resp = vectorStoreRequestHandle.sendC014006(c014006Req);
        HashMap<String, String> finalMap = new HashMap<>();
        List<LinkedHashMap<String, Object>> resultMap = c014006Resp.getResultMap();
        resultMap.forEach(map -> {
            String code = (String) map.get("fund_code");
            String name = (String) map.get("fund_name");
            finalMap.put(code,name);
        });
        return new EmbeddingResponse(finalMap);
    }
}
