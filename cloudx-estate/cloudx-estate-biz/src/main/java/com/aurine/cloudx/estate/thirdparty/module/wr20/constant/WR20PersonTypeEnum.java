package com.aurine.cloudx.estate.thirdparty.module.wr20.constant;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import lombok.AllArgsConstructor;

/**
 * WR20 人员类型 枚举
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-10-10
 * @Copyright:
 */
@AllArgsConstructor
public enum WR20PersonTypeEnum {

    PROPRIETOR("0", "住户", PersonTypeEnum.PROPRIETOR.code),
    STAFF("10", "员工", PersonTypeEnum.STAFF.code);

    public String code;
    public String desc;
    public String cloudCode;


    public static WR20PersonTypeEnum getByCode(String code) {
        WR20PersonTypeEnum[] huaweiEnums = values();
        for (WR20PersonTypeEnum huaweitEnum : huaweiEnums) {
            if (huaweitEnum.code().equals(code)) {
                return huaweitEnum;
            }
        }
        return null;
    }

    public static WR20PersonTypeEnum getByCloudCode(String cloudCode) {
        WR20PersonTypeEnum[] huaweiEnums = values();
        for (WR20PersonTypeEnum huaweitEnum : huaweiEnums) {
            if (huaweitEnum.cloudCode.equals(cloudCode)) {
                return huaweitEnum;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
