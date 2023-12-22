package com.aurine.cloudx.open.origin.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * <p>
 * 布尔值 true -> "1" false -> "0"
 * </p>
 * @author : 王良俊
 * @date : 2021-07-26 13:57:38
 */
public class BooleanToStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return "true".equals(jsonParser.getText()) ? "1" : "0";
    }
}
