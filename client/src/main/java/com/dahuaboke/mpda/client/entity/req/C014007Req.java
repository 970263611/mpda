package com.dahuaboke.mpda.client.entity.req;

/**
 * @Desc: 文本转向量-C014007 请求体
 * @Author：zhh
 * @Date：2025/9/1 10:10
 */
public class C014007Req {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "C014007Req{" +
                "text='" + text + '\'' +
                '}';
    }

}
