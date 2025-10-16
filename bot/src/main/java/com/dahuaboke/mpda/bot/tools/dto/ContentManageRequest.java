package com.dahuaboke.mpda.bot.tools.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * auth: zhangshuhan
 * time: 2025/9/25 17:06
 */
public class ContentManageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    // 系统头信息
    private SysHead sysHead;

    // 业务信息
    private Busi busi;

    // 网络类型 1为金融网，2为综合网
    private String location;

    // 行政区代码
    private String areaCode;

    public ContentManageRequest() {
    }

    public ContentManageRequest(SysHead sysHead, Busi busi, String location, String areaCode) {
        this.sysHead = sysHead;
        this.busi = busi;
        this.location = location;
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "ContentManageRequest{" +
                "sysHead=" + sysHead +
                ", busi=" + busi +
                ", location='" + location + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }

    // Getters and Setters
    public SysHead getSysHead() {
        return sysHead;
    }

    public void setSysHead(SysHead sysHead) {
        this.sysHead = sysHead;
    }

    public Busi getBusi() {
        return busi;
    }

    public void setBusi(Busi busi) {
        this.busi = busi;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public static class SysHead {

        // 操作主系统号（11位主系统号，必填）
        private String opeSys;

        // 被操作主系统号（11位主系统号，必填）
        private String byOpeSys;

        // 操作类型（6位交易码，必填）
        private String opeType;

        // 操作员（1—100位，必填）
        private String opeUser;

        // 操作机构（操作员所在机构，1—32位，必填）
        private String opeOrg;

        // 操作时间（YYYYMMDD 24hhmmss，14位，必填）
        private String opeTime;

        public SysHead() {
        }

        public SysHead(String opeSys, String byOpeSys, String opeType, String opeUser, String opeOrg, String opeTime) {
            this.opeSys = opeSys;
            this.byOpeSys = byOpeSys;
            this.opeType = opeType;
            this.opeUser = opeUser;
            this.opeOrg = opeOrg;
            this.opeTime = opeTime;
        }

        @Override
        public String toString() {
            return "SysHead{" +
                    "opeSys='" + opeSys + '\'' +
                    ", byOpeSys='" + byOpeSys + '\'' +
                    ", opeType='" + opeType + '\'' +
                    ", opeUser='" + opeUser + '\'' +
                    ", opeOrg='" + opeOrg + '\'' +
                    ", opeTime=" + opeTime +
                    '}';
        }

        // Getters and Setters
        public String getOpeSys() {
            return opeSys;
        }

        public void setOpeSys(String opeSys) {
            this.opeSys = opeSys;
        }

        public String getByOpeSys() {
            return byOpeSys;
        }

        public void setByOpeSys(String byOpeSys) {
            this.byOpeSys = byOpeSys;
        }

        public String getOpeType() {
            return opeType;
        }

        public void setOpeType(String opeType) {
            this.opeType = opeType;
        }

        public String getOpeUser() {
            return opeUser;
        }

        public void setOpeUser(String opeUser) {
            this.opeUser = opeUser;
        }

        public String getOpeOrg() {
            return opeOrg;
        }

        public void setOpeOrg(String opeOrg) {
            this.opeOrg = opeOrg;
        }

        public String getOpeTime() {
            return opeTime;
        }

        public void setOpeTime(String opeTime) {
            this.opeTime = opeTime;
        }

    }

    public static class Busi {

        // 文件索引号（12位，必填）
        private String fileId;

        // 页码（1-4 必填）
        private String pageIndex;

        public Busi() {
        }

        public Busi(String fileId, String pageIndex) {
            this.fileId = fileId;
            this.pageIndex = pageIndex;
        }

        @Override
        public String toString() {
            return "Busi{" +
                    "fileId='" + fileId + '\'' +
                    ", pageIndex=" + pageIndex +
                    '}';
        }

        // Getters and Setters
        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(String pageIndex) {
            this.pageIndex = pageIndex;
        }

    }


}
