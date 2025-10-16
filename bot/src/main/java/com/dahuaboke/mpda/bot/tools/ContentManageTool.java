package com.dahuaboke.mpda.bot.tools;

import com.alibaba.fastjson.JSON;
import com.cpecm.plugin.lc.entity.DownLoadFileResp;
import com.cpecm.plugin.lc.exception.CpecmPluginException;
import com.cpecm.plugin.lc.impl.DownloadClientImpl;
import com.cpecm.plugin.lc.interfase.CallBakI;
import com.dahuaboke.mpda.bot.tools.dto.ContentManageRequest;
import com.dahuaboke.mpda.bot.tools.dto.ContentManageResponse;
import com.post.impl.lc.CallBackImpl;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * auth: zhangshuhan
 * time: 2025/9/25 17:06
 */
@Component
public class ContentManageTool {

    private static final Logger log = LoggerFactory.getLogger(ContentManageTool.class);

    @Value("${contentManage.opeSysId}")
    private String opeSysId;

    @Value("${contentManage.byOpeSysId}")
    private String byOpeSysId;

    @Value("${contentManage.location}")
    private String location;

    @Value("${contentManage.areaCode}")
    private String areaCode;

    @Value("${contentManage.fileSavePath}")
    private String fileSavePath;

    @Value("${contentManage.opeType}")
    private String opeType;

    @Value("${contentManage.opeUser}")
    private String opeUser;

    @Value("${contentManage.opeOrg}")
    private String opeOrg;


    public ContentManageResponse downloadFilesTool(String inmngPlatfFileIndexNo, String pageCode) {
        ContentManageResponse contentManageResponse = new ContentManageResponse();
        if (StringUtils.isNotEmpty(inmngPlatfFileIndexNo) && StringUtils.isNotEmpty(pageCode)) {
            if (inmngPlatfFileIndexNo.contains(",") && pageCode.contains(",")) {
                contentManageResponse = downloadFileMore(inmngPlatfFileIndexNo, pageCode);
            } else {
                contentManageResponse = downloadFileOne(inmngPlatfFileIndexNo, pageCode);
            }
        } else {
            contentManageResponse.setDownloadSuccess(false);
        }
        return contentManageResponse;
    }

    public ContentManageResponse downloadFileOne(String inmngPlatfFileIndexNo, String pageCode) {
        ContentManageResponse contentManageResponse = new ContentManageResponse();
        boolean downloadSuccess = true;
        ContentManageRequest.SysHead sysHead = new ContentManageRequest.SysHead();
        sysHead.setOpeSys(opeSysId);
        sysHead.setByOpeSys(byOpeSysId);
        sysHead.setOpeType(opeType);
        sysHead.setOpeUser(opeUser);
        sysHead.setOpeOrg(opeOrg);
        sysHead.setOpeTime(formatCurrentTime());
        ContentManageRequest.Busi busi = new ContentManageRequest.Busi(inmngPlatfFileIndexNo, pageCode);
        ContentManageRequest contentManageRequest = new ContentManageRequest();
        contentManageRequest.setSysHead(sysHead);
        contentManageRequest.setBusi(busi);
        contentManageRequest.setLocation(location);
        contentManageRequest.setAreaCode(areaCode);
        DownloadClientImpl downloadClient = new DownloadClientImpl();
        //20250929 霍老师说明,此处需要生命callBakI,然后传入。不可在downloadAllTheImage传入null
        CallBakI callBakI = new CallBackImpl();
        try {
            DownLoadFileResp downLoadFileResp = downloadClient.downloadAllTheImage(JSON.toJSONString(contentManageRequest), callBakI, fileSavePath);
            if (!"0".equals(downLoadFileResp.getOpeResult())) {
                log.error("inmngPlatfFileIndexNo:{},pageCode:{},downloadFail,because:{}", inmngPlatfFileIndexNo, pageCode, downLoadFileResp.getOpeDesc());
                downloadSuccess = false;
            }
            if (downLoadFileResp.getFilePath() != null && downLoadFileResp.getFilePath().size() == 1) {
                contentManageResponse.setLocalFilePath(downLoadFileResp.getFilePath().get(0));
            } else {
                log.error("inmngPlatfFileIndexNo:{},pageCode:{},downloadFail,because:ContentManage did not return the file path or returned more file paths", inmngPlatfFileIndexNo, pageCode);
                downloadSuccess = false;
            }
        } catch (CpecmPluginException e) {
            log.error("inmngPlatfFileIndexNo:{},pageCode:{},downloadFail", inmngPlatfFileIndexNo, pageCode, e);
            downloadSuccess = false;
        } catch (Exception e) {
            log.error("inmngPlatfFileIndexNo:{},pageCode:{},downloadFail", inmngPlatfFileIndexNo, pageCode, e);
            downloadSuccess = false;
        }
        contentManageResponse.setDownloadSuccess(downloadSuccess);
        return contentManageResponse;
    }


    public ContentManageResponse downloadFileMore(String inmngPlatfFileIndexNo, String pageCode) {
        boolean downloadSuccess = false;
        ContentManageResponse contentManageResponse = new ContentManageResponse();
        List<String> filePathMore = new ArrayList<>();
        List<String> inmngPlatfFileIndexNoList = Arrays.asList(inmngPlatfFileIndexNo.split(","));
        List<String> pageCodeList = Arrays.asList(pageCode.split(","));
        if (CollectionUtils.isNotEmpty(inmngPlatfFileIndexNoList) && CollectionUtils.isNotEmpty(pageCodeList)
                && inmngPlatfFileIndexNoList.size() == pageCodeList.size()) {
            for (int i = 0; i < inmngPlatfFileIndexNoList.size(); i++) {
                filePathMore.add(downloadFileOne(inmngPlatfFileIndexNoList.get(i), pageCodeList.get(i)).getLocalFilePath());
            }
            try {
                contentManageResponse.setLocalFilePath(fileMerger(filePathMore));
                downloadSuccess = true;
            } catch (IOException e) {
                log.error("file index:{}, page size:{}, merge filed", inmngPlatfFileIndexNo, pageCode, e);
            }
        }
        contentManageResponse.setDownloadSuccess(downloadSuccess);
        return contentManageResponse;
    }

    public String formatCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public String fileMerger(List<String> localFilePathList) throws IOException {
        String path = "";
        PDFMergerUtility merger = new PDFMergerUtility();
        for (String filePath : localFilePathList) {
            if (StringUtils.isNotEmpty(filePath)) {
                File file = new File(filePath);
                if (file.exists()) {
                    merger.addSource(file);
                }
            }
        }
        merger.setDestinationFileName(path);
        merger.mergeDocuments(null);
        return path;
    }
}
