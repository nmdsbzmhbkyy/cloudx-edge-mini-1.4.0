

package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectNotice;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.Map;

/**
 * 阿里周界报警设别接入服务
 */
public interface AliRemotePerimeterAlarmService extends BaseRemote {

    /**
     * 获取通道列表
     *
     * @param deviceId  设备id
     * @param projectId 项目id
     * @return
     */
    JSONObject channelListQuery(String deviceId, Integer projectId);


    /**
     * 通道设置
     *
     * @param deviceId  设备SN
     * @param channelId 通道id
     * @param operation 操作：0未知，1布防，2撤防
     * @param projectId 项目id
     * @return
     */
    JSONObject channelOperate(String deviceId, String channelId, String operation, Integer projectId);

    /**
     * 通道状态查询
     *
     * @param deviceId  设备SN
     * @param channelId 通道id
     * @param projectId 项目id
     * @return
     */
    JSONObject channelStatusQuery(String deviceId, String channelId, Integer projectId);


    /**
     * 消除告警
     *
     * @param deviceId  设备SN
     * @param channelId 通道id
     * @param projectId 项目id
     */
    JSONObject clearAlarm(String deviceId, String channelId,Integer projectId);

}
