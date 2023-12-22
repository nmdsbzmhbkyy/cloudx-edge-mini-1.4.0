package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.entity.SysDeviceTypeThirdPartyConfig;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.mapper.SysDevicetypeThirdpartyConfigMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.service.SysThirdPartyInterfaceConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseConfigDTO;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 第三方接口设备配置
 *
 * @author 王伟
 * @date 2020-08-13 14:23:08
 */
@Service
@Slf4j
@RefreshScope
public class SysDeviceTypeThirdPartyConfigServiceImpl extends ServiceImpl<SysDevicetypeThirdpartyConfigMapper, SysDeviceTypeThirdPartyConfig> implements SysDeviceTypeThirdPartyConfigService {
    @Resource
    SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private SysThirdPartyInterfaceConfigService sysThirdPartyInterfaceConfigService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;

    @Value("${thirdparty.middleplatform.enable}")
    private boolean enableMiddleApi;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveConfig(SysDeviceTypeThirdPartyConfig sysDeviceTypeThirdPartyConfig) {
        //清空所属项目的数据信息后，再添加
        sysDeviceTypeThirdPartyConfigService.remove(new QueryWrapper<SysDeviceTypeThirdPartyConfig>().lambda()
                .eq(SysDeviceTypeThirdPartyConfig::getTenantId, sysDeviceTypeThirdPartyConfig.getTenantId())
                .eq(SysDeviceTypeThirdPartyConfig::getProjectId, sysDeviceTypeThirdPartyConfig.getProjectId())
                .eq(SysDeviceTypeThirdPartyConfig::getDeviceTypeId, sysDeviceTypeThirdPartyConfig.getDeviceTypeId())
        );

        return sysDeviceTypeThirdPartyConfigService.save(sysDeviceTypeThirdPartyConfig);
    }

    /**
     * 根据设备id，获取该设备所用的接口配置信息
     *
     * @param deviceId
     * @return
     */
    @Override
    public SysThirdPartyInterfaceConfig getConfigByDeviceId(String deviceId) {

        if (!enableMiddleApi) {
            return null;
        }

        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);


        if (deviceInfo == null) {
            log.error("存在脏数据：找不到ID= " + deviceId + " 的设备");
            throw new RuntimeException("数据错误，请联系管理员");
        }
        //获取当前设备类型的项目默认配置
        List<SysDeviceTypeThirdPartyConfig> deviceTypeConfigList = sysDeviceTypeThirdPartyConfigService.list(new QueryWrapper<SysDeviceTypeThirdPartyConfig>().lambda()
                .eq(SysDeviceTypeThirdPartyConfig::getDeviceTypeId, deviceInfo.getDeviceType())
                .eq(SysDeviceTypeThirdPartyConfig::getProjectId, deviceInfo.getProjectId())
                .eq(SysDeviceTypeThirdPartyConfig::getIsDefault, "1")
                .eq(SysDeviceTypeThirdPartyConfig::getTenantId, deviceInfo.getTenantId()));

        if (CollectionUtil.isEmpty(deviceTypeConfigList)) {
//            throw new RuntimeException("设备 " + deviceInfo.getDeviceName() + " ，所属设备类型：" + DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()).getNote() + " 未找到接口配置信息！");
            return null;
        }

