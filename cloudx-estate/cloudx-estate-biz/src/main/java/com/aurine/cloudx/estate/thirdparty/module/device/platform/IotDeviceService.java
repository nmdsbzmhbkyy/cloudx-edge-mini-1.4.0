

package com.aurine.cloudx.estate.thirdparty.module.device.platform;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity.StreetLightDeviceStatus;

import java.util.List;

/**
 * <p>
 * 物联网设备服务实现
 * </p>
 * @author : 王良俊
 * @date : 2021-07-21 11:34:51
 */
public interface IotDeviceService extends BaseService {

    /**
    * <p>
    * 井盖同步
    * </p>
    *
    * @param
    * @return
    */
    void syncManholeCover();

    /**
    * <p>
    * 路灯-灯光调节
    * </p>
    *
    * @param
    * @return
    */
    boolean lightAdjustment(StreetLightDeviceStatus streetLightDeviceStatus);

    /**
    * <p>
    * 路灯-灯光调节（批量）
    * </p>
    *
    * @param
    * @return
    */
    List<String> lightAdjustmentBatch(StreetLightDeviceStatus streetLightDeviceStatus);

    /**
    * <p>
    * 获取设备点位
    * </p>
    *
    * @param
    * @return
    */
    void getDevicePoint();

    VersionEnum getVer();

    /**
    * <p>
    * 下发策略联动命令
    * </p>
    *
    * @param policyJson 策略json
    * @param productId 产品ID
    * @return 是否设置成功
    */
    boolean sendDevicePolicy(String policyJson, String productId, String deviceType);

}
