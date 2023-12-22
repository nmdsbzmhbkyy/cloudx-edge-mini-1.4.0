package com.aurine.cloudx.estate.thirdparty.module.device.factory.param;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.SysThirdPartyInterfaceConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>设备参数服务工厂提供类</p>
 * @author : 王良俊
 * @date : 2021-10-20 09:57:11
 */
@Component
@Slf4j
public class DeviceParamFactoryProducer implements ApplicationContextAware {

    private static final Map<PlatformEnum, DeviceParamServiceFactory> beanMap = new HashMap<>();

    private static SysThirdPartyInterfaceConfigService sysThirdPartyInterfaceConfigService;

    private static ProjectDeviceInfoProxyService projectDeviceInfoService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        sysThirdPartyInterfaceConfigService = applicationContext.getBean(SysThirdPartyInterfaceConfigService.class);
        projectDeviceInfoService = applicationContext.getBean(ProjectDeviceInfoProxyService.class);

        Map<String, DeviceParamServiceFactory> beansOfType = applicationContext.getBeansOfType(DeviceParamServiceFactory.class);
        Collection<DeviceParamServiceFactory> values = beansOfType.values();
        if (CollUtil.isNotEmpty(values)) {
            values.forEach(factory -> {
                beanMap.put(factory.getPlatformEnum(), factory);
            });
        }
    }

    /**
     * <p>根据中台枚举获取参数服务工厂实例</p>
     *
     * @param platformEnum 中台枚举
     * @return 设备参数服务工厂
     */
    public static DeviceParamServiceFactory getFactory(@Nonnull PlatformEnum platformEnum) {
        return beanMap.get(platformEnum);
    }

    public static DeviceParamServiceFactory getFactory(@Nonnull Integer projectId, @Nonnull String deviceType) {
        if (StrUtil.isEmpty(deviceType)) {
            log.error("参数服务获取失败 projectId：{} deviceType：{}", projectId, deviceType);
            return null;
        }
        SysThirdPartyInterfaceConfig configByDeviceType = sysThirdPartyInterfaceConfigService.getConfigByDeviceType(projectId, deviceType);
        if (configByDeviceType == null) {
            log.error("参数服务获取失败-未找到对应的中台配置 projectId：{} deviceType：{}", projectId, deviceType);
            return null;
        }
        String name = configByDeviceType.getName();
        return beanMap.get(PlatformEnum.getByValue(name));
    }

    public static DeviceParamServiceFactory getFactory(@Nonnull String deviceId) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        if (deviceInfo == null) {
            log.error("参数服务获取失败-未找到指定ID设备 deviceId：{}", deviceId);
            return null;
        }
        return getFactory(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
    }

}
