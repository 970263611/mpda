package com.dahuaboke.mpda.bot.rag.controller;

import com.dahuaboke.mpda.bot.rag.ProcessingMonitor;
import com.dahuaboke.mpda.bot.rag.service.DocumentInsertService;
import com.dahuaboke.mpda.core.rag.reader.DocumentReader;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insert")
public class DocumentInsertController {

    private static final Logger log = LoggerFactory.getLogger(DocumentInsertController.class);

    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000),
            new ThreadFactoryBuilder().setNameFormat("pdf-insert-%d").build());
    @Autowired
    private DocumentInsertService documentInsertService;

    @Autowired
    private DocumentReader documentReader;

    @Autowired
    private ProcessingMonitor processingMonitor;


    @GetMapping("/batch")
    public void batch() throws Exception {
       /* Resource[] resources = documentReader.read("D:/pdfFile/*.pdf");

        List<List<Resource>> batches = FundDocUtil.splitIntoBatches(Arrays.stream(resources).toList(), 5);

        for (List<Resource> batch : batches) {
            executorService.submit(() -> {
                try {
                    documentInsertService.processPdfResources(batch);
                } catch (Exception e) {
                    log.error("子线程{}处理文档插入批次异常,批次为{}", Thread.currentThread().getName(), batch, e);
                }
            });
        }
        executorService.shutdown();*/
    }

}
