

package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.Collection;


/**
 * 华为 中台 设备设备对象操作接口 异步（ 设备 下放操作，如给设备添加卡片、面部识别、媒体等）
 * 用于发送设备下属对象操作指令，通常为子设备业务命令
 *
 * @ClassName: HuaweiRemoteDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 14:02
 * @Copyright:
 */
public interface AurineEdgeRemoteDeviceService extends BaseRemote {

    /**
     * 新增一台设备
     *
     * @param configDTO
     * @param deviceInfoDTO
     */
    AurineEdgeRespondDTO addDevice(AurineEdgeConfigDTO configDTO, AurineEdgeDeviceInfoDTO deviceInfoDTO);
    /**
     * 激活一台设备
     *
     * @param configDTO
     * @param deviceInfoDTO
     */
    AurineEdgeRespondDTO activeDevice(AurineEdgeConfigDTO configDTO, AurineEdgeDeviceInfoDTO deviceInfoDTO);

    /**
     * 新增一台设备(带产品key的，新的物联设备才会使用这个进行绑定设备)
     *
     * @param configDTO
     * @param uuid
     * @param productKey
     */
    AurineEdgeRespondDTO addDevice(AurineEdgeConfigDTO configDTO, String uuid, String productKey);

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     */
    AurineEdgeRespondDTO addDeviceBatch(AurineEdgeConfigDTO configDTO, String[] uuidArray);

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     */
    AurineEdgeRespondDTO addDeviceBatch(AurineEdgeConfigDTO configDTO, String[] uuidArray, String productKey);


    /**
     * 删除设备
     *
     * @param configDTO
     * @param deviceThirdId 设备第三方id
     */
    AurineEdgeRespondDTO delDevice(AurineEdgeConfigDTO configDTO, AurineEdgeDeviceInfoDTO deviceInfoDTO);


    /**
     * 查询产品列表
     *
     * @param configDTO
     */
    AurineEdgeRespondDTO queryProducts(AurineEdgeConfigDTO configDTO, Integer pageNo, Integer pageSize);

    /**
     * 查询产品列表(设备状态信息就在这里面 devStatus 设备在线状态)
     *
     * @param configDTO
     */
    AurineEdgeRespondDTO queryDevices(AurineEdgeConfigDTO configDTO, Integer pageNo, Integer pageSize, String productId, String fDevId);

    /**
     * <p>设置驱动设备参数</p>
     *
     * @param configDTO 配置
     * @param params 参数列表
     * @return 下发结果
     * @author : 王良俊
     */
    AurineEdgeRespondDTO setEdgeParam(AurineEdgeConfigDTO configDTO, ProjectDeviceInfo deviceInfo, JSONObject params, String objName);

    /**
     * <p>请求获取设备参数</p>
     *
     * @param configDTO 配置
     * @param deviceInfo 设备信息
     * @param serviceIdList 服务ID列表
     * @author: 王良俊
     */
    void getEdgeParam(AurineEdgeConfigDTO configDTO, ProjectDeviceInfo deviceInfo, Collection<String> serviceIdList);
    /**
    * @Author 黄健杰
    * @Description 通过设备信息服务，下发设备通讯数据
    * @Date  2022/2/7
    * @Param
    * @return
    **/
    CallBackData sendDataByDeviceInfo(AurineEdgeConfigDTO configDTO, ProjectDeviceInfo deviceInfo, String commandName, AurineEdgeDeviceInfoDTO params);
}
