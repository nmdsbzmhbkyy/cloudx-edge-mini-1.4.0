package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service.AbstractDeviceEventService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 如果不在支持的设备中则默认使用这个，不进行任何处理
 * </p>
 * @author : 王良俊
 * @date : 2021-08-06 16:54:57
 */
@Service
public class DefaultEventService extends AbstractDeviceEventService {

    @Override
    public void eventHandle(String eventJson, ProjectDeviceInfo deviceInfo) {

    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> set = new HashSet<>();
        set.add(DeviceManufactureEnum.DEFAULT_DEVICE);
        return set;
    }

}
