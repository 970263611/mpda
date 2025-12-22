package com.dahuaboke.mpda.bot.rag.utils;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;

public class MonitorUtil {
    private static final Logger log = LoggerFactory.getLogger(MonitorUtil.class);

    public static void stepStart(boolean enableMonitor,String stepName, Object... params){
        try {
            if(enableMonitor){
                log.info("【{}】{}",stepName,formatParams(params));
            }
        } catch (Exception e) {
            log.error("monitor Rag Fail is step {}",stepName);
        }
    }

    private static String formatParams(Object... params) {
        if(params == null || params.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i+=2) {
            if(i + 1 >= params.length) break;
            if(params[i] .equals("documents")){
                List<Document> param = (List<Document>)params[i + 1];
                List<Map<String, Object>> maps = param.stream().map(Document::getMetadata).toList();
                sb.append(params[i]).append("=").append(maps).append("; ");
            }else {
                sb.append(params[i]).append("=").append(params[i + 1]).append("; ");
            }
        }
        return sb.toString().trim();
    }


}
