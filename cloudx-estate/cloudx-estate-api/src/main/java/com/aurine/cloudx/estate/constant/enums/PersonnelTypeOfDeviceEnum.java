package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: 设备端的人员类型枚举属性类型
 * Description: 在设备端的人员类型枚举,用于凭证下发时根据我们平台的人员类型下发该类型在设备上的值
 *
 * @author 顾文豪
 * @version 1.0.0
 * @date 2023/11/9 17:27
 */
@AllArgsConstructor
public enum PersonnelTypeOfDeviceEnum {
    /**
     * 访客
     */
    VISITOR(0,PersonTypeEnum.VISITOR.code,"访客"),
    /**
     * 住户
     */
    INHABITANT(1,PersonTypeEnum.PROPRIETOR.code,"住户"),
    /**
     * 员工
     */
    STAFF(2,PersonTypeEnum.STAFF.code,"员工"),
    /**
     * 黑名单
     */
    BLACKLIST(10,PersonTypeEnum.BLACKLIST.code,"黑名单");


    public Integer code;
    /**
     *     边测人员类型code
     */
    public String personTypeCode;
    public String desc;

    /**
     * @param personTypeCode
     * @return
     */
    public static Integer getCodeByPersonTypeCode (String personTypeCode) {
        for (PersonnelTypeOfDeviceEnum value : PersonnelTypeOfDeviceEnum.values()) {
            if (value.personTypeCode.equals(personTypeCode)) {
                return value.code;
            }
        }
        return null;
    }
}
