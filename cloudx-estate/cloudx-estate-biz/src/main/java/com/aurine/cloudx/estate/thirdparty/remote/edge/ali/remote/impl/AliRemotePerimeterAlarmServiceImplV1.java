package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core.AliEdgeDataConnector;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote.AliRemotePerimeterAlarmService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.util.AliEdgeProjectUtil;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.util.AliEdgeRespondUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-10 15:35
 * @Copyright:
 */
@Service
@Slf4j
public class AliRemotePerimeterAlarmServiceImplV1 implements AliRemotePerimeterAlarmService {
    @Resource
    private AliEdgeDataConnector aliEdgeDataConnector;
    @Resource
    private AliEdgeProjectUtil aliEdgeProjectUtil;

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }


    /**
     * 查询通道
     *
     * @param deviceId  设备id
     * @param projectId 项目id
     * @return
     */
    @Override
    public JSONObject channelListQuery(String deviceId, Integer projectId) {
        String AliProijectId = aliEdgeProjectUtil.getAliEdgeProject(projectId);
        Map<String, Object> queryParamMap = new HashMap<>();
        queryParamMap.put("deviceId", deviceId);
        queryParamMap.put("projectId", AliProijectId);

        JSONObject respJson = aliEdgeDataConnector.get("border/channelListQuery", queryParamMap);
        log.info("[阿里边缘] 查询到周界主机通道信息：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);
        return respJson;

    }

    /**
     * 通道设置
     *
     * @param deviceId  设备SN
     * @param channelId 通道id
     * @param operation 操作：0未知，1布防，2撤防
     * @param projectId 项目id
     * @return
     */
    @Override
    public JSONObject channelOperate(String deviceId, String channelId, String operation, Integer projectId) {
        String AliProijectId = aliEdgeProjectUtil.getAliEdgeProject(projectId);
        Map<String, Object> queryParamMap = new HashMap<>();
        queryParamMap.put("deviceId", deviceId);
        queryParamMap.put("projectId", AliProijectId);
        queryParamMap.put("channelId", channelId);
        queryParamMap.put("operation", operation);

        JSONObject respJson = aliEdgeDataConnector.post("border/channelOperation", queryParamMap,null);
        log.info("[阿里边缘] 周界报警通道设置结果：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);
        return respJson;
    }

    /**
     * 通道状态查询
     *
     * @param deviceId  设备SN
     * @param channelId 通道id
     * @param projectId 项目id
     * @return
     */
    @Override
    public JSONObject channelStatusQuery(String deviceId, String channelId, Integer projectId) {
        String AliProijectId = aliEdgeProjectUtil.getAliEdgeProject(projectId);
        Map<String, Object> queryParamMap = new HashMap<>();
        queryParamMap.put("deviceId", deviceId);
        queryParamMap.put("projectId", AliProijectId);
        queryParamMap.put("channelId", channelId);

        JSONObject respJson = aliEdgeDataConnector.get("border/channelStatusQuery", queryParamMap);
        log.info("[阿里边缘] 查询到周界主机通道状态：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);
        return respJson;
    }

    /**
     * 消除告警
     *
     * @param deviceId  设备SN
     * @param channelId 通道id
     * @param projectId 项目id
     */
    @Override
    public JSONObject clearAlarm(String deviceId, String channelId, Integer projectId) {

        String AliProijectId = aliEdgeProjectUtil.getAliEdgeProject(projectId);
        Map<String, Object> queryParamMap = new HashMap<>();
        queryParamMap.put("deviceId", deviceId);
        queryParamMap.put("projectId", AliProijectId);
        queryParamMap.put("channelId", channelId);

        JSONObject respJson = aliEdgeDataConnector.post("border/clearAlarm", queryParamMap,null);
        log.info("[阿里边缘] 清空告警结果为：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);
        return respJson;
    }
}
