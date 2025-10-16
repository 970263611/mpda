package com.dahuaboke.mpda.client.entity.resp;

import java.util.List;
import java.util.Map;

/**
 * @Desc: 通用数据普通查询-C014005 返回体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014004Resp<R> extends BaseResp {

    List<R> content;

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private int  totalPage;

    public List<R> getContent() {
        return content;
    }

    public void setContent(List<R> content) {
        this.content = content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "C014004Resp{" +
                "content=" + content +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", totalPage=" + totalPage +
                '}';
    }

}
