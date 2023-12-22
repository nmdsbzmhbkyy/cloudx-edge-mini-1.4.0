package com.aurine.cloudx.open.biz.interceptor.submit;

import java.lang.annotation.*;

/**
 * @description:
 * 使用该注解的方法禁止重复提交
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-02-19
 * @Copyright:
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DuplicateSubmit {

}

