package com.dahuaboke.mpda.client.entity.req;

import java.util.List;

/**
 * @Desc: 根据文档id进行批量删除-C014008 请求体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014008Req {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 系统号
     */
    private String system;

    /**
     * 待删除的文档id
     */
    private List<String> idList;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    @Override
    public String toString() {
        return "C014008Req{" +
                "indexName='" + indexName + '\'' +
                ", system='" + system + '\'' +
                ", idList=" + idList +
                '}';
    }

}
