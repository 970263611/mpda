package com.dahuaboke.mpda.bot.tools.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import javax.persistence.Table;

/**
 * 基金产品信息实体类
 */
@Table(name = "br_netvalue")
public class  BrNetvalue{

    @TableField(value = "fund_code")
    private String fundCode;

    @TableField(value = "net_val_date")
    private String netValDate;

    @TableField(value = "unit_net_val")
    private String unitNetVal;

    @TableField(value = "indb_date")
    private String indbDate;

    @TableField(value = "thou_cop_fund_unit_profit")
    private String thouCopFundUnitProfit;

    @TableField(value = "stgy_d7_yearly_profrat")
    private String stgyD7YearlyProfrat;

    @TableField(value = "last_mod_stamp")
    private String lastModStamp;

    public BrNetvalue(String fundCode, String netValDate, String unitNetVal, String indbDate, String thouCopFundUnitProfit, String stgyD7YearlyProfrat, String lastModStamp) {
        this.fundCode = fundCode;
        this.netValDate = netValDate;
        this.unitNetVal = unitNetVal;
        this.indbDate = indbDate;
        this.thouCopFundUnitProfit = thouCopFundUnitProfit;
        this.stgyD7YearlyProfrat = stgyD7YearlyProfrat;
        this.lastModStamp = lastModStamp;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getNetValDate() {
        return netValDate;
    }

    public void setNetValDate(String netValDate) {
        this.netValDate = netValDate;
    }

    public String getUnitNetVal() {
        return unitNetVal;
    }

    public void setUnitNetVal(String unitNetVal) {
        this.unitNetVal = unitNetVal;
    }

    public String getIndbDate() {
        return indbDate;
    }

    public void setIndbDate(String indbDate) {
        this.indbDate = indbDate;
    }

    public String getThouCopFundUnitProfit() {
        return thouCopFundUnitProfit;
    }

    public void setThouCopFundUnitProfit(String thouCopFundUnitProfit) {
        this.thouCopFundUnitProfit = thouCopFundUnitProfit;
    }

    public String getStgyD7YearlyProfrat() {
        return stgyD7YearlyProfrat;
    }

    public void setStgyD7YearlyProfrat(String stgyD7YearlyProfrat) {
        this.stgyD7YearlyProfrat = stgyD7YearlyProfrat;
    }

    public String getLastModStamp() {
        return lastModStamp;
    }

    public void setLastModStamp(String lastModStamp) {
        this.lastModStamp = lastModStamp;
    }

}
