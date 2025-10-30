package com.dahuaboke.mpda.bot.tools.enums;

public enum BondFundType {

    NONE("0","无"),
    CREDIT_BOND_INDEX("1","信用债-指数型"),
    CREDIT_BOND_ACTIVE_OPEN("2","信用债主动-开放式"),
    RATE_BOND_ACTIVE_OPEN("3","利率债主动-开放式"),
    RATE_BOND_INDEX_1_3Y("4","利率债指数1-3年"),
    RATE_BOND_INDEX_3_5Y("5","利率债指数3-5年"),
    RATE_BOND_INDEX_1_5Y("6","利率债指数1-5年");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    BondFundType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BondFundType getBondFundType(String desc){
        for (BondFundType bondFundType : values()) {
            if (bondFundType.getDesc().equals(desc)) {
                return bondFundType;
            }
        }
        return NONE;
    }
    public static BondFundType getBondFundTypeDesc(String code){
        for (BondFundType bondFundType : values()) {
            if (bondFundType.getCode().equals(code)) {
                return bondFundType;
            }
        }
        return NONE;
    }
}
