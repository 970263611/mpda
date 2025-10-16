package com.dahuaboke.mpda.client.entity;


/**
 * @Desc: 新核心接口通用请求
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class CommonReq<T> {

    private TxHeaderReq txHeader;

    private TxBodyReq<T> txBody;

    public CommonReq(TxHeaderReq txHeader, TxBodyReq<T> txBody) {
        this.txHeader = txHeader;
        this.txBody = txBody;
    }

    public CommonReq() {
    }

    public TxHeaderReq getTxHeader() {
        return txHeader;
    }

    public void setTxHeader(TxHeaderReq txHeader) {
        this.txHeader = txHeader;
    }

    public TxBodyReq<T> getTxBody() {
        return txBody;
    }

    public void setTxBody(TxBodyReq<T> txBody) {
        this.txBody = txBody;
    }

    @Override
    public String toString() {
        return "CommonReq{" +
                "txHeader=" + txHeader +
                ", txBody=" + txBody +
                '}';
    }

}

