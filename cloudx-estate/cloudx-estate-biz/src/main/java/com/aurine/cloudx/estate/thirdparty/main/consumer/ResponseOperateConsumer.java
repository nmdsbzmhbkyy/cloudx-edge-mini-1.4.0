package com.aurine.cloudx.estate.thirdparty.main.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.DeviceService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>响应指令消费者</p>
 *
 * @ClassName: ResponseSearchRecordConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-26 16:31
 * @Copyright:
 */
@Slf4j
@Component
public class ResponseOperateConsumer {
    @Resource
    private DeviceService deviceService;


    @KafkaListener(topics = TopicConstant.SDI_RESPONSE_OPERATE, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("指令响应 统一接口 接口接收到数据：{}", message);
        dispatch(message);
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        try {
            ResponseOperateDTO operateDTO = JSONObject.parseObject(message, ResponseOperateDTO.class);
            if (StringUtils.isNotEmpty(operateDTO.getAction())) {

                switch (operateDTO.getAction()) {
                    case ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_BASIC:
                        deviceService.updateDeviceParam(operateDTO);
                        break;
                    case ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_FRAME:
                        deviceService.updateDeviceFrame(operateDTO);
                        break;
                    case ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS:
                        deviceService.updateCert(operateDTO);
                        break;
                    case ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS_LIST:
                        deviceService.updateCerts(operateDTO);
                        break;
                    case ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_OTHER:
                        deviceService.updateDeviceOtherParam(operateDTO);
                        break;
                    case ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_IOT:
                        deviceService.updateIotDeviceParam(operateDTO);
                        break;
                    case ResponseOperateConstant.ACTION_UPDATE_DEVICE_STATUS:
                        deviceService.updateDeviceStatus(operateDTO);
                        break;
                    case ResponseOperateConstant.ACTION_UPDATE_ALARM_STATUS:
                        deviceService.updateAlarmStatus(operateDTO);
                        break;

                    default:
                        //其他类型
//                    eventService.passGateOther(gatePassDto);
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            //JSON解析失败，抛弃处理
        }
    }
}
