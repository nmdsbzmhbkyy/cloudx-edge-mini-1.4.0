package com.aurine.cloudx.open.common.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

/**
 * <p>
 * 获取ObjectMapper对象
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-07-30 15:27:07
 */
public class ObjectMapperUtil {

    public static ObjectMapper instance() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    /**
     * <p>如果字符串是null值，在序列化成json时转换成“”</p>
     */
    public static ObjectMapper instanceNullToEmptyStr() {
        ObjectMapper instance = ObjectMapperUtil.instance();
        instance.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("");
            }
        });
        return instance;
    }
}
