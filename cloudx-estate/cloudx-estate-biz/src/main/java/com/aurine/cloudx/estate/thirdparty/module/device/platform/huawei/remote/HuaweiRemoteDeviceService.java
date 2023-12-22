

package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;


/**
 * 华为 中台 设备设备对象操作接口 异步（ 设备 下放操作，如给设备添加卡片、面部识别、媒体等）
 * 用于发送设备下属对象操作指令，通常为子设备业务命令
 *
 * @ClassName: HuaweiRemoteDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 14:02
 * @Copyright:
 */
public interface HuaweiRemoteDeviceService extends BaseRemote {

    /**
     * 新增一台设备
     *
     * @param configDTO
     * @param uuid
     */
    HuaweiRespondDTO addDevice(HuaweiConfigDTO configDTO, String uuid);

    /**
     * 新增一台设备(带产品key的，新的物联设备才会使用这个进行绑定设备)
     *
     * @param configDTO
     * @param uuid
     * @param productKey
     */
    HuaweiRespondDTO addDevice(HuaweiConfigDTO configDTO, String uuid, String productKey);

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     */
    HuaweiRespondDTO addDeviceBatch(HuaweiConfigDTO configDTO, String[] uuidArray);

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     */
    HuaweiRespondDTO addDeviceBatch(HuaweiConfigDTO configDTO, String[] uuidArray, String productKey);


    /**
     * 删除设备
     *
     * @param configDTO
     * @param deviceThirdId 设备第三方id
     */
    HuaweiRespondDTO delDevice(HuaweiConfigDTO configDTO, String deviceThirdId);


    /**
     * 查询产品列表
     *
     * @param configDTO
     */
    HuaweiRespondDTO queryProducts(HuaweiConfigDTO configDTO, Integer pageNo, Integer pageSize);

    /**
     * 查询产品列表(设备状态信息就在这里面 devStatus 设备在线状态)
     *
     * @param configDTO
     */
    HuaweiRespondDTO queryDevices(HuaweiConfigDTO configDTO, Integer pageNo, Integer pageSize, String productId, String fDevId);

}
