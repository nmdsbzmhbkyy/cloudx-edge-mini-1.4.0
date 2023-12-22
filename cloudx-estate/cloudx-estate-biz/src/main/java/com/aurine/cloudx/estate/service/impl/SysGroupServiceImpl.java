
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.common.core.util.web.ContextHolderUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.SysDeptConstant;
import com.aurine.cloudx.estate.entity.SysGroup;
import com.aurine.cloudx.estate.mapper.SysGroupMapper;
import com.aurine.cloudx.estate.service.SysDeptUserService;
import com.aurine.cloudx.estate.service.SysGroupService;
import com.aurine.cloudx.estate.vo.ProjectGroupTreeVo;
import com.aurine.cloudx.estate.vo.SysGroupFormVo;
import com.aurine.cloudx.estate.vo.SysUserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.vo.TreeUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.enums.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目组
 *
 * @author xull@aurine.cn
 * @date 2020-04-30 16:04:44
 */
@Service
@AllArgsConstructor
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {

    private final RemoteDeptService remoteDeptService;
    private final RemoteRoleService remoteRoleService;
    private final SysDeptUserService sysDeptUserService;


    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer saveReturnId(SysGroupFormVo sysGroupFormVo) {
        SysGroup entity = new SysGroup();
        BeanUtils.copyProperties(sysGroupFormVo, entity);
        SysDept sysDept = new SysDept();
        sysDept.setName(entity.getProjectGroupName());
        sysDept.setParentId(entity.getParentId());
        sysDept.setDeptTypeId(DeptTypeEnum.GROUP.getId());
        sysDept.setDeptTypeName(DeptTypeEnum.GROUP.getName());
        sysDept.setSort(SysDeptConstant.COMPANY_SORT);
        SysUserVo sysUserVo = sysGroupFormVo.getUser();
        Integer deptId = null;
        Integer roleId = null;
        //新增pigx部门对应项目组
        deptId = remoteDeptService.saveRetId(sysDept).getData();
        //设置项目组id与pigx部门同步
        entity.setProjectGroupId(deptId);
        //设置操作人
        entity.setOperator(SecurityUtils.getUser().getId());
        //新增项目组用户角色
        SysRole sysRole = new SysRole();
        sysRole.setDeptId(deptId);
        sysRole.setDsType(DataScopeTypeEnum.OWN_CHILD_LEVEL.getType());
//        sysRole.setRoleName(entity.getProjectGroupName() + SysDeptConstant.GROUP_NAME);
        sysRole.setRoleName( SysDeptConstant.GROUP_NAME);
        sysRole.setRoleCode(SysDeptConstant.GROUP_CODE + RandomUtil.randomStringUpper(18).replaceAll("\\d+", ""));
        roleId = remoteRoleService.saveRetId(sysRole).getData();
        //根据当前角色更新权限菜单
        List<String> roleIds = SecurityUtils.getRoles().stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
        List<SysRole> roles=remoteRoleService.getRoleList(roleIds).getData();
        Integer myRoleId=null;
        for (SysRole role:roles  ) {
            if (ProjectContextHolder.getProjectId().equals(role.getDeptId())) {
                myRoleId = role.getRoleId();
            }
        }
        if (ObjectUtil.isNotEmpty(myRoleId)) {
            remoteRoleService.updateRoleMenuBySysRoleId(myRoleId, roleId,DeptTypeEnum.PROJECT.getId());
        }else {
            //更新角色菜单权限
            remoteRoleService.updateRoleMenuByRoleId(DeptTypeEnum.PROJECT.getId(), roleId);
        }

        sysUserVo.setRoleId(roleId);
        sysUserVo.setDeptId(deptId);
        //添加用户信息
        sysDeptUserService.addUserAndRole(sysUserVo);
        //新增项目组
        baseMapper.insert(entity);
        return entity.getProjectGroupId();

    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateGroupAndUser(SysGroupFormVo sysGroupFormVo) {
        SysGroup entity = new SysGroup();
        BeanUtils.copyProperties(sysGroupFormVo, entity);
        R<SysDept> resultData = remoteDeptService.getById(entity.getProjectGroupId());
        SysDept sysDept = resultData.getData();
        sysDept.setName(entity.getProjectGroupName());
        //批量更新项目组用户信息
        sysGroupFormVo.getUserList().forEach(sysDeptUserService::updateUserAndRole);
        //更新pigx部门
        remoteDeptService.update(sysDept);
        return super.updateById(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @TxTransaction(isStart = true)
    public boolean removeById(Serializable id) {
        R<List<SysDeptRelation>> descendantList = remoteDeptService.getDescendantList((Integer) id);

        //返回值包含当前部门,故数值应大于1
        if (descendantList.getData() != null && descendantList.getData().size() > 1) {
            throw new RuntimeException("该项目组底下存在项目组或项目,无法删除");
        }
        remoteRoleService.removeByDeptId((Integer) id);
        remoteDeptService.removeById((Integer) id);

        return super.removeById(id);
    }

    @Override
    public Page<SysGroup> pageGroup(Page page, SysGroup sysGroup) {
        return baseMapper.pageGroup(page, sysGroup);
    }

    @Override
    public List<Integer> findAllById(Integer id) {
        return baseMapper.findAllById(id);
    }

    @Override
    public List<ProjectGroupTreeVo> selectTree(Integer id) {
        return getDeptTree(baseMapper.selectTree(id));
    }

    @Override
    public List<Integer> findByCompanyId(Integer id) {
        return baseMapper.findByCompanyId(id);
    }

    @Override
    public List<SysGroup> findById(Integer id) {
        return baseMapper.findById(id);
    }


    /**
     * 构建结构树
     *
     * @param depts 部门列表
     * @return
     */
    private List<ProjectGroupTreeVo> getDeptTree(List<SysDept> depts) {
        List<ProjectGroupTreeVo> treeList = depts.stream()
                .filter(dept -> !dept.getDeptId().equals(dept.getParentId()))
                .sorted(Comparator.comparingInt(SysDept::getSort))
                .map(dept -> {
                    ProjectGroupTreeVo node = new ProjectGroupTreeVo();
                    node.setId(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName());
                    node.setDeptTypeId(dept.getDeptTypeId());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, SysDeptConstant.AURINE_ID);
    }

}
