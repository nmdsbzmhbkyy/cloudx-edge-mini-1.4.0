package com.aurine.cloudx.estate.thirdparty.module.wr20.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * WR20 实例工厂
 *
 * @ClassName: WR20Factory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 10:30
 * @Copyright:
 */
@Component
public class WR20Factory implements ApplicationContextAware {

    private static WR20Factory factoryInstance;//工厂单例
    private  Map<String, WR20FrameService> frameServiceInstanceMap = new HashMap<>();
    private  Map<String, WR20PersonService> personServiceInstanceMap = new HashMap<>();
    private  Map<String, WR20RightService> rightServiceInstanceMap = new HashMap<>();
    private  Map<String, WR20DeviceService> deviceServiceInstanceMap = new HashMap<>();

    /**
     * 初始化注入Bean
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //注入工厂实现
        WR20Factory.factoryInstance = applicationContext.getBean(WR20Factory.class);

        //注入各版本实现
        initBean(WR20FrameService.class, frameServiceInstanceMap, applicationContext);
        initBean(WR20PersonService.class, personServiceInstanceMap, applicationContext);
        initBean(WR20RightService.class, rightServiceInstanceMap, applicationContext);
        initBean(WR20DeviceService.class, deviceServiceInstanceMap, applicationContext);

    }

    public static WR20Factory getFactoryInstance(){
        return factoryInstance;
    }


    /**
     * 获取框架接口实例
     *
     * @return
     */
    public WR20FrameService getFrameService(int projectId) {
        VersionEnum versionEnum = getVerson(projectId);

        if (frameServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 WR20FrameService 实例:" + versionEnum.code);
        }
        return frameServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取人员接口实例
     *
     * @return
     */
    public WR20PersonService getPersonService(int projectId) {
        VersionEnum versionEnum = getVerson(projectId);

        if (personServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 WR20PersonService 实例:" + versionEnum.code);
        }
        return personServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取权限接口实例
     *
     * @return
     */
    public WR20RightService getRightService(int projectId) {
        VersionEnum versionEnum = getVerson(projectId);

        if (rightServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 WR20RightService 实例:" + versionEnum.code);
        }
        return rightServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取设备接口实例
     *
     * @return
     */
    public WR20DeviceService getDeviceService(int projectId) {
        VersionEnum versionEnum = getVerson(projectId);

        if (deviceServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 WR20DeviceService 实例:" + versionEnum.code);
        }
        return deviceServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取项目配置的版本号
     *
     * @param projectId
     * @return
     */
    private VersionEnum getVerson(int projectId) {
        //TODO: 等待项目配置表 王伟 @since 2020-08-04
        return VersionEnum.V1;
    }

    /**
     * 初始化bean，并注入工厂
     */
    private <T extends BaseService> void initBean(Class<T> t, Map instanceMap, ApplicationContext applicationContext) throws BeansException {

        Map<String, T> beanMap = applicationContext.getBeansOfType(t);

        beanMap.forEach((key, instance) -> {
            instanceMap.put(instance.getVersion(), instance);
        });
    }
}
