package com.aurine.cloudx.estate.thirdparty.module.wr20.callback.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20DeviceCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20FrameCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20PersonCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20RightCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.BaseService;
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
public class WR20CallbackFactory implements ApplicationContextAware {

    private static WR20CallbackFactory factoryInstance;//工厂单例
    private Map<String, WR20FrameCallbackService> frameCallbackServiceInstanceMap = new HashMap<>();
    private Map<String, WR20PersonCallbackService> personCallbackServiceMap = new HashMap<>();
    private Map<String, WR20RightCallbackService> rightCallbackServiceInstanceMap = new HashMap<>();
    private Map<String, WR20DeviceCallbackService> deviceCallbackServiceInstanceMap = new HashMap<>();

    /**
     * 初始化注入Bean
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //注入工厂实现
        WR20CallbackFactory.factoryInstance = applicationContext.getBean(WR20CallbackFactory.class);

        //注入各版本实现
        initBean(WR20FrameCallbackService.class, frameCallbackServiceInstanceMap, applicationContext);
        initBean(WR20PersonCallbackService.class, personCallbackServiceMap, applicationContext);
        initBean(WR20RightCallbackService.class, rightCallbackServiceInstanceMap, applicationContext);
        initBean(WR20DeviceCallbackService.class, deviceCallbackServiceInstanceMap, applicationContext);
    }

    public static WR20CallbackFactory getFactoryInstance() {
        return factoryInstance;
    }


    /**
     * 获取框架接口实例
     *
     * @return
     */
    public WR20FrameCallbackService getFrameService(String version) {

        if (frameCallbackServiceInstanceMap.get(version) == null){
            throw new RuntimeException("未找到 WR20FrameService 实例:" + version);
        }
        return frameCallbackServiceInstanceMap.get(version);
    }

    /**
     * 获取人员接口实例
     *
     * @return
     */
    public WR20PersonCallbackService getPersonService(String version) {
        VersionEnum versionEnum = getVerson(version);

        if (personCallbackServiceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 WR20PersonService 实例:" + versionEnum.code);
        }
        return personCallbackServiceMap.get(versionEnum.code);
    }

    /**
     * 获取权限接口实例
     *
     * @return
     */
    public WR20RightCallbackService getRightService(String version) {
        VersionEnum versionEnum = getVerson(version);

        if (rightCallbackServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 WR20RightService 实例:" + versionEnum.code);
        }
        return rightCallbackServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取设备接口实例
     *
     * @return
     */
    public WR20DeviceCallbackService getDeviceService(String version) {
        VersionEnum versionEnum = getVerson(version);

        if (deviceCallbackServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 WR20DeviceService 实例:" + versionEnum.code);
        }
        return deviceCallbackServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取项目配置的版本号
     *
     * @param
     * @return
     */
    private VersionEnum getVerson(String version) {
        return VersionEnum.getByCode(version);
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
