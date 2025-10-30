package com.dahuaboke.mpda.bot.tools.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import javax.persistence.Table;

/**
 * @description: 节假日表
 * @author: ZHANGSHUHAN
 * @date: 2025/10/29
 */
@Table(name = "fd_holiday")
public class FdHoliday {
    //日期||日期
    @TableField(value = "thedate")
    private String thedate;

    //基金工作日标志||0-工作日 1-节假日
    @TableField(value = "fundworkday")
    private String fundworkday;

    //理财(银行间)工作日标志||0-工作日 1-节假日
    @TableField(value = "financeworkday")
    private String financeworkday;

    //理财(证券)工作日标志||0-工作日 1-节假日
    @TableField(value = "agentworkday")
    private String agentworkday;

    //凭证国债工作日标志||0-工作日 1-节假日
    @TableField(value = "cdworkday")
    private String cdworkday;

    //储蓄国债工作日标志||0-工作日 1-节假日
    @TableField(value = "sdworkday")
    private String sdworkday;

    @TableField(value = "last_mod_stamp")
    private String lastModStamp;

    public FdHoliday(String thedate, String fundworkday, String financeworkday, String agentworkday, String cdworkday, String sdworkday, String lastModStamp) {
        this.thedate = thedate;
        this.fundworkday = fundworkday;
        this.financeworkday = financeworkday;
        this.agentworkday = agentworkday;
        this.cdworkday = cdworkday;
        this.sdworkday = sdworkday;
        this.lastModStamp = lastModStamp;
    }

    public FdHoliday() {
    }

    public String getThedate() {
        return thedate;
    }

    public void setThedate(String thedate) {
        this.thedate = thedate;
    }

    public String getFundworkday() {
        return fundworkday;
    }

    public void setFundworkday(String fundworkday) {
        this.fundworkday = fundworkday;
    }

    public String getFinanceworkday() {
        return financeworkday;
    }

    public void setFinanceworkday(String financeworkday) {
        this.financeworkday = financeworkday;
    }

    public String getAgentworkday() {
        return agentworkday;
    }

    public void setAgentworkday(String agentworkday) {
        this.agentworkday = agentworkday;
    }

    public String getCdworkday() {
        return cdworkday;
    }

    public void setCdworkday(String cdworkday) {
        this.cdworkday = cdworkday;
    }

    public String getSdworkday() {
        return sdworkday;
    }

    public void setSdworkday(String sdworkday) {
        this.sdworkday = sdworkday;
    }

    public String getLastModStamp() {
        return lastModStamp;
    }

    public void setLastModStamp(String lastModStamp) {
        this.lastModStamp = lastModStamp;
    }
}
