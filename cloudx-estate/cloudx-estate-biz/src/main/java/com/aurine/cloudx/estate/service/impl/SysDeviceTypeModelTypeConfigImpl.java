package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.SysDeviceTypeModelTypeConfig;
import com.aurine.cloudx.estate.mapper.SysDeviceTypeModelTypeConfigMapper;
import com.aurine.cloudx.estate.service.SysDeviceTypeModelTypeConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysDeviceTypeModelTypeConfigImpl extends ServiceImpl<SysDeviceTypeModelTypeConfigMapper, SysDeviceTypeModelTypeConfig> implements SysDeviceTypeModelTypeConfigService {

    @Override
    public List<String> listDeviceType(String productModelType, PlatformEnum platformEnum) {
        List<SysDeviceTypeModelTypeConfig> configList = this.list(new LambdaQueryWrapper<SysDeviceTypeModelTypeConfig>()
                .eq(SysDeviceTypeModelTypeConfig::getProductModelType, productModelType)
                .eq(SysDeviceTypeModelTypeConfig::getPlatformName, platformEnum.value));
        if (CollUtil.isNotEmpty(configList)) {
            List<String> deviceTypeList = configList.stream().map(SysDeviceTypeModelTypeConfig::getDeviceTypeId).collect(Collectors.toList());
            return deviceTypeList;
        }
        return new ArrayList<>();
    }
}