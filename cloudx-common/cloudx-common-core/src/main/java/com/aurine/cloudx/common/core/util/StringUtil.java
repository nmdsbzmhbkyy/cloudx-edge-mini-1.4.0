package com.aurine.cloudx.common.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @description: 基于lang3的StringUitls扩展工具类
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/9
 * @Copyright:
 */
public class StringUtil  extends StringUtils {

    /**
     * 在前端补全数字，并返回字符串
     * @param fillLength
     * @param value
     * @return
     */
    public static String fillWithZero(int fillLength,int value){
        return String.format("%0"+fillLength+"d", value);
    }

    /**
     * 数字转换为字母 [1-26] [A-Z]
     * @param value
     */
    public static String numToLetter(int value) {
        char c1=(char) (value+64);
        return String.valueOf(c1);
    }

    /**
     * 字母转数字 [A-Z] [1-26]
     * @param value
     */
    public static String letterToNum(String value) {
        int c = value.toCharArray()[0];
        return String.valueOf(c - 64);
    }

}
