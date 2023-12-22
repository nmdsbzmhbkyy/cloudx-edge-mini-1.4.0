package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestSlave;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;


/**
 * <p>级联申请管理（从）服务</p>
 * @author : 王良俊
 * @date : 2021-12-17 09:37:40
 */
public interface EdgeCascadeRequestSlaveService extends IService<EdgeCascadeRequestSlave> {


    /**
     * <p>申请级联</p>
     *
     * @param request 级联申请对象
     * @return 是否提交申请成功
     * @author: 王良俊
     */
    R requestCascade(EdgeCascadeRequestSlave request);

    /**
     * <p>判断是否已级联</p>
     *
     * @return 是否已级联
     */
    boolean isCascade();

    /**
     * <p>撤销级联申请</p>
     *
     * @param requestId 级联申请ID
     * @return 撤销结果
     */
    R<String> revokeRequest(String requestId);

    /**
     * <p>与主边缘网关解除绑定</p>
     *
     * @param requestId 申请ID
     * @return 操作结果
     * @author: 王良俊
     */
    R<String> requestUnbind(String requestId);

    /**
     * <p>主边缘网关删除从边缘网关</p>
     *
     * @param projectCode 项目UUID（申请级联时提供的projectCode）
     */
    boolean removeSlave(String projectCode);

    /**
     * <p>主边缘网关同意从边缘网关级联申请</p>
     *
     * @param projectCode 项目UUID（申请级联时提供的projectCode）
     */
    boolean passRequest(String projectCode);

    /**
     * <p>主边缘网关拒绝从边缘网关级联申请</p>
     *
     * @param projectCode 项目UUID（申请级联时提供的projectCode）
     */
    boolean rejectRequest(String projectCode);
}
