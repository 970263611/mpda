package com.dahuaboke.mpda.bot.tools.enums;

import java.util.List;

public enum TimeType {

    NONE("0", "无"),
    LAST_WEEK("1", "近1周"),
    LAST_MONTH("2", "近1月"),
    LAST_3_MONTH("3", "近3月"),
    LAST_YEAR("4", "近一年"),
    CURRENT_YEAR_Q1("5", "第1季度"),
    CURRENT_YEAR_Q2("6", "第2季度"),
    CURRENT_YEAR_Q3("7", "第3季度"),
    CURRENT_YEAR_Q4("8", "第4季度"),
    CURRENT_YEAR("9", "今年");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    TimeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TimeType getTimeType(String desc) {
        for (TimeType timeType : values()) {
            if (timeType.getDesc().equals(desc)) {
                return timeType;
            }
        }
        return NONE;
    }

}
