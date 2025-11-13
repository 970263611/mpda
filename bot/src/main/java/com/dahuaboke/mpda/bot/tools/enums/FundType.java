package com.dahuaboke.mpda.bot.tools.enums;

public enum FundType {

    STOCK_FUND("1", "股票型基金"),
    MIXED_FUND("2", "混合型基金"),
    BOND_FUND("3", "债券型基金"),
    MONET_MARKET_FUND("4", "货币式基金"),
    QDII("5", "QDII"),
    COMMODITY_FUND("6", "商品型基金"),
    SHORT_TERM_FINANCIAL_BOND__FUND("7", "短期理财债券型基金"),
    INFRASTRUCTURE_FUND("8", "基础设施基金"),
    FOF("9", "基金中基金(FOF)");

    private final String code;

    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    FundType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FundType getFundTypeDesc(String code){
        for (FundType fundType : values()) {
            if (fundType.getCode().equals(code)) {
                return fundType;
            }
        }
        return BOND_FUND;
    }
}
