package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;

/**
 * 华为中台 设备类型转换 工具类
 *
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-11
 * @Copyright:
 */
public class HuaweiDeviceTypeUtil {

    /**
     * 通过产品品类id、产品名称，获取当前产品的设备类型
     *
     * @param modelId
     * @param produceName
     * @return
     */
    public static String getDeviceType(String modelId, String produceName) {
        DeviceTypeEnum result = null;
        switch (modelId) {
            case "020301":
                //室内机
                result = DeviceTypeEnum.INDOOR_DEVICE;
                break;
            case "020302"://可视门禁
                if (produceName.indexOf("区口") >= 0 || produceName.indexOf("大门") >= 0) {
                    //区口机
                    result = DeviceTypeEnum.GATE_DEVICE;
                } else if (produceName.indexOf("梯口") >= 0) {
                    //梯口机
                    result = DeviceTypeEnum.LADDER_WAY_DEVICE;
                }
                break;
            default:

        }

        return result == null ? null : result.getCode();
    }
}
