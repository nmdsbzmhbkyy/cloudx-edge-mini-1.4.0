package com.aurine.cloudx.edge.sync.common.componments.chain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 处理链注解
 * 使用该注解时，请继承 AbstractHandleChain
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17 9:00
 * @Copyright:
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Chain {

}

