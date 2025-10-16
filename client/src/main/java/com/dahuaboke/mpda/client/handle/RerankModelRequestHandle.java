package com.dahuaboke.mpda.client.handle;

import com.dahuaboke.mpda.client.ClientProperties;
import com.dahuaboke.mpda.client.CustomClient;
import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.TxBodyReq;
import com.dahuaboke.mpda.client.entity.TxHeaderReq;
import com.dahuaboke.mpda.client.entity.req.C014011Req;
import com.dahuaboke.mpda.client.entity.resp.C014011Resp;
import com.dahuaboke.mpda.client.utils.CommonHeaderUtils;

import java.util.List;

/**
 * @Desc: 重排序模型接口发送
 * @Author：zhh
 * @Date：2025/9/9 9:39
 */
public class RerankModelRequestHandle {

    private final CustomClient customCommonClient;

    private final ClientProperties coreClientProperties;

    public RerankModelRequestHandle(CustomClient customCommonClient, ClientProperties coreClientProperties) {
        this.customCommonClient = customCommonClient;
        this.coreClientProperties = coreClientProperties;
    }

    /**
     * 文本重排序接口发送
     *
     * @param query 查询问题
     * @param docs  文档
     * @return C014011Resp
     */
    public C014011Resp sendC014011(String query, List<String> docs) {
        CommonReq<C014011Req> bodyReq = new CommonReq<>();
        TxHeaderReq headerReq = CommonHeaderUtils.build(coreClientProperties, RagConstant.RAG_V1_C014011);
        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014011Req> txBodyReq = new TxBodyReq<>();
        C014011Req c014011Req = new C014011Req();
        c014011Req.setQuery(query);
        c014011Req.setTexts(docs);
        c014011Req.setSceneId(coreClientProperties.getResvedInputInfo());
        txBodyReq.setTxEntity(c014011Req);
        bodyReq.setTxBody(txBodyReq);

        String url = coreClientProperties.getUrl() + RagConstant.C014011;

        return customCommonClient.execute(url, bodyReq, C014011Resp.class);
    }


}
