package com.dahuaboke.mpda.client.entity.resp;

import java.util.List;

/**
 * @Desc: 向量查询-C014006 返回体
 * @Author：zhh
 * @Date：2025/9/1 10:10
 */
public class C014006Resp<R> extends BaseResp {

    /**
     * 返回对象集合
     */
    private List<R> resultMap;

    public List<R> getResultMap() {
        return resultMap;
    }

    public void setResultMap(List<R> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public String toString() {
        return "C014006Resp{" +
                "resultMap=" + resultMap +
                '}';
    }

}
