package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>设备接入方式枚举</p>
 *
 * @ClassName: FrameTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/26 11:37
 * @Copyright:
 */
@AllArgsConstructor
@Getter
public enum DeviceAccessMethodEnum {
    /**
     * 手动
     */
    MANUAL("2","0"),
    /**
     * 自动
     */
    AUTO("1","1"),
    /**
     * 未定义
     */
    UNDEFINED("9","9");

    public String code;

    public String thirdPartyCode;


    public static DeviceAccessMethodEnum getCode(String thirdPartyCode){
        for (DeviceAccessMethodEnum value : DeviceAccessMethodEnum.values()) {
            if (value.getThirdPartyCode().equals(thirdPartyCode)) {
                return value;
            }
        }
        return UNDEFINED;
    }
}
