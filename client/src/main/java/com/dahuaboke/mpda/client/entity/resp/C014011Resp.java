package com.dahuaboke.mpda.client.entity.resp;

import java.util.List;

/**
 * @Desc: rag文本重排序-C014011 返回体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014011Resp extends BaseResp {

    /**
     * 原文本
     */
    private String originalText;


    /**
     * 结果个数
     */
    private String totalResult;


    /**
     * 处理结果
     */
    private List<ProcessData> processData;

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(String totalResult) {
        this.totalResult = totalResult;
    }

    public List<ProcessData> getProcessData() {
        return processData;
    }

    public void setProcessData(List<ProcessData> processData) {
        this.processData = processData;
    }

    @Override
    public String toString() {
        return "C014011Resp{" +
                "originalText='" + originalText + '\'' +
                ", totalResult='" + totalResult + '\'' +
                ", processData=" + processData +
                '}';
    }

    public static class ProcessData {

        private double score;

        private String text;

        private int index;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "ProcessData{" +
                    "score=" + score +
                    ", text='" + text + '\'' +
                    ", index=" + index +
                    '}';
        }

    }

}
