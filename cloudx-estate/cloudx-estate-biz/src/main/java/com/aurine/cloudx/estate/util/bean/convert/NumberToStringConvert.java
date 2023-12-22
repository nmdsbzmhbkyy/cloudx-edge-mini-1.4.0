package com.aurine.cloudx.estate.util.bean.convert;

/**
 * <p>int->String</p>
 * @author : 王良俊
 * @date : 2021-12-08 13:50:32
 */
public class NumberToStringConvert extends ConvertHandler<Number, String> {

    @Override
    public String convert(Number value) {
        return String.valueOf(value);
    }

}
