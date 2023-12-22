package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.open.common.core.util.ObjectMapperUtil;
import com.aurine.cloudx.open.origin.constant.CascadeStatusConstants;
import com.aurine.cloudx.open.origin.constant.EdgeCascadeServiceIdConstant;
import com.aurine.cloudx.open.origin.constant.enums.EdgeCascadeEventCodeEnum;
import com.aurine.cloudx.open.origin.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeRequestSlave;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeResponse;
import com.aurine.cloudx.open.origin.entity.EdgeCloudRequest;
import com.aurine.cloudx.open.origin.mapper.EdgeCascadeRequestMasterMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>级联申请管理（主）服务</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 09:37:40
 */
@Slf4j
@Service
public class EdgeCascadeRequestMasterServiceImpl extends ServiceImpl<EdgeCascadeRequestMasterMapper, EdgeCascadeRequestMaster> implements EdgeCascadeRequestMasterService {

    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    EdgeCascadeRequestSlaveService edgeCascadeRequestSlaveService;
//    @Resource
//    EdgeCascadeService edgeCascadeService;
    @Resource
EdgeCascadeConfService edgeCascadeConfService;
    @Resource
    RemoteUserService remoteUserService;
    @Resource
    RemoteRoleService remoteRoleService;
    @Resource
    SysDeptUserService sysDeptUserService;
    @Resource
    ProjectStaffService projectStaffService;
    @Resource
    EdgeCloudRequestService edgeCloudRequestService;

    ObjectMapper objectMapper = ObjectMapperUtil.instance();

    @Override
    public boolean isMaster(Integer projectId) {
        String projectCode = projectInfoService.getProjectUUID(projectId);
        int requestNum = this.count(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getProjectId, projectId).notIn(EdgeCascadeRequestMaster::getCascadeStatus, CascadeStatusConstants.NOT_CASCADE, CascadeStatusConstants.REJECT));
        if (requestNum > 0) {
            return true;
        }

        int slaveCount = edgeCascadeRequestSlaveService.count(new LambdaUpdateWrapper<EdgeCascadeRequestSlave>()
                .eq(EdgeCascadeRequestSlave::getProjectId, projectId)
                .in(EdgeCascadeRequestSlave::getCascadeStatus, CascadeStatusConstants.CASCADE, CascadeStatusConstants.PENDING_AUDIT));
        // 这个如果为0则说明至少不是级联项目
        int count = this.count(new LambdaUpdateWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getProjectCode, projectCode));
        return count == 0 && slaveCount == 0;
    }

    @Override
    public boolean canRequestCascade(Integer projectId) {
        boolean master = this.isMaster(projectId);
        if (master) {
            int count = this.count(new LambdaUpdateWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getProjectId, projectId)
                    .in(EdgeCascadeRequestMaster::getCascadeStatus, CascadeStatusConstants.CASCADE, CascadeStatusConstants.PENDING_AUDIT));
            return count == 0;
        }
        return false;
    }

