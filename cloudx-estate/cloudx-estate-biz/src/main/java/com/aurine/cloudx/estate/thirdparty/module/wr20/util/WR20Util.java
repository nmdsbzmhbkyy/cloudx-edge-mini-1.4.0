package com.aurine.cloudx.estate.thirdparty.module.wr20.util;

import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 在WR20获取华为中台的配置。
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-14
 * @Copyright:
 */
@Component
@Slf4j
public class WR20Util {

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;


    public HuaweiConfigDTO getConfig(int projectId) {
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId, 1, HuaweiConfigDTO.class);
        if (config == null) {
            log.error("项目编号：{} 未配置华为中台未门禁对接平台，无法使用WR20", projectId);
            throw new RuntimeException("平台配置错误，请联系管理员");
        }

        return config;
    }

}
