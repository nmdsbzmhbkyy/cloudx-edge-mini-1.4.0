package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import lombok.AllArgsConstructor;

/**
 * 设备在线状态
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-30
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEdgeOnlineStatusEnum {

    ONLINE("1","设备在线", DeviceStatusEnum.ONLINE.code),
    UNACTIVATED ("0","未激活",DeviceStatusEnum.DEACTIVE.code),
    OFFLINE("2","设备离线",DeviceStatusEnum.OFFLINE.code),
    ERROR("3","设备异常",DeviceStatusEnum.ERROR.code);

    public String code;
    public String desc;
    public String cloudCode;//云平台的字典编码



    public static AurineEdgeOnlineStatusEnum getByCode(String code) {
        AurineEdgeOnlineStatusEnum[] eventEnums = values();
        for (AurineEdgeOnlineStatusEnum eventEnum : eventEnums) {
            if (eventEnum.code().equals(code)) {
                return eventEnum;
            }
        }
        return ERROR;
    }

    private String code() {
        return this.code;
    }
}
