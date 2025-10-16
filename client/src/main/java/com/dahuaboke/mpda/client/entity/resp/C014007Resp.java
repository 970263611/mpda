package com.dahuaboke.mpda.client.entity.resp;

import java.util.Arrays;

/**
 * @Desc: 文本转向量-C014007 返回体
 * @Author：zhh
 * @Date：2025/9/1 10:10
 */
public class C014007Resp extends BaseResp {

    /**
     * 原文本
     */
    private String originalText;

    /**
     * 向量集合
     */
    private float[] vector;

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    @Override
    public String toString() {
        return "C014007Resp{" +
                "originalText='" + originalText + '\'' +
                ", vector=" + Arrays.toString(vector) +
                '}';
    }

}
