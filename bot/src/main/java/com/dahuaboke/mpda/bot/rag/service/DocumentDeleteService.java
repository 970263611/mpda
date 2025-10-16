package com.dahuaboke.mpda.bot.rag.service;

import com.alibaba.fastjson.JSONObject;
import com.dahuaboke.mpda.bot.rag.client.FundEntity;
import com.dahuaboke.mpda.bot.constants.FundConstant;
import com.dahuaboke.mpda.bot.rag.ProcessingMonitor;
import com.dahuaboke.mpda.client.entity.resp.C014005Resp;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Desc: 文档删除服务
 * @Author：zhh
 * @Date：2025/9/9 11:12
 */
@Component
public class DocumentDeleteService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private VectorStoreRequestHandle requestHandle;


    @Autowired
    private ProcessingMonitor processingMonitor;

    public boolean doDel(Map<String, Object> conditionMap) {
        C014005Resp c014005Resp = requestHandle.sendC014005(FundConstant.INDEX_NAME, conditionMap, Map.of());
        List resultMap = c014005Resp.getResultList();
        ArrayList<String> idList = new ArrayList<>();
        for (Object obj : resultMap) {
            String jsonStr = JSONObject.toJSONString(obj);
            FundEntity fundEntity = JSONObject.parseObject(jsonStr, FundEntity.class);
            idList.add(fundEntity.getId());
        }
        if(!idList.isEmpty()){
            vectorStore.delete(idList);
        }
        return true;
    }

    public void doDelAll(Map<String, Map<String, Object>> conditionMaps) {
        ArrayList<Map.Entry<String, Map<String, Object>>> entries = new ArrayList<>(conditionMaps.entrySet());

        ProcessingMonitor.ProcessingResult<Map.Entry<String, Map<String, Object>>> result = processingMonitor.processBatch(
                entries,
                entry -> doDel(entry.getValue()),
                entry -> System.out.println(),
                Map.Entry::getKey,
                "基金通过基金名称删除"
        );

        // 将失败记录写入文件
        processingMonitor.writeFailuresToFile(result.getFailures(), "pdf_del_processing");

    }

}