package com.aurine.cloudx.open.common.core.annotation;

import com.aurine.cloudx.open.common.core.constant.enums.OpenFieldEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自动注入开关注解
 *
 * @author : Qiu
 * @date : 2022 01 17 20:21
 */

@Target({ METHOD })
@Retention(RUNTIME)
public @interface AutoInject {

    /**
     * 是否启用（默认为true）
     *
     * @return
     */
    boolean enable() default true;

    /**
     * 排除自动注入的数组
     * 输入：要排除的属性名称
     * 说明：排除自动注入是指不注入，该数组中存放的就是不需要注入的属性名称
     *
     * @return
     */
    OpenFieldEnum[] exclude() default { };
}
