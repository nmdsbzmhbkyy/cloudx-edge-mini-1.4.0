package com.aurine.cloudx.estate.thirdparty.business.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于接口模式的第三方业务切面注解，用于标记需要进行第三方业务水平扩展的方法
 * 被标记的方法根据系统配置，如果存在第三方对接信息则调用第三方的业务实现。否则继续执行原有方法。
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-11
 * @Copyright:
 */

@Retention(RetentionPolicy.RUNTIME)//运行时有效
@Target(ElementType.METHOD)//作用于方法
public @interface ThirdPartyBusinessApi {
    /**
     * 如果存在多接口实现，定义需要扩展实现的接口index
     * 默认index = 0
     *
     * @return
     */
    int interfaceIndex() default 0;
}
