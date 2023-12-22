package com.aurine.cloudx.common.core.util;

import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;

/**
 * @description:实例相关工具类
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-14
 * @Copyright:
 */
public class InstanceUtil {

    /**
     * 实例化泛型对象
     *
     * @param t
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SneakyThrows
    public static <T> T newInstance(Class<T> t) {
        ParameterizedType ptype = (ParameterizedType) t.getGenericSuperclass();
        Class clazz = (Class<T>) ptype.getActualTypeArguments()[0];
        return (T) clazz.newInstance();
    }
}
