package com.dahuaboke.mpda.bot.tools.enums;

public enum ShelfStatus {
    OFF_SHELF("0","未上架"),

    ON_SHELF("1","已上架");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ShelfStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
