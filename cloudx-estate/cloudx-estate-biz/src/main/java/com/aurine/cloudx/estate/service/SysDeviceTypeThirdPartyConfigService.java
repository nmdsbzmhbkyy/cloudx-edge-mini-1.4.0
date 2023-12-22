package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysDeviceTypeThirdPartyConfig;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseConfigDTO;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 第三方接口设备配置
 *
 * @author 王伟
 * @date 2020-08-13 14:23:08
 */
public interface SysDeviceTypeThirdPartyConfigService extends IService<SysDeviceTypeThirdPartyConfig> {

    /**
     * 保存配置
     * @param sysDeviceTypeThirdPartyConfig
     * @return
     */
    boolean saveConfig(SysDeviceTypeThirdPartyConfig sysDeviceTypeThirdPartyConfig);
    /**
     * 根据设备id，获取该设备所用的接口配置信息
     *
     * @param deviceId
     * @return
     */
    SysThirdPartyInterfaceConfig getConfigByDeviceId(String deviceId);

    SysThirdPartyInterfaceConfig getConfigByProductId(String productId);

    SysThirdPartyInterfaceConfig getConfigByProjectId(int projectId);

    /**
     * 根据设备类型，获取该设备所用的接口配置信息
     *
     * @param deviceType
     * @param projectId
     * @param tenantId
     * @return
     */
    SysThirdPartyInterfaceConfig getConfigByDeviceType(String deviceType, int projectId, int tenantId);

    /**
     * 根据设备id,接口配置对象类型，获取该设备所用的接口配置信息
     *
     * @param deviceId
     * @param t
     * @param <T>
     * @return
     */
    <T extends BaseConfigDTO> T getConfigByDeviceId(String deviceId, Class<T> t);

    /**
     * 根据设备类型,接口配置对象类型，获取该设备所用的接口配置信息
     *
     * @param deviceType
     * @param t
     * @param <T>
     * @return
     */
    <T extends BaseConfigDTO> T getConfigByDeviceType(String deviceType, int projectId, int tenantId, Class<T> t);

    <T extends BaseConfigDTO> T getConfigByProjectId(int projectId, int tenantId, Class<T> t);
}
