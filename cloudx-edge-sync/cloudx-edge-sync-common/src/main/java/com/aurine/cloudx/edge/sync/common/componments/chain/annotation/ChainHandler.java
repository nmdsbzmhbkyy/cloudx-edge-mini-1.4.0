package com.aurine.cloudx.edge.sync.common.componments.chain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 使用该注解的方法禁止重复提交
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-02-19
 * @Copyright:
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChainHandler {

    /**
     * 所属处理链类型
     *
     * @return
     */
    Class chainClass();

}

