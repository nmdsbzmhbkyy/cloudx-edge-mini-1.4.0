package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysProductService;
import com.aurine.cloudx.estate.vo.SysServiceParamConfVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 以产品为维度进行平台设备参数配置项管理(SysProductParamCategory)表服务接口
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:58
 */
public interface SysProductServiceService extends IService<SysProductService> {

    /**
    * <p>
    * 根据设备ID和服务ID列表获取设备参数VO对象列表
    * </p>
    *
    * @param deviceId 设备ID
    * @param serviceIdList 设备服务ID列表
    * @return 参数vo对象列表
    * @author: 王良俊
    */
    List<SysServiceParamConfVo> listParamConf(List<String> serviceIdList, String deviceId);
}