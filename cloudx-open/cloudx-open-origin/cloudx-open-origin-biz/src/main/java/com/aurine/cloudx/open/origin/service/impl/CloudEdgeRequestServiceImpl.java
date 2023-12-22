package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.open.origin.entity.ProjectInfo;
import com.aurine.cloudx.open.origin.mapper.CloudEdgeRequestMapper;
import com.aurine.cloudx.open.origin.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.open.origin.entity.CloudEdgeRequest;
import com.aurine.cloudx.open.origin.service.CloudEdgeRequestService;
import com.aurine.cloudx.open.origin.service.EdgeCascadeConfService;
import com.aurine.cloudx.open.origin.service.ProjectInfoService;
import com.aurine.cloudx.open.origin.service.SysDeptUserService;
import com.aurine.cloudx.open.common.core.util.ImgConvertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>边缘网关入云申请服务</p>
 *
 * @author : 王良俊
 * @date : 2021-12-02 17:34:11
 */
@Slf4j
@Service
public class CloudEdgeRequestServiceImpl extends ServiceImpl<CloudEdgeRequestMapper, CloudEdgeRequest> implements CloudEdgeRequestService {

    @Resource
    CloudEdgeRequestMapper cloudEdgeRequestMapper;
    @Resource
    EdgeCascadeConfService edgeCascadeConfService;
    //    @Resource
//    ProjectThirdpartyInfoService projectThirdpartyInfoService;
    @Resource
    SysDeptUserService sysDeptUserService;
    @Resource
    ProjectInfoService projectInfoService;
    //    @Resource
//    IntoCloudService intoCloudService;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private RemoteRoleService remoteRoleService;
    @Resource
    private ImgConvertUtil imgConvertUtil;


    @Override
    public List<CloudEdgeRequest> listRequest(Integer projectId) {
        return this.list(new LambdaQueryWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getProjectId, projectId).eq(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.PENDING_AUDIT.code));
    }

