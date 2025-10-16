package com.dahuaboke.mpda.client.handle;

import com.dahuaboke.mpda.client.ClientProperties;
import com.dahuaboke.mpda.client.CustomClient;
import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.TxBodyReq;
import com.dahuaboke.mpda.client.entity.TxHeaderReq;
import com.dahuaboke.mpda.client.entity.req.C014007Req;
import com.dahuaboke.mpda.client.entity.resp.C014007Resp;
import com.dahuaboke.mpda.client.utils.CommonHeaderUtils;

/**
 * @Desc: 向量模型接口发送
 * @Author：zhh
 * @Date：2025/9/5 9:39
 */
public class EmbeddingModelRequestHandle {

    private final CustomClient customCommonClient;

    private final ClientProperties coreClientProperties;

    public EmbeddingModelRequestHandle(CustomClient customCommonClient, ClientProperties coreClientProperties) {
        this.customCommonClient = customCommonClient;
        this.coreClientProperties = coreClientProperties;
    }

    /**
     * 文本转向量接口发送
     *
     * @param text 文本内容
     * @return 向量
     */
    public float[] sendC014007(String text) {
        CommonReq<C014007Req> bodyReq = new CommonReq<>();
        TxHeaderReq headerReq = CommonHeaderUtils.build(coreClientProperties,RagConstant.RAG_V1_C014007);

        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014007Req> txBodyReq = new TxBodyReq<>();
        C014007Req c014007Req = new C014007Req();
        c014007Req.setText(text);
        txBodyReq.setTxEntity(c014007Req);
        bodyReq.setTxBody(txBodyReq);

        String url = coreClientProperties.getUrl() + RagConstant.C014007;
        C014007Resp resp = customCommonClient.execute(url, bodyReq, C014007Resp.class);

        return resp.getVector();
    }


}
