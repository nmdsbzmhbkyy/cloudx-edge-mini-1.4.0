package com.aurine.cloudx.estate.thirdparty.main.consumer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.subscribe.factory.EventFactory;
import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import com.aurine.cloudx.estate.thirdparty.main.entity.PersonInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceGatePassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceGatePassDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.EventService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineOpenModeEnum;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.AssertFalse;

import static com.aurine.cloudx.estate.constant.decidedTypeConstant.PEOPLE_TYPE;

/**
 * <p>门禁通行事件通知 统一消费者</p>
 *
 * @ClassName: EventDeviceParkingPayConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
@Component
@Slf4j
public class EventDeviceGatePassConsumer {
    @Resource
    private EventService eventService;
    @Resource
    private EventFactory eventFactory;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    //    @KafkaListener(groupId = "THIRD_GROUP", topics = TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, errorHandler = "",containerFactory = "kafkaListenerContainerFactory")
    @KafkaListener(topics = TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, errorHandler = "", containerFactory = "kafkaListenerContainerFactory")
    public void readActiveQueue(String message) {
        TenantContextHolder.setTenantId(1);
        log.info("门禁通行事件通知 统一接口 接口接收到数据：{}", message);
        dispatch(message);
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        try {
            EventDeviceGatePassDTO gatePassDto = JSONObject.parseObject(message, EventDeviceGatePassDTO.class);
            /**
             * 监听人行记录   订阅
             */
//            String thirdPartyCode = gatePassDto.getThirdPartyCode();
//            String deviceSn = gatePassDto.getDeviceSn();
//            ProjectDeviceInfoProxyVo deviceInfo;
//            if (StringUtils.isNotEmpty(thirdPartyCode)) {
//                deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
//            } else {
//                deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
//            }
//
//            if (deviceInfo != null) {
//                PersonInfo personInfo = eventService.getPersonTypeAndPersonId(gatePassDto, deviceInfo.getProjectId());
//                JSONObject messageObject = JSONObject.parseObject(message);
//                messageObject.put("deviceId", deviceInfo.getDeviceId());
//                messageObject.put("deviceName", deviceInfo.getDeviceName());
//                if (personInfo != null) {
//                    messageObject.put("personId", personInfo.getPersonId());
//                    messageObject.put("personName", personInfo.getPersonName());
//                    EventSubscribeService eventSubscribeService = eventFactory.GetProduct(PEOPLE_TYPE);
//                    eventSubscribeService.send(JSONObject.parseObject(message), deviceInfo.getProjectId(), PEOPLE_TYPE);
//                }
//            }
            log.info("【门禁通行事件通知】构建通知对象：{}",gatePassDto.toString());

            switch (gatePassDto.getAction()) {
                case EventDeviceGatePassConstant.ACTION_CAED:
                case EventDeviceGatePassConstant.ACTION_FACE:
                case EventDeviceGatePassConstant.ACTION_FINGER:
                case EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON:
                case EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON:
                    log.info("【门禁通行事件通知】passGate");
                    eventService.passGate(gatePassDto);
                    break;
                case EventDeviceGatePassConstant.ACTION_REMOTE:
                case EventDeviceGatePassConstant.ACTION_QR_CODE:
                case EventDeviceGatePassConstant.ACTION_PASSWORD:
                    log.info("【门禁通行事件通知】passGateWithOutPerson");
                    eventService.passGateWithOutPerson(gatePassDto);
                    break;
                case EventDeviceGatePassConstant.ACTION_ERROR_PASS:
                    log.info("【门禁通行事件通知】passGateError");
                    eventService.passGateError(gatePassDto);
                    break;
                default:
                    //其他类型
                    log.info("【门禁通行事件通知】passGateWithOutPerson");
                    eventService.passGateWithOutPerson(gatePassDto);
                    break;
            }

        } catch (Exception e) {
            //JSON解析失败，抛弃处理
            e.printStackTrace();
        }
    }
}
