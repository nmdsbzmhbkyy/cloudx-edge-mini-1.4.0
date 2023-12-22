package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.PersonConstant;
import com.aurine.cloudx.estate.constant.StaffGradeConstant;
import com.aurine.cloudx.estate.constant.SysDeptConstant;
import com.aurine.cloudx.estate.constant.SysProjectDeptConstant;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.service.SysDeptUserService;
import com.aurine.cloudx.estate.service.SysProjectDeptService;
import com.aurine.cloudx.estate.vo.SysUserVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: ServiceImpl
 * Description: 操作pigx用户
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/7 17:54
 */
@Service
public class SysDeptUserServiceImpl implements SysDeptUserService {
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private RemoteRoleService remoteRoleService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private SysProjectDeptService sysProjectDeptService;
    @Resource
    private RemoteDeptService remoteDeptService;

    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Deprecated
    public Integer addDeptUser(SysUserVo sysUserVo) {

        //创建用户传输对象
        CxUserDTO userDTO = new CxUserDTO();
        //新增用户需设置用户名为手机号
        sysUserVo.setPhone(sysUserVo.getUsername());
        R<UserInfo> userInfoR = remoteUserService.info(sysUserVo.getUsername(), SecurityConstants.FROM_IN);
        if (userInfoR.getCode() == 0 && userInfoR.getData() != null) {
            UserInfo userInfo = userInfoR.getData();
            sysUserVo.setUserId(userInfo.getSysUser().getUserId());
            //校验是否含有云平台用户角色
            R<List<SysRole>> data = remoteRoleService.getByDeptId(SysDeptConstant.AURINE_ID);
            if (data != null && data.getData() != null) {
                List<SysRole> roleList = data.getData();
                Integer[] roles = roleList.stream().map(SysRole::getRoleId).toArray(Integer[]::new);
                //判断所选是否为云平台用户
                if (ArrayUtils.contains(roles, sysUserVo.getRoleId())) {
                    //判断是否已有云平台用户
                    if (ArrayUtil.containsAny(roles, userInfo.getRoles())) {
                        //已有云平台用户角色阻止新增,保证一个部门层级底下只有一个角色
                        throw new RuntimeException("新增失败,该用户已有云平台角色");
                    }
                }
            }
            //校验用户角色是否重复
            if (ArrayUtils.contains(userInfo.getRoles(), sysUserVo.getRoleId())) {
                throw new RuntimeException("新增失败,该用户已有该角色");
            }
        }
        // 用户存在情况下
        if (sysUserVo.getUserId() != null && sysUserVo.getUserId() != 0) {
            //拷贝用户信息到用户传输对象上
            BeanUtils.copyProperties(sysUserVo, userDTO);
            //新增角色关系需把roleId清空
            userDTO.setRoleId(null);
            userDTO.setNewRoleId(sysUserVo.getRoleId());
            remoteUserService.editUserRole(userDTO);
            //判断是否是项目管理员新增
            if (BeanUtil.isNotEmpty(sysUserVo.getDeptId())) {
                if (BeanUtil.isNotEmpty(sysUserVo.getDeptTypeId()) && sysUserVo.getDeptTypeId().equals(DeptTypeEnum.PROJECT.getId())) {
                    saveProjectDeptAndStaff(sysUserVo);
                }
            }
            return sysUserVo.getUserId();
        } else {
            //用户不存在情况下
            BeanUtils.copyProperties(sysUserVo, userDTO);
            userDTO.setPassword(PersonConstant.PASSWORD);
            //设置角色
            userDTO.setNewRoleId(sysUserVo.getRoleId());
            Integer userId = remoteUserService.saveUserRole(userDTO).getData();
            sysUserVo.setUserId(userId);
            //判断是否是项目管理员新增
            if (BeanUtil.isNotEmpty(sysUserVo.getDeptId())) {
                if (BeanUtil.isNotEmpty(sysUserVo.getDeptTypeId()) && sysUserVo.getDeptTypeId().equals(DeptTypeEnum.PROJECT.getId())) {
                    saveProjectDeptAndStaff(sysUserVo);
                }
            }
            return userId;
        }

    }

