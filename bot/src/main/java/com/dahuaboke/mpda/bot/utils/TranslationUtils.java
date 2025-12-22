package com.dahuaboke.mpda.bot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体类字段翻译工具（支持：List<T>/T/Map<String,Object> 三种入参重载）
 * 核心规则：
 * 1. 带FieldTranslation注解的字段：key替换为中文，按order排序（负数→0→正数）
 * 2. 无注解字段：保留英文key，按字母序后置
 * 3. Map入参要求：key为实体英文字段名，value为字段值
 */
public class TranslationUtils {

    private static final Logger log = LoggerFactory.getLogger(TranslationUtils.class);


    // ==================== 重载1：入参为List<T> ====================
    public static <T> List<Map<String, Object>> convertToChineseFieldMap(List<T> entityList) throws IllegalAccessException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (entityList == null || entityList.isEmpty()) {
            return resultList;
        }

        Class<?> clazz = entityList.get(0).getClass();
        List<Field> sortedFields = getSortedFields(clazz);

        for (T entity : entityList) {
            resultList.add(buildFieldMap(entity, sortedFields));
        }
        return resultList;
    }

    // ==================== 重载2：入参为单个实体T ====================
    public static <T> Map<String, Object> convertToChineseFieldMap(T entity) throws IllegalAccessException {
        if (entity == null) {
            return new LinkedHashMap<>();
        }
        Class<?> clazz = entity.getClass();
        List<Field> sortedFields = getSortedFields(clazz);
        return buildFieldMap(entity, sortedFields);
    }

    // ==================== 重载3：入参为Map<String, Object> + 实体Class ====================
    public static <T> Map<String, Object> convertToChineseFieldMap(Map<String, Object> sourceMap, Class<T> entityClass) {
        // 空值处理
        if (sourceMap == null || sourceMap.isEmpty()) {
            return new LinkedHashMap<>();
        }
        if (entityClass == null) {
            throw new IllegalArgumentException("实体Class不能为空");
        }

        // 1. 获取实体类排序后的字段列表（带注解+无注解）
        List<Field> sortedFields = getSortedFields(entityClass);
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // 2. 遍历排序后的字段，构建新Map
        for (Field field : sortedFields) {
            String englishFieldName = field.getName();
            // 从源Map中获取字段值（兼容key大小写？可选：sourceMap.containsKey(englishFieldName.toUpperCase())）
            if (sourceMap.containsKey(englishFieldName)) {
                Object value = sourceMap.get(englishFieldName);
                // 有注解：替换为中文key；无注解：保留英文key
                if (field.isAnnotationPresent(FieldTranslation.class)) {
                    FieldTranslation translation = field.getAnnotation(FieldTranslation.class);
                    resultMap.put(translation.value(), value);
                } else {
                    resultMap.put(englishFieldName, value);
                }
            }
        }

        // 3. 处理源Map中存在、但实体类无对应字段的key（可选：保留，按字母序追加到末尾）
        List<String> extraKeys = sourceMap.keySet().stream()
                .filter(key -> !resultMap.containsValue(sourceMap.get(key)) // 避免重复
                        && !getAllFieldNames(entityClass).contains(key))
                .sorted()
                .collect(Collectors.toList());
        for (String extraKey : extraKeys) {
            resultMap.put(extraKey, sourceMap.get(extraKey));
        }

        return resultMap;
    }

    /**
     * 重载4：入参为List<Map<String, Object>> + 实体Class（新增）
     * 将列表中每个Map的英文key转换为实体类注解定义的中文key
     */
    public static <T> List<Map<String, Object>> convertToChineseFieldMap(List<Map<String, Object>> sourceList, Class<T> entityClass) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (sourceList == null || sourceList.isEmpty()) {
            return resultList;
        }
        if (entityClass == null) {
            throw new IllegalArgumentException("实体Class不能为空");
        }

        // 遍历列表中每个Map，调用已有的Map转换方法进行处理
        for (Map<String, Object> sourceMap : sourceList) {
            resultList.add(convertToChineseFieldMap(sourceMap, entityClass));
        }
        return resultList;
    }

    // ==================== 通用工具方法 ====================

    /**
     * 构建单个实体的字段Map（复用逻辑）
     */
    private static <T> Map<String, Object> buildFieldMap(T entity, List<Field> sortedFields) throws IllegalAccessException {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        for (Field field : sortedFields) {
            String className = field.getDeclaringClass().getName();
            if (className.startsWith("java.") || className.startsWith("javax.")) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (field.isAnnotationPresent(FieldTranslation.class)) {
                    FieldTranslation translation = field.getAnnotation(FieldTranslation.class);
                    fieldMap.put(translation.value(), value);
                } else {
                    fieldMap.put(field.getName(), value);
                }
            } catch (InaccessibleObjectException e) {
                log.error("跳过无法访问的字段{}，报错信息{}", field.getName(), e.getMessage());
            }
        }
        return fieldMap;
    }

    /**
     * 获取类及其父类的所有字段，并按规则排序
     */
    private static List<Field> getSortedFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        // 拆分带注解/无注解字段并排序
        List<Field> annotatedFields = allFields.stream()
                .filter(field -> field.isAnnotationPresent(FieldTranslation.class))
                .sorted((f1, f2) -> {
                    int order1 = f1.getAnnotation(FieldTranslation.class).order();
                    int order2 = f2.getAnnotation(FieldTranslation.class).order();
                    return order1 != order2 ? Integer.compare(order1, order2) : f1.getName().compareTo(f2.getName());
                })
                .collect(Collectors.toList());

        List<Field> nonAnnotatedFields = allFields.stream()
                .filter(field -> !field.isAnnotationPresent(FieldTranslation.class))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());

        // 合并：带注解字段在前，无注解在后
        List<Field> sortedFields = new ArrayList<>();
        sortedFields.addAll(annotatedFields);
        sortedFields.addAll(nonAnnotatedFields);
        return sortedFields;
    }

    /**
     * 获取实体类所有字段名（含父类），用于Map入参的额外key校验
     */
    private static Set<String> getAllFieldNames(Class<?> clazz) {
        Set<String> fieldNames = new HashSet<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                fieldNames.add(field.getName());
            }
            clazz = clazz.getSuperclass();
        }
        return fieldNames;
    }
}