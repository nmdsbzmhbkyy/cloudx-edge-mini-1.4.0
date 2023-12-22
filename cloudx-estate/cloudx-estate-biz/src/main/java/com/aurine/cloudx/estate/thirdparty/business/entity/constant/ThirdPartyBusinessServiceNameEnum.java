package com.aurine.cloudx.estate.thirdparty.business.entity.constant;

import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl.Wr20ProjectHousePersonRelServiceImplV1;
import lombok.AllArgsConstructor;

/**
 * 第三方业务水平扩展 注册 枚举
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-18
 * @Copyright:
 */
@AllArgsConstructor
public enum ThirdPartyBusinessServiceNameEnum {
    WebProjectHousePersonRelServiceImpl_WR20("wr20WebProjectHousePersonRelServiceImpl", Wr20ProjectHousePersonRelServiceImplV1.class, ProjectHousePersonRelService.class, ThirdPartyBusinessPlatformEnum.WR20);


    public String serviceName;
    public Class implClass;
    public Class interfaceClass;
    public ThirdPartyBusinessPlatformEnum platformEnum;


    /**
     * 通过平台类型和接口，获取映射的服务名
     * 可用于通过服务名来获得Bean实例
     *
     * @param interfaceClass 接口类型
     * @param platformEnum   平台枚举
     * @return
     */
    public static ThirdPartyBusinessServiceNameEnum getByPlatformAndInterface(Class interfaceClass, ThirdPartyBusinessPlatformEnum platformEnum) {
        for (ThirdPartyBusinessServiceNameEnum serviceNameEnum : ThirdPartyBusinessServiceNameEnum.values()) {
            if (serviceNameEnum.interfaceClass == interfaceClass && serviceNameEnum.platformEnum == platformEnum) {
                return serviceNameEnum;
            }
        }
        return null;
    }
}
