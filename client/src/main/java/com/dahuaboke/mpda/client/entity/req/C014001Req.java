package com.dahuaboke.mpda.client.entity.req;

import java.util.List;

/**
 * @Desc: 通用数据批量写入-C014001 请求体
 * @Author：zhh
 * @Date：2025/9/1 10:10
 */
public class C014001Req<T> {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 系统号
     */
    private String systemNo;

    /**
     * 数据列表
     */
    private List<T> dataMapList;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public List<T> getDataMapList() {
        return dataMapList;
    }

    public void setDataMapList(List<T> dataMapList) {
        this.dataMapList = dataMapList;
    }


    @Override
    public String toString() {
        return "C014001Req{" +
                "indexName='" + indexName + '\'' +
                ", systemNo='" + systemNo + '\'' +
                ", dataMapList=" + dataMapList +
                '}';
    }

}
