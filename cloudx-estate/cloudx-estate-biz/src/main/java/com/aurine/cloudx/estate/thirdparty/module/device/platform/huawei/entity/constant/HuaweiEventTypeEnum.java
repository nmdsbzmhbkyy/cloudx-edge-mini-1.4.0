package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29
 * @Copyright:
 */
@AllArgsConstructor
public enum HuaweiEventTypeEnum {

    DjOpenDoorEvent("DjOpenDoorEvent", "人行进门事件", "WR20"),
    DjAbnormalEvent("DjAbnormalEvent", "异常事件", "WR20"),
    DjAlarmEvent("DjAlarmEvent", "报警事件", "WR20"),
    DjTalkRecordEvent("DjTalkRecordEvent", "通话记录事件", ""),
    DjFQAlarmEvent("DjFQAlarmEvent", "安防报警事件", ""),
    GwNormalEvent("GwNormalEvent", "通行介质下载状态推送/第三方物联网设备事件回调", "WR20"),
    FaceMonitorEvent("FaceMonitorEvent", "人脸监控事件", "");

    public String code;
    public String desc;
    public String type;

    public static HuaweiEventTypeEnum getByCode(String code) {
        HuaweiEventTypeEnum[] huaweiEventTypeEnums = values();
        for (HuaweiEventTypeEnum huaweiEventTypeEnum : huaweiEventTypeEnums) {
            if (huaweiEventTypeEnum.code().equals(code)) {
                return huaweiEventTypeEnum;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