//    @Override
//    public R<EdgeCascadeResponse> requestCascade(CascadeRequestInfoDTO requestInfo) {
//        EdgeCascadeResponse edgeCascadeResponse = new EdgeCascadeResponse();
//        edgeCascadeResponse.setServiceId(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE);
//        if (StrUtil.isEmpty(requestInfo.getProjectCode()) || StrUtil.isEmpty(requestInfo.getConnectCode())) {
//            // 未填写参数的情况
//            edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.REQUEST_FAILED_MISSING_PARAMETERS.code);
//            return R.ok(edgeCascadeResponse);
//        }
//
//        EdgeCascadeConf cascadeConf = edgeCascadeConfService.getOne(new LambdaQueryWrapper<EdgeCascadeConf>()
//                .eq(EdgeCascadeConf::getConnectCode, requestInfo.getConnectCode()).last("limit 1"));
//
//        if (cascadeConf == null) {
//            // 级联码错误的情况
//            edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.REQUEST_FAILED_CODE_ERROR.code);
//            return R.ok(edgeCascadeResponse);
//        }
//
//        boolean master = this.isMaster(cascadeConf.getProjectId());
//        // 所申请的边缘网关不是主边缘网关的情况
//        if (!master) {
//            edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.REQUEST_FAILED_IS_SLAVE.code);
//            return R.failed(edgeCascadeResponse);
//        }
//
//        EdgeCascadeRequestMaster cascadeRequestMaster = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestMaster>()
//                .eq(EdgeCascadeRequestMaster::getProjectCode, requestInfo.getProjectCode()));
//        if (cascadeRequestMaster == null) {
//            cascadeRequestMaster = new EdgeCascadeRequestMaster();
//            BeanPropertyUtil.copyProperty(cascadeRequestMaster, requestInfo, requestInfoTORequestMaster);
//        } else if (cascadeRequestMaster.getCascadeStatus() == CascadeStatusConstants.CASCADE) {
//            // 已级联的情况
//            edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.REQUEST_SUCCESS_HAS_CASCADE.code);
//            return R.ok(edgeCascadeResponse);
//        }
//
//        Integer projectId = cascadeConf.getProjectId();
//        cascadeRequestMaster.setProjectId(projectId);
//
//        boolean account = edgeCascadeService.createAccount(cascadeRequestMaster);
//        if (!account) {
//            // MQTT账号创建失败的情况
//            edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.REQUEST_FAILED_MQTT.code);
//            return R.ok(edgeCascadeResponse);
//        }
//
//        // 设置状态为待审核
//        cascadeRequestMaster.setCascadeStatus(CascadeStatusConstants.PENDING_AUDIT);
//
//        try {
//            // 存入预先生成的项目UUID
//            cascadeRequestMaster.setConfigJson(objectMapper.writeValueAsString(new CascadeCloudConf(cascadeRequestMaster.getProjectCode(), cascadeRequestMaster.getProjectCode(), cascadeConf.getConnectCode())));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            cascadeRequestMaster.setConfigJson(objectMapper.createObjectNode().toString());
//        }
//
//        this.saveOrUpdate(cascadeRequestMaster);
//
//        String projectUUID = projectInfoService.getProjectUUID(projectId);
//
//        edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.REQUEST_SUCCESS.code);
//        ObjectNode responseDataNode = objectMapper.createObjectNode();
//        // 级联主边缘网关项目UUID使用的就是从边缘网关的UUID
//        responseDataNode.put("projectCode", cascadeRequestMaster.getProjectCode());
//        ProjectInfoVo cascadeProjectInfoVo = projectInfoService.getCascadeProjectInfoVo(projectId);
//        try {
//            responseDataNode.put("projectInfo", objectMapper.writeValueAsString(cascadeProjectInfoVo));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        edgeCascadeResponse.setData(responseDataNode.toString());
//
//        return R.ok(edgeCascadeResponse);
//    }
//
//    @Override
//    public R<String> passRequest(String requestId) {
//        EdgeCascadeRequestMaster requestMaster = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getRequestId, requestId).last("limit 1"));
//        if (requestMaster == null) {
//            return R.failed("未找到级联申请");
//        }
//        // 通过级联申请后就要为其创建一个项目
//        Integer cascadeProjectId = projectInfoService.createCascadeProject(requestMaster.getProjectId(), requestMaster);
//
//        ProjectInfo cascadeProjectInfo = projectInfoService.getById(cascadeProjectId);
//        // 获取到级联申请时从边缘网关给的第三方UUID 这里要作为新生成项目的UUID
//        String projectUUID = requestMaster.getProjectCode();
//        // 级联比较特殊审核通过之后才创建项目，所以项目的UUID在申请的时候就已经生成好了，这里创建好项目需要配置UUID
//        // 级联从和主的第三方ID已经项目UUID都是一样的
//        cascadeProjectInfo.setProjectUUID(projectUUID);
//        cascadeProjectInfo.setProjectCode(projectUUID);
//        projectInfoService.updateById(cascadeProjectInfo);
//        requestMaster.setCascadeStatus(CascadeStatusConstants.CASCADE);
//        AdminUserInfo currentAdminUserInfo = projectInfoService.getCurrentAdminUserInfo();
//        CascadeAdminPersonInfo cascadeAdminPersonInfo = new CascadeAdminPersonInfo();
//        BeanPropertyUtil.copyProperty(cascadeAdminPersonInfo, currentAdminUserInfo, new FieldMapping<CascadeAdminPersonInfo, AdminUserInfo>()
//                .add(CascadeAdminPersonInfo::getPersonName, AdminUserInfo::getTrue_name)
//                .add(CascadeAdminPersonInfo::getAvatar, AdminUserInfo::getAvatar)
//                .add(CascadeAdminPersonInfo::getGender, AdminUserInfo::getSex)
//                .add(CascadeAdminPersonInfo::getTelephone, AdminUserInfo::getPhone)
//                .add(CascadeAdminPersonInfo::getCredentialNo, AdminUserInfo::getCredential_no)
//        );
//        // 给当前项目管理员也配置一个级联项目的管理员
//        this.createAdminAccount(cascadeAdminPersonInfo, cascadeProjectId);
//        // 这里配置这个级联项目是否启用级联为 级联
//        edgeCascadeConfService.initCascadeConf(cascadeProjectId);
//
//        this.updateById(requestMaster);
//        return R.ok("通过级联申请成功");
//    }
//
//    @Override
//    public R<String> passRequestBatch(List<String> requestIdList) {
//        requestIdList.forEach(this::passRequest);
//        return R.ok("操作成功");
//    }

    @Override
    public R<String> rejectRequest(String requestId) {
        EdgeCascadeRequestMaster requestMaster = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getRequestId, requestId));
        if (requestMaster != null) {
            if (CascadeStatusConstants.CASCADE == requestMaster.getCascadeStatus()) {
                return R.failed("已级联无法拒绝");
            }
            requestMaster.setCascadeStatus(CascadeStatusConstants.REJECT);
            this.updateById(requestMaster);
            return R.ok("拒绝级联申请成功");
        }

        return R.failed("未找到级联申请");
    }

