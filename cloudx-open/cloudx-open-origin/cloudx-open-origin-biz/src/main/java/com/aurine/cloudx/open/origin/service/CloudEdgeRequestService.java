package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.CloudEdgeRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>边缘网关入云申请服务</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:31:32
 */
public interface CloudEdgeRequestService extends IService<CloudEdgeRequest> {

    /**
     * <p>获取入云申请列表</p>
     *
     * @param projectId 项目ID
     * @return 入云申请列表
     */
    List<CloudEdgeRequest> listRequest(Integer projectId);

//    /**
//     * <p>申请入云</p>
//     *
//     * @param request 入云申请类
//     * @return 是否申请成功
//     */
//    IntoCloudResponse saveRequest(IntoCloudRequestInfoDTO request);
//
//    /**
//     * <p>通过入云申请</p>
//     *
//     * @param requestId 入云申请ID
//     * @return 是否通过成功
//     */
//    boolean passRequest(String requestId);

    /**
     * <p>拒绝入云申请</p>
     *
     * @param requestId 入云申请ID
     * @return 是否拒绝成功
     */
    boolean rejectRequest(String requestId);

    /**
     * <p>批量拒绝申请</p>
     *
     * @param projectId 要批量拒绝的项目ID
     * @param requestId 除了这个申请其余申请全部拒绝
     */
    void rejectRequest(Integer projectId, String requestId);

    /**
     * <p>获取通过的申请ID</p>
     *
     * @param projectId 项目ID
     * @return 入云申请ID
     */
    String getPassRequestId(Integer projectId);

//    /**
//     * <p>取消（撤销）入云申请 - 接收边缘侧取消入云申请请求</p>
//     *
//     * @param projectCode 第三方项目ID
//     * @return 是否取消成功
//     */
//    boolean cancelRequest(String projectCode, String connectCode);
//
    /**
     * <p>取消（撤销）入云申请 - 接收边缘侧取消入云申请请求</p>
     *
     * @param projectCode 第三方项目ID
     * @return 是否取消成功
     */
    Boolean cancelRequest(String projectUUID, String projectCode);

    /**
     * <p>通过解绑边缘网关申请</p>
     *
     * @param requestId 入云申请ID
     * @return 是否解绑成功
     */
    Boolean passUnbindRequest(String requestId);

//    /**
//     * <p>取消（撤销）解绑申请 - 接收边缘侧取消解绑申请请求</p>
//     *
//     * @param projectCode 第三方项目ID
//     * @return 是否撤销成功
//     */
//    boolean cancelUnbindRequest(String projectCode, String connectCode);
//
    /**
     * <p>取消（撤销）解绑申请 - 接收边缘侧取消解绑申请请求</p>
     *
     * @param projectCode 第三方项目ID
     * @return 是否撤销成功
     */
    boolean cancelUnbindRequest(String projectCode);

    /**
     * <p>删除边缘网关</p>
     *
     * @param requestId 入云申请ID
     * @return 是否删除成功
     */
    boolean removeEdge(String requestId);

//    /**
//     * <p>申请解绑云端项目 - 接收边缘侧申请解绑请求</p>
//     *
//     * @param projectCode 第三方项目ID
//     * @return 结果
//     */
//    IntoCloudResponse requestUnbind(String projectCode, String connectCode);
//
    /**
     * <p>申请解绑云端项目 - 接收边缘侧申请解绑请求</p>
     *
     * @param projectCode 第三方项目ID
     * @return 结果
     */
    boolean requestUnbind(String projectCode);

//    /**
//     * <p>创建管理员账号</p>
//     *
//     * @param cloudEdgeRequest 入云申请对象 带负责人信息和项目ID
//     * @return 是否创建成功
//     */
//    boolean createAdminAccountByRequest(CloudEdgeRequest cloudEdgeRequest);
}
