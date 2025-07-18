package com.dahuaboke.mpda.tools;

import java.util.Map;

public class ToolResult {
    private String status;
    private String message;
    private Map<String, Object> data;

    public ToolResult(String success, String s, Map<String, Object> data) {
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
