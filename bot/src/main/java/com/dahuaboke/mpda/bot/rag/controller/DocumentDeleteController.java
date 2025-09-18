package com.dahuaboke.mpda.bot.rag.controller;

import com.dahuaboke.mpda.bot.rag.service.DocumentDeleteService;
import com.dahuaboke.mpda.core.rag.reader.DocumentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/delete")
public class DocumentDeleteController {

    private static final Logger log = LoggerFactory.getLogger(DocumentDeleteController.class);


    @Autowired
    private DocumentDeleteService documentDeleteService;

    @Autowired
    private DocumentReader documentReader;


    @GetMapping("/batch")
    public void batch() throws Exception {

        Resource[] resources = documentReader.read("D:/pdfFile/*.pdf");


        Map<String, Map<String, Object>> conditionMaps = new HashMap<>();

        for (Resource resource : resources) {
            String filename = resource.getFilename();
            conditionMaps.put(filename, Map.of("file_name", filename));
        }
        documentDeleteService.doDelAll(conditionMaps);

    }

}
