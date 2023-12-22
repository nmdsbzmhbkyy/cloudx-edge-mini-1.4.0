package com.aurine.cloudx.common.core.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName BeanUtil
 * @Description BeanUtils增强类
 * @USER cyw
 * @Date 2023年05月27日 0027
 **/
public class BeanUtil extends BeanUtils {
    /**
     * 获取对象非空字段
     *
     * @param source
     * @return
     */
    public static String[] getNullFieldNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * @return java.lang.String[]
     * @throws //
     * @description 获取字段名相同的空值字段名
     * @author cyw
     * @time 2023/6/9 15:14
     */
    public static String[] compareNullPropertyNames(Object source, Class<?> targetClass) {
        String[] nullPropertyNames = getNullFieldNames(source);
        Set<String> comparedNames = new HashSet<String>();
        List<String> targetFieldNames = Stream.of(targetClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

        for (String name : nullPropertyNames) {
            if (targetFieldNames.contains(name)) {
                comparedNames.add(name);
            }
        }
        String[] result = new String[comparedNames.size()];
        return comparedNames.toArray(result);
    }

    /**
     * @return java.lang.String[]
     * @throws //
     * @description 获取属性名
     * @author cyw
     * @time 2023/6/9 15:14
     */
    public static String[] getPropertyNames(Class<?> clazz) {
        List<String> fieldNames = Stream.of(clazz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        return fieldNames.toArray(new String[fieldNames.size()]);
    }


    /**
     * 将非空字段进行赋值
     *
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, compareNullPropertyNames(src, target.getClass()));
    }

    /**
     * 将类内的某字段以外的字段进行复制
     *
     * @param src
     * @param target
     * @param clazz
     */
    public static void copyPropertiesIgnore(Object src, Object target, Class<?> clazz) {
        BeanUtils.copyProperties(src, target, getPropertyNames(clazz));
    }

    /**
     * @param o
     * @param clazz
     * @return T
     * @throws //
     * @description 根据类类型生成并赋值指定对象
     * @author cyw
     * @time 2023/6/12 9:17
     */
    public static <T> T copyProperties(Object o, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T t = clazz.newInstance();
        copyProperties(o, t);
        return t;
    }
}
