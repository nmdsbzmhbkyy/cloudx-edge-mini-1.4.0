package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.CascadeRequestInfoDTO;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.entity.EdgeCascadeResponse;
import com.aurine.cloudx.estate.vo.CascadeManageQuery;
import com.aurine.cloudx.estate.vo.CascadeProjectInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * <p>级联申请管理（主）服务</p>
 * @author : 王良俊
 * @date : 2021-12-17 09:37:40
 */
public interface EdgeCascadeRequestMasterService extends IService<EdgeCascadeRequestMaster> {

    /**
     * <p>判断是否是主边缘网关</p>
     *
     * @param projectId 项目ID
     * @return 是否是主边缘网关
     */
    boolean isMaster(Integer projectId);

    /**
     * <p>可否申请级联</p>
     *
     * @param projectId 项目ID
     * @return 是否能够申请级联（没有从边缘网关）
     */
    boolean canRequestCascade(Integer projectId);

    /**
     * <p>申请级联</p>
     *
     * @param requestInfo 级联申请信息
     * @return 提交级联申请结果
     */
    R<EdgeCascadeResponse> requestCascade(CascadeRequestInfoDTO requestInfo);

    /**
     * <p>通过审核</p>
     *
     * @param requestId 申请ID
     * @return 是否操作成功
     */
    R<String> passRequest(String requestId);

    /**
     * <p>批量通过审核</p>
     *
     * @param requestIdList 申请ID列表
     * @return 是否操作成功
     */
    R<String> passRequestBatch(List<String> requestIdList);

    /**
     * <p>拒绝级联申请</p>
     *
     * @param requestId 申请ID
     * @return 是否操作成功
     */
    R<String> rejectRequest(String requestId);

    boolean createAdminAccount(CascadeAdminPersonInfo personInfo, Integer projectId);

    /**
     * <p>删除边缘网关（只是删除主边缘网关在该项目下的管理员，更新级联申请状态为未级联）</p>
     *
     * @param projectId 要删除的边缘网关级联项目
     * @return 操作结果
     */
    R<String> removeEdge(Integer projectId);

    /**
     * <p>供边缘网关（从）取消级联申请使用</p>
     *
     * @param projectCode 边缘网关（从）的项目UUID
     * @return 处理结果对象JSON
     */
    R cancelRequest(String projectCode);


    /**
     * <p>是否已存在级联设备（从边缘网关）</p>
     *
     * @return 是否存在级联设备
     */
    Integer countSlave();

    /**
     * <p>分页查询级联管理项目信息</p>
     *
     * @param cascadeManageQuery 查询条件
     * @return 分页数据
     */
    R<Page<CascadeProjectInfoVo>> pageCascadeManage(Page page, CascadeManageQuery cascadeManageQuery);

    /**
     * <p>获取级联申请列表</p>
     *
     * @param projectId 要查询的项目ID（一般是主边缘网关自有项目ID）
     * @return 级联申请列表
     */
    R<Page<EdgeCascadeRequestMaster>> listRequest(Page page, Integer projectId);

    /**
     * <p>获取边缘网关自带项目的项目ID</p>
     *
     * @return 边缘网关自带非级联生成的项目ID
     */
    Integer getOriginProjectId();

    /**
     * <p>管理员人员信息对象</p>
     * @author : 王良俊
     * @date : 2021-12-31 14:10:50
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class CascadeAdminPersonInfo {

        /**
         * 人员姓名
         */
        private String personName;

        /**
         * 人员性别
         */
        private String gender;

        /**
         * 人员头像uri
         */
        private String avatar;

        /**
         * 人员手机号
         */
        private String telephone;

        /**
         * 人员身份证号
         */
        private String credentialNo;

    }


}
