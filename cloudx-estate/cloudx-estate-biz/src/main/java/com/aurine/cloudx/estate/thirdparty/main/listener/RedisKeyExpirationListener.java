package com.aurine.cloudx.estate.thirdparty.main.listener;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.AurineDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.FujicaRemoteParkingCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.entity.constant.FujicaConstant;
import com.aurine.project.entity.constant.ServiceProviderEnum;
import com.aurine.project.entity.constant.TransportTopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: Redis失效监听
 * Redis 需配置 notify-keyspace-events Ex
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-09
 * @Copyright:
 */
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private AurineDataConnector aurineDataConnector;
    @Resource
    private FujicaRemoteParkingCallbackService fujicaRemoteParkingCallbackService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * Redis-key失效监听事件
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //  获取失效的key
        String expiredKey = message.toString();

        //  指定key 的前缀=xxxxxx，处理对应业务逻辑，可看步骤三生成订单号：public static final String   ORDER_OVER_TIME_KEY= "order-over-time:";
        if (expiredKey.indexOf("AURINE_MIDDLE_TOKEN") != -1) {

            String[] orderOn = expiredKey.split("AURINE_MIDDLE_TOKEN_");
            String projectIdStr = orderOn[1];
            int projectId = Integer.valueOf(projectIdStr);

            log.info("捕获到冠林中台Token超时，超时项目：{}，重新获取Token", projectId);

            //执行登录
            AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode(), projectId, 1, AurineConfigDTO.class);
            if (config == null) {
                config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode(), projectId, 1, AurineConfigDTO.class);
            }

            if (config != null) {
                AurineRequestObject requestObject = new AurineRequestObject();
                requestObject.setConfig(config);

                aurineDataConnector.login(requestObject);
            }
            log.info("冠林中台项目：{}，获取Token完成", projectId);

        }else if(HuaweiCacheConstant.HUAWEI_TOKEN.equalsIgnoreCase(expiredKey)){
            //TODO：华为中台token过期自动获取策略 -> 2020-12-01
            //执行登录
//            HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode(), projectId, 1, HuaweiConfigDTO.class);
//            if (config == null)
//                config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode(), projectId, 1, HuaweiConfigDTO.class);
//
//            if (config != null) {
//                AurineRequestObject requestObject = new AurineRequestObject();
//                requestObject.setConfig(config);
//
//                aurineDataConnector.login(requestObject);
//            }
//            log.info("冠林中台项目：{}，获取Token完成", projectId);
        }else if(expiredKey.contains(FujicaConstant.APP_ID+"-"+ ServiceProviderEnum.PARKING_FUJICA.code+"-"+ ProjectContextHolder.getProjectId())){
            //车场离线
            log.info("检测到车场已离线");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("company", ParkingAPICompanyEnum.FUJICA.value);
            jsonObject.put("isOnline", '0');
            fujicaRemoteParkingCallbackService.isOnline(jsonObject);
        }
    }

}
