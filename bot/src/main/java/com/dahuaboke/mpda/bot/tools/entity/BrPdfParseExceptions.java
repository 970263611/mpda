package com.dahuaboke.mpda.bot.tools.entity;

/**
 * @Desc: 文件解析异常处理表
 * @Author：zhh
 * @Date：2025/11/19 08:51
 */
public class BrPdfParseExceptions {

    private String id;

    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 基金信息类型  0-定期报告 1-资料概要
     */
    private String ancmTpBclsCd;

    /**
     * 基金全称
     */
    private String fundProdtFullNm;

    /**
     * 异常类型 0-文件异常 1-文件内容问题提取异常
     */
    private String exceptionType;

    /**
     * 文件提取错误的问题，异常级别是问题
     */
    private String question;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 异常细节
     */
    private String details;

    /**
     * 重试次数,默认0,最多不超过三次
     */
    private int count;

    /**
     * 重试状态,查看FileDealFlag枚举，默认失败
     */
    private String status;

    public BrPdfParseExceptions() {
    }

    public BrPdfParseExceptions(String id, String fundCode, String ancmTpBclsCd, String fundProdtFullNm, String exceptionType, String question, String description, String details, int count, String status) {
        this.id = id;
        this.fundCode = fundCode;
        this.ancmTpBclsCd = ancmTpBclsCd;
        this.fundProdtFullNm = fundProdtFullNm;
        this.exceptionType = exceptionType;
        this.question = question;
        this.description = description;
        this.details = details;
        this.count = count;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getAncmTpBclsCd() {
        return ancmTpBclsCd;
    }

    public void setAncmTpBclsCd(String ancmTpBclsCd) {
        this.ancmTpBclsCd = ancmTpBclsCd;
    }

    public String getFundProdtFullNm() {
        return fundProdtFullNm;
    }

    public void setFundProdtFullNm(String fundProdtFullNm) {
        this.fundProdtFullNm = fundProdtFullNm;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
