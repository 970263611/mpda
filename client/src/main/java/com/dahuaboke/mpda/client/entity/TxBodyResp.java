package com.dahuaboke.mpda.client.entity;

/**
 * @Desc: 新核心接口通用返回体
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class TxBodyResp<R> {

    /**
     * 实体域
     */
    private R txEntity;

    public TxBodyResp(R txEntity) {
        this.txEntity = txEntity;
    }

    public TxBodyResp() {
    }

    public R getTxEntity() {
        return txEntity;
    }

    public void setTxEntity(R txEntity) {
        this.txEntity = txEntity;
    }

    @Override
    public String toString() {
        return "TxBodyResp{" +
                "txEntity=" + txEntity +
                '}';
    }

}
