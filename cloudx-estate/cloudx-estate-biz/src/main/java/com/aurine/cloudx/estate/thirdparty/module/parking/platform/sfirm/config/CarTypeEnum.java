package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config;

import lombok.AllArgsConstructor;

/**
 * @description:车辆类型枚举
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29
 * @Copyright:
 */
@AllArgsConstructor
public enum CarTypeEnum {

    TEMP_A("3651", "3", "临时车A"),
    TEMP_B("3650", "3", "临时车B"),
    TEMP_C("3649", "3", "临时车C"),
    TEMP_D("3648", "3", "临时车D"),
    MONTH_A("3652", "2", "月租车A"),
    MONTH_B("3653", "2", "月租车B"),
    MONTH_C("3654", "2", "月租车C"),
    MONTH_D("3655", "2", "月租车D"),
    MONTH_E("3661", "2", "月租车E"),
    MONTH_F("3662", "2", "月租车F"),
    MONTH_G("3663", "2", "月租车G"),
    MONTH_H("3664", "2", "月租车H"),
    FREE("3656", "1", "免费车");

    public String code;
    public String type;
    public String note;

    public static String getTypeByCode(String code) {
        CarTypeEnum[] carTypeEnums = values();
        for (CarTypeEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.code().equals(code)) {
                return carTypeEnum.type();
            }
        }
        return null;
    }

    private String type() {
        return this.type;
    }

    private String code() {
        return this.code;
    }
}
