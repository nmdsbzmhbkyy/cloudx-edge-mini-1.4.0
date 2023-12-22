package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.message.InfoObj;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;


/**
 * 华为中台 设备操作接口
 */
public interface HuaweiRemoteDeviceOperateService extends BaseRemote {

    /**
     * 异步下发指令
     *
     * @param configDTO
     * @param thirdDeviceId
     * @param serviceId
     * @param commandName
     * @param paramsJson
     * @param otherParams
     * @return
     */
    HuaweiRespondDTO commandsDown(HuaweiConfigDTO configDTO, String thirdDeviceId, String serviceId, String commandName, JSONObject paramsJson, JSONObject otherParams);


    /**
     * 命令下发 同步
     *
     * @param configDTO     配置信息
     * @param thirdDeviceId 设备第三方编码
     */
    HuaweiRespondDTO syncCommandsDown(HuaweiConfigDTO configDTO, String productId, String thirdDeviceId, String serviceId, String commandName, JSONObject otherParams);


    /**
     * 同步设置设备属性
     *
     * @param configDTO     配置信息
     * @param thirdDeviceId 设备第三方编码
     */
    HuaweiRespondDTO propertiesDown(HuaweiConfigDTO configDTO, String thirdDeviceId, String productId, JSONArray properties, JSONObject otherParams);


    /**
     * 下发设备消息 异步 可用
     *
     * @param configDTO     配置信息
     * @param thirdDeviceId 设备第三方编码
     */
    HuaweiRespondDTO messageDown(HuaweiConfigDTO configDTO, String thirdDeviceId, String productId, JSONObject messageInfo, JSONObject otherParams);


    /**
     * 子设备指令统一方法
     *
     * @param thirdDeviceId 设备ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams);
    /**
     * 子设备指令统一方法
     *
     * @param thirdDeviceId 设备ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams);

    /**
     * 子设备指令统一方法
     *
     * @param thirdDeviceId 设备ID
     * @param msgId         自定义消息ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams);
     /**
     * 子设备指令统一方法
     *
     * @param thirdDeviceId 设备ID
     * @param msgId         自定义消息ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    HuaweiRespondDTO objectManageSync(HuaweiConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams);
    /**
     * 子设备指令统一方法
     *
     * @param thirdDeviceId 设备ID
     * @param msgId         自定义消息ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams);

    /**
     * <p>发送通知信息到设备上（非媒体广告）</p>
     *
     * @param config 配置信息
     * @param infoObj 设备通知消息对象
     * @param thirdpartyCode 第三方设备ID
     * @return 是否发送成功
     * @author: 王良俊
     */
    HuaweiRespondDTO sendMessage(HuaweiConfigDTO config, InfoObj infoObj, String thirdpartyCode);

    /**
     * <p>清空设备消息</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    HuaweiRespondDTO cleanMessage(HuaweiConfigDTO config, String thirdpartyCode);
}
