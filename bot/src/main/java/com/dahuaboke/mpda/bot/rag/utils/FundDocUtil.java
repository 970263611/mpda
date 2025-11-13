package com.dahuaboke.mpda.bot.rag.utils;

import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Desc: 基金pdf文件工具类
 * @Author：zhh
 * @Date：2025/9/1 22:41
 */
public class FundDocUtil {

    /**
     * 通过文件名称,提取基金代码和基金名称
     *
     * @return
     */
    public static Map<String, String> getProductMap(String filename) {
        Map<String, String> mapRelation = new HashMap<>();
        // 1. 去掉 ".pdf" 后缀
        String filenameWithoutExt = filename.replace(".pdf", "");

        // 2. 提取基金代码
        String fundCode = filenameWithoutExt.replaceAll("\\D.*", "");

        // 3. 剩余字符串
        String remaining = filenameWithoutExt.substring(fundCode.length() + 1);

        // 4. 清理后缀
        int lastSeparator = Math.max(remaining.lastIndexOf("_"), remaining.lastIndexOf("-"));
        String fundName = remaining.substring(0, lastSeparator);

        mapRelation.put(fundCode, fundName);
        return mapRelation;
    }


    public static Map<String, String> initAllQuery(Class cls) {
        FundFieldMapper fundFieldMapper = new FundFieldMapper(cls);
        return fundFieldMapper.getQuestionKeyWordMap();
    }

    public static List<Map<String, String>> splitIntoBatches(Map<String, String> map, int bachSize) {
        ArrayList<Map<String, String>> batches = new ArrayList<>();
        HashMap<String, String> currentBatch = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (i >= bachSize) {
                batches.add(currentBatch);
                currentBatch = new HashMap<>();
                i = 0;
            }
            currentBatch.put(entry.getKey(), entry.getValue());
            i++;
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }
        return batches;
    }

    public static <T> List<List<T>> splitIntoBatches(List<T> resources, int bachSize) {
        List<List<T>> batches = new ArrayList<>();
        List<T> currentBatch = new ArrayList<>();
        int i = 0;
        for (T resource : resources) {
            if (i >= bachSize) {
                batches.add(currentBatch);
                currentBatch = new ArrayList<>();
                i = 0;
            }
            currentBatch.add(resource);
            i++;
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }
        return batches;
    }


    public void writeTxt(Object data, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of(fileName, data));
            Files.write(Paths.get("D:/jsonDir06/" + fileName + ".json"), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
