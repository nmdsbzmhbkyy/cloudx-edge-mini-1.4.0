package com.aurine.cloudx.estate.thirdparty.module.edge.cascade.service;

import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestSlave;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.DriverManagerReq;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.SubCfgInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * <p>级联第三方服务类</p>
 * @author : 王良俊
 * @date : 2021-12-21 17:47:31
 */
public interface EdgeCascadeService {



    /**
     * <p>请求中台获取设备SN</p>
     *
     * @return 返回设备SN
     */
    String getDeviceSN(Integer projectId);



    /**
     * <p>创建MQTT账号</p>
     *
     * @return 是否创建成功
     */
    boolean createAccount(EdgeCascadeRequestMaster edgeCascadeRequestMaster);

    /**
     * <p>申请级联</p>
     *
     * @param request 级联申请对象
     * @return 是否发送级联申请成功
     */
    R<String> requestCascade(EdgeCascadeRequestSlave request);

    /**
     * <p>配置桥接</p>
     *
     * @param request 级联请求（从）
     * @return 是否配置成功
     * @author: 王良俊
     */
    boolean configBridging(EdgeCascadeRequestSlave request);

    /**
     * <p>取消级联申请（撤销不走HTTP）</p>
     *
     * @param requestId 级联（从）申请ID
     * @return 操作结果
     */
    @Deprecated
    R<String> cancelRequest(String requestId);

    /**
     * <p>配置中台订阅</p>
     *
     * @param subCfgInfo 订阅信息对象
     * @return 操作结果
     */
    boolean pubSubCfg(SubCfgInfo subCfgInfo);

    /**
     * <p>配置驱动社区ID</p>
     *
     * @param req 中台驱动主社区配置对象
     * @return 是否配置成功
     */
    boolean confDriverCommunityId(DriverManagerReq req);
}
