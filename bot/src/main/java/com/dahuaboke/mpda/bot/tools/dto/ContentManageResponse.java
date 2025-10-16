package com.dahuaboke.mpda.bot.tools.dto;

import java.io.Serializable;

/**
 * auth: zhangshuhan
 * time: 2025/9/25 17:06
 */
public class ContentManageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    // 本地文件路径
    private String localFilePath;

    // 下载成功失败
    private boolean downloadSuccess;

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public boolean isDownloadSuccess() {
        return downloadSuccess;
    }

    public void setDownloadSuccess(boolean downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
    }

    public ContentManageResponse() {
    }

    public ContentManageResponse(String localFilePath, boolean downloadSuccess) {
        this.localFilePath = localFilePath;
        this.downloadSuccess = downloadSuccess;
    }

}
