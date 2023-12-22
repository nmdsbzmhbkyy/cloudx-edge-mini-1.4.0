package com.aurine.cloudx.open.common.validate.validator;

import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串长度自定义校验器
 * 规则：如果字符串属性不为null时，trim之后进行长度校验（默认trim之后长度不能小于1）
 *
 * @author : Qiu
 * @date : 2022 01 16 14:34
 */

public class SizeCustomValidator implements ConstraintValidator<SizeCustom, String> {

    private SizeCustom sizeCustom;

    @Override
    public void initialize(SizeCustom constraintAnnotation) {
        this.sizeCustom = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        int length = value.trim().length();

        if (sizeCustom != null) {
            int min = sizeCustom.min();
            int max = sizeCustom.max();
            if (min > max) return false;

            // 如果min或者max有一项大于0，则配置生效
            if (min > 0 || max > 0) {
                // 如果min或者max小于等于0，则直接为true，否则进行校验
                return (min <= 0 || length > min) && (max <= 0 || length <= max);
            }
        }

        return length > 0;
    }
}
