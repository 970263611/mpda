package com.dahuaboke.mpda.bot.tools.enums;

public enum PdfExceptionType {
    FILE_LEVEL("0","文件级别错误"),

    QUESTION_LEVEL("1","问题级别错误");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    PdfExceptionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
