package com.aurine.cloudx.open.common.core.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 反射工具类
 *
 * @author : Qiu
 * @date : 2022 01 20 22:02
 */

public class ReflectUtils {

    // 属性列表map
    private static final Map<Class, List<Field>> fieldListMap = new HashMap<>();

    /**
     * 根据属性名称查询并且设置属性值
     *
     * @param object    对象
     * @param fieldName 属性名称
     * @param value     值
     * @return
     */
    public static Boolean findAndSetFieldByName(Object object, String fieldName, Object value) {
        if (object == null || StringUtils.isEmpty(fieldName) || value == null) return false;

        Field appIdField = findFieldByName(object, fieldName);
        if (appIdField != null) {
            appIdField.setAccessible(true);
            try {
                appIdField.set(object, value);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 根据属性名称查找属性
     *
     * @param object    目标对象
     * @param fieldName 属性名称
     * @return
     */
    public static Field findFieldByName(Object object, String fieldName) {
        if (object == null || StringUtils.isEmpty(fieldName)) return null;

        if (fieldListMap.containsKey(object.getClass())) {
            List<Field> fieldList = fieldListMap.get(object.getClass());
            return fieldList.stream().filter(field -> field.getName().equals(fieldName)).findFirst().orElse(null);
        }

        List<Field> fieldList = Arrays.asList(getAllFields(object));
        fieldListMap.put(object.getClass(), fieldList);

        return findFieldByName(object, fieldName);
    }

    /**
     * 获取所有属性的方法，包括父类
     *
     * @param object 目标对象
     * @return
     */
    private static Field[] getAllFields(Object object) {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
