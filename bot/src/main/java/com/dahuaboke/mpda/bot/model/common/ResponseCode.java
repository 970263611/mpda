package com.dahuaboke.mpda.bot.model.common;

/**
 * @Desc: 对外接口返回枚举
 * @Author：zhh
 * @Date：2025/9/15 9:14
 */
public enum ResponseCode {

    SUCCESS("0000000000000000", "交易成功"),
    PARAM_FORMAT_ERROR("0000000000010001", "参数格式错误"),
    MISSING_PARAM("0000000000010002", "缺少必要参数"),
    INVALID_SESSION("0000000000010003", "会话ID无效或已过期"),
    SERVICE_UNAVAILABLE("0000000000010004", "服务不可用"),
    INTERNAL_ERROR("0000000000010005", "系统内部错误");

    private final String code;
    private final String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 根据code查找枚举
    public static ResponseCode getByCode(String code) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        return INTERNAL_ERROR;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
