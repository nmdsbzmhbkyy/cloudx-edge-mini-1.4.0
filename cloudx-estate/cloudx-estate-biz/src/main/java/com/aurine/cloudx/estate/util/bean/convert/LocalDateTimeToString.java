package com.aurine.cloudx.estate.util.bean.convert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>日期->字符串</p>
 * @author : 王良俊
 * @date : 2021-12-08 14:03:26
 */
public class LocalDateTimeToString extends ConvertHandler<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String timeStr) {
        return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
