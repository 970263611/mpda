package com.dahuaboke.mpda.client.entity.resp;

import java.util.List;

/**
 * @Desc: 通用数据普通查询-C014005 返回体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014005Resp<R> extends BaseResp {

    /**
     * 返回对象集合
     */
    private List<R> resultList;


    public List<R> getResultList() {
        return resultList;
    }

    public void setResultList(List<R> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "C014005Resp{" +
                "resultList=" + resultList +
                '}';
    }

}
