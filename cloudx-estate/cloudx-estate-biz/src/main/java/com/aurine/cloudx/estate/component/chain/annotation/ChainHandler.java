package com.aurine.cloudx.estate.component.chain.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

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

