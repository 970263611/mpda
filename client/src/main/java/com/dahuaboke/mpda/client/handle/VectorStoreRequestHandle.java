package com.dahuaboke.mpda.client.handle;

import com.dahuaboke.mpda.client.ClientProperties;
import com.dahuaboke.mpda.client.CustomClient;
import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.TxBodyReq;
import com.dahuaboke.mpda.client.entity.TxHeaderReq;
import com.dahuaboke.mpda.client.entity.req.C014001Req;
import com.dahuaboke.mpda.client.entity.req.C014005Req;
import com.dahuaboke.mpda.client.entity.req.C014006Req;
import com.dahuaboke.mpda.client.entity.req.C014008Req;
import com.dahuaboke.mpda.client.entity.resp.C014001Resp;
import com.dahuaboke.mpda.client.entity.resp.C014005Resp;
import com.dahuaboke.mpda.client.entity.resp.C014006Resp;
import com.dahuaboke.mpda.client.entity.resp.C014008Resp;
import com.dahuaboke.mpda.client.utils.CommonHeaderUtils;

import java.util.List;
import java.util.Map;

/**
 * @Desc: 数据库接口发送
 * @Author：zhh
 * @Date：2025/9/4 17:32
 */
public class VectorStoreRequestHandle {

    private final CustomClient customCommonClient;

    private final ClientProperties coreClientProperties;


    public VectorStoreRequestHandle(CustomClient customCommonClient, ClientProperties coreClientProperties) {
        this.customCommonClient = customCommonClient;
        this.coreClientProperties = coreClientProperties;
    }

    /**
     * 通用数据批量写入接口发送
     *
     * @param indexName       索引名(表名)
     * @param c014001Entities 数据集合
     * @return 返回对象
     */
    public <T> C014001Resp sendC014001(String indexName, List<T> c014001Entities) {
        CommonReq<C014001Req<T>> bodyReq = new CommonReq<>();
        String sendSysNo = coreClientProperties.getSendSysNo();
        TxHeaderReq headerReq = CommonHeaderUtils.build(coreClientProperties, RagConstant.RAG_V1_C014001);
        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014001Req<T>> insertReq = new TxBodyReq<>();
        C014001Req<T> c014001Req = new C014001Req<>();
        c014001Req.setDataMapList(c014001Entities);
        c014001Req.setSystemNo(sendSysNo);
        c014001Req.setIndexName(indexName);
        insertReq.setTxEntity(c014001Req);
        bodyReq.setTxBody(insertReq);

        return customCommonClient.execute(coreClientProperties.getUrl() + RagConstant.C014001, bodyReq, C014001Resp.class);
    }

    /**
     * 通用数据普通查询接口发送
     *
     * @param indexName      索引名称
     * @param conditionMap   筛选条件 非必输
     * @param orderCondition 排序字段 非必输
     * @return
     */
    public C014005Resp sendC014005(String indexName, Map<String, Object> conditionMap, Map<String, String> orderCondition) {
        CommonReq<C014005Req> bodyReq = new CommonReq<>();
        TxHeaderReq headerReq = CommonHeaderUtils.build(coreClientProperties, RagConstant.RAG_V1_C014005);
        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014005Req> txBodyReq = new TxBodyReq<>();
        C014005Req c014005Req = new C014005Req();
        c014005Req.setSystemNo(coreClientProperties.getSendSysNo());
        c014005Req.setIndexName(indexName);
        if (!conditionMap.isEmpty()) {
            c014005Req.setConditionMap(conditionMap);
        }
        if (!orderCondition.isEmpty()) {
            c014005Req.setOrderCondition(orderCondition);
        }
        txBodyReq.setTxEntity(c014005Req);
        bodyReq.setTxBody(txBodyReq);

        return customCommonClient.execute(coreClientProperties.getUrl() + RagConstant.C014005, bodyReq, C014005Resp.class);

    }

    /**
     * 通用向量检索接口发送
     *
     * @param c014006Req 请求封装对象
     * @return C014006Resp
     */
    public C014006Resp sendC014006(C014006Req c014006Req) {
        c014006Req.setSystemNo(coreClientProperties.getSendSysNo());
        CommonReq<C014006Req> bodyReq = new CommonReq<>();
        TxHeaderReq headerReq = CommonHeaderUtils.build(coreClientProperties, RagConstant.RAG_V1_C014006);
        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014006Req> txBodyReq = new TxBodyReq<>();
        txBodyReq.setTxEntity(c014006Req);
        bodyReq.setTxBody(txBodyReq);

        return customCommonClient.execute(coreClientProperties.getUrl() + RagConstant.C014006, bodyReq, C014006Resp.class);

    }


    /**
     * 根据文档id进行批量删除接口发送
     *
     * @param indexName 索引名称
     * @param idList    待删除Ids
     * @return C014008Resp
     */
    public C014008Resp sendC014008(String indexName, List<String> idList) {
        CommonReq<C014008Req> bodyReq = new CommonReq<>();
        TxHeaderReq headerReq = CommonHeaderUtils.build(coreClientProperties, RagConstant.RAG_V1_C014008);
        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014008Req> txBodyReq = new TxBodyReq<>();

        C014008Req c014008Req = new C014008Req();
        c014008Req.setSystem(coreClientProperties.getSendSysNo());
        c014008Req.setIndexName(indexName);
        c014008Req.setIdList(idList);
        txBodyReq.setTxEntity(c014008Req);
        bodyReq.setTxBody(txBodyReq);

        return customCommonClient.execute(coreClientProperties.getUrl() + RagConstant.C014008, bodyReq, C014008Resp.class);

    }


}
