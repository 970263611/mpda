package com.dahuaboke.mpda.bot.rag.utils;


import com.dahuaboke.mpda.bot.tools.enums.BondFundType;
import com.dahuaboke.mpda.core.rag.convert.PdfDocumentConvert;
import com.dahuaboke.mpda.core.rag.reader.DefaultDocumentReader;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FundDocUtilTest {


    private static final String pdfFilePath = "D:/pdfFile/*.pdf";

    @Test
    public void testGetProductMap() throws IOException {
        Map<String, String> productMap = new HashMap<>();
        DefaultDocumentReader documentReader = new DefaultDocumentReader();
        Resource[] resources = documentReader.read(pdfFilePath);
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            productMap.putAll(FundDocUtil.getProductMap(filename));

        }
        List<Map<String, String>> batches = FundDocUtil.splitIntoBatches(productMap, 5);
        batches.forEach(System.out::println);
    }

    @Test
    public void testPdfDocumentConvert() throws IOException {
        DefaultDocumentReader documentReader = new DefaultDocumentReader();
        Resource[] resources = documentReader.read(pdfFilePath);

        PdfDocumentConvert pdfDocumentConvert = new PdfDocumentConvert();
        for (Resource resource : resources) {
            List<Document> documents = pdfDocumentConvert.readToDocuments(resource);
            System.out.println(documents);
        }
    }

    @Test
    public void testBond(){
        String str ="利率债/利率债-被动式/利率债指数1-5年 ";
        BondFundType bondFundType = BondFundType.getBondFundType(str);
    }

}
