package com.aurine.cloudx.estate.util.bean.convert;

/**
 * <p>String->int</p>
 * @author : 王良俊
 * @date : 2021-12-08 13:51:12
 */
public class StringToIntegerConvert extends ConvertHandler<String, Integer> {

    @Override
    public Integer convert(String s) {
        return Integer.valueOf(s.trim());
    }
}
