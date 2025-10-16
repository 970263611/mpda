package com.dahuaboke.mpda.bot.tools.enums;

public enum FileDealFlag {

    UNPROCESSED("0","未处理"),
    PROCESSED("1","已处理"),

    PROCESSING("2","处理中");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    FileDealFlag(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
