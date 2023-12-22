package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DefaultDeptEnum;
import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.mapper.SysProjectDeptMapper;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.SysProjectDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 项目内部门信息
 *
 * @author lingang
 * @date 2020-05-07 18:44:46
 */
@Service
@AllArgsConstructor
public class SysProjectDeptServiceImpl extends ServiceImpl<SysProjectDeptMapper, SysProjectDept> implements SysProjectDeptService {



    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    private final RemoteDeptService deptRemote;

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysProjectDept entity) {
        /**
         * 校验是否存在同名部门
         * @author: 王伟
         * @since 20200825
         *
         */
        if (this.checkDeptNameExist(entity.getDeptName(), ProjectContextHolder.getProjectId(), null)) {
            throw new RuntimeException("部门名称不允许重复");
        }

        SysDept dept = new SysDept();
        dept.setName(entity.getDeptName());
        dept.setDeptTypeId(DeptTypeEnum.DEPT.getId());
        dept.setDeptTypeName(DeptTypeEnum.DEPT.getName());
        dept.setSort(1);
        dept.setParentId(ProjectContextHolder.getProjectId());

        R<Integer> r = deptRemote.saveRetId(dept);

        if (r.getCode() == CommonConstants.SUCCESS) {
            entity.setProjectId(ProjectContextHolder.getProjectId());
            entity.setDeptId(r.getData());
            return super.save(entity);
        } else {
            return false;
        }
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysProjectDept entity) {
        /**
         * 校验是否存在同名部门
         * @author: 王伟
         * @since 20200825
         *
         */
        if (this.checkDeptNameExist(entity.getDeptName(), ProjectContextHolder.getProjectId(), entity.getDeptId())) {
            throw new RuntimeException("部门名称不允许重复");
        }

        SysDept dept = new SysDept();
        dept.setDeptId(entity.getDeptId());
        dept.setName(entity.getDeptName());
        dept.setSort(1);

        R<Boolean> r = deptRemote.update(dept);

        if (r.getCode() == CommonConstants.SUCCESS) {
            entity.setProjectId(ProjectContextHolder.getProjectId());
            return super.updateById(entity);
        } else {
            return false;
        }
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {


        R<Boolean> r = deptRemote.removeById((Integer) id);

        if (r.getCode() == CommonConstants.SUCCESS) {
            return super.removeById(id);
        } else {
            return false;
        }
    }

    /**
     * 初始化部门信息
     *
     * @return
     * @author: 黄阳光
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean initDept(Integer projectId, Integer tenantId) {
        /*
        判断默认部门是否存在
         */
        List<SysProjectDept> defaultDept = baseMapper.defaultDept(
                projectId, DefaultDeptEnum.OFFICE.code, DefaultDeptEnum.FINANCE.code, DefaultDeptEnum.COMPREHENSIVE.code,
                DefaultDeptEnum.SECURITY_STAFF.code, DefaultDeptEnum.REPAIR.code, DefaultDeptEnum.ENVIRONMENT.code
        );
        if (CollUtil.isNotEmpty(defaultDept)) {
            return false;
        }
        /*
        生成默认部门
         */
        List<SysProjectDept> result = baseMapper.selectByTemplate();
        for (SysProjectDept po : result) {
            SysDept dept = new SysDept();
            dept.setName(po.getDeptName());
            dept.setDeptTypeId(DeptTypeEnum.DEPT.getId());
            dept.setDeptTypeName(DeptTypeEnum.DEPT.getName());
            dept.setSort(1);

            dept.setParentId(projectId);
//            dept.setParentId(ProjectContextHolder.getProjectId());

            R<Integer> r = deptRemote.saveRetId(dept);

            if (r.getCode() == CommonConstants.SUCCESS) {
                po.setProjectId(projectId);
                po.setDeptId(r.getData());
                super.save(po);
            }
        }
        return true;
    }

    /**
     * 校验部门名称是否已存在
     *
     * @param deptName
     * @param deptId
     * @param projectId
     * @return
     */
    private boolean checkDeptNameExist(String deptName, Integer projectId, Integer deptId) {
        int count = this.count(new QueryWrapper<SysProjectDept>().lambda()
                .eq(SysProjectDept::getDeptName, deptName)
                .eq(SysProjectDept::getProjectId, projectId)
                .notLike(deptId != null, SysProjectDept::getDeptId, deptId)
        );
        return count >= 1;
    }
}
