package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.vo.DeviceParamJsonVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

/**
 * <p>
 *  用于设备事件记录
 * </p>
 * @author : 王良俊
 * @date : 2021-08-06 16:47:41
 */
@Service
public abstract class AbstractDeviceEventService {

    protected ObjectMapper objectMapper = ObjectMapperUtil.instance();

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 设备当前报警状态在设备参数信息表中的serviceId
     * */
    protected static final String EVENT_SERVICE_ID = "IotDeviceParamObj";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
    * <p>
    * 具体设备事件处理
    * </p>
    *
    * @param eventJson 设备回调的事件数据
    */
    public abstract void eventHandle(String eventJson, ProjectDeviceInfo deviceInfo) throws JsonProcessingException;

    /**
     * <p>
     * 获取当前支持的设备枚举
     * </p>
     *
     * @return 支持的设备枚举列表
     */
    public abstract Set<DeviceManufactureEnum> getApplicableDeviceProducts();

    /**
    * <p>
    *  保存事件到设备参数信息表 (默认serviceId就是jsonKey)
    * </p>
    *
    * @param thirdPartyCode 设备的第三方设备编号
    * @param jsonKey json键值
    * @param dataString 事件json数据
    */
    protected void saveIotDeviceEvent(String thirdPartyCode, String jsonKey, String dataString) {
        if (StrUtil.isEmpty(jsonKey)) {
            return;
        }
        ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
        responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);
        responseOperateDTO.setThirdPartyCode(thirdPartyCode);
        responseOperateDTO.setDeviceParamJsonVo(new DeviceParamJsonVo(jsonKey, dataString));
        responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_IOT);
        sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);

    }

    /**
     * <p>
     *  保存事件到设备参数信息表
     * </p>
     *
     * @param thirdPartyCode 设备的第三方设备编号
     * @param serviceId 设备参数信息表的serviceId
     * @param jsonKey json键值
     * @param dataString 事件json数据
     */
    protected void saveIotDeviceEvent(String thirdPartyCode, String serviceId, String jsonKey, String dataString) {
        if (StrUtil.isEmpty(serviceId)) {
            return;
        }
        ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
        responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);
        responseOperateDTO.setThirdPartyCode(thirdPartyCode);
        responseOperateDTO.setDeviceParamJsonVo(new DeviceParamJsonVo(serviceId, jsonKey, dataString));
        responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_IOT);
        sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);

    }

    /**
    * <p>
    * 发送kafka消息
    * </p>
    *
    * @param topic kafka主题
    * @param message 消息内容
    * @return
    * @author: 王良俊
    */
    protected <T> void sendMsg(String topic, T message) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * 将毫秒转换成localDateTime对象
     * </p>
     *
     * @param epochMilli 单位毫秒
     */
    protected LocalDateTime epochMilliConvertToLocalDateTime(Long epochMilli) {
        if (epochMilli == null || epochMilli == 0) {
            return LocalDateTime.now();
        } else {
            return Instant.ofEpochMilli(epochMilli).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        }
    }

}