//    @SneakyThrows
//    @Override
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
//    public IntoCloudResponse saveRequest(IntoCloudRequestInfoDTO request) {
//        // 第三方项目ID
//        String projectCode = request.getProjectCode();
//        if (StrUtil.isEmpty(projectCode) || StrUtil.isEmpty(request.getConnectCode()) || StrUtil.isEmpty(request.getContactPhone()) || StrUtil.isEmpty(request.getContactPersonName())) {
//            return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, IntoCloudEventCodeEnum.REQUEST_FAILED_MISSING_PARAMETERS);
//        }
//        String connectCode = request.getConnectCode().toUpperCase();
//
//        Integer targetProjectId = edgeCascadeConfService.getProjectId(connectCode);
//        if (targetProjectId == null) {
//            return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, IntoCloudEventCodeEnum.REQUEST_FAILED_CODE_ERROR);
//        }
//        int count = this.count(new LambdaQueryWrapper<CloudEdgeRequest>()
//                .ne(CloudEdgeRequest::getProjectCode, projectCode)
//                .eq(CloudEdgeRequest::getProjectId, targetProjectId)
//                .eq(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code));
//        // 如果不为0说明这个项目已经绑定了一个边缘网关（已入云）
//        if (count > 0) {
//            return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, IntoCloudEventCodeEnum.REQUEST_FAILED_EXIST_CLOUD);
//        }
//
//        CloudEdgeRequest passRequest = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getProjectCode, projectCode)
//                .in(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.UNBINDING.code, IntoCloudStatusEnum.INTO_CLOUD.code));
//        if (passRequest != null) {
//            if (targetProjectId.equals(passRequest.getProjectId())) {
//                return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, IntoCloudEventCodeEnum.REQUEST_SUCCESS_HAS_INTO_CLOUD);
//            } else {
//                return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, IntoCloudEventCodeEnum.REQUEST_FAILED_BOUND_OTHER);
//            }
//        }
//
//        // 这里是为边缘网关创建MQTT账号
//        IntoCloudAccount intoCloudAccount = new IntoCloudAccount("1", request.getDeviceSn(), projectCode, request.getConnectCode());
//        boolean createResult = intoCloudService.createAccount(intoCloudAccount);
//        if (!createResult) {
//            return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, IntoCloudEventCodeEnum.REQUEST_FAILED_MQTT);
//        }
//
//        CloudEdgeRequest cloudEdgeRequest;
//        // 判断是否已在当前项目有申请记录
//        List<CloudEdgeRequest> existRequestList = this.list(new LambdaQueryWrapper<CloudEdgeRequest>()
//                .eq(CloudEdgeRequest::getProjectCode, projectCode).eq(CloudEdgeRequest::getProjectId, targetProjectId)
//                .in(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.REJECT.code, IntoCloudStatusEnum.NOT_INTO_CLOUD.code, IntoCloudStatusEnum.PENDING_AUDIT.code));
//        if (CollUtil.isNotEmpty(existRequestList)) {
//            cloudEdgeRequest = existRequestList.get(0);
//        } else {
//            cloudEdgeRequest = new CloudEdgeRequest();
//            cloudEdgeRequest.setProjectId(targetProjectId);
//        }
//        BeanPropertyUtil.copyProperty(cloudEdgeRequest, request, requestMapping);
//        ObjectMapper objectMapper = ObjectMapperUtil.instance();
//        cloudEdgeRequest.setConfigJson(objectMapper.writeValueAsString(intoCloudAccount));
//        cloudEdgeRequest.setCloudStatus(IntoCloudStatusEnum.PENDING_AUDIT.code);
//        this.saveOrUpdate(cloudEdgeRequest);
//        return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, IntoCloudEventCodeEnum.REQUEST_SUCCESS);
//    }
//
//    @Override
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
//    public boolean passRequest(String requestId) {
//        CloudEdgeRequest cloudEdgeRequest = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>()
//                .eq(CloudEdgeRequest::getRequestId, requestId).last("limit 1"));
//        if (cloudEdgeRequest != null) {
//            int count = this.count(new LambdaQueryWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getProjectCode, cloudEdgeRequest.getProjectCode())
//                    .in(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.UNBINDING.code, IntoCloudStatusEnum.INTO_CLOUD.code));
//            if (count != 0) {
//                throw new RuntimeException("该边缘网关已入云，无法再次入云");
//            }
//            cloudEdgeRequest.setCloudStatus(IntoCloudStatusEnum.INTO_CLOUD.code);
//            this.updateById(cloudEdgeRequest);
//            // 这里为边缘网关创建管理员账号
//            this.createAdminAccountByRequest(cloudEdgeRequest);
//            // 保存或是更新边缘侧第三方项目信息
//            projectThirdpartyInfoService.saveOrUpdateThirdpartyProjectInfoByRequest(cloudEdgeRequest);
//            this.rejectRequest(cloudEdgeRequest.getProjectId(), requestId);
//            return true;
//        }
//        log.info("无法找到对应申请 requestId：{}" + requestId);
//        throw new RuntimeException("无法找到对应申请");
//    }

    @Override
    public boolean rejectRequest(String requestId) {
        return this.update(new LambdaUpdateWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getRequestId, requestId).set(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.REJECT.code));
    }

    @Override
    public void rejectRequest(Integer projectId, String requestId) {
        log.info("批量拒绝入云申请 项目ID：{} 申请ID：{}", projectId, requestId);
        List<CloudEdgeRequest> cloudEdgeRequestList = this.list(new LambdaQueryWrapper<CloudEdgeRequest>()
                .eq(CloudEdgeRequest::getProjectId, projectId)
                .ne(CloudEdgeRequest::getRequestId, requestId)
                .ne(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.REJECT));
        if (CollUtil.isNotEmpty(cloudEdgeRequestList)) {
            cloudEdgeRequestList.forEach(item -> {
                this.rejectRequest(item.getRequestId());
            });
        }

    }

    @Override
    public String getPassRequestId(Integer projectId) {
        List<CloudEdgeRequest> cloudEdgeRequestList = this.list(new LambdaQueryWrapper<CloudEdgeRequest>()
                .in(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code)
                .eq(CloudEdgeRequest::getProjectId, projectId)
                .orderByDesc(CloudEdgeRequest::getUpdateTime));
        if (CollUtil.isNotEmpty(cloudEdgeRequestList)) {
            if (cloudEdgeRequestList.size() > 1) {
                log.warn("[边缘网关入云申请] 出现多条已通过申请记录 项目ID：{}", projectId);
            }
            return cloudEdgeRequestList.get(0).getRequestId();
        }
        return null;
    }

    //    @Override
