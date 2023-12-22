package com.aurine.cloudx.estate.constant.enums;

import com.aurine.cloudx.estate.excel.parking.ParkingManageRelExcel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: ParkingManageRelExcelEnum
 * @author: 王良俊 <>
 * @date:  2020年08月24日 下午07:16:23
 * @Copyright:
*/
@Getter
@AllArgsConstructor
public enum ParkingManageRelExcelEnum {


    /**
     * 住户迁入-系统标准
     */
    PARKING_SYSTEM("0","车位归属人迁入—系统标准.xlsx", ParkingManageRelExcel.class),

    /**
     * 住户迁入—公安标准
     */
    PARKING_PUBLIC("1","车位归属人迁入—公安标准.xlsx", ParkingManageRelExcel.class);


    /**
     * 模板类型
     */
    private final String type;

    /**
     * 文件名称
     */
    private final String name;

    /**
     * 导入模板类
     */
    private final Class clazz;


    /**
     * @param type
     * @return
     */
    public static ParkingManageRelExcelEnum getEnum(String type) {
        for (ParkingManageRelExcelEnum value : ParkingManageRelExcelEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}