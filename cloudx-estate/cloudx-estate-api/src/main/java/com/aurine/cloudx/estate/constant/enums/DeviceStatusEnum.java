package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>设备状态枚举</p>
 *
 * @ClassName: DeviceStatusEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-07 10:49
 * @Copyright:
 */
@AllArgsConstructor
@Getter
public enum DeviceStatusEnum {

    /**
     * 在线
     */
    ONLINE("1",1),

    /**
     * 离线
     */
    OFFLINE("2",2),

    /**
     * 故障
     */
    ERROR("3",3),

    /**
     * 未激活
     */
    DEACTIVE("4",0),

    /**
     * 未知
     */
    UNKNOW("9",9);

    public String code;


    /**
     * 第三方编号(中台设备状态)
     */
    public Integer thirdPartyCode;

    /**
     * @param thirdPartyCode
     * @return
     */
    public static DeviceStatusEnum getByCode(Integer thirdPartyCode) {
        for (DeviceStatusEnum value : DeviceStatusEnum.values()) {
            if (value.getThirdPartyCode().equals(thirdPartyCode)) {
                return value;
            }
        }
        return DEACTIVE;
    }

}
