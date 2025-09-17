package com.dahuaboke.mpda.bot.rag.client;

import java.math.BigDecimal;
import java.util.List;


public class FundEntity {

    /**
     * 文本内容
     */
    private String id;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 文本嵌入向量
     */
    private float[] embedding;

    /**
     * 页码
     */
    private int page_number;

    /**
     * 文件名称
     */
    private String file_name;

    /**
     * 关键字
     */
    private List<String> excerpt_keywords;

    /**
     * 文件名关键字
     */
    private List<String> file_name_keywords;

    /**
     * 分数
     */
    private BigDecimal score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public List<String> getExcerpt_keywords() {
        return excerpt_keywords;
    }

    public void setExcerpt_keywords(List<String> excerpt_keywords) {
        this.excerpt_keywords = excerpt_keywords;
    }

    public List<String> getFile_name_keywords() {
        return file_name_keywords;
    }

    public void setFile_name_keywords(List<String> file_name_keywords) {
        this.file_name_keywords = file_name_keywords;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

}
