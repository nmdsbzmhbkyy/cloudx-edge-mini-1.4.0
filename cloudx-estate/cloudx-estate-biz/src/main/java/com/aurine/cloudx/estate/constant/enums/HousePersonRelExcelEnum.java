package com.aurine.cloudx.estate.constant.enums;

import com.aurine.cloudx.estate.excel.person.HousePersonRelExcel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: HousePersonRelExcelEnum
 * @author: 王良俊 <>
 * @date:  2020年08月24日 上午10:25:27
 * @Copyright:
*/
@Getter
@AllArgsConstructor
public enum HousePersonRelExcelEnum {


    /**
     * 住户迁入-系统标准
     */
    RESIDENT_SYSTEM("0","迁入住户—系统标准.xlsx", HousePersonRelExcel.class),

    /**
     * 住户迁入—公安标准
     */
    RESIDENT_PUBLIC("1","迁入住户—公安标准.xlsx", HousePersonRelExcel.class);


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
    public static HousePersonRelExcelEnum getEnum(String type) {
        for (HousePersonRelExcelEnum value : HousePersonRelExcelEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}