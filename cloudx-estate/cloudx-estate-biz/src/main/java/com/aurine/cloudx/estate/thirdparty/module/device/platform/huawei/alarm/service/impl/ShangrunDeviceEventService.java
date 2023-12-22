package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service.impl;

import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.entity.IotEventCallback;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventIotDeviceCallbackDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service.AbstractDeviceEventService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.entity.shangrun.LevelGaugeShangrunEventData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.entity.shangrun.ManholeCoverShangrunEventData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.entity.shangrun.WaterMeterShangrunEventData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventTypeMapEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 上润的设备事件处理
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-08-06 16:54:57
 */
@Service
public class ShangrunDeviceEventService extends AbstractDeviceEventService {

    /**
     * {
     * "onNotifyData": {
     * "changeTime": "1626742788",
     * "devId": "60c1af39b86d7b02bc679584_865118046484840",
     * "gatewayId": "60c1af39b86d7b02bc679584_865118046484840",
     * "msgId": "6bc7dc94-4235-40c5-818e-27c05e343e03",
     * "objManagerData": {
     * "action": "UPDATE",
     * "data": {..具体报警事件..},
     * "eventCode": "602000",
     * "eventName": "GwNormalEvent",
     * "eventSrc": "gateway",
     * "eventTime": "1626747118116",
     * "eventType": "gwcm",
     * "objName": "EventData",
     * "serviceId": "EventData"
     * }
     * },
     * "resource": "device.event",
     * "event": "report",
     * "subscriptionId": "15"
     * }
     */
    @Override
    public void eventHandle(String eventJson, ProjectDeviceInfo deviceInfo) throws JsonProcessingException {
        log.info("上润IOT设备事件处理");
        ObjectNode event = objectMapper.readValue(eventJson, ObjectNode.class);
        String eventName = event.findPath("eventName").asText();
        String eventCode = event.findPath("eventCode").asText();
        String serviceId = event.findPath("serviceId").asText();
        long eventTime = event.findPath("eventTime").asLong();
        ObjectNode data = (ObjectNode) event.findPath("data");
        HuaweiEventTypeEnum eventTypeEnum = HuaweiEventTypeEnum.getByCode(eventName);

        if (eventTypeEnum != null) {
            switch (eventTypeEnum) {
                // 报警事件
                case DjAlarmEvent:
                    if (HuaweiEventEnum.ALERT_EQUIPMENT_ERROR.code.equals(eventCode)
                            && !DeviceTypeConstants.GATE_DEVICE.equals(deviceInfo.getDeviceType())
                            && !DeviceTypeConstants.LADDER_WAY_DEVICE.equals(deviceInfo.getDeviceType()))
                    {
                        ArrayNode arrayNode = objectMapper.createArrayNode();
                        // 将报警事件转换成对应的描述
                        /*
                         * [
                         *  "故障",
                         *  "超磁报警"
                         * ]
                         * */
                        data.fields().forEachRemaining(alarmItem -> {
                            EventTypeEnum alarmEventEnum = HuaweiEventTypeMapEnum.getEventTypeEnumByAlarmCode(alarmItem.getKey(), deviceInfo.getDeviceType());
                            if (alarmEventEnum != null && alarmItem.getValue().asText().equals("1")) {
                                arrayNode.add(alarmEventEnum.eventTypeName);
                            }
                        });
                        this.saveIotDeviceEvent(deviceInfo.getThirdpartyCode(), EVENT_SERVICE_ID, serviceId, arrayNode.toString());
                    }
                    break;
                // eventData事件（上润设备才有的服务）
                case GwNormalEvent:
                    if (HuaweiEventEnum.IOT_DEVICE_DATA_REPORTING.code.equals(eventCode)) {

                        Set<IotEventCallback> eventCallbackSet = getEventCallbackSet(
                                data.toString(),
                                data.get("devType").toString(),
                                deviceInfo.getDeviceId(),
                                epochMilliConvertToLocalDateTime(eventTime));
                        try {
                            sendMsg(TopicConstant.IOT_DEVICE_EVENT, new EventIotDeviceCallbackDTO(eventCallbackSet));
                            log.info("设备：{}；本次新增事件：{}；", deviceInfo.getDeviceId(), objectMapper.writeValueAsString(eventCallbackSet));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
            }
        }
    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> set = new HashSet<>();
        set.add(DeviceManufactureEnum.SHANGRUN_SMART_MANHOLE_COVER);
        set.add(DeviceManufactureEnum.SHANGRUN_SMART_WATER_METER);
        set.add(DeviceManufactureEnum.SHANGRUN_LEVEL_GAUGE);
        return set;
    }


    /**
     * <p>
     *
     * </p>
     *
     * @param deviceType 第三方的设备类型ID（不是我们系统的）
     * @param deviceId   设备ID
     * @param eventTime  事件上报时间
     * @return 物联网设备事件集合
     */
    private Set<IotEventCallback> getEventCallbackSet(String eventData, String deviceType, String deviceId, LocalDateTime eventTime) {
        Set<IotEventCallback> iotEventCallbackSet = new HashSet<>();
        try {
            switch (deviceType) {
                // 液位计
                case "27":
                    iotEventCallbackSet = objectMapper.readValue(eventData, LevelGaugeShangrunEventData.class).getStatusAndAlarmEvents(deviceId, eventTime);
                    break;
                // 井盖
                case "130":
                    iotEventCallbackSet = objectMapper.readValue(eventData, ManholeCoverShangrunEventData.class).getStatusAndAlarmEvents(deviceId, eventTime);
                    break;
                // 水表
                case "7":
                    iotEventCallbackSet = objectMapper.readValue(eventData, WaterMeterShangrunEventData.class).getStatusAndAlarmEvents(deviceId, eventTime);
                    break;
                default:
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return iotEventCallbackSet;
    }

}
