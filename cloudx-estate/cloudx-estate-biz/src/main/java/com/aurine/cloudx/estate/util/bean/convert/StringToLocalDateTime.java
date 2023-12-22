package com.aurine.cloudx.estate.util.bean.convert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>字符串->日期</p>
 * @author : 王良俊
 * @date : 2021-12-08 14:03:26
 */
public class StringToLocalDateTime extends ConvertHandler<LocalDateTime, String> {

    @Override
    public String convert(LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);
    }

}