//    @Override
//    public Integer countSlave() {
//        return this.count(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().in(EdgeCascadeRequestMaster::getCascadeStatus, CascadeStatusConstants.CASCADE, CascadeStatusConstants.PENDING_AUDIT));
//    }
//
//    static FieldMapping<EdgeCascadeRequestMaster, CascadeRequestInfoDTO> requestInfoTORequestMaster = new FieldMapping<EdgeCascadeRequestMaster, CascadeRequestInfoDTO>()
//            .add(EdgeCascadeRequestMaster::getProjectCode, CascadeRequestInfoDTO::getProjectCode)
//            .add(EdgeCascadeRequestMaster::getSlaveContactPerson, CascadeRequestInfoDTO::getContactPersonName)
//            .add(EdgeCascadeRequestMaster::getSlaveGender, CascadeRequestInfoDTO::getContactGender)
//            .add(EdgeCascadeRequestMaster::getSlaveContactPhone, CascadeRequestInfoDTO::getContactPhone)
//            .add(EdgeCascadeRequestMaster::getRequestTime, CascadeRequestInfoDTO::getRequestTime, BeanPropertyUtil.TIMESTAMP_TO_LOCAL_DATE_TIME)
//            .add(EdgeCascadeRequestMaster::getSlaveEdgeDeviceId, CascadeRequestInfoDTO::getDeviceSn)
//            .add(EdgeCascadeRequestMaster::getSlaveProjectName, CascadeRequestInfoDTO::getProjectName);
//
//    @Override
//    public boolean createAdminAccount(CascadeAdminPersonInfo personInfo, Integer projectId) {
//        log.info("准备创建级联项目管理员：{}", personInfo);
//        Integer roleId = baseMapper.getAdminRoleId(projectId);
//        if (roleId == null || roleId == 0) {
//            log.error("无法创建入云项目管理员 未查询到管理员角色ID projectId：{}", projectId);
//            return false;
//        }
//        SysUserVo sysUserVo = new SysUserVo();
//        BeanPropertyUtil.copyProperty(sysUserVo, personInfo, personToAdminInfo);
//
//        // 创建的是项目管理员所以是 3
//        sysUserVo.setDeptId(projectId);
//        sysUserVo.setDeptTypeId(DeptTypeEnum.PROJECT.getId());
//        sysUserVo.setRoleId(roleId);
//
//        // 这里因为创建管理员接口会抛出异常所以把判断的方法复制过来
//        boolean canCreate = true;
//        R<UserInfo> userInfoR = remoteUserService.info(sysUserVo.getUsername(), SecurityConstants.FROM_IN);
//        //如果存在该手机号的用户
//        if (userInfoR.getCode() == 0 && userInfoR.getData() != null) {
//            UserInfo userInfo = userInfoR.getData();
//            sysUserVo.setUserId(userInfo.getSysUser().getUserId());
//            //校验是否含有云平台用户角色
//            R<List<SysRole>> data = remoteRoleService.getByDeptId(SysDeptConstant.AURINE_ID);
//            if (data != null && data.getData() != null) {
//                List<SysRole> roleList = data.getData();
//                Integer[] roles = roleList.stream().map(SysRole::getRoleId).toArray(Integer[]::new);
//                //判断所选是否为云平台用户
//                if (ArrayUtils.contains(roles, sysUserVo.getRoleId()) && ArrayUtil.containsAny(roles, userInfo.getRoles())) {
//                    //判断是否已有云平台用户
//                    //已有云平台用户角色阻止新增,保证一个部门层级底下只有一个角色
////                        throw new RuntimeException("新增失败,该用户已有云平台角色");
//                    canCreate = false;
//                }
//            }
//            //校验用户角色是否重复
//            if (ArrayUtils.contains(userInfo.getRoles(), sysUserVo.getRoleId())) {
////                throw new RuntimeException("新增失败,该用户已有该角色");
//                canCreate = false;
//            }
//        }
//        if (canCreate) {
//            ProjectStaff projectStaff = projectStaffService.getOne(new LambdaQueryWrapper<ProjectStaff>().eq(ProjectStaff::getMobile, sysUserVo.getPhone()).eq(ProjectStaff::getProjectId, projectId).last("limit 1"));
//            if (projectStaff != null) {
//                sysDeptUserService.updateUserAndRole(sysUserVo);
//            } else {
//                sysDeptUserService.addUserAndRole(sysUserVo);
//            }
//        }
//        log.info("边缘网关如遇你项目管理员创建成功 {}", personInfo);
//        return true;
//    }
//
//    FieldMapping<SysUserVo, CascadeAdminPersonInfo> personToAdminInfo = new FieldMapping<SysUserVo, CascadeAdminPersonInfo>()
//            .add(SysUserVo::getTrueName, CascadeAdminPersonInfo::getPersonName)
//            .add(SysUserVo::getAvatar, CascadeAdminPersonInfo::getAvatar)
//            .add(SysUserVo::getPhone, CascadeAdminPersonInfo::getTelephone)
//            .add(SysUserVo::getSex, CascadeAdminPersonInfo::getGender)
//            .add(SysUserVo::getUsername, CascadeAdminPersonInfo::getTelephone)
//            .add(SysUserVo::getCredentialNo, CascadeAdminPersonInfo::getCredentialNo);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<String> removeEdge(Integer projectId) {
        String projectUUID = projectInfoService.getProjectUUID(projectId);
        boolean master = this.isMaster(projectId);
        if (master) {
            return R.failed("无法删除主边缘网关项目");
        }

        int count = edgeCloudRequestService.count(new LambdaQueryWrapper<EdgeCloudRequest>()
                .eq(EdgeCloudRequest::getProjectCode, projectUUID)
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code, IntoCloudStatusEnum.PENDING_AUDIT.code, IntoCloudStatusEnum.REVOKE_REQUEST.code));
        if (count > 0) {
            return R.failed("无法删除边缘网关，正在申请入云或是已入云");
        }

        //TODO: 级联项目UUID就是级联申请时的第三方项目ID 至少现在是这样
        EdgeCascadeRequestMaster requestMaster = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getProjectCode, projectUUID).last("limit 1"));
        if (requestMaster == null) {
            return R.failed("未找到对应的级联申请");
        }
        String slaveContactPhone = requestMaster.getSlaveContactPhone();
        List<SysUserVo> userVoList = projectStaffService.getUserVosByDeptId(projectId);
        if (CollUtil.isNotEmpty(userVoList)) {
            // 除了边缘网关负责人的管理员，其他管理员全都删了
            List<SysUserVo> otherAdmin = userVoList.stream()
                    .filter(sysUserVo -> !sysUserVo.getPhone().equals(slaveContactPhone)).collect(Collectors.toList());
            // 如果级联申请时的管理员正好就是主边缘网关上的管理员，这里就不删了
            if (CollUtil.isNotEmpty(otherAdmin) && userVoList.size() != 1) {
                log.info("要删除的管理员信息：{}", JSON.toJSONString(otherAdmin));
                otherAdmin.forEach(item -> {
                    // 这里不删除员工的话，再次同意级联会因为员工手机号已存在而出现问题
                    sysDeptUserService.removeDeptUser(item);
                });
            }
        }
        requestMaster.setCascadeStatus(CascadeStatusConstants.NOT_CASCADE);
        this.updateById(requestMaster);
        return R.ok("删除成功");
    }

