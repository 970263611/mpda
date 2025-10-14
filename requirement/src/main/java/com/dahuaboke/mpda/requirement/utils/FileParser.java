package com.dahuaboke.mpda.requirement.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 2025/10/14 10:10
 * auth: dahua
 * desc:
 */
public class FileParser {

    private static final Logger logger = LoggerFactory.getLogger(FileParser.class);

    public static String parseText(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名无效");
        }

        try (InputStream is = file.getInputStream()) {
            if (fileName.endsWith(".doc")) {
                return parseDocText(is);
            } else if (fileName.endsWith(".docx")) {
                return parseDocxText(is);
            } else {
                throw new IllegalArgumentException("不支持的文件格式：" + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Word解析失败：" + e.getMessage(), e);
        }
    }

    private static String parseDocText(InputStream is) throws Exception {
        try (HWPFDocument doc = new HWPFDocument(is);
             WordExtractor extractor = new WordExtractor(doc)) {
            String text = extractor.getText();
            return cleanText(text);
        }
    }

    private static String parseDocxText(InputStream is) throws Exception {
        try (XWPFDocument docx = new XWPFDocument(is);
             XWPFWordExtractor extractor = new XWPFWordExtractor(docx)) {
            String text = extractor.getText();
            return cleanText(text);
        }
    }

    private static String cleanText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        return text.replaceAll("\\n+", "\n").trim();
    }
}
