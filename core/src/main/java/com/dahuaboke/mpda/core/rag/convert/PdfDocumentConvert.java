package com.dahuaboke.mpda.core.rag.convert;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Desc: pdf文档转换器
 * @Author：zhh
 * @Date：2025/9/1 21:51
 */
@Component
public class PdfDocumentConvert implements DocumentConvert {

    //Windows适配参数
    private static final int PAGE_MARGIN = 70;
    private static final int BOTTOM_MARGIN = 60;
    private static final int TOP_TEXT_LINES_TO_DELETE = 3;
    private static final int BOTTOM_TEXT_LINES_TO_DELETE = 3;

    //Linux适配参数
    private static final int LINUX_PAGE_MARGIN = 40;
    private static final int LINUX_BOTTOM_MARGIN = 50;
    private static final int LINUX_TOP_TEXT_LINES_TO_DELETE = 1;
    private static final int LINUX_BOTTOM_TEXT_LINES_TO_DELETE = 2;


    protected final Logger log = LoggerFactory.getLogger(PdfDocumentConvert.class);

    @Override
    public List<Document> readToDocuments(Resource resource) throws IOException {
        String filename = resource.getFilename();
        log.debug("开始读取PDF文件: {}", filename);

        int pageMargin;
        int bottomMargin;
        int topTextLinesToDelete;
        int bottomTextLinesToDelete;
        log.debug("os name is{}", System.getProperty("os.name").toLowerCase());
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            pageMargin = PAGE_MARGIN;
            bottomMargin = BOTTOM_MARGIN;
            topTextLinesToDelete = TOP_TEXT_LINES_TO_DELETE;
            bottomTextLinesToDelete = BOTTOM_TEXT_LINES_TO_DELETE;
        } else {
            pageMargin = LINUX_PAGE_MARGIN;
            bottomMargin = LINUX_BOTTOM_MARGIN;
            topTextLinesToDelete = LINUX_TOP_TEXT_LINES_TO_DELETE;
            bottomTextLinesToDelete = LINUX_BOTTOM_TEXT_LINES_TO_DELETE;
        }

        // 配置PDF读取规则（边距、文本清理、按页拆分）
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(pageMargin)
                .withPageBottomMargin(bottomMargin)
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(topTextLinesToDelete)
                        .withNumberOfBottomTextLinesToDelete(bottomTextLinesToDelete)
                        .build())
                .withPagesPerDocument(1)
                .build();


        // 自动关闭输入流（try-with-resources）
        try (InputStream is = resource.getInputStream()) {
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource, config);
            List<Document> documents = pdfReader.read();
            log.debug("PDF文件{}读取完成，生成{}个Document", filename, documents.size());
            return documents;
        }
    }


}
