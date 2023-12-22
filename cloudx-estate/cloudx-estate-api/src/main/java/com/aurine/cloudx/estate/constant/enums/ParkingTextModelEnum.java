package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * <p>
 * 车场中台 车道一体机文本/语言播报 模板
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/16 10:33
 */
@Getter
@AllArgsConstructor
public enum ParkingTextModelEnum {

    /*
     * 车牌号
     **/
    PLATE_NUMBER("&plateNumber;"),

    /*
     * 有效时间
     **/
    VALID_TIME("有效期:&endDate;"),

    /*
     * 场内车辆信息-空余车位
     **/
    NOT_PARKING_CAR_COUNT("空余车位:&unParkedCount;"),

    /*
     * 停车时长
     **/
    STOP_TIME("停车时长:&stopTime;"),

    /*
     * 停车费
     **/
    PARKING_FEE("停车费:&parkingFee;"),

    /*
     * 当前时间
     **/
    CURRENT_TIME("&currentTime;"),

    /*
     * 无
     **/
    NONE(""),
    ;

    private String model;

}
