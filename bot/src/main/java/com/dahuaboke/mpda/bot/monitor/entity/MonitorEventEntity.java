package com.dahuaboke.mpda.bot.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "br_monitor")
public class MonitorEventEntity {

    private String conversationId;

    private String busiData;

    private String eventTypeName;

    private String createTime;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getBusiData() {
        return busiData;
    }

    public void setBusiData(String busiData) {
        this.busiData = busiData;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public MonitorEventEntity(String conversationId, String busiData, String eventTypeName, String createTime) {
        this.conversationId = conversationId;
        this.busiData = busiData;
        this.eventTypeName = eventTypeName;
        this.createTime = createTime;
    }

    public MonitorEventEntity() {
    }

}
