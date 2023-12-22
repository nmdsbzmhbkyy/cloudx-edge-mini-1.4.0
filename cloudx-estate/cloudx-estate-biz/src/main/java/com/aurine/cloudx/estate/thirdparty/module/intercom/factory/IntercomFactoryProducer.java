package com.aurine.cloudx.estate.thirdparty.module.intercom.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.config.DongdongConfig;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.config.TencentConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 工厂生成器,根据服务选择工厂实例
 *
 * @ClassName: DeviceFactoryProducer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29 9:01
 * @Copyright:
 */

@Component
@Slf4j
public class IntercomFactoryProducer implements ApplicationContextAware {
    private static IntercomFactoryProducer intercomFactoryProducer;
    @Value("${thirdparty.intercom.tencent.version}")
    private String version;
    /**
     * 根据serviceCode获取工厂
     *
     * @param serviceCode
     * @return
     */
    public static AbstractIntercomFactory getFactory(String serviceCode) {
        //获取到项目接口配置信息
        String platformName;
        String version;
        VersionEnum versionEnum = VersionEnum.V1;


        //根据设备接入厂商，分配对应接口实现
        if (serviceCode.equalsIgnoreCase(PlatformEnum.INTERCOM_DONGDONG.code)) {
            versionEnum = VersionEnum.getByNumber(DongdongConfig.version);
            return IntercomDongDongFactory.getFactory(versionEnum);
        } else if (serviceCode.equalsIgnoreCase(PlatformEnum.INTERCOM_TENCENT.code)) {
            versionEnum = VersionEnum.getByNumber(TencentConfig.version);
            return IntercomTencentFactory.getFactory(versionEnum);
        } else {
            return IntercomOtherFactory.getFactory(versionEnum);
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        intercomFactoryProducer = applicationContext.getBean(IntercomFactoryProducer.class);
    }
}
