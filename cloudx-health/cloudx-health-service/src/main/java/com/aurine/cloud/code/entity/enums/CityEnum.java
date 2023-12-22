package com.aurine.cloud.code.entity.enums;

import lombok.AllArgsConstructor;

/**
 * @author admin
 */
@AllArgsConstructor
public enum CityEnum {


    FUJIAN_CODE("1", "FUJIAN_CODE", "福建健康码");




    public String code;
    public String value;
    public String desc;


    /**
     * @param code
     * @return
     */
    public static CityEnum getByCode(String code) {
        for (CityEnum value : CityEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * @param value
     * @return
     */
    public static CityEnum getByValue(String value) {
        for (CityEnum platformEnum : CityEnum.values()) {
            if (platformEnum.value.equals(value)) {
                return platformEnum;
            }
        }
        return null;
    }

}
