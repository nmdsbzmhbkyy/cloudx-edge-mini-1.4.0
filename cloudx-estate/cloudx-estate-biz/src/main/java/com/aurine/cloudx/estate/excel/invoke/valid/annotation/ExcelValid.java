package com.aurine.cloudx.estate.excel.invoke.valid.annotation;

import com.aurine.cloudx.estate.excel.invoke.valid.ExcelCustomizeValid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>用来进行一些基本的校验</p>
 * @author : 王良俊
 * @date : 2021-09-10 16:12:19
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelValid {

    /**
     * 列名
     */
    String fieldName();

    /**
     * 是否必填
     */
    boolean required() default false;

    /**
     * 是否必填（根据公安系统是否启用判断）
     */
    boolean requiredPolice() default false;

    /**
     * 最小长度（最小长度不为0不一定代表必填）
     */
    int minLength() default 0;

    /**
     * 最大长度
     */
    int maxLength() default 0;

    /**
     * 正则表达式
     */
    String regex() default "";

    /**
     * 正则表达式校验失败提示内容
     */
    String regexTip() default "";

    /**
     * 自定义校验类
     */
    Class<? extends ExcelCustomizeValid> using() default ExcelCustomizeValid.None.class;


}
