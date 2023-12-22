package com.aurine.cloudx.estate.util.bean.convert;

/**
 * <p>属性复制转换类</p>
 * @author : 王良俊
 * @date : 2021-12-08 13:49:15
 */
public abstract class ConvertHandler<S, T> {

    public abstract T convert(S s);
}
