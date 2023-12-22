package com.aurine.cloudx.estate.thirdparty.module.edge.cascade.remote;

import com.aurine.cloudx.estate.entity.CascadeRequestInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.edge.CascadeCloudRemoteServiceAbstract;
import com.aurine.cloudx.estate.entity.EdgeCascadeResponse;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.MQTTAccount;
import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class CascadeRemoteService extends CascadeCloudRemoteServiceAbstract {

    /**
     * <p>申请级联</p>
     *
     * @param cascadeRequestInfoDTO 级联申请对象
     * @return
     * @author: 王良俊
     */
    public abstract EdgeCascadeResponse requestCascade(String parentIp, CascadeRequestInfoDTO cascadeRequestInfoDTO);

    /**
     * <p>用于创建账号</p>
     *
     */
    public abstract boolean createAccount(MQTTAccount mqttAccount);

    public abstract EdgeCascadeResponse cancelRequest(String projectCode, String parentIp) throws JsonProcessingException;
}
