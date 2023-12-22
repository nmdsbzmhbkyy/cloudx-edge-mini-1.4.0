package com.aurine.cloudx.estate.thirdparty.business.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务实现工厂，用于分配具体的业务实现
 *
 * @ClassName: ServiceFactory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-14 8:44
 * @Copyright:
 */
//@Component
public class ServiceFactory {

    @Resource
    private static Map<String, IService> serviceInstanceMap = new HashMap<>();//所有service实例

    /**
     * 通过原有的业务实例对象，自动匹配获取第三方或原有业务实例。
     * 获取规则依赖命名规范: 第三方对接的业务系统命名应为 平台名 + 原服务 + 版本Code
     *
     * @param serviceImpl 原系统业务实例
     * @param <T>
     * @return 第三方对接业务实例，如不存在，返回原系统业务实例
     */
    public static <T extends IService> T getService(T serviceImpl) {
        //获取当前项目配置
//        ProjectContextHolder.getProjectId()

        //获取实例class，用于拼接实例对象名
        String simpleServiceName  =  PlatformEnum.BUSINESS_WR20.code + serviceImpl.getClass().getSimpleName() + VersionEnum.V1.code;
        return (T) serviceInstanceMap.get(simpleServiceName);
    }

}
