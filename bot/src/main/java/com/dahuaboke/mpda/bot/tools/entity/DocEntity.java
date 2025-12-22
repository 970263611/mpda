package com.dahuaboke.mpda.bot.tools.entity;

import com.dahuaboke.mpda.core.rag.entity.FieldComment;

public class DocEntity {

    /**
     * 理财产品名称
     **/
    @FieldComment(question = "理财产品名称是什么，只要答案,结果不要带上问题")
    private String 理财产品名称;

    /**
     * 产品管理人
     **/
    @FieldComment(question = "产品管理人是什么，只要答案,结果不要带上问题")
    private String 产品管理人;

    /**
     * 产品托管人
     **/
    @FieldComment(question = "产品托管人是什么，只要答案,结果不要带上问题")
    private String 产品托管人;

    /**
     * 产品运作模式
     **/
    @FieldComment(question = "产品运作模式是什么，只要答案,结果不要带上问题")
    private String 产品运作模式;

    /**
     * 基金经理说明
     **/
    @FieldComment(question = "风险等级是什么，只要答案,结果不要带上问题")
    private String 风险等级;

    /**
     * 投资管理费率
     **/
    @FieldComment(question = "投资管理费率是什么，只要答案,结果不要带上投资管理费率")
    private String 投资管理费率;

    /**
     * 销售服务费率
     **/
    @FieldComment(question = "销售服务费率是什么，只要答案,结果不要带上销售服务费率")
    private String 销售服务费率;

    /**
     * 报告期末按行业分类的境内股票投资组合
     **/
    @FieldComment(question = "托管费率是什么，只要答案,结果不要带上托管费率")
    private String 托管费率;

    /**
     * 认购费率
     **/
    @FieldComment(question = "认购费率是什么，只要答案,结果不要带上认购费率")
    private String 认购费率;

    /**
     * 申购费率
     **/
    @FieldComment(question = "申购费率是什么，只要答案,结果不要带上问题,结果不要带上申购费率")
    private String 申购费率;

    /**
     * 赎回费率
     **/
    @FieldComment(question = "赎回费率，只要答案,结果不要带上赎回费率")
    private String 赎回费率;

    @Override
    public String toString() {
        return "TestEntity{" +
                "理财产品名称='" + 理财产品名称 + '\'' +
                ", 产品管理人='" + 产品管理人 + '\'' +
                ", 产品托管人='" + 产品托管人 + '\'' +
                ", 产品运作模式='" + 产品运作模式 + '\'' +
                ", 风险等级='" + 风险等级 + '\'' +
                ", 投资管理费率='" + 投资管理费率 + '\'' +
                ", 销售服务费率='" + 销售服务费率 + '\'' +
                ", 托管费率='" + 托管费率 + '\'' +
                ", 认购费率='" + 认购费率 + '\'' +
                ", 申购费率='" + 申购费率 + '\'' +
                ", 赎回费率='" + 赎回费率 + '\'' +
                '}';
    }

    public String get理财产品名称() {
        return 理财产品名称;
    }

    public void set理财产品名称(String 理财产品名称) {
        this.理财产品名称 = 理财产品名称;
    }

    public String get产品管理人() {
        return 产品管理人;
    }

    public void set产品管理人(String 产品管理人) {
        this.产品管理人 = 产品管理人;
    }

    public String get产品托管人() {
        return 产品托管人;
    }

    public void set产品托管人(String 产品托管人) {
        this.产品托管人 = 产品托管人;
    }

    public String get产品运作模式() {
        return 产品运作模式;
    }

    public void set产品运作模式(String 产品运作模式) {
        this.产品运作模式 = 产品运作模式;
    }

    public String get风险等级() {
        return 风险等级;
    }

    public void set风险等级(String 风险等级) {
        this.风险等级 = 风险等级;
    }

    public String get投资管理费率() {
        return 投资管理费率;
    }

    public void set投资管理费率(String 投资管理费率) {
        this.投资管理费率 = 投资管理费率;
    }

    public String get销售服务费率() {
        return 销售服务费率;
    }

    public void set销售服务费率(String 销售服务费率) {
        this.销售服务费率 = 销售服务费率;
    }

    public String get托管费率() {
        return 托管费率;
    }

    public void set托管费率(String 托管费率) {
        this.托管费率 = 托管费率;
    }

    public String get认购费率() {
        return 认购费率;
    }

    public void set认购费率(String 认购费率) {
        this.认购费率 = 认购费率;
    }

    public String get申购费率() {
        return 申购费率;
    }

    public void set申购费率(String 申购费率) {
        this.申购费率 = 申购费率;
    }

    public String get赎回费率() {
        return 赎回费率;
    }

    public void set赎回费率(String 赎回费率) {
        this.赎回费率 = 赎回费率;
    }

}