    /**
     * 根据用户信息创建本地员工以及用户账号和角色
     *
     * @param sysUserVo
     * @return
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer addUserAndRole(SysUserVo sysUserVo) {

        //创建用户传输对象
        CxUserDTO userDTO = new CxUserDTO();
        //新增用户需设置用户名为手机号
        sysUserVo.setPhone(sysUserVo.getUsername());
        R<UserInfo> userInfoR = remoteUserService.info(sysUserVo.getUsername(), SecurityConstants.FROM_IN);
        //如果存在该手机号的用户
        if (userInfoR.getCode() == 0 && userInfoR.getData() != null) {
            UserInfo userInfo = userInfoR.getData();
            sysUserVo.setUserId(userInfo.getSysUser().getUserId());
            //校验是否含有云平台用户角色
            R<List<SysRole>> data = remoteRoleService.getByDeptId(SysDeptConstant.AURINE_ID);
            if (data != null && data.getData() != null) {
                List<SysRole> roleList = data.getData();
                Integer[] roles = roleList.stream().map(SysRole::getRoleId).toArray(Integer[]::new);
                //判断所选是否为云平台用户
                if (ArrayUtils.contains(roles, sysUserVo.getRoleId())) {
                    //判断是否已有云平台用户
                    if (ArrayUtil.containsAny(roles, userInfo.getRoles())) {
                        //已有云平台用户角色阻止新增,保证一个部门层级底下只有一个角色
                        throw new RuntimeException("新增失败,该用户已有云平台角色");
                    }
                }
            }
            //校验用户角色是否重复
            if (ArrayUtils.contains(userInfo.getRoles(), sysUserVo.getRoleId())) {
                throw new RuntimeException("新增失败,该用户已有该角色");
            }
        }
        // 用户存在情况下
        if (sysUserVo.getUserId() != null && sysUserVo.getUserId() != 0) {
            //拷贝用户信息到用户传输对象上
            BeanUtils.copyProperties(sysUserVo, userDTO);
            //新增角色关系需把roleId清空
            userDTO.setRoleId(null);
            userDTO.setNewRoleId(sysUserVo.getRoleId());
            remoteUserService.editUserRole(userDTO);
            //新增员工信息
            saveDeptAndStaff(sysUserVo);
            return sysUserVo.getUserId();
        } else {
            //用户不存在情况下
            BeanUtils.copyProperties(sysUserVo, userDTO);
            userDTO.setPassword(PersonConstant.PASSWORD);
            //设置角色
            userDTO.setNewRoleId(sysUserVo.getRoleId());
            Integer userId = remoteUserService.saveUserRole(userDTO).getData();
            sysUserVo.setUserId(userId);
            //新增员工信息
            saveDeptAndStaff(sysUserVo);

            return userId;
        }

    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer addUserAndRoleInner(SysUserVo sysUserVo) {
        //创建用户传输对象
        CxUserDTO userDTO = new CxUserDTO();
        //新增用户需设置用户名为手机号
        sysUserVo.setPhone(sysUserVo.getUsername());
        R<UserInfo> userInfoR = remoteUserService.info(sysUserVo.getUsername(), SecurityConstants.FROM_IN);
        //如果存在该手机号的用户
        if (userInfoR.getCode() == 0 && userInfoR.getData() != null) {
            UserInfo userInfo = userInfoR.getData();
            sysUserVo.setUserId(userInfo.getSysUser().getUserId());
            //校验是否含有云平台用户角色
            R<List<SysRole>> data = remoteRoleService.getByDeptIdInner(SysDeptConstant.AURINE_ID, SecurityConstants.FROM_IN);
            if (data != null && data.getData() != null) {
                List<SysRole> roleList = data.getData();
                Integer[] roles = roleList.stream().map(SysRole::getRoleId).toArray(Integer[]::new);
                //判断所选是否为云平台用户
                if (ArrayUtils.contains(roles, sysUserVo.getRoleId())) {
                    //判断是否已有云平台用户
                    if (ArrayUtil.containsAny(roles, userInfo.getRoles())) {
                        //已有云平台用户角色阻止新增,保证一个部门层级底下只有一个角色
                        throw new RuntimeException("新增失败,该用户已有云平台角色");
                    }
                }
            }
            //校验用户角色是否重复
            if (ArrayUtils.contains(userInfo.getRoles(), sysUserVo.getRoleId())) {
                return  0;
            }
        }
        // 用户存在情况下
        if (sysUserVo.getUserId() != null && sysUserVo.getUserId() != 0) {
            //拷贝用户信息到用户传输对象上
            BeanUtils.copyProperties(sysUserVo, userDTO);
            //新增角色关系需把roleId清空
            userDTO.setRoleId(null);
            userDTO.setNewRoleId(sysUserVo.getRoleId());
            remoteUserService.editUserRoleInner(userDTO, SecurityConstants.FROM_IN);
            //新增员工信息
            saveDeptAndStaff(sysUserVo);
            return sysUserVo.getUserId();
        } else {
            //用户不存在情况下
            BeanUtils.copyProperties(sysUserVo, userDTO);
            userDTO.setPassword(PersonConstant.PASSWORD);
            //设置角色
            userDTO.setNewRoleId(sysUserVo.getRoleId());
            Integer userId = remoteUserService.saveUserRoleInner(userDTO, SecurityConstants.FROM_IN).getData();
            sysUserVo.setUserId(userId);
            //新增员工信息
            saveDeptAndStaff(sysUserVo);

            return userId;
        }
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserAndRole(SysUserVo sysUserVo) {
        //判断编辑后切换的角色是否与其他角色重复
        R<UserInfo> userInfoR = remoteUserService.info(sysUserVo.getUsername(), SecurityConstants.FROM_IN);
        if (userInfoR.getCode() == 0 && userInfoR.getData() != null) {
            UserInfo userInfo = userInfoR.getData();
            sysUserVo.setUserId(userInfo.getSysUser().getUserId());
            Integer[] roleIds = userInfo.getRoles();
            roleIds = ArrayUtils.removeElement(roleIds, sysUserVo.getOldRoleId());
            if (ArrayUtils.contains(roleIds, sysUserVo.getRoleId())) {
                throw new RuntimeException("编辑失败,该用户已有该角色");
            }
        }
//        //判断手机号是否已存在
//        Boolean flag = (Boolean) remoteUserService.checkUser(userId, sysUserVo.getUsername()).getData();
//        if (flag) {
//            throw new RuntimeException("修改失败，该手机号已存在");
//        }
        //创建用户传输对象
        CxUserDTO userDTO = new CxUserDTO();
        userDTO.setUserId(sysUserVo.getUserId());
        //更新时将新角色赋值给newRoleId
        userDTO.setNewRoleId(sysUserVo.getRoleId());
        //更新时将旧角色赋值给roleId
        userDTO.setRoleId(sysUserVo.getOldRoleId());
        userDTO.setSex(sysUserVo.getSex());
        userDTO.setCredentialNo(sysUserVo.getCredentialNo());
        userDTO.setTrueName(sysUserVo.getTrueName());

//        userDTO.setRoleExpTime(sysUserVo.getExpTime());
        remoteUserService.editUserRole(userDTO);


        ProjectStaff e = projectStaffService.getStaffByUserId(sysUserVo.getUserId(), sysUserVo.getDeptId());

        //由于原需求逻辑并不存在员工信息 故这边做员工的判断的处理 不存在则再插入员工信息
        if (e != null) {
            e.setStaffName(sysUserVo.getTrueName());
            e.setMobile(sysUserVo.getUsername());
            e.setSex(sysUserVo.getSex());
            e.setCredentialNo(sysUserVo.getCredentialNo());
            e.setPicUrl(sysUserVo.getAvatar());
            //只更新当前项目下的员工角色id
            if (e.getProjectId().equals(sysUserVo.getDeptId())) {
                e.setRoleId(sysUserVo.getRoleId());
            }
            projectStaffService.updateById(e);
        } else {
            saveDeptAndStaff(sysUserVo);
        }



/*        projectStaffService.update(Wrappers.lambdaUpdate(ProjectStaff.class)
                .eq(ProjectStaff::getUserId, sysUserVo.getUserId())
                .set(ProjectStaff::getStaffName, sysUserVo.getTrueName())
                .set(ProjectStaff::getCredentialNo, sysUserVo.getCredentialNo())
                .set(ProjectStaff::getSex, sysUserVo.getSex())
                .set(ProjectStaff::getMobile, sysUserVo.getUsername()));*/
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Deprecated
    public void updateDeptUser(SysUserVo sysUserVo) {
        Integer userId = sysUserVo.getUserId();
        //判断编辑后切换的角色是否与其他角色重复
        R<UserInfo> userInfoR = remoteUserService.info(sysUserVo.getUsername(), SecurityConstants.FROM_IN);
        if (userInfoR.getCode() == 0 && userInfoR.getData() != null) {
            UserInfo userInfo = userInfoR.getData();
            Integer[] roleIds = userInfo.getRoles();
            roleIds = ArrayUtils.removeElement(roleIds, sysUserVo.getOldRoleId());
            if (ArrayUtils.contains(roleIds, sysUserVo.getRoleId())) {
                throw new RuntimeException("编辑失败,该用户已有该角色");
            }
        }
        //判断手机号是否已存在
        Boolean flag = (Boolean) remoteUserService.checkUser(userId, sysUserVo.getUsername()).getData();
        if (flag) {
            throw new RuntimeException("修改失败，该手机号已存在");
        }
        //创建用户传输对象
        CxUserDTO userDTO = new CxUserDTO();
        BeanUtils.copyProperties(sysUserVo, userDTO);
        //更新时将新角色赋值给newRoleId
        userDTO.setNewRoleId(sysUserVo.getRoleId());
        //更新时将旧角色赋值给roleId
        userDTO.setRoleId(sysUserVo.getOldRoleId());
        userDTO.setPhone(sysUserVo.getUsername());
//        userDTO.setRoleExpTime(sysUserVo.getExpTime());
        remoteUserService.editUserRole(userDTO);
        List<ProjectStaff> projectStaffs = projectStaffService.list(Wrappers.lambdaQuery(ProjectStaff.class)
                .eq(ProjectStaff::getUserId, sysUserVo.getUserId()));
        projectStaffs.forEach(e -> {
            e.setStaffName(sysUserVo.getTrueName());
            e.setMobile(sysUserVo.getUsername());
            e.setSex(sysUserVo.getSex());
            e.setCredentialNo(sysUserVo.getCredentialNo());
            //只更新当前项目下的员工角色id
            if (e.getProjectId().equals(sysUserVo.getDeptId())) {
                e.setRoleId(sysUserVo.getRoleId());
            }
            projectStaffService.updateById(e);
        });
/*        projectStaffService.update(Wrappers.lambdaUpdate(ProjectStaff.class)
                .eq(ProjectStaff::getUserId, sysUserVo.getUserId())
                .set(ProjectStaff::getStaffName, sysUserVo.getTrueName())
                .set(ProjectStaff::getCredentialNo, sysUserVo.getCredentialNo())
                .set(ProjectStaff::getSex, sysUserVo.getSex())
                .set(ProjectStaff::getMobile, sysUserVo.getUsername()));*/
    }

