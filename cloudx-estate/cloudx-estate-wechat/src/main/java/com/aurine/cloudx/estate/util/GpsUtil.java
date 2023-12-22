package com.aurine.cloudx.estate.util;

import com.aurine.cloudx.estate.vo.GPS;

public class GpsUtil {

    private static double EARTH_RADIUS = 6378.137;
    /**
     * https://blog.csdn.net/weixin_42862117/article/details/88992158
     * 计算两个坐标点之间的距离，单位米
     * @param lat1	//实际坐标点
     * @param lng1	//实际坐标点
     * @param lat2 //目标坐标点
     * @param lng2  //目标坐标点
     * @return 返回的是double类型
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }
    /**
     * https://www.jianshu.com/p/c39a2c72dc65?from=singlemessage
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static GPS gcj02_To_Bd09(Double lat, Double lon) {
        if (lat == null || lon == null) {
            return new GPS();
        }
        Double x = lon, y = lat;
        Double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        Double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        Double bd_lon = z * Math.cos(theta) + 0.0065;
        Double bd_lat = z * Math.sin(theta) + 0.006;
        return new GPS(bd_lat, bd_lon);
    }


    /**
     * https://www.jianshu.com/p/c39a2c72dc65?from=singlemessage
     * 将 BD-09 坐标转换成GCJ-02 坐标
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static GPS bd09_To_Gcj02(Double bd_lat, Double bd_lon) {
        Double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        Double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        Double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        Double gg_lon = z * Math.cos(theta);
        Double gg_lat = z * Math.sin(theta);
        return new GPS(gg_lat, gg_lon);
    }

    private static double rad(double d){
        return d * Math.PI / 180.0;
    }
}
