package com.dahuaboke.mpda.bot.tools.enums;

public enum FundInfoType {

    REPORT("0","季报"),

    SUMMARY("1","概要");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    FundInfoType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
