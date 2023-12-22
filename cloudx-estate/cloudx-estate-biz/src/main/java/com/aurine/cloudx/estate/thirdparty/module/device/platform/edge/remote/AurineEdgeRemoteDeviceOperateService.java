package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.message.NoticeMessageInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.List;


/**
 * 华为中台 设备操作接口
 */
public interface AurineEdgeRemoteDeviceOperateService extends BaseRemote {

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
    AurineEdgeRespondDTO commandsDown(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String commandName, JSONObject paramsJson, JSONObject otherParams);

    /**
     * 响应指令
     *
     * @param configDTO
     * @param thirdDeviceId
     * @param serviceId
     * @param commandName
     * @param paramsJson
     * @param otherParams
     * @param msgId
     * @return
     */
    AurineEdgeRespondDTO commandsDown(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String commandName, JSONObject paramsJson, JSONObject otherParams,String msgId);
    /**
     * 命令下发 同步
     *
     * @param configDTO     配置信息
     * @param thirdDeviceId 设备第三方编码
     */
    AurineEdgeRespondDTO syncCommandsDown(AurineEdgeConfigDTO configDTO, String productId, String thirdDeviceId, String serviceId, String commandName, JSONObject otherParams);


    /**
     * 同步设置设备属性
     *
     * @param configDTO     配置信息
     * @param thirdDeviceId 设备第三方编码
     */
    AurineEdgeRespondDTO propertiesDown(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String productId, JSONArray properties, JSONObject otherParams);


    /**
     * 下发设备消息 异步 可用
     *
     * @param configDTO     配置信息
     * @param thirdDeviceId 设备第三方编码
     */
    AurineEdgeRespondDTO messageDown(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String productId, JSONObject messageInfo, JSONObject otherParams);


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
    AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams);

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
    AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams);

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
    AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams);

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
    AurineEdgeRespondDTO objectManageSync(AurineEdgeConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams);

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
     * @param projectDeviceInfo   设备对象
     * @param doorKeyIdList
     * @param priotity 优先级
     * @return
     */
    AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams, ProjectDeviceInfo projectDeviceInfo, List doorKeyIdList,Integer priotity);

    /**
     * <p>广告发送</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    public AurineEdgeRespondDTO mediaAdManage(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams);

    /**
         * <p>发送通知信息到设备上（非媒体广告）</p>
         *
         * @param config 配置信息
         * @param infoObj 设备通知消息对象
         * @param thirdpartyCode 第三方设备ID
         * @return 是否发送成功
         * @author: 王良俊
         */
    AurineEdgeRespondDTO sendMessage(AurineEdgeConfigDTO config, NoticeMessageInfo infoObj, String thirdpartyCode);

    /**
     * <p>清空设备消息</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    AurineEdgeRespondDTO cleanMessage(AurineEdgeConfigDTO config, String thirdpartyCode);

    /**
     * 子设备指令统一方法
     *
     * @param thirdDeviceId 设备ID
     * @return
     */
    AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO,String thirdDeviceId,String productId,JSONObject jsonObject);

}
