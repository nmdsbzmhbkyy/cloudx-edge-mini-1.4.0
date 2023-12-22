package com.aurine.cloudx.estate.util.bean.convert;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * <p>时间戳->LocalDateTime</p>
 * @author : 王良俊
 * @date : 2021-12-08 14:03:26
 */
public class TimeStampToLocalDateTimeConvert extends ConvertHandler<Long, LocalDateTime> {

    @Override
    public LocalDateTime convert(Long timestamp) {
        if (timestamp != null) {
            String s = timestamp.toString();
            if(s.length() > 10) {
                timestamp = Long.valueOf(s.substring(0, 10));
            }
            return LocalDateTime.ofEpochSecond(timestamp,0, ZoneOffset.ofHours(8));
        }
        return null;
    }
}