    @Override
    public R removeDeptUser(SysUserVo sysUserVo) {
        return R.ok(remoteUserService.removeUserRole(sysUserVo.getUserId(), sysUserVo.getRoleId()));
    }

    /**
     * 根据用户id和部门id删除用户角色关系及员工信息的方法
     *
     * @param userId
     * @param deptId
     * @return
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R removeUserAndStaff(Integer userId, Integer deptId) {

        ProjectStaff projectStaff = projectStaffService.getStaffByUserId(userId, deptId);
        if(ObjectUtil.isNull(projectStaff)) {
            return R.ok();
        }
        remoteUserService.removeUserRole(projectStaff.getUserId(), projectStaff.getRoleId());
        projectStaffService.removeById(projectStaff.getStaffId());
        return R.ok();
    }




    /**
     * user to staff
     *
     * @param sysUserVo
     * @return
     */
    private ProjectStaff parseStaff(SysUserVo sysUserVo) {
        ProjectStaff projectStaff = new ProjectStaff();
        projectStaff.setMobile(sysUserVo.getPhone());
        projectStaff.setSex(sysUserVo.getSex());
        projectStaff.setCredentialNo(sysUserVo.getCredentialNo());
        projectStaff.setStaffName(sysUserVo.getTrueName());
        projectStaff.setRoleId(sysUserVo.getRoleId());
        projectStaff.setUserId(sysUserVo.getUserId());
        projectStaff.setPicUrl(sysUserVo.getAvatar());
        return projectStaff;
    }

