package com.aurine.cloudx.estate.util;

/**
 * @author:zy
 * @data:2022/9/22 2:10 下午
 */
public class PercentageUtil {

    /**
     * @param success：成功数；
     * @param total：总数；
     * @param dot：要保留的小数
     * @return
     */
    public static double percentage(double success, double total, int dot) {
        double bs = Math.pow(10.0, toDouble(dot + 2, 0.0));
        double cs = Math.pow(10.0, toDouble(dot, 0.0));
        double num = 0.0;
        if (total > 0) {
            num = Math.floor(success * bs / total) / cs;
        }
        return num;

    }

    private static Double toDouble(Object value, Double defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        return Double.parseDouble(value.toString());
    }
}
