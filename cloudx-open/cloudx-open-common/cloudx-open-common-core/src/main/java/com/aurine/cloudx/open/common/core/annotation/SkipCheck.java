package com.aurine.cloudx.open.common.core.annotation;

import com.aurine.cloudx.open.common.core.constant.enums.OpenFieldEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 跳过检测开关注解
 *
 * @author : Qiu
 * @date : 2022 01 17 20:21
 */

@Target({ METHOD })
@Retention(RUNTIME)
public @interface SkipCheck {

    /**
     * 是否启用
     *
     * @return
     */
    boolean enable() default true;

    /**
     * 排除跳过检测的数组
     * 输入：要排除的属性名称
     * 说明：排除跳过检测是指需要检测，该数组中存放的就是需要检测的属性名称
     *
     * @return
     */
    OpenFieldEnum[] exclude() default { };
}
