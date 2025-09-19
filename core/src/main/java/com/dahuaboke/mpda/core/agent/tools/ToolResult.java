package com.dahuaboke.mpda.core.agent.tools;

import java.util.Map;

import static com.dahuaboke.mpda.core.agent.tools.ToolResult.Status.ERROR;
import static com.dahuaboke.mpda.core.agent.tools.ToolResult.Status.SUCCESS;

public class ToolResult {

    private Status status;
    private String message;
    private Map<String,Object> toolInputArguments;
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

    public Map<String, Object> getToolInputArguments() {
        return toolInputArguments;
    }

    public void setToolInputArguments(Map<String, Object> toolInputArguments) {
        this.toolInputArguments = toolInputArguments;
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
