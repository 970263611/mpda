package com.dahuaboke.mpda.client.entity.resp;

import java.util.List;

/**
 * @Desc: 根据文档id进行批量删除-C014008 返回体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014008Resp extends BaseResp {

    /**
     * 删除失败的文档id
     */
    private List<String> failedDelIdList;

    /**
     * 删除成功的记录数
     */
    private long successTotalCount;

    public List<String> getFailedDelIdList() {
        return failedDelIdList;
    }

    public void setFailedDelIdList(List<String> failedDelIdList) {
        this.failedDelIdList = failedDelIdList;
    }

    public long getSuccessTotalCount() {
        return successTotalCount;
    }

    public void setSuccessTotalCount(long successTotalCount) {
        this.successTotalCount = successTotalCount;
    }

    @Override
    public String toString() {
        return "C014008Resp{" +
                "failedDelIdList=" + failedDelIdList +
                ", successTotalCount=" + successTotalCount +
                '}';
    }

}
