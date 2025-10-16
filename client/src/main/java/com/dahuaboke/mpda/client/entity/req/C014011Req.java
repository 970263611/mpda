package com.dahuaboke.mpda.client.entity.req;

import java.util.List;

/**
 * @Desc: rag文本重排序-C014011 请求体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014011Req {

    /**
     * 文本
     */
    private String query;

    /**
     * 对比文本
     */
    private List<String> texts;

    /**
     * 场景id
     */
    private String sceneId;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    @Override
    public String toString() {
        return "C014011Req{" +
                "query='" + query + '\'' +
                ", texts=" + texts +
                ", sceneId='" + sceneId + '\'' +
                '}';
    }

}
