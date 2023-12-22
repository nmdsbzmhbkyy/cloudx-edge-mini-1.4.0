package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service.impl;

import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.entity.IotEventCallback;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventIotDeviceCallbackDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service.AbstractDeviceEventService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 骐驭的设备事件处理
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-08-06 16:54:57
 */
@Service
public class QiyuDeviceEventService extends AbstractDeviceEventService {

    /**
     * 这是烟感的（路灯和烟感差异较大）
     * 路灯无法知道是否解除报警所以不在这里进行处理（直接查警情记录表）
     * {
     * "onNotifyData": {
     * "changeTime": "1628049905",
     * "devId": "61079c2a0ad1ed0286344d11_868530027500526",
     * "gatewayId": "61079c2a0ad1ed0286344d11_868530027500526",
     * "msgId": "df13ecfd-0f42-44d2-a5eb-6e14189a3562",
     * "objManagerData": {
     * "action": "UPDATE",
     * "data": {
     * "event_id": 2,
     * "event_type": 2,
     * "Imei": "868530027500526",
     * "cmd": "event",
     * "event_time": 20210804
     * },
     * "eventCode": "402000",
     * "eventName": "DjAlarmEvent",
     * "eventSrc": "gateway",
     * "eventTime": "1628049514463",
     * "eventType": "gwcm",
     * "objName": "AlarmEvent",
     * "serviceId": "AlarmEvent"
     * }
     * },
     * "resource": "device.event",
     * "event": "report",
     * "subscriptionId": "15"
     * }
     */
    @Override
    public void eventHandle(String eventJson, ProjectDeviceInfo deviceInfo) throws JsonProcessingException {
        log.info("骐驭IOT设备事件处理");
        ObjectNode event = objectMapper.readValue(eventJson, ObjectNode.class);
        String eventName = event.findPath("eventName").asText();
        String eventCode = event.findPath("eventCode").asText();
        String serviceId = event.findPath("serviceId").asText();
        long eventTime = event.findPath("eventTime").asLong();
        ObjectNode data = (ObjectNode) event.findPath("data");

        HuaweiEventTypeEnum huaweiEventTypeEnum = HuaweiEventTypeEnum.getByCode(eventName);
        if (huaweiEventTypeEnum == HuaweiEventTypeEnum.DjAlarmEvent
                && HuaweiEventEnum.ALERT_EQUIPMENT_ERROR.code.equals(eventCode)
                && DeviceTypeConstants.SMOKE.equals(deviceInfo.getDeviceType())
        ) {
            String eventType = data.get("event_type").asText();
            // 保存设备报警事件日志
            IotEventCallback eventCallback = getEventCallback(data, deviceInfo.getDeviceType(), deviceInfo.getDeviceId(),
                    epochMilliConvertToLocalDateTime(eventTime));
            Set<IotEventCallback> eventCallbackSet = new HashSet<>();
            eventCallbackSet.add(eventCallback);
            try {
                sendMsg(TopicConstant.IOT_DEVICE_EVENT, new EventIotDeviceCallbackDTO(eventCallbackSet));
                log.info("设备：{}；本次新增事件：{}；", deviceInfo.getDeviceId(), objectMapper.writeValueAsString(eventCallbackSet));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // 保存设备异常状态
            ArrayNode arrayNode = objectMapper.createArrayNode();
            // 烟感设备报警类型如果是2则说明解除报警
            if (!"2".equals(eventType)) {
                arrayNode.add(EventEnum.getAlarmDesc(eventType, deviceInfo.getDeviceType()));
            }
            this.saveIotDeviceEvent(deviceInfo.getThirdpartyCode(), EVENT_SERVICE_ID, serviceId, arrayNode.toString());
        }
    }

    private IotEventCallback getEventCallback(JsonNode eventData, String deviceType, String deviceId, LocalDateTime eventTime) {
        return new IotEventCallback(deviceId, EventEnum.getAlarmDesc(eventData.get("event_type").asText(), deviceType), eventTime);
    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> set = new HashSet<>();
        set.add(DeviceManufactureEnum.QIYU_SMOKE);
        set.add(DeviceManufactureEnum.QIYU_SMART_STREET_LIGHT);
        return set;
    }

    @AllArgsConstructor
    private enum EventEnum {
        // 报警解除只做记录
        // alarmDismissed("2", "报警解除", DeviceTypeConstants.SMOKE),
        smokeAlarm("0", "烟雾报警", DeviceTypeConstants.SMOKE),
        smokeLowBatteryAlarm("1", "低电报警", DeviceTypeConstants.SMOKE),
        smokeAlarmDismissed("2", "报警解除", DeviceTypeConstants.SMOKE),

        StreetLightDeviceOffline("0", "设备离线", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightTurnOnTheLightsAbnormally("1", "异常开灯", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightTurnOffTheLightsAbnormally("2", "异常关灯", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightDeviceOvervoltage("3", "设备过压", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightDeviceOvercurrent("4", "设备过流", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightDeviceUndervoltage("5", "设备欠压", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightAbnormalLightBulbOrWiring("6", "灯泡或线路异常", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightTheDeviceIsOverheated("7", "设备过温", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightAbnormalContactorOrCircuit("8", "接触器或回路异常", DeviceTypeConstants.SMART_STREET_LIGHT),
        StreetLightAbnormalPowerDrive("9", "电源驱动器异常", DeviceTypeConstants.SMART_STREET_LIGHT);

        public String code;
        public String desc;
        public String deviceType;

        public static String getAlarmDesc(String code, String deviceType) {
            for (EventEnum e : values()) {
                if (e.deviceType.equals(deviceType) && e.code.equals(code)) {
                    return e.desc;
                }
            }
            return "未知异常报警";
        }

    }

}
