package com.aurine.cloudx.estate.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Joiner;

public class ObjectPaseUtils {

    private static Logger logger = LoggerFactory.getLogger(ObjectPaseUtils.class);

    /**
     * @desc 将对象转换成指定String
     * @param <T>
     * @param t
     * @return
     */
    public static <T> String objectToStr(T t) {
        List<String> list = new ArrayList<String>();
        String[] fieldNames = getFiledName(t);
        for (int i = 0; i < fieldNames.length; i++) {
            String name = fieldNames[i];
            Object value = getFieldValueByName(name, t);
            if (null != value) {
                if (getFiledType(name, t).equals(Date.class)) {
                    value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)value);
                }
                list.add(name + " : " + value);
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            return Joiner.on("\r").skipNulls().join(list);
        }
        return null;
    }

    /**
     * @desc 获取属性名数组
     * @param o
     * @return
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * @desc 根据属性名获取属性值
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            logger.error("获取属性值失败！" + e, e);
        }
        return null;
    }

    /**
     * @desc 获取属性的数据类型
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getFiledType(String fieldName, Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Objects.equals(fieldName, field.getName())) {
                return field.getType();
            }
        }
        return null;
    }

}
