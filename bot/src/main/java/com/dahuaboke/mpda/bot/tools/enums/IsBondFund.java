package com.dahuaboke.mpda.bot.tools.enums;

public enum IsBondFund {

    BOND("0","债基"),

    NO_BOND("1","非债基");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    IsBondFund(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
