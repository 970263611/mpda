package com.dahuaboke.mpda.client.entity;

/**
 * @Desc: 新核心接口通用返回头
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class TxHeaderResp {

    private String pubMsgHeadLen;

    private String msgrptFmtVerNo;

    private String msgrptTotalLen;

    private String embedMsgrptLen;

    private String msgAgrType;

    private String startSysOrCmptNo;

    private String sendSysOrCmptNo;

    private String dataCenterCode;

    private String txRecvTime;

    private String txRespTime;

    private String msgrptMac;

    private String globalBusiTrackNo;

    private String subtxNo;

    private String servTpCd;

    private String servNo;

    private String servVerNo;

    private String servReturnTpCd;

    private String servRespCd;

    private String servRespDescInfo;

    private String reqSysSriNo;

    private String resvedInputInfo;

    public TxHeaderResp(String pubMsgHeadLen, String msgrptFmtVerNo, String msgrptTotalLen, String embedMsgrptLen, String msgAgrType, String startSysOrCmptNo, String sendSysOrCmptNo, String dataCenterCode, String txRecvTime, String txRespTime, String msgrptMac, String globalBusiTrackNo, String subtxNo, String servTpCd, String servNo, String servVerNo, String servReturnTpCd, String servRespCd, String servRespDescInfo, String reqSysSriNo, String resvedInputInfo) {
        this.pubMsgHeadLen = pubMsgHeadLen;
        this.msgrptFmtVerNo = msgrptFmtVerNo;
        this.msgrptTotalLen = msgrptTotalLen;
        this.embedMsgrptLen = embedMsgrptLen;
        this.msgAgrType = msgAgrType;
        this.startSysOrCmptNo = startSysOrCmptNo;
        this.sendSysOrCmptNo = sendSysOrCmptNo;
        this.dataCenterCode = dataCenterCode;
        this.txRecvTime = txRecvTime;
        this.txRespTime = txRespTime;
        this.msgrptMac = msgrptMac;
        this.globalBusiTrackNo = globalBusiTrackNo;
        this.subtxNo = subtxNo;
        this.servTpCd = servTpCd;
        this.servNo = servNo;
        this.servVerNo = servVerNo;
        this.servReturnTpCd = servReturnTpCd;
        this.servRespCd = servRespCd;
        this.servRespDescInfo = servRespDescInfo;
        this.reqSysSriNo = reqSysSriNo;
        this.resvedInputInfo = resvedInputInfo;
    }

    public TxHeaderResp() {
    }

    public String getPubMsgHeadLen() {
        return pubMsgHeadLen;
    }

    public void setPubMsgHeadLen(String pubMsgHeadLen) {
        this.pubMsgHeadLen = pubMsgHeadLen;
    }

    public String getMsgrptFmtVerNo() {
        return msgrptFmtVerNo;
    }

    public void setMsgrptFmtVerNo(String msgrptFmtVerNo) {
        this.msgrptFmtVerNo = msgrptFmtVerNo;
    }

    public String getMsgrptTotalLen() {
        return msgrptTotalLen;
    }

    public void setMsgrptTotalLen(String msgrptTotalLen) {
        this.msgrptTotalLen = msgrptTotalLen;
    }

    public String getEmbedMsgrptLen() {
        return embedMsgrptLen;
    }

    public void setEmbedMsgrptLen(String embedMsgrptLen) {
        this.embedMsgrptLen = embedMsgrptLen;
    }

    public String getMsgAgrType() {
        return msgAgrType;
    }

    public void setMsgAgrType(String msgAgrType) {
        this.msgAgrType = msgAgrType;
    }

    public String getStartSysOrCmptNo() {
        return startSysOrCmptNo;
    }

    public void setStartSysOrCmptNo(String startSysOrCmptNo) {
        this.startSysOrCmptNo = startSysOrCmptNo;
    }

    public String getSendSysOrCmptNo() {
        return sendSysOrCmptNo;
    }

    public void setSendSysOrCmptNo(String sendSysOrCmptNo) {
        this.sendSysOrCmptNo = sendSysOrCmptNo;
    }

    public String getDataCenterCode() {
        return dataCenterCode;
    }

    public void setDataCenterCode(String dataCenterCode) {
        this.dataCenterCode = dataCenterCode;
    }

    public String getTxRecvTime() {
        return txRecvTime;
    }

    public void setTxRecvTime(String txRecvTime) {
        this.txRecvTime = txRecvTime;
    }

    public String getTxRespTime() {
        return txRespTime;
    }

    public void setTxRespTime(String txRespTime) {
        this.txRespTime = txRespTime;
    }

    public String getMsgrptMac() {
        return msgrptMac;
    }

    public void setMsgrptMac(String msgrptMac) {
        this.msgrptMac = msgrptMac;
    }

    public String getGlobalBusiTrackNo() {
        return globalBusiTrackNo;
    }

    public void setGlobalBusiTrackNo(String globalBusiTrackNo) {
        this.globalBusiTrackNo = globalBusiTrackNo;
    }

    public String getSubtxNo() {
        return subtxNo;
    }

    public void setSubtxNo(String subtxNo) {
        this.subtxNo = subtxNo;
    }

    public String getServTpCd() {
        return servTpCd;
    }

    public void setServTpCd(String servTpCd) {
        this.servTpCd = servTpCd;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServVerNo() {
        return servVerNo;
    }

    public void setServVerNo(String servVerNo) {
        this.servVerNo = servVerNo;
    }

    public String getServReturnTpCd() {
        return servReturnTpCd;
    }

    public void setServReturnTpCd(String servReturnTpCd) {
        this.servReturnTpCd = servReturnTpCd;
    }

    public String getServRespCd() {
        return servRespCd;
    }

    public void setServRespCd(String servRespCd) {
        this.servRespCd = servRespCd;
    }

    public String getServRespDescInfo() {
        return servRespDescInfo;
    }

    public void setServRespDescInfo(String servRespDescInfo) {
        this.servRespDescInfo = servRespDescInfo;
    }

    public String getReqSysSriNo() {
        return reqSysSriNo;
    }

    public void setReqSysSriNo(String reqSysSriNo) {
        this.reqSysSriNo = reqSysSriNo;
    }

    public String getResvedInputInfo() {
        return resvedInputInfo;
    }

    public void setResvedInputInfo(String resvedInputInfo) {
        this.resvedInputInfo = resvedInputInfo;
    }

    @Override
    public String toString() {
        return "TxHeaderResp{" +
                "pubMsgHeadLen='" + pubMsgHeadLen + '\'' +
                ", msgrptFmtVerNo='" + msgrptFmtVerNo + '\'' +
                ", msgrptTotalLen='" + msgrptTotalLen + '\'' +
                ", embedMsgrptLen='" + embedMsgrptLen + '\'' +
                ", msgAgrType='" + msgAgrType + '\'' +
                ", startSysOrCmptNo='" + startSysOrCmptNo + '\'' +
                ", sendSysOrCmptNo='" + sendSysOrCmptNo + '\'' +
                ", dataCenterCode='" + dataCenterCode + '\'' +
                ", txRecvTime='" + txRecvTime + '\'' +
                ", txRespTime='" + txRespTime + '\'' +
                ", msgrptMac='" + msgrptMac + '\'' +
                ", globalBusiTrackNo='" + globalBusiTrackNo + '\'' +
                ", subtxNo='" + subtxNo + '\'' +
                ", servTpCd='" + servTpCd + '\'' +
                ", servNo='" + servNo + '\'' +
                ", servVerNo='" + servVerNo + '\'' +
                ", servReturnTpCd='" + servReturnTpCd + '\'' +
                ", servRespCd='" + servRespCd + '\'' +
                ", servRespDescInfo='" + servRespDescInfo + '\'' +
                ", reqSysSriNo='" + reqSysSriNo + '\'' +
                ", resvedInputInfo='" + resvedInputInfo + '\'' +
                '}';
    }

}
