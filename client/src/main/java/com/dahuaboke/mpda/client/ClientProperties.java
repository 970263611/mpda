package com.dahuaboke.mpda.client;

import com.dahuaboke.mpda.client.constants.RagConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Desc: 新核心客户端模型配置类
 * @Author：zhh
 * @Date：2025/9/11 10:37
 */
@ConfigurationProperties(prefix = RagConstant.MODEL_PREFIX)
public class ClientProperties {

    // 地址
    private String url;

    // 发送方系统号
    private String sendSysNo;

    // 接收方系统号
    private String targetSysNo;

    //产品需求场景编号
    private String resvedInputInfo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSendSysNo() {
        return sendSysNo;
    }

    public void setSendSysNo(String sendSysNo) {
        this.sendSysNo = sendSysNo;
    }

    public String getTargetSysNo() {
        return targetSysNo;
    }

    public void setTargetSysNo(String targetSysNo) {
        this.targetSysNo = targetSysNo;
    }

    public String getResvedInputInfo() {
        return resvedInputInfo;
    }

    public void setResvedInputInfo(String resvedInputInfo) {
        this.resvedInputInfo = resvedInputInfo;
    }

}
