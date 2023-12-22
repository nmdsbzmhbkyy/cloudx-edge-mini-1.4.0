package com.aurine.cloudx.estate.constant.enums;

import com.aurine.cloudx.estate.excel.parking.ParCarRegisterExcel;
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
public enum ParCarRegisterExcelEnum {


    /**
     * 车辆导入公共模板
     */
    PARKING_SYSTEM("车辆导入公共模板.xlsx","0", ParCarRegisterExcel.class);


    /**
     * 文件名称
     */
    private final String name;
    /**
     * 文件类型
     */
    private final String type;

    /**
     * 导入模板类
     */
    private final Class clazz;


    /**
     * @param type
     * @return
     */
    public static ParCarRegisterExcelEnum getEnum(String type) {
        for (ParCarRegisterExcelEnum value : ParCarRegisterExcelEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return ParCarRegisterExcelEnum.values()[0];
    }

}