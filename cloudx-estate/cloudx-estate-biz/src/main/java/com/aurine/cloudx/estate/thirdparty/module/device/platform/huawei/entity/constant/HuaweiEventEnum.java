package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29
 * @Copyright:
 */
@AllArgsConstructor
public enum HuaweiEventEnum {

    OPEN_DOOR_KEY("401000", "钥匙开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_PSW("401001", "密码开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_CARD("401002", "刷卡开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_FINGER("401003", "指纹开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_VISITOR("401006", "访客开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_MONITOR("401007", "监视开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_BY_PHONE("401008", "手机一键开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_FACE("401004", "人脸识别开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_TEMP("401009", "临时授权开锁", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_REMOTE("401011", "远程一键开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_BLUETOOTH("401012", "手机蓝牙开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_BY_SHARE("401013", "分享密码开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_QR("401020", "住户二维码开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_QR_AUTH("401021", "授权二维码开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_QR_TEMP("401022", "临时二维码开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_CHECK_BY_HUMAN("401030", "人证核验开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_CALL_INDOOR("401050", "访客呼叫室内机开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_CALL_PHONE("401051", "访客呼叫手机开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_CALL_CENTER("401052", "呼叫中心开门", "DjOpenDoorEvent", "人行进门事件"),
    OPEN_DOOR_EXIT_BUTTON("401053", "出门按钮开门", "DjOpenDoorEvent", "人行进门事件"),

    ABNORMA_CARD("401102", "无效卡刷卡", "DjAbnormalEvent", "异常事件"),
    ABNORMA_QR("401103", "无效二维码", "DjAbnormalEvent", "异常事件"),
    ABNORMA_QR_OUT_TIME("401104", "过期二维码", "DjAbnormalEvent", "异常事件"),
    ABNORMA_STRANGER("401105", "陌生人抓拍", "DjAbnormalEvent", "异常事件"),
    ABNORMA_WRONG_FINGER("401106", "无效指纹", "DjAbnormalEvent", "异常事件"),
    ABNORMA_FINGER_OUT_TIME("401107", "过期指纹", "DjAbnormalEvent", "异常事件"),
    ABNORMA_CARD_OUT_TIME("401108", "过期卡", "DjAbnormalEvent", "异常事件"),
    ABNORMA_PASSWORD_OUT_TIME("401109", "过期密码", "DjAbnormalEvent", "异常事件"),
    ABNORMA_FACE_OUT_TIME("401110", "过期人脸", "DjAbnormalEvent", "异常事件"),
    ABNORMA_QR_WRONG("401199", "异常操作", "DjAbnormalEvent", "异常事件"),

//    ABNORMA_WRONG_CARD("401102", "无效卡刷卡", "DjAbnormalEvent", "异常事件"),
    ALERT_THREE_WRONG_PASSWORD("401202", "3次密码错误告警", "DjAlarmEvent", "报警事件"),
    ALERT_HOLD_TOO_LONG("401203", "长时间逗留报警", "DjAlarmEvent", "报警事件"),
    ALERT_THREE_WRONG_CARD("401204", "3次刷卡错误告警", "DjAlarmEvent", "报警事件"),
    ALERT_THREE_WRONG_FINGER("401205", "3次指纹错误告警", "DjAlarmEvent", "报警事件"),
    ALERT_OPENDOOR("401201", "挟持开门", "DjAlarmEvent", "报警事件"),
    ALERT_OPENDOOR_FORCE("401210", "强行开门报警", "DjAlarmEvent", "报警事件"),
    ALERT_NO_CLOSE("401211", "门未关报警", "DjAlarmEvent", "报警事件"),
    ALERT_BREAK("401212", "防拆报警", "DjAlarmEvent", "报警事件"),
    ALERT_BLACKLIST_ENTRY("401220", "黑名单进门", "DjAlarmEvent", "报警事件"),
    ALERT_FITH_ERROR_OPATER("401221", "五次操作失败异常", "DjAlarmEvent", "报警事件"),
    ALERT_DOOR_ERROR("401222", "锁舌故障", "DjAlarmEvent", "报警事件"),
    ALERT_OTHER_ERROR("401300", "其他报警", "DjAlarmEvent", "报警事件"),
    ALERT_EQUIPMENT_ERROR("402000", "物联传感设备综合报警", "DjAlarmEvent", "报警事件"),

    DEFENCE_AREA_ERROR("401400","防区报警","DjAlarmEvent","报警事件"),
    SOS_ALARM("401401","SOS报警","DjAlarmEvent","报警事件"),

    /**
     * 网关独有异常
     */
    ALERT_THREE_WRONG_PASSWORD_GATEWAY("401213", "3次密码错误告警", "DjAlarmEvent", "报警事件"),

    FACE_CATCH("501001", "人脸抓拍", "FaceMonitorEvent", "人脸监控"),
    FACE_CATCH2("501002", "人脸抓拍事件", "FaceMonitorEvent", "人脸监控"),
    FACE_COMPALE("501003", "人脸比对", "FaceMonitorEvent", "人脸监控"),
    FACE_VIP("501004", "重点人员识别", "FaceMonitorEvent", "人脸监控"),
    FACE_STRANGER("501005", "陌生人识别", "FaceMonitorEvent", "人脸监控"),
    FACE_HEIG("501006", "高频人员识别", "FaceMonitorEvent", "人脸监控"),

    CERT_DOWNlOAD_STATE_PUSH("601001", "通行介质下载状态推送", "GwNormalEvent", "网关通用事件"),
    CERT_CARD_ADD_ING("601002", "卡添加", "GwNormalEvent", "网关通用事件"),
    CERT_CARD_DEL_ING("601003", "卡删除", "GwNormalEvent", "网关通用事件"),
    CERT_CARD_CHANGE("601004", "卡权限变更", "GwNormalEvent", "网关通用事件"),
    CERT_FACE_ADD_ING("601005", "人脸添加", "GwNormalEvent", "网关通用事件"),
    CERT_FACE_DEL_ING("601006", "人脸删除", "GwNormalEvent", "网关通用事件"),
    CERT_FACE_CHANGE("601007", "人脸权限变更", "GwNormalEvent", "网关通用事件"),
    CERT_DEL_DONE("601008", "通行介质下载状态推送-已删除", "GwNormalEvent", "网关通用事件"),
    CERT_FAIL("601009", "通行介质下载状态推送-失败", "GwNormalEvent", "网关通用事件"),
    CERT_ADD_FULL("601010", "通行介质下载状态推送-卡已满", "GwNormalEvent", "网关通用事件"),
    CERT_ADD_SUCCESS("601011", "通行介质下载状态推送-已下载", "GwNormalEvent", "网关通用事件"),
    CERT_FREEZE_SUCCESS("601012", "通行介质下载状态推送-已停用", "GwNormalEvent", "网关通用事件"),
    IOT_DEVICE_DATA_REPORTING("602000", "物联传感设备数据上报", "GwNormalEvent", "网关通用事件"),

    CALL_RECORD("401301", "通话记录", "DjTalkRecordEvent", "通话记录"),
    ;

    public String code;
    public String desc;
    public String type;
    public String typeDesc;


    public static HuaweiEventEnum getByCode(String code) {
        HuaweiEventEnum[] huaweiEventEnums = values();
        for (HuaweiEventEnum huaweiEventEnum : huaweiEventEnums) {
            if (huaweiEventEnum.code().equals(code)) {
                return huaweiEventEnum;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
