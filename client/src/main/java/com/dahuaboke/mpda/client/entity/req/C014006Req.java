package com.dahuaboke.mpda.client.entity.req;

import java.util.Arrays;
import java.util.Map;

/**
 * @Desc: 通用向量检索-C014006 请求体
 * @Author：zhh
 * @Date：2025/9/1 10:10
 */
public class C014006Req {

    /**
     * 索引名称				Y
     */
    private String indexName;

    /**
     * 系统号				Y		需要先申请系统号，待apollo配置通过白名单校验后可使用
     */
    private String systemNo;

    /**
     * 向量字段名				Y
     */
    private String vectorFieldName;

    /**
     * 向量			    	Y		1536维
     */
    private float[] vector;

    /**
     * 筛选条件				N		key为字段，value为对应的值
     */
    private Map<String, Object> conditionMap;

    /**
     * top-K				N		默认为1
     */
    private int topK;

    /**
     * 候选数量				N		默认为3
     */
    private int numCandidates;

    /**
     * 返回数量				N		默认为1
     */
    private int size;

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

    public String getVectorFieldName() {
        return vectorFieldName;
    }

    public void setVectorFieldName(String vectorFieldName) {
        this.vectorFieldName = vectorFieldName;
    }

    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public Map<String, Object> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, Object> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    public int getNumCandidates() {
        return numCandidates;
    }

    public void setNumCandidates(int numCandidates) {
        this.numCandidates = numCandidates;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "C014006Req{" +
                "indexName='" + indexName + '\'' +
                ", systemNo='" + systemNo + '\'' +
                ", vectorFieldName='" + vectorFieldName + '\'' +
                ", vector=" + Arrays.toString(vector) +
                ", conditionMap=" + conditionMap +
                ", topK=" + topK +
                ", numCandidates=" + numCandidates +
                ", size=" + size +
                '}';
    }

}
