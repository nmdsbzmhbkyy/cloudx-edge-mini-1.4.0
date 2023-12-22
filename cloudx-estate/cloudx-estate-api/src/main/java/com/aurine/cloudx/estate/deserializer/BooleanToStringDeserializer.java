package com.aurine.cloudx.estate.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

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
