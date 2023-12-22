package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.remote.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.remote.DongDongIntercomRemoteService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>咚咚 remote 工厂类</p>
 * @ClassName: DongDongRemoteServiceFactory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-13 17:23
 * @Copyright:
 */
@Component
public class DongDongRemoteServiceFactory implements ApplicationContextAware {

   private static Map<String, DongDongIntercomRemoteService> instanceMap;

    /**
     * 获取接口下所有系统的接口调用实现，并注入Map中
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, DongDongIntercomRemoteService> map = applicationContext.getBeansOfType(DongDongIntercomRemoteService.class);
        instanceMap = new HashMap<>();
        map.forEach((key, value) -> instanceMap.put(value.getVersion(), value));
    }

    /**
     * <p>根据对接的系统获取实例</p>
     *
     * @param versionEnum
     * @return
     */
    public static DongDongIntercomRemoteService getInstance(VersionEnum versionEnum) {
        if (instanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到DongDongRemoteService实例:" + versionEnum.code);
        }
        return instanceMap.get(versionEnum.code);
    }

}
