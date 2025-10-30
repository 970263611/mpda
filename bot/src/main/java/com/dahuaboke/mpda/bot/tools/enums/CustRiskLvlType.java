package com.dahuaboke.mpda.bot.tools.enums;

public enum CustRiskLvlType {

    LOWRISK("PR1", "低风险"),
    LOWMEDIUMRISK("PR2", "中低风险"),
    MEDIUMRISK("PR3", "中风险"),

    MEDIUMHEIGHTRISK("PR4", "中高风险"),
    HEIGHTRISK("PR5", "高风险");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    CustRiskLvlType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
