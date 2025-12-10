package com.dahuaboke.mpda.core.monitor.entity;


import com.dahuaboke.mpda.core.event.Event;

public class MonitorEventEntity {

    private String conversationId;

    private String data;

    private Event.Type eventType;

    private Long createTime;

    public MonitorEventEntity() {
    }

    public MonitorEventEntity(String conversationId, String data, Event.Type eventType, Long createTime) {
        this.conversationId = conversationId;
        this.data = data;
        this.eventType = eventType;
        this.createTime = createTime;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Event.Type getEventType() {
        return eventType;
    }

    public void setEventType(Event.Type eventType) {
        this.eventType = eventType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
