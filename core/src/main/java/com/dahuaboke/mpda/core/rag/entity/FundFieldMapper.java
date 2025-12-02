package com.dahuaboke.mpda.core.rag.entity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @Desc: 字段映射工具（基于注解）
 * @Author：zhh
 * @Date：2025/8/7 14:13
 */
public class FundFieldMapper {
    private final Map<String, Field> commentToFieldMap = new HashMap<>();
    private final Map<String, String> questionKeyWordMap = new HashMap<>();

    public FundFieldMapper(Class<?> clazz) {
        // 初始化时构建注释到字段的映射
        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(FieldComment.class))
                .forEach(f -> {
                    String question = f.getAnnotation(FieldComment.class).question();
                    String keyWord = f.getAnnotation(FieldComment.class).keyWord();
                    f.setAccessible(true);
                    commentToFieldMap.put(question, f);
                    questionKeyWordMap.put(question, keyWord);
                });
    }

    public FundFieldMapper() {
    }

    /**
     * 根据注释设置字段值
     *
     * @param target  目标对象
     * @param comment 字段注释
     * @param value   要设置的值
     */
    public void setFieldByComment(Object target, String comment, String value) throws Exception {
        Field field = commentToFieldMap.get(comment);
        if (field != null) {
            field.set(target, value);
        }
    }

    /**
     * 批量设置字段值
     *
     * @param target   目标对象
     * @param fieldMap 字段注释到值的映射
     */
    public void setFieldsByComments(Object target, Map<String, String> fieldMap) throws Exception {
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            setFieldByComment(target, entry.getKey(), entry.getValue());
        }
    }

    public Map<String, String> getQuestionKeyWordMap() {
        return questionKeyWordMap;
    }

    public Map<String,String> getQuestionByName (Class<?> clazz) {
        Map<String,String> map = new HashMap<>();
        // 初始化时构建注释到字段的映射
        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(FieldComment.class))
                .forEach(f -> {
                    String question = f.getAnnotation(FieldComment.class).question();
                    String name = f.getName();
                    f.setAccessible(true);
                    map.put(name, question);
                });
        return map;
    }
    
}