//    public boolean cancelRequest(String projectCode, String connectCode) {
//        Integer projectId = edgeCascadeConfService.getProjectId(connectCode);
//        if (projectId == null) {
//            log.info("[取消入云申请] 未查询到连接码对应的项目 连接码：{} 第三方项目ID：{}", connectCode, projectCode);
//            return true;
//        }
//        CloudEdgeRequest cloudEdgeRequest = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>()
//                .eq(CloudEdgeRequest::getProjectCode, projectCode)
//                .eq(CloudEdgeRequest::getProjectId, projectId)
//                .last("limit 1"));
//        if (cloudEdgeRequest.getCloudStatus() != IntoCloudStatusEnum.NOT_INTO_CLOUD.code && cloudEdgeRequest.getCloudStatus() != IntoCloudStatusEnum.PENDING_AUDIT.code) {
//            log.info("[取消入云申请] 项目已入云无法取消入云申请");
//            return false;
//        }
//        return this.remove(new LambdaQueryWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getRequestId, cloudEdgeRequest.getRequestId()));
//    }
//
    @Override
    public Boolean cancelRequest(String projectUUID, String projectCode) {
//        CloudEdgeRequest cloudEdgeRequest = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>()
//                .eq(CloudEdgeRequest::getProjectCode, projectCode)
//                .last("limit 1"));
        /*if (cloudEdgeRequest.getCloudStatus() != IntoCloudStatusEnum.NOT_INTO_CLOUD.code && cloudEdgeRequest.getCloudStatus() != IntoCloudStatusEnum.PENDING_AUDIT.code) {
            log.info("[取消入云申请] 项目已入云无法取消入云申请");
            return false;
        }*/
//        cloudEdgeRequest.setCloudStatus(IntoCloudStatusEnum.NOT_INTO_CLOUD.code);
        ProjectInfo byProjectUUID = projectInfoService.getByProjectUUID(projectUUID);
        return this.update(new LambdaUpdateWrapper<CloudEdgeRequest>()
                .eq(CloudEdgeRequest::getProjectId, byProjectUUID.getProjectId())
                .eq(CloudEdgeRequest::getProjectCode, projectCode)
                .set(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.NOT_INTO_CLOUD.code));
//        return this.updateById(cloudEdgeRequest);
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public Boolean passUnbindRequest(String requestId) {
        //TODO: 这里应该通知边缘网关解绑成功
        return this.update(new LambdaUpdateWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getRequestId, requestId)
                .set(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.NOT_INTO_CLOUD.code));
    }

    //    @Override
//    public boolean cancelUnbindRequest(String projectCode, String connectCode) {
//        Integer projectId = edgeCascadeConfService.getProjectId(connectCode);
//        if (projectId == null) {
//            log.info("[取消入云申请] 未查询到连接码对应的项目 连接码：{} 第三方项目ID：{}", connectCode, projectCode);
//            return true;
//        }
//        CloudEdgeRequest request = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>()
//                .eq(CloudEdgeRequest::getProjectCode, projectCode)
//                .eq(CloudEdgeRequest::getProjectId, projectId)
//                .eq(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.UNBINDING)
//                .last("limit 1")
//        );
//
//        if (request != null) {
//            request.setCloudStatus(IntoCloudStatusEnum.INTO_CLOUD.code);
//            this.updateById(request);
//        }
//        return true;
//    }
//
    @Override
    public boolean cancelUnbindRequest(String projectCode) {
        CloudEdgeRequest request = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>()
                .eq(CloudEdgeRequest::getProjectCode, projectCode)
                .in(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.UNBINDING.code, IntoCloudStatusEnum.INTO_CLOUD.code)
                .last("limit 1")
        );

        if (request != null) {
            request.setCloudStatus(IntoCloudStatusEnum.INTO_CLOUD.code);
            this.updateById(request);
        }
        return false;
    }

    @Override
    public boolean removeEdge(String requestId) {
        return this.update(new LambdaUpdateWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getRequestId, requestId).set(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.NOT_INTO_CLOUD.code));
    }

    //    @Override
