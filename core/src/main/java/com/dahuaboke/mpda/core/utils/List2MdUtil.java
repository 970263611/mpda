package com.dahuaboke.mpda.core.utils;

import java.util.List;

/**
 * @description: zhangjie da fw
 * @author: ZHANGSHUHAN
 * @date: 2025/12/01
 */
public class List2MdUtil {

    // 缩进空格数（Markdown 嵌套列表标准缩进）
    private static final int INDENT_SPACES = 4;

    /**
     * 1. 基础带编号列表（手动指定序号，按顺序渲染）
     *
     * @param list 原始列表
     * @return Markdown 格式字符串
     */
    public static String convert(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(i + 1).append(". ").append(list.get(i)).append("\n");
        }
        return sb.toString().trim(); // 去除末尾换行
    }

    /**
     * 2. 简化写法（所有项用 1. 开头，Markdown 自动排序）
     *
     * @param list 原始列表
     * @return Markdown 格式字符串
     */
    public static String toSimplifiedNumberedList(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("1. ").append(item).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * 3. 嵌套带编号列表（支持一级列表包含二级子列表）
     *
     * @param parentList 父列表项
     * @param childLists 子列表项（与父列表索引一一对应，无则传 null）
     * @return Markdown 格式字符串
     */
    public static String toNestedNumberedList(List<String> parentList, List<List<String>> childLists) {
        if (parentList == null || parentList.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parentList.size(); i++) {
            // 父列表项
            sb.append(i + 1).append(". ").append(parentList.get(i)).append("\n");
            // 子列表项（存在时添加）
            if (childLists != null && i < childLists.size() && childLists.get(i) != null) {
                List<String> childList = childLists.get(i);
                for (int j = 0; j < childList.size(); j++) {
                    // 子列表缩进 + 编号
                    sb.append(" ".repeat(INDENT_SPACES))
                            .append(j + 1).append(". ").append(childList.get(j)).append("\n");
                }
            }
        }
        return sb.toString().trim();
    }
}
