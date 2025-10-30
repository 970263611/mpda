package com.dahuaboke.mpda.bot.tools.dto;

/**
 * @description: 基金净值
 * @author: ZHANGSHUHAN
 * @date: 2025/10/23
 */
public class BrNetvalueDto {
    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 净值
     **/
    private String unitNetVal;


    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getUnitNetVal() {
        return unitNetVal;
    }

    public void setUnitNetVal(String unitNetVal) {
        this.unitNetVal = unitNetVal;
    }
}
