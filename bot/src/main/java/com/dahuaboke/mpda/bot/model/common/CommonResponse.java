package com.dahuaboke.mpda.bot.model.common;

/**
 * @Desc: 对外接口通用返回对象
 * @Author：zhh
 * @Date：2025/9/15 9:15
 */
public class CommonResponse<T> {
    private String code;
    private String msg;
    private T content;
    private T extend;
    private Long timestamp;

    public CommonResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    // 成功响应
    public static <T> CommonResponse<T> success() {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMsg(ResponseCode.SUCCESS.getMsg());
        return response;
    }

    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> response = success();
        response.setContent(data);
        return response;
    }

    public static <T> CommonResponse<T> success(T data,T extend) {
        CommonResponse<T> response = success();
        response.setContent(data);
        response.setExtend(extend);
        return response;
    }

    // 错误响应
    public static <T> CommonResponse<T> error(ResponseCode responseCode) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(responseCode.getCode());
        response.setMsg(responseCode.getMsg());
        return response;
    }

    public static <T> CommonResponse<T> error(ResponseCode responseCode, String customMsg) {
        CommonResponse<T> response = error(responseCode);
        response.setMsg(customMsg);
        return response;
    }

    public static <T> CommonResponse<T> error(String code, String msg) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public T getExtend() {
        return extend;
    }

    public void setExtend(T extend) {
        this.extend = extend;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return ResponseCode.SUCCESS.getCode().equals(this.code);
    }
}
