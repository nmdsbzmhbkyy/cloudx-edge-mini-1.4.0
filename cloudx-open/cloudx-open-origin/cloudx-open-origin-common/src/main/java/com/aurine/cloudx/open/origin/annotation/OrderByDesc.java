package com.aurine.cloudx.open.origin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 设备回调排序注解（只有Iot设备对象有用）
 * </p>
 * @author : 王良俊
 * @date : 2021-07-15 10:01:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD})
public @interface OrderByDesc {

    /**
     * 排序优先级 0 优先级比 1 高，优先级相同按照属性顺序
     * */
    int priority() default 0;

}
