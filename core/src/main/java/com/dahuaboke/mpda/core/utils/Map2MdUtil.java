package com.dahuaboke.mpda.core.utils;

import java.util.Collection;
import java.util.Map;

public class Map2MdUtil {

    /**
     * 将 Map 转换为 Markdown 列表字符串
     *
     * @param map 待转换的 Map
     * @return Markdown 格式字符串
     */
    public static String convert(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        processMap(map, sb, 1);
        return sb.toString().trim();
    }

    /**
     * 递归处理 Map 并构建 Markdown
     *
     * @param map   待处理的 Map
     * @param sb    字符串构建器
     * @param level 当前层级（用于生成不同级别的列表）
     */
    private static void processMap(Map<?, ?> map, StringBuilder sb, int level) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            // 添加当前层级的列表前缀
            appendListPrefix(sb, level);
            sb.append(key).append(": ");

            // 处理值的不同类型
            if (value instanceof Map<?, ?>) {
                // 嵌套 Map 换行后递归处理
                sb.append("\n");
                processMap((Map<?, ?>) value, sb, level + 1);
            } else if (value instanceof Collection<?>) {
                // 集合类型
                sb.append("\n");
                processCollection((Collection<?>) value, sb, level + 1);
            } else {
                // 基本类型和其他对象
                sb.append(value != null ? value.toString() : "null").append("\n");
            }
        }
    }

    /**
     * 处理集合类型
     *
     * @param collection 待处理的集合
     * @param sb         字符串构建器
     * @param level      当前层级
     */
    private static void processCollection(Collection<?> collection, StringBuilder sb, int level) {
        for (Object item : collection) {
            appendListPrefix(sb, level);
            if (item instanceof Map<?, ?>) {
                sb.append("\n");
                processMap((Map<?, ?>) item, sb, level + 1);
            } else if (item instanceof Collection<?>) {
                sb.append("\n");
                processCollection((Collection<?>) item, sb, level + 1);
            } else {
                sb.append(item != null ? item.toString() : "null").append("\n");
            }
        }
    }

    /**
     * 添加 Markdown 列表前缀（根据层级生成）
     *
     * @param sb    字符串构建器
     * @param level 层级（1 对应一级列表，2 对应二级列表等）
     */
    private static void appendListPrefix(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("  "); // 每级缩进两个空格
        }
        sb.append("- "); // 列表项前缀
    }
}