package com.aurine.cloudx.estate.thirdparty.module.edge.cloud.service;

import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * <p>入云申请服务</p>
 * @author : 王良俊
 * @date : 2021-12-17 11:02:55
 */
public interface IntoCloudService {

    /**
     * <p>申请入云</p>
     *
     * @param edgeCloudRequest 入云申请对象 带入云申请requestId
     * @return 是否申请成功
     */
    R<String> requestIntoCloud(EdgeCloudRequest edgeCloudRequest);

    /**
     * <p>撤销入云申请</p>
     *
     * @param requestId 要撤销的入云申请ID
     * @return 是否撤销成功
     */
    R<String> cancelRequest(String requestId);

    /**
     * <p>请求中台获取设备SN</p>
     *
     * @return 返回设备SN
     */
    String getDeviceSN(Integer projectId);

    /**
     * <p>配置桥接</p>
     *
     * @param request 入云申请信息
     * @return 桥接结果
     */
    boolean configCloudBridging(EdgeCloudRequest request);
}