        //获取配置实体
        SysDeviceTypeThirdPartyConfig deviceTypeThirdPartyConfig = deviceTypeConfigList.get(0);
        return sysThirdPartyInterfaceConfigService.getById(deviceTypeThirdPartyConfig.getThirdPartyNo());

    }

    @Override
    public SysThirdPartyInterfaceConfig getConfigByProductId(String productId) {

        SysDeviceProductMap product = sysDeviceProductMapService.getById(productId);
        if(product == null){
            return null;
        }
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = sysThirdPartyInterfaceConfigService.getOne(Wrappers.lambdaQuery(SysThirdPartyInterfaceConfig.class)
                .eq(SysThirdPartyInterfaceConfig::getThirdPartyNo, product.getThirdPartyNo()));

        return sysThirdPartyInterfaceConfig;
    }


    @Override
    public SysThirdPartyInterfaceConfig getConfigByProjectId(int projectId) {
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = this.getConfigByDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode(), projectId, 1);

        if (sysThirdPartyInterfaceConfig == null) {
            sysThirdPartyInterfaceConfig = this.getConfigByDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode(), projectId, 1);

            if (sysThirdPartyInterfaceConfig == null) {
                return null;
            }
        }

        return sysThirdPartyInterfaceConfig;
    }

    /**
     * 根据设备id,接口配置对象类型，获取该设备所用的接口配置信息
     *
     * @param deviceId
     * @param t
     * @return
     */

    @Override
    public <T extends BaseConfigDTO> T getConfigByDeviceId(String deviceId, Class<T> t) {
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = this.getConfigByDeviceId(deviceId);
        return this.getConfig(sysThirdPartyInterfaceConfig, t);
    }

    /**
     * 根据设备类型,接口配置对象类型，获取该设备所用的接口配置信息
     *
     * @param deviceType
     * @param t
     * @return
     */

    @Override
    public <T extends BaseConfigDTO> T getConfigByDeviceType(String deviceType, int projectId, int tenantId, Class<T> t) {
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = this.getConfigByDeviceType(deviceType, projectId, tenantId);

        if (sysThirdPartyInterfaceConfig == null) {
            return null;
        }

        return this.getConfig(sysThirdPartyInterfaceConfig, t);
    }


    @Override
    public <T extends BaseConfigDTO> T getConfigByProjectId(int projectId, int tenantId, Class<T> t) {
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = this.getConfigByDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode(), projectId, tenantId);

        if (sysThirdPartyInterfaceConfig == null) {
            sysThirdPartyInterfaceConfig = this.getConfigByDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode(), projectId, tenantId);

            if (sysThirdPartyInterfaceConfig == null) {
                return null;
            }
        }

        return this.getConfig(sysThirdPartyInterfaceConfig, t);
    }


    /**
     * 根据设备类型，获取该设备所用的接口配置信息
     *
     * @param deviceType
     * @param projectId
     * @param tenantId
     * @return
     */
    @Override
    public SysThirdPartyInterfaceConfig getConfigByDeviceType(String deviceType, int projectId, int tenantId) {
        if (!enableMiddleApi) {
            return null;
        }

        //获取当前设备类型的项目默认配置
        List<SysDeviceTypeThirdPartyConfig> deviceTypeConfigList = sysDeviceTypeThirdPartyConfigService.list(new QueryWrapper<SysDeviceTypeThirdPartyConfig>().lambda()
                .eq(SysDeviceTypeThirdPartyConfig::getDeviceTypeId, deviceType)
                .eq(SysDeviceTypeThirdPartyConfig::getProjectId, projectId)
                .eq(SysDeviceTypeThirdPartyConfig::getIsDefault, "1")
                .eq(SysDeviceTypeThirdPartyConfig::getTenantId, tenantId));

        if (CollectionUtil.isEmpty(deviceTypeConfigList)) {
//            throw new RuntimeException("设备类型：" + DeviceTypeEnum.getByCode(deviceType).getNote() + " 未找到接口配置信息！");
            return null;
        }

        //获取配置实体
        SysDeviceTypeThirdPartyConfig deviceTypeThirdPartyConfig = deviceTypeConfigList.get(0);
        return sysThirdPartyInterfaceConfigService.getById(deviceTypeThirdPartyConfig.getThirdPartyNo());
    }

    private <T extends BaseConfigDTO> T getConfig(SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig, Class<T> t) {
        String configJsonStr = sysThirdPartyInterfaceConfig.getJsonConfig();

        PlatformEnum platformEnum = PlatformEnum.getByValue(sysThirdPartyInterfaceConfig.getName());
        VersionEnum versionEnum = VersionEnum.getByNumber(sysThirdPartyInterfaceConfig.getVersion());

        if (platformEnum == null) {
            throw new RuntimeException("不存在名称为：" + sysThirdPartyInterfaceConfig.getName() + " 的对接平台");
        }
        if (versionEnum == null) {
            throw new RuntimeException("不存在：" + sysThirdPartyInterfaceConfig.getVersion() + " 的版本号");
        }
        if (!JSONUtil.isJsonObj(configJsonStr)) {
            throw new RuntimeException("接口：" + sysThirdPartyInterfaceConfig.getName() + sysThirdPartyInterfaceConfig.getVersion() + " 配置格式错误");
        }

        try {
            T resultDTO = JSONObject.parseObject(sysThirdPartyInterfaceConfig.getJsonConfig(), t);

            resultDTO.setPlatform(platformEnum);
            resultDTO.setVersion(versionEnum);
            resultDTO.setProjectId(sysThirdPartyInterfaceConfig.getProjectId());
            return resultDTO;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("接口：" + sysThirdPartyInterfaceConfig.getName() + sysThirdPartyInterfaceConfig.getVersion() + " 属性格式错误");
        }
    }

}
