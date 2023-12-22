package com.aurine.cloudx.estate.thirdparty.module.device.factory;

import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 工厂生成器,根据设备ID选择工厂实例
 *
 * @ClassName: DeviceFactoryProducer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29 9:01
 * @Copyright:
 */

@Component
@Slf4j
public class DeviceFactoryProducer implements ApplicationContextAware {
    private static DeviceFactoryProducer deviceFactoryProducer;

    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    /**
     * 根据设备ID获取工厂
     *
     * @param deviceId
     * @return
     */
    public static AbstractDeviceFactory getFactory(String deviceId) {
        //获取到项目接口配置信息
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = deviceFactoryProducer.sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId);
        return getFactory(sysThirdPartyInterfaceConfig);
    }

    /**
     * 根据产品型号获取工厂
     *
     * @param productCode
     * @return
     */
    public static AbstractDeviceFactory getFactoryByProductId(String productId) {
        //获取到项目接口配置信息
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = deviceFactoryProducer.sysDeviceTypeThirdPartyConfigService.getConfigByProductId(productId);
        return getFactory(sysThirdPartyInterfaceConfig);
    }

    /**
     * 根据项目编号获取工厂
     *
     * @param projectId
     * @return
     */
    public static AbstractDeviceFactory getFactory(int projectId) {
        //获取到项目接口配置信息
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = deviceFactoryProducer.sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId);
        return getFactory(sysThirdPartyInterfaceConfig);
    }

    /**
     * 根据设备类型、项目和租户信息获取到接口工厂
     *
     * @param deviceType
     * @param projectId
     * @param tenantId
     * @return
     */
    public static AbstractDeviceFactory getFactory(String deviceType, int projectId, int tenantId) {
        //获取到项目接口配置信息
        SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = deviceFactoryProducer.sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId);
        return getFactory(sysThirdPartyInterfaceConfig);
    }

    private static AbstractDeviceFactory getFactory(SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig) {

        //获取到项目接口配置信息
        String platformName;
        String version;
        VersionEnum versionEnum;
        if (sysThirdPartyInterfaceConfig != null) {
            platformName = sysThirdPartyInterfaceConfig.getName();
            version = sysThirdPartyInterfaceConfig.getVersion();
            versionEnum = VersionEnum.getByNumber(version);
        } else {
            log.info("无配置信息");
            platformName = PlatformEnum.OTHER.value;
            versionEnum = VersionEnum.V1;
        }


        //根据设备接入厂商，分配对应接口实现
        if (platformName.equalsIgnoreCase(PlatformEnum.HUAWEI_MIDDLE.value)) {
            return DeviceHuaweiFactory.getFactory(versionEnum);
        } else if (platformName.equalsIgnoreCase(PlatformEnum.AURINE_MIDDLE.value)) {
            return DeviceAurineFactory.getFactory(versionEnum);
        } else if (platformName.equalsIgnoreCase(PlatformEnum.AURINE_EDGE_MIDDLE.value)) {
            return DeviceAurineEdgeFactory.getFactory(versionEnum);
        }  else if (platformName.equalsIgnoreCase(PlatformEnum.AURINE_PARKING.value)) {
            return DeviceParkingFactory.getFactory(versionEnum);
        } else if (platformName.equalsIgnoreCase(PlatformEnum.OTHER.value)) {
            return DeviceOtherFactory.getFactory(versionEnum);
        } else if (platformName.equalsIgnoreCase(PlatformEnum.YUSHI.value)) {
            return DeviceYushiFactory.getFactory(versionEnum);
        } else {
            throw new RuntimeException("设备对接参数配置错误");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        deviceFactoryProducer = applicationContext.getBean(DeviceFactoryProducer.class);
        deviceFactoryProducer.projectDeviceInfoProxyService = applicationContext.getBean(ProjectDeviceInfoProxyService.class);
        deviceFactoryProducer.sysDeviceTypeThirdPartyConfigService = applicationContext.getBean(SysDeviceTypeThirdPartyConfigService.class);
    }
}
