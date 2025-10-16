package com.dahuaboke.mpda.client.entity;

/**
 * @Desc: 新核心接口通用请求体
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class TxBodyReq<T> {

    /**
     * 企业级通用域（必送）
     */
    private TxComni txComni = new TxComni();


    /**
     * 实体域
     */
    private T txEntity;


    public TxComni getTxComni() {
        return txComni;
    }

    public void setTxComni(TxComni txComni) {
        this.txComni = txComni;
    }

    public T getTxEntity() {
        return txEntity;
    }

    public void setTxEntity(T txEntity) {
        this.txEntity = txEntity;
    }

    @Override
    public String toString() {
        return "TxBodyReq{" +
                "txComni=" + txComni +
                ", txEntity=" + txEntity +
                '}';
    }

    class TxComni {

        private String oprTellerNo;

        public TxComni() {
        }

        public TxComni(String oprTellerNo) {
            this.oprTellerNo = oprTellerNo;
        }

        public String getOprTellerNo() {
            return oprTellerNo;
        }

        public void setOprTellerNo(String oprTellerNo) {
            this.oprTellerNo = oprTellerNo;
        }

        @Override
        public String toString() {
            return "TxComni{" +
                    "oprTellerNo='" + oprTellerNo + '\'' +
                    '}';
        }

    }

}
