package com.dahuaboke.mpda.bot.rag.controller;

import com.dahuaboke.mpda.bot.rag.service.DocumentQueryService;
import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.core.rag.reader.DocumentReader;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/query")
public class DocumentQueryController {

    private static final Logger log = LoggerFactory.getLogger(DocumentQueryController.class);

    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000),
            new ThreadFactoryBuilder().setNameFormat("pdf-query-%d").build());

    @Autowired
    private DocumentQueryService documentQueryService;

    @Autowired
    private DocumentReader documentReader;


    @GetMapping("/batch")
    public void batch() throws Exception {
        Map<String, String> productMap = new HashMap<>();

        Resource[] resources = documentReader.read("D:/pdfFile/*.pdf");
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            productMap.putAll(FundDocUtil.getProductMap(filename));

        }
        List<Map<String, String>> batches = FundDocUtil.splitIntoBatches(productMap, 5);
        for (Map<String, String> batch : batches) {
            executorService.submit(() -> {
                try {
                    documentQueryService.processProducts(batch);
                } catch (Exception e) {
                    log.error("子线程{}处理文档提取批次异常,批次为{}", Thread.currentThread().getName(), batch, e);
                }
            });
        }
        executorService.shutdown();
    }

}
