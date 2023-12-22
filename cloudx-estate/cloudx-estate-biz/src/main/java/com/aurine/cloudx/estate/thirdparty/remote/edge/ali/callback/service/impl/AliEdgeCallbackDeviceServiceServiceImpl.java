package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceNoticeConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiOnlineStatusEnum;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service.AliEdgeCallbackDeviceService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.constant.AliEdgeOnlineStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
public class AliEdgeCallbackDeviceServiceServiceImpl implements AliEdgeCallbackDeviceService {


    @Resource
    private KafkaTemplate kafkaTemplate;


    /**
     * 设备状态变化通知 数据清洗
     *
     * @param requestJson
     */
    @Override
    public void deviceStatusUpdate(JSONObject requestJson) {
        String deviceSn = requestJson.getString("iotId");
        String status = requestJson.getString("status");


        EventDeviceNoticeDTO noticeDTO = new EventDeviceNoticeDTO();
        noticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_STATUS_UPDATE);
        noticeDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
        noticeDTO.setStatus(AliEdgeOnlineStatusEnum.getByCode(status).cloudCode);//转换为云系统字典值
        noticeDTO.setDeviceSn(deviceSn);

        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, noticeDTO);
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }

}
