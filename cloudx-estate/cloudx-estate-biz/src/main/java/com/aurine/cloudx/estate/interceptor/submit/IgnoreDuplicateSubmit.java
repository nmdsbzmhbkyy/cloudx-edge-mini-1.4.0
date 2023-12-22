package com.aurine.cloudx.estate.interceptor.submit;

import java.lang.annotation.*;

/**
 * @description:
 * 使用该注解的方法将忽略重复提交校验
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-02-19
 * @Copyright:
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreDuplicateSubmit {

}

