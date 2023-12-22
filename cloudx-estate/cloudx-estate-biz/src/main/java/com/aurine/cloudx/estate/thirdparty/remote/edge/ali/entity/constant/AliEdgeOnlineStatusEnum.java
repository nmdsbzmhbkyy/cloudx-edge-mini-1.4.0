package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.constant;

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
public enum AliEdgeOnlineStatusEnum {

    ONLINE("online","设备在线", DeviceStatusEnum.ONLINE.code),
//    UNACTIVATED ("0","未激活",DeviceStatusEnum.DEACTIVE.code),
    OFFLINE("offline","设备离线",DeviceStatusEnum.OFFLINE.code),
    ERROR("3","设备异常",DeviceStatusEnum.ERROR.code);

    public String code;
    public String desc;
    public String cloudCode;//云平台的字典编码



    public static AliEdgeOnlineStatusEnum getByCode(String code) {
        AliEdgeOnlineStatusEnum[] eventEnums = values();
        for (AliEdgeOnlineStatusEnum eventEnum : eventEnums) {
            if (eventEnum.code().equals(code)) {
                return eventEnum;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
