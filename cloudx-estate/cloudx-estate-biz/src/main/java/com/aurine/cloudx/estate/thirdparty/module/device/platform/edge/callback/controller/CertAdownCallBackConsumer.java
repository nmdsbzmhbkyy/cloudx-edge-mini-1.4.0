package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineCertSendKafkaDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle.AurineCertSendHandleV2_1;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.AurineEdgeDeviceCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceOperateServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class CertAdownCallBackConsumer {
    @Resource
    private CertAdownCallBackController certAdownCallBackController;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    @KafkaListener(topics = "ADOWN_SEND")
    public void readActiveQueue(String message) {
        log.info("[冠林边缘网关] 接受到回调下发：{}", message);
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);

            Integer projectId = ProjectContextHolder.getProjectId();
            JSONObject body = jsonObject.getJSONObject("body");

            //数据转换
            AurineEdgeDeviceCertVo aurineEdgeDeviceCertVo = JSONObject.parseObject(JSON.toJSONString(body.get("aurineEdgeDeviceCertVo")), AurineEdgeDeviceCertVo.class);

            //取本地配置 替换
            int tenantId = 1;
            String apiEndPoint = aurineEdgeDeviceCertVo.getConfig().getApiEndPoint();
            if (apiEndPoint == null || !apiEndPoint.contains("pigx-iaas")) {
                AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(aurineEdgeDeviceCertVo.getDeviceInfo().getDeviceType(), projectId, tenantId, AurineEdgeConfigDTO.class);
                log.debug("[冠林边缘网关] 替换后的配置:{}",JSONObject.toJSONString(config));
                aurineEdgeDeviceCertVo.setConfig(config);
            }
            //发送给中台
            AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1)
                    .objectManage(aurineEdgeDeviceCertVo.getConfig(),jsonObject.getString("requestId"), aurineEdgeDeviceCertVo.getDeviceInfo().getThirdpartyCode(), aurineEdgeDeviceCertVo.getCertType(), aurineEdgeDeviceCertVo.getAction(), "DoorKeyObj", aurineEdgeDeviceCertVo.getParamsJsonArray(), null,aurineEdgeDeviceCertVo.getDeviceInfo(),aurineEdgeDeviceCertVo.getDoorKeyIdList(),aurineEdgeDeviceCertVo.getPriotity());


        }catch (Exception e){
            log.error("[冠林边缘网关] 接受到回调下发错误：{}，{}", message,e.getStackTrace());
        }
    }

}
