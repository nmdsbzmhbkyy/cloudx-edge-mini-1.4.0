package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * <p>
 * Iot设备Date属性时间戳反序列化器
 * </p>
 * @author : 王良俊
 * @date : 2021-07-16 16:17:02
 */
public class DateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Instant.ofEpochMilli(Long.parseLong(jsonParser.getText())).atZone(ZoneOffset.ofHours(8)).toLocalDate();
    }
}
