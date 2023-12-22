package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.serializer;


import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * <p>发布日期序列化</p>
 * @author : 王良俊
 * @date : 2021-09-10 14:14:17
 */
public class InfoReleaseTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(String.valueOf(value.toEpochSecond(ZoneOffset.of("+8"))));
    }
}
