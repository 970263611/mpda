package com.dahuaboke.mpda.bot.scheduler;

import com.dahuaboke.mpda.bot.constants.FundConstant;
import com.dahuaboke.mpda.bot.tools.service.RobotService;
import com.dahuaboke.mpda.client.entity.resp.C014004Resp;
import com.dahuaboke.mpda.client.entity.resp.C014005Resp;
import com.dahuaboke.mpda.client.entity.resp.C014008Resp;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddProductNameTask {

    private static final Logger log = LoggerFactory.getLogger(AddProductNameTask.class);

    @Autowired
    RobotService robotService;

    @Autowired
    VectorStoreRequestHandle vectorStoreRequestHandle;

    @Autowired
    EmbeddingModel embeddingModel;

    private static final int INSERT_BATCH = 100;

    public void addProductNameJob() {
        log.info("开始执行添加基金编码任务...........");
        //1. 获取当前全量基金代码和基金名称映射关系
        Map<String, String> map = robotService.getMap();
        List<LinkedHashMap<String, Object>> resultMap;
        //2. 分页查询
        try {
            resultMap = selectPage(1000);
        } catch (Exception e) {
            //查询失败,走删除新增
            log.warn("分页查询接口失败,走删除新增......",e);
            batchDelInsert(map);
            return;
        }
        //3. 全量查询成功,去重后分批插入
        HashMap<Object, Object> oldMap = new HashMap<>();
        if(CollectionUtils.isEmpty(resultMap)){
            return;
        }
        for (LinkedHashMap<String, Object> entry : resultMap) {
            String fundCode = (String) entry.get("fund_code");
            String fundName = (String) entry.get("fund_name");
            if (StringUtils.isNotBlank(fundCode) && StringUtils.isNotBlank(fundName)) {
                oldMap.put(fundCode, fundName);
            }
        }
        map.keySet().removeAll(oldMap.keySet());
        List<String> keys = map.keySet().stream().toList();
        log.info("本次新增{}个新产品...........",keys.size());
        for (int i = 0; i < keys.size(); i += INSERT_BATCH) {
            int min = Math.min(i + INSERT_BATCH, keys.size());
            List<String> batchKeys = keys.subList(i, min);
            doDel(batchKeys);
            doInsert(batchKeys, map);
            log.info("删除插入执行结束,等待5s,开始下个批次...........");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("执行添加基金编码任务结束...........");
    }
    
    private List<LinkedHashMap<String, Object>> selectPage(int pageSize){
        List<LinkedHashMap<String, Object>> resultMap = new ArrayList<>();
        int currentPage = 1;
        boolean hasMore = true;
        long total = 0;
        int totalPage;

        log.info("开始手动分页分析空字段，每页大小: {}", pageSize);

        while (hasMore) {
            // 执行查询
            C014004Resp c014004Resp = vectorStoreRequestHandle.sendC014004(FundConstant.CODE_INDEX_NAME, Map.of(), Map.of(), currentPage, pageSize);
            total = c014004Resp.getTotal();
            totalPage = c014004Resp.getTotalPage();
            if (total == 0) {
                break;
            }
            List<LinkedHashMap<String, Object>> content = c014004Resp.getContent();
            if(CollectionUtils.isEmpty(content)){
                break;
            }
            int size = content.size();
            log.info("处理第 {}页，共 {} 条记录", currentPage, size);

            resultMap.addAll(content);

            // 结束分页条件
            if (currentPage >= totalPage) {
                hasMore = false;
            }
            if (size < pageSize) {
                hasMore = false;
            }

            currentPage++;

            // 安全限制，防止无限循环
            if (currentPage > 10000) {
                log.warn("已达到最大偏移量限制，强制退出循环");
                break;
            }
        }
        log.info("手动分页分析完成，查询出来 {} 条记录", resultMap.size());
        log.info("向量库共发现 {} 条记录, 查询出来 {} 条记录",total,resultMap.size());
        return resultMap;

    }

    private void batchDelInsert(Map<String, String> map) {
        List<String> keys = map.keySet().stream().toList();
        for (int i = 0; i < keys.size(); i += INSERT_BATCH) {
            int min = Math.min(i + INSERT_BATCH, keys.size());
            List<String> batchKeys = keys.subList(i, min);
            doDel(batchKeys);
            doInsert(batchKeys, map);
            log.info("删除插入执行结束,等待5s,开始下个批次...........");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void doDel(List<String> batchKeys) {
        batchKeys.forEach(key -> {
            C014005Resp c014005Resp = vectorStoreRequestHandle.sendC014005(FundConstant.CODE_INDEX_NAME, Map.of("fund_code", key), Map.of());
            ArrayList<String> idList = new ArrayList<>();
            List<LinkedHashMap<String, Object>> resultMap = (List<LinkedHashMap<String, Object>>) c014005Resp.getResultList();
            for (LinkedHashMap<String, Object> entry : resultMap) {
                String id = (String) entry.get("id");
                if (StringUtils.isNotBlank(id)) {
                    idList.add(id);
                }
            }
            if (!idList.isEmpty()) {
                C014008Resp c014008Resp = vectorStoreRequestHandle.sendC014008(FundConstant.CODE_INDEX_NAME, idList);
                if (!c014008Resp.getFailedDelIdList().isEmpty()) {
                    log.error("fail Ids is{}", c014008Resp.getFailedDelIdList());
                }
            }
        });
    }


    public void doInsert(List<String> batchKeys, Map<String, String> map) {
        ArrayList<Map<String, Object>> entities = new ArrayList<>();
        batchKeys.forEach(key -> {
            String name = map.get(key);
            float[] embedding = embeddingModel.embed(name);
            entities.add(Map.of(
                    "fund_code", key,
                    "fund_name", name,
                    "embedding", embedding,
                    "content", name));
        });
        vectorStoreRequestHandle.sendC014001(FundConstant.CODE_INDEX_NAME, entities);
    }

}
