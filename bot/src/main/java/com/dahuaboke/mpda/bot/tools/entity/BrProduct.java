package com.dahuaboke.mpda.bot.tools.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName(value = "br_product")
public class BrProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 文件编号
     */
    private String fileNo;

    /**
     * 证券内码
     */
    private String securityIno;

    /**
     * 基金全称
     */
    private String fundProdtFullNm;

    /**
     * 基金简称
     */
    private String fundProdtSname;

    /**
     * 基金信息类型  0-定期报告 1-资料概要
     */
    private String ancmTpBclsCd;

    /**
     * 公告日期
     */
    private String issuDate;

    /**
     * 截止日期
     */
    private String dlineDate;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 下载地址
     */
    private String fileName;

    /**
     * 分类代码 0-债基 1-非债基
     */
    private String prodtClsCode;

    /**
     * 文件索引
     */
    private String inmngPlatfFileIndexNo;

    /**
     * 所在页数
     */
    private String pageCode;

    /**
     * 处理标志
     */
    private String dealFlag;

    /**
     * 0-未上架 1-已上架
     */
    private String validFlag;

    /**
     * 入库时间
     */
    private String indbDate;

    /**
     * 时间戳
     */
    private String lastModStamp;

    /**
     * 开始时间
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime dealBgnTime;

    /**
     * 超时时间
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long tmoutTimeNum;


    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getSecurityIno() {
        return securityIno;
    }

    public void setSecurityIno(String securityIno) {
        this.securityIno = securityIno;
    }

    public String getFundProdtFullNm() {
        return fundProdtFullNm;
    }

    public void setFundProdtFullNm(String fundProdtFullNm) {
        this.fundProdtFullNm = fundProdtFullNm;
    }

    public String getFundProdtSname() {
        return fundProdtSname;
    }

    public void setFundProdtSname(String fundProdtSname) {
        this.fundProdtSname = fundProdtSname;
    }

    public String getAncmTpBclsCd() {
        return ancmTpBclsCd;
    }

    public void setAncmTpBclsCd(String ancmTpBclsCd) {
        this.ancmTpBclsCd = ancmTpBclsCd;
    }

    public String getIssuDate() {
        return issuDate;
    }

    public void setIssuDate(String issuDate) {
        this.issuDate = issuDate;
    }

    public String getDlineDate() {
        return dlineDate;
    }

    public void setDlineDate(String dlineDate) {
        this.dlineDate = dlineDate;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProdtClsCode() {
        return prodtClsCode;
    }

    public void setProdtClsCode(String prodtClsCode) {
        this.prodtClsCode = prodtClsCode;
    }

    public String getInmngPlatfFileIndexNo() {
        return inmngPlatfFileIndexNo;
    }

    public void setInmngPlatfFileIndexNo(String inmngPlatfFileIndexNo) {
        this.inmngPlatfFileIndexNo = inmngPlatfFileIndexNo;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(String dealFlag) {
        this.dealFlag = dealFlag;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getIndbDate() {
        return indbDate;
    }

    public void setIndbDate(String indbDate) {
        this.indbDate = indbDate;
    }

    public String getLastModStamp() {
        return lastModStamp;
    }

    public void setLastModStamp(String lastModStamp) {
        this.lastModStamp = lastModStamp;
    }

    public LocalDateTime getDealBgnTime() {
        return dealBgnTime;
    }

    public void setDealBgnTime(LocalDateTime dealBgnTime) {
        this.dealBgnTime = dealBgnTime;
    }

    public Long getTmoutTimeNum() {
        return tmoutTimeNum;
    }

    public void setTmoutTimeNum(Long tmoutTimeNum) {
        this.tmoutTimeNum = tmoutTimeNum;
    }


    @Override
    public String toString() {
        return "BrProduct{" +
                "fundCode='" + fundCode + '\'' +
                ", ancmTpBclsCd='" + ancmTpBclsCd + '\'' +
                '}';
    }

}



