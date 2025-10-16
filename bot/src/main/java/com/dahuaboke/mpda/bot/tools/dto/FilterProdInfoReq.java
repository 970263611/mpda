package com.dahuaboke.mpda.bot.tools.dto;

public class FilterProdInfoReq {

    //年化利率
    private String yearRita;
    //债券基金类型
    private String fundClassificationCode;
    //一个月最大回车率
    private String withDrawal;

    public FilterProdInfoReq() {
    }

    public FilterProdInfoReq(String yearRita, String fundClassificationCode, String withDrawal) {
        this.yearRita = yearRita;
        this.fundClassificationCode = fundClassificationCode;
        this.withDrawal = withDrawal;
    }

    public String getYearRita() {
        return yearRita;
    }

    public void setYearRita(String yearRita) {
        this.yearRita = yearRita;
    }

    public String getFundClassificationCode() {
        return fundClassificationCode;
    }

    public void setFundClassificationCode(String fundClassificationCode) {
        this.fundClassificationCode = fundClassificationCode;
    }

    public String getWithDrawal() {
        return withDrawal;
    }

    public void setWithDrawal(String withDrawal) {
        this.withDrawal = withDrawal;
    }
}
