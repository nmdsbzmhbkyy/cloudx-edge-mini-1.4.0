package com.aurine.cloudx.estate.thirdparty.main.consumer;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constants.DeviceTopicConstant;
import com.aurine.cloudx.estate.dto.DeviceRegDto;
import com.aurine.cloudx.estate.dto.DeviceStatusDto;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 车场设备事件消费类
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/6 17:46
 */
@Component
@Slf4j
public class EventParkingDeviceConsumer {

    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @KafkaListener(topics = DeviceTopicConstant.DEVICE_REG, errorHandler = "", containerFactory = "kafkaListenerContainerFactory")
    public void deviceReg(String message) {
        log.info("设备自动注册：{}", message);
        TenantContextHolder.setTenantId(1);
        DeviceRegDto deviceRegDto = JSON.parseObject(message, DeviceRegDto.class);
        DeviceFactoryProducer.getFactory(deviceRegDto.getDeviceType(), ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()).getDeviceService().regDevice(deviceRegDto);
    }

    @KafkaListener(topics = DeviceTopicConstant.DEVICE_STATUS_CHANGE, errorHandler = "", containerFactory = "kafkaListenerContainerFactory")
    public void deviceStatusChange(String message) {
        log.info("设备状态变更：{}", message);
        TenantContextHolder.setTenantId(1);
        DeviceStatusDto statusDto = JSON.parseObject(message, DeviceStatusDto.class);
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(statusDto.getThirdpartyCode());
        ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
        DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()).getDeviceService().statusChange(statusDto);
    }

}
