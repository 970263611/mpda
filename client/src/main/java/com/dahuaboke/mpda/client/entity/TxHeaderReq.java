package com.dahuaboke.mpda.client.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Desc: 新核心接口通用请求头
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class TxHeaderReq {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 发起系统或组件编码  11填调用方系统号
     */
    private String startSysOrCmptNo = "99370000000";

    /**
     * 发送系统或组件编码
     */
    private String sendSysOrCmptNo = "99370000000";

    /**
     * 发起渠道标识码
     */
    private String startChnlFgCd = "99";

    /**
     * 业务发起机构号
     * 总行
     */
    private String busiSendInstNo = "11005293";

    /**
     * 该字段存放发送报文系统/组件所部署的数据中心代码，每次发送报文都要更新该字段。
     * 用1位字符代表数据中心，数据中心代码 F-丰台数据中心；Y-亦庄数据中心；H-合肥数据中心；L-廊坊数据中心
     */
    private String dataCenterCode = "L";

    /**
     * 交易发起时间 YYYYMMDDHHmmssSSS
     */
    private String txStartTime = SDF.format(new Date());

    /**
     * 交易发送时间
     * YYYYMMDDHHmmssSSS
     */
    private String txSendTime = SDF.format(new Date());

    /**
     * 报文总长度
     */
    private String msgrptTotalLen = "999";

    /**
     * 公共报文头长度
     */
    private String msgrptFmtVerNo = "10000";

    /**
     * 报文协议类型
     */
    private String msgAgrType = "1";

    /**
     * 公共报文头长度
     * N..10
     */
    private String pubMsgHeadLen = "999";

    /**
     * 嵌入报文长度  10
     * N..10
     */
    private String embedMsgrptLen = "999";

    /**
     * 目标系统或组件编码 11
     */
    private String targetSysOrCmptNo = "99900180002";

    /**
     * 服务类型代码
     */
    private String servTpCd = "1";

    /**
     * 服务编码   调用哪个就用哪个 ,例如: rag_v1_C014007
     */
    private String servNo;

    /**
     * 服务版本号
     */
    private String servVerNo = "10000";

    /**
     * 报文鉴别码
     * ANS..32
     */
    private String msgrptMac = "00000000000000000000000000000000";

    /**
     * 4段32位编码。
     * 第一段：时间戳，长度14位，表示交易发起时间，格式为YYYYMMDDHHMMSS;
     * 第二段：源系统，长度7位，标识交易发起的系统；
     * 第三位：发起交易实例序号，长度5位；
     * 第四段：长度6位，由机器自身的序列号生成器产生，从0开始递增，每取出一个序列号后即向上加一，到达999999后循环从0开始，序列号与时间不相关。（必传且不能写死）
     */
    private String globalBusiTrackNo = "20220817000123456000000000001411";

    /**
     * 子交易序号
     * 子交易序列号由三部分组成：七位系统编码（系统编码大于七位则取前七位）
     * +五位workid（workid生成逻辑为从系统变量获取workid，然后从环境变量中获取，如果不存在就会拼接mac地址+dubboPort+maxWorkerId拼接，目前平台写了一个demo，WorkIdGeneratorUtil仅供参考！！目标就是不同的服务workid不同即可）
     * +20位序列号（传20位0即可）----（必传且不能写死）
     */
    private String subtxNo = "10000000000000000000000000000001";

    /**
     * 请求系统流水号  与globalBusiTrackNo的值一致
     */
    private String reqSysSriNo = "20220817000123456000000000001411";

    /**
     * 描述交易主要映射要素信息，可以是卡号、账号、本号、客户编号其中一个；前2位代表映射要素类型（09-回单编号），后34位填写映射要素值。
     */
    private String mainMapElemIntInfo = "090000000000000000000000000000000001";

    /**
     * resvedInputInfo字段是场景编码，产品给这个需求定场景编号了没
     */
    private String resvedInputInfo = "0288";


    public TxHeaderReq(String startSysOrCmptNo, String sendSysOrCmptNo, String startChnlFgCd, String busiSendInstNo, String dataCenterCode, String txStartTime, String txSendTime, String msgrptTotalLen, String msgrptFmtVerNo, String msgAgrType, String pubMsgHeadLen, String embedMsgrptLen, String targetSysOrCmptNo, String servTpCd, String servNo, String servVerNo, String msgrptMac, String globalBusiTrackNo, String subtxNo, String reqSysSriNo, String mainMapElemIntInfo, String resvedInputInfo) {
        this.startSysOrCmptNo = startSysOrCmptNo;
        this.sendSysOrCmptNo = sendSysOrCmptNo;
        this.startChnlFgCd = startChnlFgCd;
        this.busiSendInstNo = busiSendInstNo;
        this.dataCenterCode = dataCenterCode;
        this.txStartTime = txStartTime;
        this.txSendTime = txSendTime;
        this.msgrptTotalLen = msgrptTotalLen;
        this.msgrptFmtVerNo = msgrptFmtVerNo;
        this.msgAgrType = msgAgrType;
        this.pubMsgHeadLen = pubMsgHeadLen;
        this.embedMsgrptLen = embedMsgrptLen;
        this.targetSysOrCmptNo = targetSysOrCmptNo;
        this.servTpCd = servTpCd;
        this.servNo = servNo;
        this.servVerNo = servVerNo;
        this.msgrptMac = msgrptMac;
        this.globalBusiTrackNo = globalBusiTrackNo;
        this.subtxNo = subtxNo;
        this.reqSysSriNo = reqSysSriNo;
        this.mainMapElemIntInfo = mainMapElemIntInfo;
        this.resvedInputInfo = resvedInputInfo;
    }

    public TxHeaderReq() {
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

    public String getStartChnlFgCd() {
        return startChnlFgCd;
    }

    public void setStartChnlFgCd(String startChnlFgCd) {
        this.startChnlFgCd = startChnlFgCd;
    }

    public String getBusiSendInstNo() {
        return busiSendInstNo;
    }

    public void setBusiSendInstNo(String busiSendInstNo) {
        this.busiSendInstNo = busiSendInstNo;
    }

    public String getDataCenterCode() {
        return dataCenterCode;
    }

    public void setDataCenterCode(String dataCenterCode) {
        this.dataCenterCode = dataCenterCode;
    }

    public String getTxStartTime() {
        return txStartTime;
    }

    public void setTxStartTime(String txStartTime) {
        this.txStartTime = txStartTime;
    }

    public String getTxSendTime() {
        return txSendTime;
    }

    public void setTxSendTime(String txSendTime) {
        this.txSendTime = txSendTime;
    }

    public String getMsgrptTotalLen() {
        return msgrptTotalLen;
    }

    public void setMsgrptTotalLen(String msgrptTotalLen) {
        this.msgrptTotalLen = msgrptTotalLen;
    }

    public String getMsgrptFmtVerNo() {
        return msgrptFmtVerNo;
    }

    public void setMsgrptFmtVerNo(String msgrptFmtVerNo) {
        this.msgrptFmtVerNo = msgrptFmtVerNo;
    }

    public String getMsgAgrType() {
        return msgAgrType;
    }

    public void setMsgAgrType(String msgAgrType) {
        this.msgAgrType = msgAgrType;
    }

    public String getPubMsgHeadLen() {
        return pubMsgHeadLen;
    }

    public void setPubMsgHeadLen(String pubMsgHeadLen) {
        this.pubMsgHeadLen = pubMsgHeadLen;
    }

    public String getEmbedMsgrptLen() {
        return embedMsgrptLen;
    }

    public void setEmbedMsgrptLen(String embedMsgrptLen) {
        this.embedMsgrptLen = embedMsgrptLen;
    }

    public String getTargetSysOrCmptNo() {
        return targetSysOrCmptNo;
    }

    public void setTargetSysOrCmptNo(String targetSysOrCmptNo) {
        this.targetSysOrCmptNo = targetSysOrCmptNo;
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

    public String getReqSysSriNo() {
        return reqSysSriNo;
    }

    public void setReqSysSriNo(String reqSysSriNo) {
        this.reqSysSriNo = reqSysSriNo;
    }

    public String getMainMapElemIntInfo() {
        return mainMapElemIntInfo;
    }

    public void setMainMapElemIntInfo(String mainMapElemIntInfo) {
        this.mainMapElemIntInfo = mainMapElemIntInfo;
    }

    public String getResvedInputInfo() {
        return resvedInputInfo;
    }

    public void setResvedInputInfo(String resvedInputInfo) {
        this.resvedInputInfo = resvedInputInfo;
    }

    @Override
    public String toString() {
        return "TxHeaderReq{" +
                "startSysOrCmptNo='" + startSysOrCmptNo + '\'' +
                ", sendSysOrCmptNo='" + sendSysOrCmptNo + '\'' +
                ", startChnlFgCd='" + startChnlFgCd + '\'' +
                ", busiSendInstNo='" + busiSendInstNo + '\'' +
                ", dataCenterCode='" + dataCenterCode + '\'' +
                ", txStartTime='" + txStartTime + '\'' +
                ", txSendTime='" + txSendTime + '\'' +
                ", msgrptTotalLen='" + msgrptTotalLen + '\'' +
                ", msgrptFmtVerNo='" + msgrptFmtVerNo + '\'' +
                ", msgAgrType='" + msgAgrType + '\'' +
                ", pubMsgHeadLen='" + pubMsgHeadLen + '\'' +
                ", embedMsgrptLen='" + embedMsgrptLen + '\'' +
                ", targetSysOrCmptNo='" + targetSysOrCmptNo + '\'' +
                ", servTpCd='" + servTpCd + '\'' +
                ", servNo='" + servNo + '\'' +
                ", servVerNo='" + servVerNo + '\'' +
                ", msgrptMac='" + msgrptMac + '\'' +
                ", globalBusiTrackNo='" + globalBusiTrackNo + '\'' +
                ", subtxNo='" + subtxNo + '\'' +
                ", reqSysSriNo='" + reqSysSriNo + '\'' +
                ", mainMapElemIntInfo='" + mainMapElemIntInfo + '\'' +
                ", resvedInputInfo='" + resvedInputInfo + '\'' +
                '}';
    }

}
