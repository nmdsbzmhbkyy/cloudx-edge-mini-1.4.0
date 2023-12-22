package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.vo.SysThirdPartyInterfaceConfigVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 第三方接口配置
 *
 * @author 王伟
 * @date 2020-08-13 14:20:04
 */
public interface SysThirdPartyInterfaceConfigService extends IService<SysThirdPartyInterfaceConfig> {

    /**
     * 获取接口配置信息(对于项目级而言为模板信息)
     *
     * @param platformName 对接平台名称
     * @param tenantId     租户ID
     * @return
     */
    List<SysThirdPartyInterfaceConfig> listInterfaceConfig(String platformName, int tenantId);


    /**
     * 通过平台级接口 id获取配置详情
     * 如果当前项目是项目对接，且未创建，则自动复制一份项目配置模板，并返回
     * 如当前项目是项目级对接，且已创建当前项目配置，则自动返回当前项目的配置信息
     *
     * @param id
     * @return
     */
    SysThirdPartyInterfaceConfig getConfig(Integer projectId, String id);
    /**
     * 获取某个项目下，某个设备使用的平台级配置，如果这个设备使用的是项目级配置，则返回模板对象
     *
     * @param projectId
     * @return
     */
    SysThirdPartyInterfaceConfig getConfigByDeviceType(Integer projectId, String deviceTypeCode);

    /**
     * 保存第三方对接配置信息
     *
     * @param sysThirdPartyInterfaceConfigVo
     * @return
     */
    boolean save(SysThirdPartyInterfaceConfigVo sysThirdPartyInterfaceConfigVo);
}
