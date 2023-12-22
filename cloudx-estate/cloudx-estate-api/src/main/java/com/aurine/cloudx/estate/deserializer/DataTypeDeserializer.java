package com.aurine.cloudx.estate.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * <p>
 * 用于设备参数，如果参数数据类型是decimal类型，则前端组件使用的是String数据类型（这里使用正则表达式限制小数位）
 * </p>
 * @author : 王良俊
 * @date : 2021-07-26 14:18:13
 */
public class DataTypeDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dataType = jsonParser.getText();
        switch (dataType) {
            case "decimal":
                return "string";
            default:
                return dataType;
        }
    }
}