//    public IntoCloudResponse requestUnbind(String projectCode, String connectCode) {
//        Integer projectId = edgeCascadeConfService.getProjectId(connectCode);
//        CloudEdgeRequest request = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getProjectCode, projectCode).eq(CloudEdgeRequest::getProjectId, projectId));
//        IntoCloudEventCodeEnum intoCloudEventCodeEnum = IntoCloudEventCodeEnum.UNBIND_SUCCESS;
//        ObjectNode data = ObjectMapperUtil.instance().createObjectNode();
//        data.put("projectCode", projectCode);
//        data.put("connectCode", connectCode);
//        if (request != null) {
//            switch (request.getCloudStatus()) {
//                /*
//                 * 未入云
//                 */
//                case '0':
//                    //TODO: 代表已解绑成功 应该通知边缘网关已经解绑设备了
//                    intoCloudEventCodeEnum = IntoCloudEventCodeEnum.UNBIND_SUCCESS;
//                    break;
//                /*
//                 * 已拒绝
//                 */
//                case '2':
//                    //TODO: 更新为未入云，通知设备已解绑
//                    this.update(new LambdaUpdateWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getProjectCode, projectCode).eq(CloudEdgeRequest::getProjectId, projectId).set(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.NOT_INTO_CLOUD.code));
//                    intoCloudEventCodeEnum = IntoCloudEventCodeEnum.UNBIND_SUCCESS;
//                    break;
//                /*
//                 * 待审核
//                 */
//                case '1':
//                    //TODO: 这里也是要解绑成功，但是要更新申请状态为未入云，并通知设备已解绑
//                    this.update(new LambdaUpdateWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getProjectCode, projectCode).eq(CloudEdgeRequest::getProjectId, projectId).set(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.NOT_INTO_CLOUD.code));
//                    intoCloudEventCodeEnum = IntoCloudEventCodeEnum.UNBIND_SUCCESS;
//                    break;
//                /*
//                 * 已入云
//                 */
//                case '3':
//                    //TODO: 更新设备状态为解绑中
//                    this.update(new LambdaUpdateWrapper<CloudEdgeRequest>().eq(CloudEdgeRequest::getProjectCode, projectCode).eq(CloudEdgeRequest::getProjectId, projectId).set(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.UNBINDING.code));
//                    intoCloudEventCodeEnum = IntoCloudEventCodeEnum.REQUEST_UNBIND_SUCCESS;
//                    break;
//                /*
//                 * 解绑中
//                 */
//                case '4':
//                    //TODO: 通知设备申请成功
//                    intoCloudEventCodeEnum = IntoCloudEventCodeEnum.REQUEST_UNBIND_SUCCESS;
//                    break;
//                default:
//                    log.info("入云申请状态未知：{}", request);
//            }
//        }
//        return new IntoCloudResponse(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, intoCloudEventCodeEnum, data);
//    }
//
    @Override
    public boolean requestUnbind(String projectCode) {
        CloudEdgeRequest cloudEdgeRequest = this.getOne(new LambdaQueryWrapper<CloudEdgeRequest>()
                .eq(CloudEdgeRequest::getProjectCode, projectCode)
                .in(CloudEdgeRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));

        if (cloudEdgeRequest != null) {
            cloudEdgeRequest.setCloudStatus(IntoCloudStatusEnum.UNBINDING.code);
            return this.updateById(cloudEdgeRequest);
        }
        return false;
    }

