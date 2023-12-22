package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.entity.constant;

import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import lombok.AllArgsConstructor;

/**
 * @description:缴费类型(卡类型)枚举
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29
 * @Copyright:
 */
@AllArgsConstructor
public enum CardTypeEnum {

    TEMP_A("4", ParkingRuleTypeEnum.TEMP, "临时车A"),
    MONTH_A("2", ParkingRuleTypeEnum.MONTH, "月租车A"),
    FREE("1", ParkingRuleTypeEnum.FREE, "免费车");

    public String fujicaCode;
    public ParkingRuleTypeEnum cloudCode;
    public String note;

    public static ParkingRuleTypeEnum getCloudCodeByFujica(String fujicaCode) {
        CardTypeEnum[] carTypeEnums = values();
        for (CardTypeEnum cardTypeEnum : carTypeEnums) {
            if (cardTypeEnum.fujicaCode.equals(fujicaCode)) {
                return cardTypeEnum.cloudCode;
            }
        }
        return null;
    }


}