    /**
     * 保存部门和员工
     *
     * @param sysUserVo
     */
    private void saveDeptAndStaff(SysUserVo sysUserVo) {
        ProjectStaff projectStaff = parseStaff(sysUserVo);
        projectStaff.setGrade(StaffGradeConstant.Project_Manager);
        projectStaff.setCredentialType("111");
        //如果是项目管理员的情况
        if (DeptTypeEnum.PROJECT.getId().equals(sysUserVo.getDeptTypeId())) {
            //获取当前项目下名称为其它部门的一级部门
            SysProjectDept sysProjectDept = sysProjectDeptService.getOne(Wrappers.lambdaQuery(SysProjectDept.class)
                    .eq(SysProjectDept::getProjectId, sysUserVo.getDeptId())
                    .eq(SysProjectDept::getDeptName, SysProjectDeptConstant.OTHER_DEPT_NAME));
            if (BeanUtil.isEmpty(sysProjectDept)) {
                SysProjectDept projectDept = new SysProjectDept();
                projectDept.setDeptName(SysProjectDeptConstant.OTHER_DEPT_NAME);
                projectDept.setDeptDesc(SysProjectDeptConstant.OTHER_DEPT_DESC);
                SysDept sysDept = new SysDept();
                sysDept.setName(projectDept.getDeptName());
                sysDept.setSort(SysDeptConstant.COMPANY_SORT);
                sysDept.setParentId(sysUserVo.getDeptId());
                sysDept.setDeptTypeId(DeptTypeEnum.DEPT.getId());
                sysDept.setDeptTypeName(DeptTypeEnum.DEPT.getName());
                //远端生成的项目内部门id
                Integer remoteDeptId = remoteDeptService.saveRetId(sysDept).getData();
                projectDept.setProjectId(sysUserVo.getDeptId());
                projectDept.setDeptId(remoteDeptId);
                sysProjectDeptService.getBaseMapper().insert(projectDept);
                projectStaff.setDepartmentId(projectDept.getDeptId());
                projectStaff.setUserId(sysUserVo.getUserId());
                projectStaff.setProjectId(sysUserVo.getDeptId());
                int count = projectStaffService.count(Wrappers.lambdaQuery(ProjectStaff.class)
                        .eq(ProjectStaff::getProjectId, projectStaff.getProjectId())
                        .eq(ProjectStaff::getMobile, projectStaff.getMobile()));
                if (count != 0) {
                    throw new RuntimeException("该手机号已存在");
                }
                //将项目管理员添加到项目中的员工列表
                projectStaffService.save(projectStaff);
            } else {
                projectStaff.setDepartmentId(sysProjectDept.getDeptId());
                projectStaff.setUserId(sysUserVo.getUserId());
                projectStaff.setProjectId(sysUserVo.getDeptId());
                int count = projectStaffService.count(Wrappers.lambdaQuery(ProjectStaff.class)
                        .eq(ProjectStaff::getProjectId, projectStaff.getProjectId())
                        .eq(ProjectStaff::getMobile, projectStaff.getMobile()));
                if (count != 0) {
                    throw new RuntimeException("该手机号已存在");
                }
                //将项目管理员添加到项目中的员工列表
                projectStaffService.save(projectStaff);
            }
        } else {
            //如果是集团或项目管理员
            projectStaff.setDepartmentId(sysUserVo.getDeptId());
            projectStaff.setUserId(sysUserVo.getUserId());
            projectStaff.setProjectId(sysUserVo.getDeptId());
            int count = projectStaffService.count(Wrappers.lambdaQuery(ProjectStaff.class)
                    .eq(ProjectStaff::getProjectId, projectStaff.getProjectId())
                    .eq(ProjectStaff::getMobile, projectStaff.getMobile()));
            if (count != 0) {
                throw new RuntimeException("该手机号已存在");
            }
            //将项目管理员添加到项目中的员工列表
            projectStaffService.save(projectStaff);
        }

    }

