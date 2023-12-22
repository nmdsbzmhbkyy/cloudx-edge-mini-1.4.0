package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.SyncConfig;
import com.aurine.cloudx.estate.constant.DeviceCollectConstant;
import com.aurine.cloudx.estate.constant.ProjectInfoConstant;
import com.aurine.cloudx.estate.constant.SysDeptConstant;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceCollectTypeEnum;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.constant.enums.TableNameEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectInfoMapper;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.openapi.sync.factory.SyncFactoryProducer;
import com.aurine.cloudx.estate.openapi.sync.util.SyncUtil;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.util.bean.FieldMapping;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Functions;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.enums.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 项目
 *
 * @author xull@aurine.cn
 * @date 2020-05-06 19:14:05
 */
@Slf4j
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements ProjectInfoService {
    @Resource

    private RemoteDeptService remoteDeptService;
    @Resource
    private SysDeptUserService sysDeptUserService;
    @Resource
    private RemoteRoleService remoteRoleService;
    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private ProjectDeviceCollectService projectDeviceCollectService;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectLabelConfigService projectLabelConfigService;
    @Resource
    private ProjectDeviceSubsystemService projectDeviceSubsystemService;
    @Resource
    private SysProjectDeptService sysProjectDeptService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectCarouselService projectCarouselService;
    @Resource
    private ProjectParkingInfoService projectParkingInfoService;
    @Resource
    @Lazy
    private EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;

    @Resource
    @Lazy
    private EdgeCloudRequestService edgeCloudRequestService;
    @Resource
    private OpenApiMessageService openApiMessageService;
    @Resource
    private SyncConfig syncConfig;
    @Resource
    private EdgeSyncLogService edgeSyncLogService;
    @Resource
    private EdgeCascadeProcessMasterService edgeCascadeProcessMasterService;
    @Resource
    private  ProjectStaffService projectStaffService;
    @Resource
    private ProjectPassPlanService projectPassPlanService;

    private static final String SYNC_TOPIC_REQUEST = "PARKING_INTOCLOUD_REQUEST";

    private static final String SYNC_TOPIC_RESPONSE = "PARKING_INTOCLOUD_RESPONSE";
    @Resource
    private MinioTemplate minioTemplate;

    /**
     * 更新说明
     * 1: 调整新增方法,补全用户新增校验逻辑()
     *
     * @param projectInfoFormVo 项目视图
     * @return 返回项目id
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer saveReturnId(ProjectInfoFormVo projectInfoFormVo) {
        ProjectInfo entity = new ProjectInfo();
        BeanUtils.copyProperties(projectInfoFormVo, entity);

        //新增初始化设置审核状态为待审核 xull@aurine.cn 2020年5月9日 16点30分
        entity.setAuditStatus(ProjectInfoConstant.APPROVAL_PENDING);

        SysDept sysDept = new SysDept();
        sysDept.setName(entity.getProjectName());
        sysDept.setSort(SysDeptConstant.COMPANY_SORT);
        //设置父级部门为所选项目组id
        sysDept.setParentId(entity.getProjectGroupId());
        sysDept.setDeptTypeId(DeptTypeEnum.PROJECT.getId());
        sysDept.setDeptTypeName(DeptTypeEnum.PROJECT.getName());

        Integer deptId = null;
        Integer roleId = null;

        SysUserVo sysUserVo = projectInfoFormVo.getUser();
        //新增部门
        deptId = remoteDeptService.saveRetId(sysDept).getData();
        //设置项目id与pigx部门同步
        entity.setProjectId(deptId);
        //设置操作人
        entity.setOperator(SecurityUtils.getUser().getId());
        //新增项目
        baseMapper.insert(entity);
        //新增项目用户角色
        SysRole sysRole = new SysRole();
        sysRole.setDeptId(deptId);
        sysRole.setDsType(DataScopeTypeEnum.OWN_LEVEL.getType());
//        sysRole.setRoleName(entity.getProjectName() + SysDeptConstant.PROJECT_NAME);
        sysRole.setRoleName(SysDeptConstant.PROJECT_NAME);
        sysRole.setRoleCode(SysDeptConstant.PROJECT_CODE + RandomUtil.randomStringUpper(18).replaceAll("\\d+", ""));
        roleId = remoteRoleService.saveRetId(sysRole).getData();
        // 新增小程序业主角色
//        SysRole wechatRole = new SysRole();
//        wechatRole.setDeptId(deptId);
//        wechatRole.setDsType(DataScopeTypeEnum.CUSTOM.getType());
//        wechatRole.setRoleName(entity.getProjectName() + "业主");
//        wechatRole.setRoleCode("wechat_" + RandomUtil.randomStringUpper(18).replaceAll("\\d+", ""));
//        remoteRoleService.saveRetId(wechatRole);

        //根据当前角色更新权限菜单
        List<String> roleIds = SecurityUtils.getRoles().stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
        List<SysRole> roles = remoteRoleService.getRoleList(roleIds).getData();
        Integer myRoleId = null;
        for (SysRole role : roles) {
            if (ProjectContextHolder.getProjectId().equals(role.getDeptId())) {
                myRoleId = role.getRoleId();
            }
        }
        if (ObjectUtil.isNotEmpty(myRoleId)) {
            remoteRoleService.updateRoleMenuBySysRoleId(myRoleId, roleId, DeptTypeEnum.DEPT.getId());
        } else {
            //更新角色菜单权限
            remoteRoleService.updateRoleMenuByRoleId(DeptTypeEnum.DEPT.getId(), roleId);
        }

        sysUserVo.setRoleId(roleId);
        sysUserVo.setDeptId(deptId);
        sysUserVo.setDeptTypeId(DeptTypeEnum.PROJECT.getId());
        //新增项目管理员
        sysDeptUserService.addUserAndRole(sysUserVo);


        return entity.getProjectId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @TxTransaction(isStart = true)
    public boolean removeById(Serializable id) {

        remoteDeptService.removeById((Integer) id);
        remoteRoleService.removeByDeptId((Integer) id);

        return super.removeById(id);
    }


    /**
     * 更新说明
     * 1:调整更新逻辑,补充更新用户方法 xull@aurine.cn 2020年5月8日 15点16分
     *
     * @param projectInfoFormVo 项目视图
     * @return 返回更新是否成功
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateProjectAndUser(ProjectInfoFormVo projectInfoFormVo) {
        ProjectInfo entity = new ProjectInfo();
        BeanUtils.copyProperties(projectInfoFormVo, entity);
        //未通过的情况下重置申请为待审批
        if (ProjectInfoConstant.FAIL.equals(entity.getAuditStatus())) {
            entity.setAuditStatus(ProjectInfoConstant.APPROVAL_PENDING);
        }
        //更新用户信息
        projectInfoFormVo.getUserList().forEach(sysDeptUserService::updateUserAndRole);
        R<SysDept> resultData = remoteDeptService.getById(entity.getProjectId());
        SysDept sysDept = resultData.getData();
        sysDept.setName(entity.getProjectName());
        remoteDeptService.update(sysDept);
        return super.updateById(entity);
    }

    @Override
    public Page<ProjectInfoPageVo> pageByAdmin(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo) {
        //获取当前用户角色
        List<Integer> roles = SecurityUtils.getRoles();
        List<String> strRoles = roles.stream().map(Functions.toStringFunction()::apply).collect(Collectors.toList());
        List<SysRole> roleList = remoteRoleService.getRoleList(strRoles).getData();
        Integer deptId = null;
        //遍历该角色下的部门
        for (SysRole sysRole : roleList) {
            if (sysRole.getDeptId().equals(projectInfoByAdminFormVo.getPlatformId())) {
                deptId = sysRole.getDeptId();
                break;
            }
        }
        if (deptId == null) {
            throw new RuntimeException("没有该部门角色权限");
        }
        return baseMapper.pageByAdmin(page, projectInfoByAdminFormVo);

    }

    @Override
    public ProjectInfoPageVo getProjectInfoVoById(Integer id) {
        ProjectInfoPageVo projectInfoPageVo = baseMapper.getProjectInfoVoById(id);

        //获取公安对接接口判断是否启用
        List<ProjectDeviceCollectListVo> ProjectDeviceCollectListVos = projectDeviceCollectService
                .getDeviceCollectListVo(ProjectContextHolder.getProjectId(), DeviceCollectTypeEnum.POLICE.code, DeviceCollectConstant.POLICE_PARAM_NAME);
        String policeStatus = "0";
        if (ProjectDeviceCollectListVos != null && ProjectDeviceCollectListVos.size() > 0) {
            policeStatus = ProjectDeviceCollectListVos.get(0).getAttrValue();
        }
        projectInfoPageVo.setPoliceEnable(policeStatus);

        return projectInfoPageVo;
    }

    @Override
    public Page<ProjectInfoPageVo> pageByConfig(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo) {
        //获取当前用户角色
        List<Integer> roles = SecurityUtils.getRoles();
        List<String> strRoles = roles.stream().map(Functions.toStringFunction()::apply).collect(Collectors.toList());
        List<SysRole> roleList = remoteRoleService.getRoleList(strRoles).getData();
        Integer deptId = null;
        //遍历该角色下的部门
        for (SysRole sysRole : roleList) {
            if (sysRole.getDeptId().equals(projectInfoByAdminFormVo.getPlatformId())) {
                deptId = sysRole.getDeptId();
                break;
            }
        }
        if (deptId == null) {
            throw new RuntimeException("没有该部门角色权限");
        }
        projectInfoByAdminFormVo.setAuditStatusList(Arrays.asList(AuditStatusEnum.pass.code, AuditStatusEnum.inAudit.code));
        return baseMapper.pageByConfig(page, projectInfoByAdminFormVo);
    }

    @Override
    public Page<ProjectInfoPageVo> pageByVisible(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo) {
        projectInfoByAdminFormVo.setAuditStatusList(Arrays.asList(AuditStatusEnum.pass.code));
        return baseMapper.pageByConfig(page, projectInfoByAdminFormVo);
    }

    @Override
    public Page<ProjectInfoPageVo> pageProject(Page page, ProjectInfo projectInfo) {
        return baseMapper.pageProject(page, projectInfo);
    }

    /**
     * 获取所有啓用中且过审核的项目列表
     *
     * @return
     * @author: 王伟
     * @since :2020-09-16
     */
    @Override
    public List<ProjectInfo> listProject() {
        return list(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getStatus, "1").eq(ProjectInfo::getAuditStatus, AuditStatusEnum.pass.code));
    }

    @Override
    public Page<ProjectInfoPageVo> pageProjectByStaff(Page page) {
        //获取到当前登录用户的
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.pageProjectByStaff(page, userId);
    }

    @Override
    public Page<ProjectInfoPageVo> pageProjectByPerson(Page page) {
        //获取到当前登录用户的
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.pageProjectByPerson(page, userId);
    }

    @Override
    public Page<ProjectInfoSimplePageVo> pageAll(Page page, ProjectAddressParamVo address) {

        return baseMapper.pageAll(page, address, address.getUserId(), address.getType());
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean pass(Integer projectId) {
        ProjectInfo projectInfo = getById(projectId);
        //如果项目已经通过无需重复设置通过
        if (ProjectInfoConstant.PASSED.equals(projectInfo.getAuditStatus())) {
            return false;
        }

        projectInfo.setAuditStatus(ProjectInfoConstant.PASSED);
        //迁移到审核通过后初始化数据 xull
        /**
         * 创建项目后，初始化数据
         * @author: 王伟
         * @since 2020-09-08
         */
        //设备子系统
        projectDeviceSubsystemService.initDeviceSubSysTem(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
        //组团
        projectEntityLevelCfgService.initEntityLevelCfg(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
        //人员标签
        projectLabelConfigService.initLabel(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
        //部门
        sysProjectDeptService.initDept(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
        //区域
        projectDeviceRegionService.initDefaultData(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
        //轮播图
        projectCarouselService.init(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
        //通行方案
        projectPassPlanService.createByDefault(projectInfo.getProjectId(), TenantContextHolder.getTenantId());

        updateById(projectInfo);
        return true;
    }

    /**
     * 审核通过
     *
     * @param projectInfoApprovalVo 项目id
     * @return
     */
    @Override
    public boolean passByVo(ProjectInfoApprovalVo projectInfoApprovalVo) {
        ProjectInfo projectInfo = this.getById(projectInfoApprovalVo.getProjectId());
        if (projectInfoApprovalVo.getPass()) {
            projectInfo.setAuditStatus(ProjectInfoConstant.PASSED);

            /**
             * 创建项目后，初始化数据
             * @author: 王伟
             * @since 2020-09-08
             */
            //设备子系统
            projectDeviceSubsystemService.initDeviceSubSysTem(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
            //组团
            projectEntityLevelCfgService.initEntityLevelCfg(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
            //人员标签
            projectLabelConfigService.initLabel(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
            //部门
            sysProjectDeptService.initDept(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
            //区域
            projectDeviceRegionService.initDefaultData(projectInfo.getProjectId(), TenantContextHolder.getTenantId());

            //审批状态初始化
            projectConfigService.initDefaultData(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
            //轮播图
            projectCarouselService.init(projectInfo.getProjectId(), TenantContextHolder.getTenantId());
            // 车场
            projectParkingInfoService.init(projectInfo.getProjectName(), projectInfo.getProjectId(), TenantContextHolder.getTenantId());

        } else {
            projectInfo.setAuditStatus(ProjectInfoConstant.FAIL);
        }
        //项目过期时间未填则为永久有效
        if (ObjectUtil.isEmpty(projectInfoApprovalVo.getExpTime())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse("2199-12-30 23:59:59", df);
            projectInfo.setExpTime(date);
        } else {
            projectInfo.setExpTime(DateUtil.parseLocalDateTime(projectInfoApprovalVo.getExpTime().toString() + " 23:59:59"));
        }
        projectInfo.setAuditReason(projectInfoApprovalVo.getAuditReason());


        return this.updateById(projectInfo);

    }

    @Override
    public List<ProjectInfoSimplePageVo> listAll(ProjectAddressParamVo address) {
        return baseMapper.listAll(address, address.getUserId(), address.getType());
    }

    @Override
    public Integer getIntervals(Integer projectId) {
        return baseMapper.getIntervals(projectId);
    }

    /**
     * 计算两地距离（米）
     *
     * @param longitudeFrom 来源经度
     * @param latitudeFrom  来源维度
     * @param longitudeTo   目标经度
     * @param latitudeTo    目标维度
     * @return 距离（米）
     */
    public static double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo) {
        GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
        GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);

        return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }

    @Override
    public String getProjectUUID(Integer projectId) {
        ProjectInfo projectInfo = this.getOne(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
        if (StrUtil.isEmpty(projectInfo.getProjectUUID())) {
            projectInfo.setProjectUUID(UUID.randomUUID().toString().replaceAll("-", ""));
            this.updateById(projectInfo);
        }
        return projectInfo.getProjectUUID();
    }

    @Override
    public AdminUserInfo getCurrentAdminUserInfo() {
        PigxUser user = SecurityUtils.getUser();
        if (user != null) {
            AdminUserInfo adminUserInfo = baseMapper.getAdminUserInfo(user.getId());
            if (adminUserInfo != null) {
//                ProjectInfo projectInfo = this.getOne(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, adminUserInfo.getProjectId()));
//                if (projectInfo != null) {
//                    adminUserInfo.setProjectName(projectInfo.getProjectName());
//                    adminUserInfo.setProjectId(adminUserInfo.getDept_id());
//                }
                return adminUserInfo;
            }
        }
        return null;
    }

    @Override
    public ProjectInfoVo getCascadeProjectInfoVo(Integer projectId) {
        return baseMapper.getCascadeProjectInfoVo(projectId);
    }

    @Override
    public Integer createCascadeProject(Integer projectId, EdgeCascadeRequestMaster requestMaster) {
        String projectCode = requestMaster.getProjectCode();
        ProjectInfo cascadeProjectInfo = this.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectCode, projectCode).last("limit 1"));
        // 级联 从 边缘网关 和 主 边缘网关的项目UUID是一样的
        if (cascadeProjectInfo != null) {
            return cascadeProjectInfo.getProjectId();
        }
        ProjectInfoFormVo projectInfoFormVo = new ProjectInfoFormVo();
        BeanPropertyUtil.copyProperty(projectInfoFormVo, requestMaster, requestMasterToFormVo);
        ProjectInfo projectInfo = this.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId).last("limit 1"));
        BeanPropertyUtil.copyProperty(projectInfoFormVo, projectInfo, projectInfoToFormVo);
        projectInfoFormVo.setStatus("1");
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setDeptTypeId(DeptTypeEnum.PROJECT.getId());
        BeanPropertyUtil.copyProperty(sysUserVo, requestMaster, new FieldMapping<SysUserVo, EdgeCascadeRequestMaster>()
                .add(SysUserVo::getTrueName, EdgeCascadeRequestMaster::getSlaveContactPerson)
                .add(SysUserVo::getSex, EdgeCascadeRequestMaster::getSlaveGender)
                .add(SysUserVo::getPhone, EdgeCascadeRequestMaster::getSlaveContactPhone)
                .add(SysUserVo::getUsername, EdgeCascadeRequestMaster::getSlaveContactPhone)
        );
        projectInfoFormVo.setUser(sysUserVo);
        Integer newProjectId = this.saveReturnId(projectInfoFormVo);
        //TODO: 这里要调用项目审核通过接口

        // 这里审核通过项目
        ProjectInfoApprovalVo projectInfoApprovalVo = new ProjectInfoApprovalVo();
        projectInfoApprovalVo.setProjectId(newProjectId);
        projectInfoApprovalVo.setPass(true);
        this.passProject(projectInfoApprovalVo);
        return newProjectId;
    }

    static FieldMapping<ProjectInfoFormVo, EdgeCascadeRequestMaster> requestMasterToFormVo = new FieldMapping<ProjectInfoFormVo, EdgeCascadeRequestMaster>()
            .add(ProjectInfo::getProjectName, EdgeCascadeRequestMaster::getSlaveProjectName)
            .add(ProjectInfo::getContactPerson, EdgeCascadeRequestMaster::getSlaveContactPerson)
            .add(ProjectInfo::getContactPhone, EdgeCascadeRequestMaster::getSlaveContactPhone);
    static FieldMapping<ProjectInfoFormVo, ProjectInfo> projectInfoToFormVo = new FieldMapping<ProjectInfoFormVo, ProjectInfo>()
            .add(ProjectInfo::getGisType, ProjectInfo::getGisType)
            .add(ProjectInfo::getCityCode, ProjectInfo::getCityCode)
            .add(ProjectInfo::getCountyCode, ProjectInfo::getCountyCode)
            .add(ProjectInfo::getProvinceCode, ProjectInfo::getProvinceCode)
            .add(ProjectInfo::getStreetCode, ProjectInfo::getStreetCode)
            .add(ProjectInfo::getEntraExitNum, ProjectInfo::getEntraExitNum)
            .add(ProjectInfo::getLat, ProjectInfo::getLat)
            .add(ProjectInfo::getLon, ProjectInfo::getLon)
            .add(ProjectInfo::getProjectGroupId, ProjectInfo::getProjectGroupId);

    @Override
    public void updateProjectCode(Integer projectId, String projectCode) {
        this.update(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId).set(ProjectInfo::getProjectCode, projectCode));
    }

    @Override
    public void updateProjectUUID(Integer projectId, String uuid) {
        this.update(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId).set(ProjectInfo::getProjectUUID, uuid));
    }

    @Override
    public Integer getProjectId(String projectUUID) {
        ProjectInfo projectInfo = this.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectUUID, projectUUID).last("limit 1"));
        return projectInfo != null ? projectInfo.getProjectId() : null;
    }

    @Override
    public void passProject(ProjectInfoApprovalVo projectInfoApprovalVo) {
        ProjectInfo projectInfo = this.getById(projectInfoApprovalVo.getProjectId());
        if (projectInfoApprovalVo.getPass()) {
            projectInfo.setAuditStatus(ProjectInfoConstant.PASSED);
        } else {
            projectInfo.setAuditStatus(ProjectInfoConstant.FAIL);
        }
        //项目过期时间未填则为永久有效
        if (ObjectUtil.isEmpty(projectInfoApprovalVo.getExpTime())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse("2199-12-30 23:59:59", df);
            projectInfo.setExpTime(date);
        } else {
            projectInfo.setExpTime(DateUtil.parseLocalDateTime(projectInfoApprovalVo.getExpTime().toString() + " 23:59:59"));
        }
        projectInfo.setAuditReason(projectInfoApprovalVo.getAuditReason());
        this.passByVo(projectInfoApprovalVo);
        this.updateById(projectInfo);
    }

    @Override
    public boolean checkCommunityIdIsOriginProject(String communityId) {
        if (StrUtil.isNotEmpty(communityId)) {
            Integer originProjectId = edgeCascadeRequestMasterService.getOriginProjectId();
            String projectUUID = this.getProjectUUID(originProjectId);
            // 如果社区ID不是自带项目的UUID这里就结束自动注册流程
            return projectUUID.equals(communityId);
        }
        return true;
    }

    @Override
    public boolean checkProjectIdIsOriginProject(Integer projectId) {

        Integer originProjectId = edgeCascadeRequestMasterService.getOriginProjectId();
        return projectId.equals(originProjectId);
    }

    @Override
    public boolean checkCommunityIdIsOriginProject(String json, String communityIdFieldName) {
        ObjectMapper instance = ObjectMapperUtil.instance();
        try {
            JsonNode jsonNode = instance.readTree(json);
            JsonNode communityIdNode = jsonNode.findPath(communityIdFieldName);
            if (!communityIdNode.isMissingNode()) {
                return this.checkCommunityIdIsOriginProject(communityIdNode.asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    @Override
    public R delAllObj(Integer projectId) {
        LambdaQueryWrapper<EdgeCloudRequest> query = new QueryWrapper<EdgeCloudRequest>().lambda();
        query.eq(EdgeCloudRequest::getProjectId, projectId);
        query.eq(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code);
        List<SysUserVo> userVosByDeptId = projectStaffService.getUserVosByDeptId(projectId);
        EdgeCloudRequest one = edgeCloudRequestService.getOne(query);
        if(one != null && one.getSyncType() != null && StringUtils.equals("2",one.getSyncType().toString())){
            List<String> serviceName = TableNameEnum.getServiceName();
            //List<String> parkingServiceName = TableNameEnum.getParkingServiceName();
            one.setDelStatus('2');
            edgeCloudRequestService.update(one,query);
            new Thread(() -> {
                String params = null;
                boolean flag = true;
                for (String tableName : serviceName) {
                    try {
                        log.info("删除表：{}",tableName);
                        if(tableName.equals("project_staff")) {

                            String join = StringUtils.join(userVosByDeptId.stream().filter(item->StringUtils.isNotBlank(item.getStaffId())).map(item -> item.getStaffId()).collect(Collectors.toList()), "','");
                            params="staffId not in ('"+join+"')";
                            userVosByDeptId.forEach(item->{
                                remoteUserService.removeUserRoleInner(item.getUserId(),item.getRoleId(), SecurityConstants.FROM_IN);
                            });
                        } else if("project_device_info".equals(tableName)){
                            params = "deviceType not in ('" + DeviceTypeConstants.DEVICE_DRIVER +"')";
                        } else {
                            params = null;
                        }
                        baseMapper.deleteByTableName("aurine",tableName, projectId,params);
                    } catch (Exception e) {
                        log.error("删除失败：",e);
                        log.info("删除失败：{}", tableName);
                        flag = false;
                    }
                }
//                for (String tableName : parkingServiceName) {
//                    try {
//                        log.info("删除表：{}",tableName);
//                        baseMapper.deleteByTableName("aurine_parking",tableName, projectId,null);
//                    } catch (Exception e) {
//                        log.error("删除失败：",e);
//                        log.info("删除失败：{}", tableName);
//                        flag = false;
//                    }
//                }
                if (flag) {
                    one.setDelStatus('1');
                    edgeCloudRequestService.update(one, query);
                    log.info("通知边缘网关侧已经删除成功:{}", projectId);
                    OpenApiEntity openApiEntity = new OpenApiEntity();
                    openApiEntity.setTenantId(1);
                    openApiEntity.setProjectUUID(this.getProjectUUID(projectId));
                    openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.COMMAND.name);
                    openApiEntity.setServiceName(OpenApiServiceNameEnum.CASCADE_CLOUD_EDGE_REQUEST.name);
                    openApiEntity.setCommandType(OpenApiCommandTypeEnum.EMPTY.name);
                    openApiEntity.setData(one);
                    openApiMessageService.sendOpenApiMessage(openApiEntity);

                }else{
                    one.setDelStatus('0');
                    edgeCloudRequestService.update(one, query);
                }
            }).start();
        }else{
            return R.failed("已选择以边缘侧为主，不能删除边缘侧数据！");
        }
        return R.ok();
    }

    @Override
    public R sync(Integer projectId) {
        //校验清空状态
        EdgeCloudRequest cloudRequest = edgeCloudRequestService.getOne(Wrappers.lambdaQuery(EdgeCloudRequest.class).eq(EdgeCloudRequest::getProjectId, projectId));
        if (cloudRequest == null) {
            return R.failed("未找到该数据");
        }
        if (!cloudRequest.getDelStatus().equals('1')) {
            return R.failed("数据未清空");
        }
        String syncId = UUID.randomUUID().toString().replaceAll("-", "");
        //查询项目的UUID
        String projectUUID = getProjectUUID(projectId);
        try {
            //通知云端开始同步
            startSync(cloudRequest,projectUUID);

            //生成同步日志
            generateSyncLogs(projectId, projectUUID,syncId);

            //生成同步任务
            generateSyncTask(projectId, projectUUID, syncId);
            //执行同步任务 查询数据丢向openApi
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                performSyncTask(projectId, projectUUID, syncId);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            EdgeSyncLog edgeSyncLog = edgeSyncLogService.getOne(Wrappers.lambdaQuery(EdgeSyncLog.class).eq(EdgeSyncLog::getSyncId, syncId));
            edgeSyncLog.setErrMsg(e.getMessage());
            edgeSyncLogService.updateById(edgeSyncLog);
            //更新同步日志
            OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiCascadeTypeEnum.UPDATE.name, OpenApiServiceNameEnum.CASCADE_SYNC_LOG.name, edgeSyncLog);
            log.info("传输数据：{}", JSONObject.toJSONString(openApiEntity));
            openApiMessageService.sendOpenApiMessage(openApiEntity);
            return R.failed("同步失败");
        }

        return R.ok();
    }

    /**
     * 通知云端开始同步
     *
     * @param edgeCloudRequest
     * @param projectUUID
     */
    private void startSync(EdgeCloudRequest edgeCloudRequest,String projectUUID) {
        edgeCloudRequest.setIsSync('1');
        edgeCloudRequestService.updateById(edgeCloudRequest);

        OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByCommand(projectUUID, OpenPushSubscribeCallbackTypeEnum.COMMAND.name, OpenApiCommandTypeEnum.CHANGE.name, OpenApiServiceNameEnum.CASCADE_CLOUD_EDGE_REQUEST.name, edgeCloudRequest);
        log.info("传输数据：{}", JSONObject.toJSONString(openApiEntity));
        openApiMessageService.sendOpenApiMessage(openApiEntity);
    }

    /**
     * 生成同步日志
     *
     * @param projectId
     */
    private String generateSyncLogs(Integer projectId, String projectUUID,String syncId) {
        //查询项目的同步方式
        EdgeCloudRequest cloudRequest = edgeCloudRequestService.getOne(Wrappers.lambdaQuery(EdgeCloudRequest.class).eq(EdgeCloudRequest::getProjectId, projectId));
        Character syncType = cloudRequest.getSyncType();

        //记录同步日志
        EdgeSyncLog edgeSyncLog = new EdgeSyncLog();
        edgeSyncLog.setSyncType(String.valueOf(syncType));
        edgeSyncLog.setSyncTime(LocalDateTime.now());
        edgeSyncLog.setSyncProcess(0.0);
        edgeSyncLog.setSyncStatus("0");
        edgeSyncLog.setProjectId(projectId);
        edgeSyncLog.setTenantId(1);
        edgeSyncLog.setSyncId(syncId);

        //int syncNum = syncConfig.getServiceName().size() + syncConfig.getParkingServiceName().size();
        int syncNum = syncConfig.getServiceName().size();
        edgeSyncLog.setSyncNum(syncNum);

        edgeSyncLogService.save(edgeSyncLog);

        //传输对象
        OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiCascadeTypeEnum.ADD.name, OpenApiServiceNameEnum.CASCADE_SYNC_LOG.name, edgeSyncLog);
        log.info("传输数据：{}", JSONObject.toJSONString(openApiEntity));

        openApiMessageService.sendOpenApiMessage(openApiEntity);
        return edgeSyncLog.getSyncId();
    }

    /**
     * 生成同步任务
     */
    private void generateSyncTask(Integer projectId, String projectUUID, String syncId) {
        List<String> serviceNameList = syncConfig.getServiceName();
        //List<String> parkingServiceNameList = syncConfig.getParkingServiceName();
        //生成同步任务
        serviceNameList.forEach(serviceName -> taskSendOpenApi(projectId, projectUUID, syncId, serviceName));
        //parkingServiceNameList.forEach(serviceName -> taskSendOpenApi(projectId, projectUUID, syncId, serviceName));
    }

    /**
     * 任务发送给openApi
     *
     * @param projectId
     * @param projectUUID
     * @param syncId
     * @param serviceName
     */
    private void taskSendOpenApi(Integer projectId, String projectUUID, String syncId, String serviceName) {
        EdgeCascadeProcessMaster edgeCascadeProcessMaster = new EdgeCascadeProcessMaster();
        edgeCascadeProcessMaster.setProjectId(projectId);
        edgeCascadeProcessMaster.setSyncId(syncId);
        edgeCascadeProcessMaster.setCreateTime(LocalDateTime.now());
        edgeCascadeProcessMaster.setServiceName(serviceName);
        edgeCascadeProcessMaster.setStatus("0");
        edgeCascadeProcessMasterService.save(edgeCascadeProcessMaster);

        OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiCascadeTypeEnum.ADD.name, OpenApiServiceNameEnum.CASCADE_PROCESS_MASTER.name, edgeCascadeProcessMaster);
        log.info("传输数据：{}", JSONObject.toJSONString(openApiEntity));

        openApiMessageService.sendOpenApiMessage(openApiEntity);
    }

    /**
     * 执行同步任务
     *
     * @param projectId
     * @param projectUUID
     * @param syncId
     */
    public void performSyncTask(Integer projectId, String projectUUID, String syncId) {
        List<String> serviceNameList = syncConfig.getServiceName();
        //处理aurine库的
        serviceNameList.forEach(e-> SyncFactoryProducer.getInstance(e).perform(projectId, projectUUID));
        //处理aurine_parking库
//        List<String> parkingServiceNameList = syncConfig.getParkingServiceName();
//        Integer number = syncConfig.getNumber();
//
//        SyncVo syncVo = new SyncVo();
//        syncVo.setParkingServiceNameList(parkingServiceNameList);
//        syncVo.setProjectId(projectId);
//        syncVo.setProjectUUID(projectUUID);
//        syncVo.setNum(number);
//        syncVo.setSyncId(syncId);
//        KafkaProducer.sendMessage(SYNC_TOPIC_REQUEST,syncVo);
    }

    @KafkaListener(topics = SYNC_TOPIC_RESPONSE)
    public void syncListener(String message) {
        log.info("[车场入云] 处理完的数据:{}",message);
        SyncVo syncVo = JSONObject.parseObject(message, SyncVo.class);

        ProjectContextHolder.setProjectId(syncVo.getProjectId());
        TenantContextHolder.setTenantId(1);

        //更新状态
        EdgeCascadeProcessMaster edgeCascadeProcessMaster = edgeCascadeProcessMasterService.getOne(Wrappers.lambdaQuery(EdgeCascadeProcessMaster.class)
                .eq(EdgeCascadeProcessMaster::getServiceName, syncVo.getServiceName())
                .orderByDesc(EdgeCascadeProcessMaster::getCreateTime)
                .last("limit 1"));
        edgeCascadeProcessMaster.setStatus("1");
        edgeCascadeProcessMasterService.updateById(edgeCascadeProcessMaster);

        //传输对象
        OpenApiEntity edgeCascadeProcessMasterEntity = SyncUtil.getOpenApiEntityByOperate(syncVo.getProjectUUID(), OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiCascadeTypeEnum.UPDATE.name, OpenApiServiceNameEnum.CASCADE_PROCESS_MASTER.name, edgeCascadeProcessMaster);
        openApiMessageService.sendOpenApiMessage(edgeCascadeProcessMasterEntity);

        if((syncVo.getParkingServiceNameList().get(syncVo.getParkingServiceNameList().size()-1)).equals(syncVo.getServiceName())){
            EdgeSyncLog edgeSyncLog = edgeSyncLogService.getOne(Wrappers.lambdaQuery(EdgeSyncLog.class).eq(EdgeSyncLog::getSyncId, syncVo.getSyncId()));
            edgeSyncLog.setSyncStatus("1");
            edgeSyncLogService.updateById(edgeSyncLog);

            //更新同步日志
            OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(syncVo.getProjectUUID(), OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiCascadeTypeEnum.UPDATE.name, OpenApiServiceNameEnum.CASCADE_SYNC_LOG.name, edgeSyncLog);
            log.info("传输数据：{}", JSONObject.toJSONString(openApiEntity));
            openApiMessageService.sendOpenApiMessage(openApiEntity);
        }
    }


    @Override
    public R initMinio() {
        //      #- MINIO_DEFAULT_BUCKETS=saas-res-project,saasv4-device,saasv4-project-res,saasv4-res-face
        List<String> list = new ArrayList<>();
        list.add("saas-res-project");
        list.add("saasv4-device");
        list.add("saasv4-project-res");
        list.add("saasv4-res-face");
        list.forEach(e-> minioTemplate.createBucket(e));
        return R.ok();
    }
}
