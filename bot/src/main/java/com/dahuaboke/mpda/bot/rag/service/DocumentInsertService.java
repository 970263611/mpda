package com.dahuaboke.mpda.bot.rag.service;

import com.dahuaboke.mpda.bot.rag.RagPrompt;
import com.dahuaboke.mpda.bot.rag.ProcessingMonitor;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.rag.convert.PdfDocumentConvert;
import com.dahuaboke.mpda.core.rag.enricher.KeywordEnricher;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * @Desc: 文档处理服务
 * @Author：zhh
 * @Date：2025/9/2 17:27
 */
@Service
public class DocumentInsertService {

    private static final Logger log = LoggerFactory.getLogger(DocumentInsertService.class);

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ProcessingMonitor processingMonitor;

    /**
     * 处理单个 PDF 资源
     */
    public List<Document> processPdfResource(Resource resource) {
        String filename = resource.getFilename();
        PdfDocumentConvert pdfDocumentConvert = new PdfDocumentConvert();
        List<Document> docs = null;
        try {
            docs = pdfDocumentConvert.readToDocuments(resource);
        } catch (IOException e) {
            log.error("文档解析失败{}", filename);
        }
        KeywordEnricher keywordEnricher = new KeywordEnricher(
                chatModel,
                RagPrompt.DEFAULT_PROMPT_TEMPLATE
                        .render(Map.of("keyWords", RagPrompt.FUND_KEYS)),
                "『RESULT』",
                "『END』"
        );

        if(CollectionUtils.isEmpty(docs)){
            throw new MpdaRuntimeException("pdf parse fail");
        }
        return keywordEnricher.apply(docs);

    }

    public void insertVectorStore(List<Document> documents){
        vectorStore.add(documents);
    }



}