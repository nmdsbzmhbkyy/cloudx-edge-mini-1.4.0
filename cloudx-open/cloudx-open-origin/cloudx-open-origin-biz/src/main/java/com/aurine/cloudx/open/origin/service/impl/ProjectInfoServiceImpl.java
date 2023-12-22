
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.aurine.cloudx.open.origin.constant.DeviceCollectConstant;
import com.aurine.cloudx.open.origin.constant.ProjectInfoConstant;
import com.aurine.cloudx.open.origin.constant.SysDeptConstant;
import com.aurine.cloudx.open.origin.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.open.origin.constant.enums.DeviceCollectTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectInfo;
import com.aurine.cloudx.open.origin.mapper.ProjectInfoMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import com.google.common.base.Functions;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.enums.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements ProjectInfoService {
    @Resource

    private RemoteDeptService remoteDeptService;
    @Resource
    private SysDeptUserService sysDeptUserService;
    @Resource
    private RemoteRoleService remoteRoleService;
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
    private ProjectInfoMapper baseMapper;


    /**
     * 更新说明
     * 1: 调整新增方法,补全用户新增校验逻辑()
     *
     * @param projectInfoFormVo 项目视图
     * @return 返回项目id
     */
    @Override
    @GlobalTransactional
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
    @GlobalTransactional
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
    @GlobalTransactional
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
//        //通行方案
//        projectPassPlanService.createByDefault(entity.getProjectId(), TenantContextHolder.getTenantId());

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

//    @Override
//    public AdminUserInfo getCurrentAdminUserInfo() {
//        PigxUser user = SecurityUtils.getUser();
//        if (user != null) {
//            AdminUserInfo adminUserInfo = baseMapper.getAdminUserInfo(user.getId());
//            if (adminUserInfo != null) {
//                ProjectInfo projectInfo = this.getOne(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, adminUserInfo.getDept_id()));
//                if (projectInfo != null) {
//                    adminUserInfo.setProjectName(projectInfo.getProjectName());
//                    adminUserInfo.setProjectId(adminUserInfo.getDept_id());
//                }
//                return adminUserInfo;
//            }
//        }
//        return null;
//    }

    @Override
    public ProjectInfoVo getCascadeProjectInfoVo(Integer projectId) {
        return baseMapper.getCascadeProjectInfoVo(projectId);
    }

//    @Override
//    public Integer createCascadeProject(Integer projectId, EdgeCascadeRequestMaster requestMaster) {
//        String projectCode = requestMaster.getProjectCode();
//        ProjectInfo cascadeProjectInfo = this.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectUUID, projectCode).last("limit 1"));
//        // 级联 从 边缘网关 和 主 边缘网关的项目UUID是一样的
//        if (cascadeProjectInfo != null) {
//            return cascadeProjectInfo.getProjectId();
//        }
//        ProjectInfoFormVo projectInfoFormVo = new ProjectInfoFormVo();
//        BeanPropertyUtil.copyProperty(projectInfoFormVo, requestMaster, requestMasterToFormVo);
//        ProjectInfo projectInfo = this.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId).last("limit 1"));
//        BeanPropertyUtil.copyProperty(projectInfoFormVo, projectInfo, projectInfoToFormVo);
//        projectInfoFormVo.setStatus("1");
//        SysUserVo sysUserVo = new SysUserVo();
//        sysUserVo.setDeptTypeId(DeptTypeEnum.PROJECT.getId());
//        BeanPropertyUtil.copyProperty(sysUserVo, requestMaster, new FieldMapping<SysUserVo, EdgeCascadeRequestMaster>()
//                .add(SysUserVo::getTrueName, EdgeCascadeRequestMaster::getSlaveContactPerson)
//                .add(SysUserVo::getSex, EdgeCascadeRequestMaster::getSlaveGender)
//                .add(SysUserVo::getPhone, EdgeCascadeRequestMaster::getSlaveContactPhone)
//                .add(SysUserVo::getUsername, EdgeCascadeRequestMaster::getSlaveContactPhone)
//        );
//        projectInfoFormVo.setUser(sysUserVo);
//        Integer newProjectId = this.saveReturnId(projectInfoFormVo);
//        //TODO: 这里要调用项目审核通过接口
//
//        // 这里审核通过项目
//        ProjectInfoApprovalVo projectInfoApprovalVo = new ProjectInfoApprovalVo();
//        projectInfoApprovalVo.setProjectId(newProjectId);
//        projectInfoApprovalVo.setPass(true);
//        this.passProject(projectInfoApprovalVo);
//        return newProjectId;
//    }
//
//    static FieldMapping<ProjectInfoFormVo, EdgeCascadeRequestMaster> requestMasterToFormVo = new FieldMapping<ProjectInfoFormVo, EdgeCascadeRequestMaster>()
//            .add(ProjectInfo::getProjectName, EdgeCascadeRequestMaster::getSlaveProjectName)
//            .add(ProjectInfo::getContactPerson, EdgeCascadeRequestMaster::getSlaveContactPerson)
//            .add(ProjectInfo::getContactPhone, EdgeCascadeRequestMaster::getSlaveContactPhone);
//    static FieldMapping<ProjectInfoFormVo, ProjectInfo> projectInfoToFormVo = new FieldMapping<ProjectInfoFormVo, ProjectInfo>()
//            .add(ProjectInfo::getGisType, ProjectInfo::getGisType)
//            .add(ProjectInfo::getCityCode, ProjectInfo::getCityCode)
//            .add(ProjectInfo::getCountyCode, ProjectInfo::getCountyCode)
//            .add(ProjectInfo::getProvinceCode, ProjectInfo::getProvinceCode)
//            .add(ProjectInfo::getStreetCode, ProjectInfo::getStreetCode)
//            .add(ProjectInfo::getEntraExitNum, ProjectInfo::getEntraExitNum)
//            .add(ProjectInfo::getLat, ProjectInfo::getLat)
//            .add(ProjectInfo::getLon, ProjectInfo::getLon)
//            .add(ProjectInfo::getProjectGroupId, ProjectInfo::getProjectGroupId);

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
    public ProjectInfo getByProjectUUID(String projectUUID) {
        if (StringUtils.isEmpty(projectUUID)) return null;
        return this.getOne(new LambdaQueryWrapper<ProjectInfo>()
                .eq(ProjectInfo::getProjectUUID, projectUUID)
                .last("LIMIT 1"));
    }

    @Override
    public Page<ProjectInfoVo> page(Page page, ProjectInfoVo vo) {
        ProjectInfo po = new ProjectInfo();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }

    @Override
    public List<ProjectInfoVo> listCascadeByCloud() {
        return baseMapper.listCascadeByCloud();
    }

    @Override
    public List<ProjectInfoVo> listCascadeByEdge() {
        return baseMapper.listCascadeByEdge();
    }

    @Override
    public List<ProjectInfoVo> listCascadeByMaster() {
        return baseMapper.listCascadeByMaster();
    }

    @Override
    public List<ProjectInfoVo> listCascadeBySlave() {
        return baseMapper.listCascadeBySlave();
    }
}
