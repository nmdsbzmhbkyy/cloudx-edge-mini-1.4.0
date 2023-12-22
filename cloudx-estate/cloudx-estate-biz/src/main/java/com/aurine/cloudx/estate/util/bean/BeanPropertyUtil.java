package com.aurine.cloudx.estate.util.bean;

import com.aurine.cloudx.estate.util.bean.convert.ConvertHandler;
import com.aurine.cloudx.estate.util.bean.convert.NumberToStringConvert;
import com.aurine.cloudx.estate.util.bean.convert.StringToIntegerConvert;
import com.aurine.cloudx.estate.util.bean.convert.TimeStampToLocalDateTimeConvert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.springframework.beans.BeanUtils;

/**
 * <p>bean属性复制工具</p>
 *
 * @author : 王良俊
 * @date : 2021-11-29 11:10:38
 */
@Slf4j
public class BeanPropertyUtil {

    public static ConvertHandler<?, ?> DEFAULT_HANDLER = new ConvertHandler() {
        @Override
        public Object convert(Object o) {
            return o;
        }
    };

    /**
     * 数值转换成字符串
     */
    public static ConvertHandler<?, ?> NUMBER_TO_STRING = new NumberToStringConvert();

    /**
     * 字符串转换成整形
     */
    public static ConvertHandler<?, ?> STRING_TO_INTEGER = new StringToIntegerConvert();

    /**
     * 字符串转换成整形
     */
    public static ConvertHandler<?, ?> TIMESTAMP_TO_LOCAL_DATE_TIME = new TimeStampToLocalDateTimeConvert();

    /**
     * <p>复制参数到bean</p>
     *
     * @param source 源 Bean
     * @param target 目标 Bean
     */
    public static CopyProperty copyProperty(Object target, Object source) {
        BeanUtils.copyProperties(source, target);
        return new CopyProperty(target);
    }

    /**
     * <p>复制参数到bean</p>
     *
     * @param target       源 Bean
     * @param source       目标 Bean
     * @param fieldMapping 自定义属性名映射
     */
    public static void copyProperty(Object target, Object source, FieldMapping<?, ?> fieldMapping) {
        BeanMap sourceMap = BeanMap.create(source);
        BeanMap targetMap = BeanMap.create(target);
        fieldMapping.foreach(mapping -> {
            Object value = mapping.defaultValue;
            if (sourceMap.get(mapping.sourceField) != null) {
                value = mapping.convert(sourceMap.get(mapping.sourceField));
            }
            targetMap.put(mapping.getTargetField(), value);
        });
    }

    @AllArgsConstructor
    public static class CopyProperty {
        private Object target;

        public CopyProperty copyProperty(Object source) {
            BeanUtils.copyProperties(source, target);
            return this;
        }

    }
}
