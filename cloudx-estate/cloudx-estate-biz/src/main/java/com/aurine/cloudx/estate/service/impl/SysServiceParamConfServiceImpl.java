package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.entity.SysServiceParamConf;
import com.aurine.cloudx.estate.mapper.SysServiceParamConfMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.service.SysServiceParamConfService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamFormService;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.vo.SysServiceParamConfVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 平台设备参数定义表(SysServiceParamConf)表服务实现类
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:38
 */
@Slf4j
@Service
public class SysServiceParamConfServiceImpl extends ServiceImpl<SysServiceParamConfMapper, SysServiceParamConf>
        implements SysServiceParamConfService {

    @Autowired
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Autowired
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Autowired
    private SysDeviceProductMapService sysDeviceProductMapService;

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();

    private static Map<String, String> pServiceIdDict = new HashMap();


    @Override
    public List<SysServiceParamConfVo> listParamConf(List<String> serviceIdList) {
        return baseMapper.listParamConfV2(serviceIdList);
    }

    @Override
    public String getDeviceParamFormData(List<String> serviceIdList, String deviceId) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        if (deviceInfo != null) {
            SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                    .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
            DeviceParamServiceFactory deviceParamServiceFactory = DeviceParamFactoryProducer.getFactory(deviceId);
            if (deviceParamServiceFactory == null) {
                log.error("这台设备所属的设备类型未配置中台对接参数 deviceId：{}", deviceId);
            }
            DeviceParamFormService paramService = deviceParamServiceFactory.getParamFormService(productMap.getManufacture(), deviceInfo.getDeviceType());
            if (paramService != null) {
                try {
                    return paramService.getParamFormJson(serviceIdList, deviceId);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public List<String> getRebootServiceIdList() {
        return baseMapper.getRebootServiceIdList();
    }

    @Override
    public List<String> getValidServiceIdList(List<String> serviceIdList) {
        List<SysServiceParamConf> list = this.list(new QueryWrapper<SysServiceParamConf>().lambda().in(SysServiceParamConf::getServiceId, serviceIdList).select(SysServiceParamConf::getServiceId));
        return list.stream().map(SysServiceParamConf::getServiceId).distinct().collect(Collectors.toList());
    }


}