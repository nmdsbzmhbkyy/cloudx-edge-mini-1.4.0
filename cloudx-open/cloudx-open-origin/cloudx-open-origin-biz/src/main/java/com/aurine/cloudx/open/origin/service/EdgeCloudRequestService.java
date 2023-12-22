package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.open.origin.entity.EdgeCloudRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * <p>边缘网关入云申请服务</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:31:32
 */
public interface EdgeCloudRequestService extends IService<EdgeCloudRequest> {

//    /**
//     * <p>申请入云</p>
//     *
//     * @param syncType 入云方式
//     * @param connectCode 入云码
//     * @return 是否申请成功
//     */
//    R requestIntoCloud(Character syncType, String connectCode, Integer projectId);


    /**
     * <p>申请解绑</p>
     *
     * @param requestId 申请ID
     * @return 是否申请成功
     */
    R requestUnbind(String requestId);

//    /**
//     * <p>撤销入云申请</p>
//     *
//     * @param requestId 申请ID
//     * @return 是否撤销成功
//     */
//    R<Boolean> revokeIntoCloudRequest(String requestId);

    /**
     * <p>用来判断是否存在已入云或是入云申请中的项目</p>
     *
     * @param projectId 项目ID
     * @return 是否存在入云或是入云申请中的项目
     */
    Boolean canDisableNetwork(Integer projectId);

    /**
     * <p>撤销解绑申请</p>
     *
     * @param requestId 申请ID
     * @return 操作成功？
     */
    R<Boolean> revokeUnbindRequest(String requestId);

    /**
     * <p>更新入云状态</p>
     *
     * @param requestId 申请ID
     * @param statusEnum 入云状态
     */
    boolean updateCloudStatus(String requestId, IntoCloudStatusEnum statusEnum);

    /**
     * <p>接收云平台通过入云申请</p>
     *
     * @param projectCode 项目UUID
     */
    Boolean passRequest(String projectCode);

    /**
     * <p>接收云平台拒绝入云申请</p>
     *
     * @param projectCode 项目UUID
     */
    Boolean rejectRequest(String projectCode);

    /**
     * <p>接收云平台删除边缘网关或是同意解绑边缘网关申请</p>
     *
     * @param projectCode 项目UUID
     */
    Boolean removeEdge(String projectCode);

    /**
     * <p>接收云平台删除边缘网关或是同意解绑边缘网关申请</p>
     *
     * @param projectCode 项目UUID
     */
    Boolean rejectUnbindRequest(String projectCode);
}
