package com.dahuaboke.mpda.client.entity.req;

import java.util.Map;

/**
 * @Desc: 通用数据普通查询-C014005 请求体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014005Req {

    /**
     * 索引名称				Y
     */
    private String indexName;

    /**
     * 系统号				Y		需要先申请系统号，待apollo配置通过白名单校验后可使用
     */
    private String systemNo;


    /**
     * 筛选条件				N		key为字段，value为对应的值
     */
    private Map<String, Object> conditionMap;


    /**
     * 排序字段              N		key是排序字段，value是ASC或者DESC
     */
    private Map<String, String> orderCondition;

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

    public Map<String, Object> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, Object> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public Map<String, String> getOrderCondition() {
        return orderCondition;
    }

    public void setOrderCondition(Map<String, String> orderCondition) {
        this.orderCondition = orderCondition;
    }

    @Override
    public String toString() {
        return "C014005Req{" +
                "indexName='" + indexName + '\'' +
                ", systemNo='" + systemNo + '\'' +
                ", conditionMap=" + conditionMap +
                ", orderCondition=" + orderCondition +
                '}';
    }

}
