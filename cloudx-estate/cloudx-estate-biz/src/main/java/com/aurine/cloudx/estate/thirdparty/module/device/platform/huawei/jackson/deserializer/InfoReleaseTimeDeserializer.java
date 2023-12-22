package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * <p>反序列化发布时间</p>
 * @author : 王良俊
 * @date : 2021-09-10 14:25:42
 */
public class InfoReleaseTimeDeserializer extends JsonDeserializer<LocalDateTime> {


    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(Integer.parseInt(jsonParser.getText())), ZoneId.systemDefault());
    }
}
