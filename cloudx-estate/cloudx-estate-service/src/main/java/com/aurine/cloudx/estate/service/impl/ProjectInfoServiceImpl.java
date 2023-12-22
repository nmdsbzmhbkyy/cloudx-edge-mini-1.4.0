
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.DeviceCollectConstant;
import com.aurine.cloudx.estate.constant.ProjectInfoConstant;
import com.aurine.cloudx.estate.constant.SysDeptConstant;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceCollectTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.mapper.ProjectInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.google.common.base.Functions;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.enums.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.time.DateUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
        sysRole.setRoleName(entity.getProjectName() + SysDeptConstant.PROJECT_NAME);
        sysRole.setRoleCode(SysDeptConstant.PROJECT_CODE + RandomUtil.randomStringUpper(18).replaceAll("\\d+", ""));
        roleId = remoteRoleService.saveRetId(sysRole).getData();
        // 新增小程序业主角色
//        SysRole wechatRole = new SysRole();
//        sysRole.setDeptId(deptId);
//        sysRole.setDsType(DataScopeTypeEnum.CUSTOM.getType());
//        sysRole.setRoleName(entity.getProjectName() + "业主");
//        sysRole.setRoleCode("wechat_" + RandomUtil.randomStringUpper(18).replaceAll("\\d+", ""));
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
            remoteRoleService.updateRoleMenuByThisRoleId(myRoleId, roleId);
        } else {
            //更新角色菜单权限
            remoteRoleService.updateRoleMenuByRoleId(DeptTypeEnum.PROJECT.getId(), roleId);
        }

        sysUserVo.setRoleId(roleId);
        sysUserVo.setDeptId(deptId);
        sysUserVo.setDeptTypeId(DeptTypeEnum.PROJECT.getId());
        //新增项目管理员
        sysDeptUserService.addDeptUser(sysUserVo);


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
        projectInfoFormVo.getUserList().forEach(sysDeptUserService::updateDeptUser);
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
    public Page<ProjectInfoPageVo> pageProjectByStaff(Page page, String projectId) {
        //获取到当前登录用户的
        Integer userId = SecurityUtils.getUser().getId();
        Page<ProjectInfoPageVo> projectInfoPageVo = baseMapper.pageProjectByStaff(page, userId, projectId);
        projectInfoPageVo.getRecords().stream().forEach(e-> {
            //获取公安对接接口判断是否启用
            List<ProjectDeviceCollectListVo> ProjectDeviceCollectListVos = projectDeviceCollectService
                    .getDeviceCollectListVo(e.getProjectId(), DeviceCollectTypeEnum.POLICE.code, DeviceCollectConstant.POLICE_PARAM_NAME);
            String policeStatus = "0";
            if (ProjectDeviceCollectListVos != null && ProjectDeviceCollectListVos.size() > 0) {
                policeStatus = ProjectDeviceCollectListVos.get(0).getAttrValue();
            }
            e.setPoliceEnable(policeStatus);
        });
        return projectInfoPageVo;
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

        } else {
            projectInfo.setAuditStatus(ProjectInfoConstant.FAIL);
        }
        //项目过期时间未填则为永久有效
        if (ObjectUtil.isEmpty(projectInfoApprovalVo.getExpTime())) {
            projectInfo.setExpTime(LocalDateTime.parse("2199-12-30 23:59:59"));
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
}
