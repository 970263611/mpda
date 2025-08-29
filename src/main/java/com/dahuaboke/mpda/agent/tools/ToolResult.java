package com.dahuaboke.mpda.agent.tools;

import static com.dahuaboke.mpda.agent.tools.ToolResult.Status.ERROR;
import static com.dahuaboke.mpda.agent.tools.ToolResult.Status.SUCCESS;

public class ToolResult {

    private Status status;
    private String message;
    private Object data;

    public ToolResult() {
    }

    public ToolResult(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ToolResult success() {
        return new ToolResult(SUCCESS, null, null);
    }

    public static ToolResult success(String message) {
        return new ToolResult(SUCCESS, message, null);
    }

    public static ToolResult success(String message, Object data) {
        return new ToolResult(SUCCESS, message, data);
    }

    public static ToolResult error() {
        return new ToolResult(ERROR, null, null);
    }

    public static ToolResult error(String message) {
        return new ToolResult(ERROR, message, null);
    }

    public static ToolResult error(String message, Object data) {
        return new ToolResult(ERROR, message, data);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public enum Status {
        SUCCESS,
        ERROR
    }
}