//    @Override
//    public boolean createAdminAccountByRequest(CloudEdgeRequest cloudEdgeRequest) {
//        log.info("准备创建边缘网关入云项目管理员：{}", cloudEdgeRequest);
//        ProjectInfo projectInfo = projectInfoService.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, cloudEdgeRequest.getProjectId()));
//        Integer roleId = baseMapper.getAdminRoleId(cloudEdgeRequest.getProjectId());
//        if (roleId == null || roleId == 0) {
//            log.error("无法创建入云项目管理员 未查询到管理员角色ID projectId：{}", cloudEdgeRequest.getProjectId());
//            return false;
//        }
//        SysUserVo sysUserVo = new SysUserVo();
//        BeanPropertyUtil.copyProperty(sysUserVo, cloudEdgeRequest, new FieldMapping<SysUserVo, CloudEdgeRequest>()
//                .add(SysUserVo::getCredentialNo, CloudEdgeRequest::getEdgeIdNumber)
//                .add(SysUserVo::getPhone, CloudEdgeRequest::getEdgeContactPhone)
//                .add(SysUserVo::getAvatar, CloudEdgeRequest::getEdgePicUrl)
//                .add(SysUserVo::getDeptId, CloudEdgeRequest::getProjectId)
//                .add(SysUserVo::getSex, CloudEdgeRequest::getEdgeGender)
//                .add(SysUserVo::getTrueName, CloudEdgeRequest::getEdgeContactPerson)
//        );
//        // 创建的是项目管理员所以是 3
//        sysUserVo.setDeptTypeId(DeptTypeEnum.PROJECT.getId());
//        sysUserVo.setRoleId(roleId);
//
//        R<UserInfo> userInfoR = remoteUserService.newinfo(sysUserVo.getPhone(), SecurityConstants.FROM_IN);
//        //如果存在该手机号的用户
//        boolean canCreate = true;
//        if (userInfoR.getCode() == 0 && userInfoR.getData() != null) {
//            UserInfo userInfo = userInfoR.getData();
//            sysUserVo.setUserId(userInfo.getSysUser().getUserId());
//            //校验是否含有云平台用户角色
//            R<List<SysRole>> data = remoteRoleService.getByDeptId(SysDeptConstant.AURINE_ID);
//            if (data != null && data.getData() != null) {
//                List<SysRole> roleList = data.getData();
//                Integer[] roles = roleList.stream().map(SysRole::getRoleId).toArray(Integer[]::new);
//                //判断所选是否为云平台用户
//                if (ArrayUtils.contains(roles, sysUserVo.getRoleId())) {
//                    //判断是否已有云平台用户
//                    if (ArrayUtil.containsAny(roles, userInfo.getRoles())) {
//                        //已有云平台用户角色阻止新增,保证一个部门层级底下只有一个角色
//                        canCreate = false;
//                    }
//                }
//            }
//            //校验用户角色是否重复
//            if (ArrayUtils.contains(userInfo.getRoles(), sysUserVo.getRoleId())) {
//                canCreate = false;
//            }
//        }
//        if (canCreate) {
//            sysDeptUserService.addUserAndRole(sysUserVo);
//        }
//        log.info("边缘网关如遇你项目管理员创建成功 {}", cloudEdgeRequest);
//        return true;
//    }
//
//    FieldMapping<CloudEdgeRequest, IntoCloudRequestInfoDTO> requestMapping = new FieldMapping<CloudEdgeRequest, IntoCloudRequestInfoDTO>()
//            .add(CloudEdgeRequest::getProjectCode, IntoCloudRequestInfoDTO::getProjectCode)
//            .add(CloudEdgeRequest::getEdgeContactPerson, IntoCloudRequestInfoDTO::getContactPersonName)
//            .add(CloudEdgeRequest::getEdgeGender, IntoCloudRequestInfoDTO::getContactGender)
//            .add(CloudEdgeRequest::getEdgeIdNumber, IntoCloudRequestInfoDTO::getContactIdNumber)
//            .add(CloudEdgeRequest::getEdgeContactPhone, IntoCloudRequestInfoDTO::getContactPhone)
//            .add(CloudEdgeRequest::getEdgeProjectName, IntoCloudRequestInfoDTO::getProjectName)
//            .add(CloudEdgeRequest::getEdgeDeviceId, IntoCloudRequestInfoDTO::getDeviceSn)
//            .add(CloudEdgeRequest::getRequestTime, IntoCloudRequestInfoDTO::getRequestTime, BeanPropertyUtil.TIMESTAMP_TO_LOCAL_DATE_TIME)
//            .add(CloudEdgeRequest::getEdgePicUrl, IntoCloudRequestInfoDTO::getContactPicBase64, new Base64ToPicUrl());
//
//    private class Base64ToPicUrl extends ConvertHandler<String, String> {
//
//        @Override
//        public String convert(String base64) {
//            try {
//                return imgConvertUtil.base64ToMinio(base64);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return "";
//        }
//    }
}