    /**
     * 保存项目内部门和员工
     *
     * @param sysUserVo
     */
    @Deprecated
    private void saveProjectDeptAndStaff(SysUserVo sysUserVo) {
        ProjectStaff projectStaff = parseStaff(sysUserVo);
        projectStaff.setGrade(StaffGradeConstant.Project_Manager);
        //获取当前项目下名称为其它部门的一级部门
        SysProjectDept sysProjectDept = sysProjectDeptService.getOne(Wrappers.lambdaQuery(SysProjectDept.class)
                .eq(SysProjectDept::getProjectId, sysUserVo.getDeptId())
                .eq(SysProjectDept::getDeptName, SysProjectDeptConstant.OTHER_DEPT_NAME));
        if (BeanUtil.isEmpty(sysProjectDept)) {
            SysProjectDept projectDept = new SysProjectDept();
            projectDept.setDeptName(SysProjectDeptConstant.OTHER_DEPT_NAME);
            projectDept.setDeptDesc(SysProjectDeptConstant.OTHER_DEPT_DESC);
            SysDept sysDept = new SysDept();

            sysDept.setName(projectDept.getDeptName());
            sysDept.setSort(SysDeptConstant.COMPANY_SORT);
            sysDept.setParentId(sysUserVo.getDeptId());
            sysDept.setDeptTypeId(DeptTypeEnum.DEPT.getId());
            sysDept.setDeptTypeName(DeptTypeEnum.DEPT.getName());
            //远端生成的项目内部门id
            Integer remoteDeptId = remoteDeptService.saveRetId(sysDept).getData();
            projectDept.setProjectId(sysUserVo.getDeptId());
            projectDept.setDeptId(remoteDeptId);
            sysProjectDeptService.getBaseMapper().insert(projectDept);

            projectStaff.setDepartmentId(projectDept.getDeptId());
            projectStaff.setUserId(sysUserVo.getUserId());
            projectStaff.setProjectId(sysUserVo.getDeptId());
            int count = projectStaffService.count(Wrappers.lambdaQuery(ProjectStaff.class)
                    .eq(ProjectStaff::getProjectId, projectStaff.getProjectId())
                    .eq(ProjectStaff::getMobile, projectStaff.getMobile()));
            if (count != 0) {
                throw new RuntimeException("该手机号已存在");
            }
            //将项目管理员添加到项目中的员工列表
            projectStaffService.save(projectStaff);
        } else {
            projectStaff.setDepartmentId(sysProjectDept.getDeptId());
            projectStaff.setUserId(sysUserVo.getUserId());
            projectStaff.setProjectId(sysUserVo.getDeptId());
            int count = projectStaffService.count(Wrappers.lambdaQuery(ProjectStaff.class)
                    .eq(ProjectStaff::getProjectId, projectStaff.getProjectId())
                    .eq(ProjectStaff::getMobile, projectStaff.getMobile()));
            if (count != 0) {
                throw new RuntimeException("该手机号已存在");
            }
            //将项目管理员添加到项目中的员工列表
            projectStaffService.save(projectStaff);
        }
    }


}
