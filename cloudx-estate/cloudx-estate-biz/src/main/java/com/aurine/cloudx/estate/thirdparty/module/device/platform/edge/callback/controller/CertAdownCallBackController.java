package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.AurineEdgeDeviceCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceOperateServiceFactory;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 接收下发服务的回调
 *
 * @author zouyu
 */

@RestController
@RequestMapping("/certAdownCallBack")
@Slf4j
public class CertAdownCallBackController {

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;


    /**
     * 回调下发
     *
     * @param jsonObject
     */
    @PostMapping("/issued")
    @Inner(false)
    public void certIssued(@RequestBody JSONObject jsonObject) {
        log.debug("[冠林边缘网关] 接受到回调下发：{}", jsonObject);

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

    }

    /**
     * 回调结果
     *
     * @param jsonObject
     */
    @PostMapping("/result")
    @Inner(false)
    public void certResult(@RequestBody JSONObject jsonObject) {
        log.debug("[冠林边缘网关] 接受到下发服务传递的回调结果,操作已忽略：{}", jsonObject);

    }
}
