package com.aurine.cloudx.estate.thirdparty.module.edge.cloud.remote;

import com.aurine.cloudx.estate.thirdparty.module.edge.CascadeCloudRemoteServiceAbstract;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.IntoCloudRequestInfoDTO;
import com.aurine.cloudx.estate.entity.IntoCloudResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>边缘侧入云Remote接口</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 15:20:28
 */
public abstract class IntoCloudRemoteService extends CascadeCloudRemoteServiceAbstract {

    /**
     * <p>申请入云</p>
     *
     * @param intoCloudRequestInfoDTO 入云申请信息
     * @return 入云申请提交是否成功
     */
    public abstract IntoCloudResponse requestIntoCloud(IntoCloudRequestInfoDTO intoCloudRequestInfoDTO) throws JsonProcessingException;

    /**
     * <p>取消入云申请</p>
     *
     * @param connectionCode 入云连接码
     * @param projectCode 项目UUID(入云申请时使用的第三方ID)
     */
    public abstract IntoCloudResponse cancelRequest(String projectCode, String connectionCode) throws JsonProcessingException;
}
