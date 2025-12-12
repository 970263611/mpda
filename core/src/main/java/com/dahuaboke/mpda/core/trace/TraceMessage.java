package com.dahuaboke.mpda.core.trace;

public class TraceMessage {

    private String conversationId;
    private String sceneName;
    private String description;
    private RequestType requestType;
    private TraceType traceType;
    private long time;

    public TraceMessage() {
    }

    public TraceMessage(String conversationId, String sceneName, String description, RequestType requestType, TraceType traceType) {
        this.conversationId = conversationId;
        this.sceneName = sceneName;
        this.description = description;
        this.requestType = requestType;
        this.traceType = traceType;
        this.time = System.currentTimeMillis();
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public String getDescription() {
        return description;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public TraceType getTraceType() {
        return traceType;
    }

    public long getTime() {
        return time;
    }

    public enum RequestType {
        SYNC,
        ASYNC,
        UNKNOW
    }

    public enum TraceType {
        IN,
        OUT,
        EXCEPTION
    }
}
