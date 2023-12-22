package com.aurine.cloudx.open.common.validate.annotation;

import com.aurine.cloudx.open.common.validate.validator.SizeCustomValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符串长度自定义校验注解
 * 规则：如果字符串属性不为null时，trim之后进行长度校验（默认trim之后长度不能小于1）
 *
 * @author : Qiu
 * @date : 2022 01 16 14:26
 */

@Documented
@Constraint(validatedBy = {SizeCustomValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface SizeCustom {

    String message() default "长度校验不通过";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 校验规则：大于不等于（length > min）
     * 注：小于或等于0的时候，校验时判断大于0
     *
     * @return
     */
    int min() default 0;

    /**
     * 校验规则：小于或等于（length <= max）
     * 注：小于或等于0的时候，不校验max
     *
     * @return
     */
    int max() default 0;
}
