package com.dahuaboke.mpda.client.entity;


/**
 * @Desc: 新核心接口通用返回
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class CommonResp<R> {

    private TxHeaderResp txHeader;

    private TxBodyResp<R> txBody;

    public CommonResp() {
    }

    public CommonResp(TxHeaderResp txHeader, TxBodyResp<R> txBody) {
        this.txHeader = txHeader;
        this.txBody = txBody;
    }

    public TxHeaderResp getTxHeader() {
        return txHeader;
    }

    public void setTxHeader(TxHeaderResp txHeader) {
        this.txHeader = txHeader;
    }

    public TxBodyResp<R> getTxBody() {
        return txBody;
    }

    public void setTxBody(TxBodyResp<R> txBody) {
        this.txBody = txBody;
    }

    @Override
    public String toString() {
        return "CommonResp{" +
                "txHeader=" + txHeader +
                ", txBody=" + txBody +
                '}';
    }

}
