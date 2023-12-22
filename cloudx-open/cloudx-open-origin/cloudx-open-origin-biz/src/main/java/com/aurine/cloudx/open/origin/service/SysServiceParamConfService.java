package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.SysServiceParamConfVo;
import com.aurine.cloudx.open.origin.entity.SysServiceParamConf;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 平台设备参数定义表(SysServiceParamConf)表服务接口
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:38
 */
public interface SysServiceParamConfService extends IService<SysServiceParamConf> {


    List<SysServiceParamConfVo> listParamConf(List<String> serviceIdList);

    String getDeviceParamFormData(List<String> serviceIdList, String deviceId);

    List<String> getRebootServiceIdList();
    /**
     * <p>
     *  获取到可以进行参数设置的参数服务ID
     * </p>
     *
     * @param serviceIdList 需要继续筛选的参数服务ID
    */
    List<String> getValidServiceIdList(List<String> serviceIdList);

}