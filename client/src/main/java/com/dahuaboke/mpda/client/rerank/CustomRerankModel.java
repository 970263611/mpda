package com.dahuaboke.mpda.client.rerank;

import com.dahuaboke.mpda.client.entity.resp.C014011Resp;
import com.dahuaboke.mpda.client.handle.RerankModelRequestHandle;
import com.dahuaboke.mpda.core.rag.rerank.Rerank;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Desc: 新核心重排序模型调用实现
 * @Author：zhh
 * @Date：2025/9/15 14:47
 */
public class CustomRerankModel implements Rerank {

    RerankModelRequestHandle rerankModelRequestHandle;

    public CustomRerankModel(RerankModelRequestHandle rerankModelRequestHandle) {
        this.rerankModelRequestHandle = rerankModelRequestHandle;
    }

    @Override
    public List<Document> handler(SearchRequest searchRequest, List<Document> documents, int topK) {
        if (documents.isEmpty()) {
            return List.of();
        }

        List<String> docs = documents.stream().map(Document::getText).toList();

        C014011Resp c014011Resp = rerankModelRequestHandle.sendC014011(searchRequest.getQuery(), docs);
        List<C014011Resp.ProcessData> processData = c014011Resp.getProcessData();

        //取topK个
        return processData.stream()
                .sorted(Comparator.comparingDouble(C014011Resp.ProcessData::getScore).reversed())
                .limit(topK)
                .map(data -> documents.get(data.getIndex()))
                .toList();
    }
}
