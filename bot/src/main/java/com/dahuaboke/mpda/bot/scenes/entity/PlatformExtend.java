package com.dahuaboke.mpda.bot.scenes.entity;

import java.util.List;

public class PlatformExtend {

    /**
     * 购买链接标识
     */
    private boolean buyLink;

    /**
     * 下载链接标识
     */
    private boolean downloadLink;

    /**
     * 基金代码
     */
    private List<String> fundCode;

    /**
     * 债券基金类型
     */
    private String finBondType;

    /**
     * 时间范围
     */
    private String period;

    public boolean isBuyLink() {
        return buyLink;
    }

    public void setBuyLink(boolean buyLink) {
        this.buyLink = buyLink;
    }

    public boolean isDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(boolean downloadLink) {
        this.downloadLink = downloadLink;
    }

    public List<String> getFundCode() {
        return fundCode;
    }

    public void setFundCode(List<String> fundCode) {
        this.fundCode = fundCode;
    }

    public String getFinBondType() {
        return finBondType;
    }

    public void setFinBondType(String finBondType) {
        this.finBondType = finBondType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public PlatformExtend() {
    }

    public PlatformExtend(boolean buyLink, boolean downloadLink, List<String> fundCode, String finBondType, String period) {
        this.buyLink = buyLink;
        this.downloadLink = downloadLink;
        this.fundCode = fundCode;
        this.finBondType = finBondType;
        this.period = period;


    }

}