//    @Override
//    public R<Page<CascadeProjectInfoVo>> pageCascadeManage(Page page, CascadeManageQuery cascadeManageQuery) {
//        EdgeCascadeRequestMaster requestMaster = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().last("limit 1"));
//        Integer masterProjectId;
//        if (requestMaster != null) {
//            masterProjectId = requestMaster.getProjectId();
//        } else {
//            // 默认第一个项目就是主边缘网关项目
//            ProjectInfo projectInfo = projectInfoService.getOne(new LambdaQueryWrapper<ProjectInfo>().orderByAsc(ProjectInfo::getSeq).last("limit 1"));
//            masterProjectId = projectInfo.getProjectId();
//        }
//        return R.ok(baseMapper.pageCascadeManage(page, cascadeManageQuery, masterProjectId));
//    }
//
//    @Override
//    public R<List<EdgeCascadeRequestMaster>> listRequest(Integer projectId) {
//        // 万一没有项目ID
//        if (projectId != null) {
//            return R.ok(this.list(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getCascadeStatus, CascadeStatusConstants.PENDING_AUDIT)));
//        }
//        List<EdgeCascadeRequestMaster> requestList = this.list(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getProjectId, projectId).eq(EdgeCascadeRequestMaster::getCascadeStatus, CascadeStatusConstants.PENDING_AUDIT));
//        return R.ok(requestList);
//    }

    @Override
    public EdgeCascadeResponse cancelRequest(String projectCode) {
        EdgeCascadeRequestMaster requestMaster = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getProjectCode, projectCode).last("limit 1"));
        EdgeCascadeResponse edgeCascadeResponse = new EdgeCascadeResponse();
        edgeCascadeResponse.setServiceId(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE);

        if (requestMaster == null) {
            edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.CANCEL_CASCADE_REQUEST_SUCCESS.code);
            return edgeCascadeResponse;
        }
        Character cascadeStatus = requestMaster.getCascadeStatus();
        // 被拒绝也是一样算取消失败
        if (CascadeStatusConstants.CASCADE == cascadeStatus || CascadeStatusConstants.REJECT == cascadeStatus) {
            edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.CANCEL_CASCADE_REQUEST_FAILED.code);
            return edgeCascadeResponse;
        }
        requestMaster.setCascadeStatus(CascadeStatusConstants.NOT_CASCADE);
        this.updateById(requestMaster);
        edgeCascadeResponse.setResultCode(EdgeCascadeEventCodeEnum.CANCEL_CASCADE_REQUEST_SUCCESS.code);
        return edgeCascadeResponse;
    }

//    @Override
//    public Integer getOriginProjectId() {
//        return baseMapper.getOriginProjectId();
//    }
}
