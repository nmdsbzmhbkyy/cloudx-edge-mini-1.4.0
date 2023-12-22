package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysDeviceTypeModelTypeConfig;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>产品和设备关联关系服务</p>
 *
 * @author 王良俊
 * @date "2022/4/18"
 */
public interface SysDeviceTypeModelTypeConfigService extends IService<SysDeviceTypeModelTypeConfig> {

    /**
     * <p>根据产品品类标识获取对应的设备类型列表</p>
     *
     * @param
     * @return
     * @author 王良俊
     */
    List<String> listDeviceType(String productModelType, PlatformEnum platformEnum);

}
