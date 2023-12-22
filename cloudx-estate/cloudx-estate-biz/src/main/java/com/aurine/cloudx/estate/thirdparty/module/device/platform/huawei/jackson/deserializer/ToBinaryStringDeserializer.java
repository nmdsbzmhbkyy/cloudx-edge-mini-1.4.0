package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * <p>
 *  反序列化时十进制转二进制（没用到）
 * </p>
 * @author : 王良俊
 * @date : 2021-07-19 14:37:04
 */
public class ToBinaryStringDeserializer extends JsonDeserializer<String> {


    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Integer.toBinaryString(Integer.parseInt(jsonParser.getText()));
    }
}
