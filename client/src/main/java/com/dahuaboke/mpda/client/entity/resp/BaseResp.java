package com.dahuaboke.mpda.client.entity.resp;

/**
 * @Desc: 通用返回内容
 * @Author：zhh
 * @Date：2025/9/1 10:10
 */
public class BaseResp {

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回码
     */
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

}
