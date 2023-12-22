package com.aurine.cloudx.common.data.aspect;

import com.aurine.cloudx.common.data.annotation.Desensitization;
import com.aurine.cloudx.common.data.enums.DesensitizationTypeEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.pig4cloud.pigx.common.core.util.DesensitizedUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

import static cn.hutool.core.util.StrUtil.replace;

/**
 * @description 自定义脱敏注解切面,序列化时进行脱敏
 * @author 顾文豪
 * @time 2023/11/13 10:56
 */
@NoArgsConstructor
@AllArgsConstructor
public class DesensitizationSerialize extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizationTypeEnum type;

    private Integer startInclude;

    private Integer endExclude;
    @Override
    public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (type) {
            // 自定义类型脱敏
            case CUSTOMER:
                jsonGenerator.writeString(hide(str,startInclude,endExclude));
                break;
            // 中文姓名脱敏
            case CHINESE_NAME:
                jsonGenerator.writeString(DesensitizedUtils.chineseName(String.valueOf(str)));
                break;
            // 身份证脱敏
            case ID_CARD:
                jsonGenerator.writeString(DesensitizedUtils.idCardNum(String.valueOf(str)));
                break;
            // 固定电话脱敏
            case FIXED_PHONE:
                jsonGenerator.writeString(DesensitizedUtils.fixedPhone(String.valueOf(str)));
                break;
            // 手机号脱敏
            case MOBILE_PHONE:
                jsonGenerator.writeString(DesensitizedUtils.mobilePhone(String.valueOf(str)));
                break;
            // 地址脱敏
            case ADDRESS:
                jsonGenerator.writeString(DesensitizedUtils.address(String.valueOf(str)));
                break;
            // 邮箱脱敏
            case EMAIL:
                jsonGenerator.writeString(DesensitizedUtils.email(String.valueOf(str)));
                break;
            // 密码脱敏
            case PASSWORD:
                jsonGenerator.writeString(DesensitizedUtils.password(String.valueOf(str)));
                break;
            // 银行卡脱敏
            case BANK_CARD:
                jsonGenerator.writeString(DesensitizedUtils.bankCard(String.valueOf(str)));
                break;
            default:
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            // 判断数据类型是否为String类型
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 获取定义的注解
                Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
                // 为null
                if (desensitization == null) {
                    desensitization = beanProperty.getContextAnnotation(Desensitization.class);
                }
                // 不为null
                if (desensitization != null) {
                    // 创建定义的序列化类的实例并且返回，入参为注解定义的type,开始位置，结束位置。
                    return new DesensitizationSerialize(desensitization.type(), desensitization.startInclude(),
                            desensitization.endExclude());
                }
            }

            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
    public static String hide(CharSequence str, int startInclude, int endExclude) {
        return replace(str, startInclude, endExclude, '*');
    }
}

