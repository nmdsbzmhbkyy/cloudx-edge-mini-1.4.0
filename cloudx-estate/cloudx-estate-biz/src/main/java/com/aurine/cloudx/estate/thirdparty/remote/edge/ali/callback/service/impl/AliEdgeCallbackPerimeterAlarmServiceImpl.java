package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.feign.RemoteFileService;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceParkingPassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventWarningErrorConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventWarningErrorDTO;
import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.callback.SfirmRemoteParkingCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config.CarTypeEnum;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service.AliEdgeCallbackPerimeterAlarmService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.constant.AliEdgePerimeterAlarmEnum;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.dto.AliEdgeEventPerimeterAlarmDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>赛菲姆车场对接，回调接口实现</p>
 *
 * @ClassName: SfirmRemoteParkingCallbackServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29 14:15
 * @Copyright:
 */
@Service
@Slf4j
public class AliEdgeCallbackPerimeterAlarmServiceImpl implements AliEdgeCallbackPerimeterAlarmService {

    @Resource
    ProjectParkingInfoService projectParkingInfoService;
    @Resource
    private KafkaTemplate kafkaTemplate;


    /**
     * 告警事件
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void channelAlarm(JSONObject jsonObject) {
        AliEdgeEventPerimeterAlarmDTO alarmDTO = JSONObject.parseObject(jsonObject.toJSONString(), AliEdgeEventPerimeterAlarmDTO.class);

        EventWarningErrorDTO eventWarningErrorDTO = new EventWarningErrorDTO();
        String channelNo = alarmDTO.getValue().getString("channelID");
        String errorType = alarmDTO.getValue().getString("alarmType");

        errorType = AliEdgePerimeterAlarmEnum.getEnumByAliCode(errorType).getCloudCode();


        eventWarningErrorDTO.setAction(EventWarningErrorConstant.ACTION_CHANNEL_ALARM);
        eventWarningErrorDTO.setDeviceSn(alarmDTO.getIotId());
        eventWarningErrorDTO.setEventTime(LocalDateTime.now());
        eventWarningErrorDTO.setErrorType(errorType);
        eventWarningErrorDTO.setErrorDeviceId(channelNo);

        kafkaTemplate.send(TopicConstant.SDI_EVENT_WARNING_ERROR, JSONObject.toJSONString(eventWarningErrorDTO));
        log.info("[阿里边缘] 周界告警 告警事件已推送到云服务");

    }
}
