package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.SysDeptConstant;
import com.aurine.cloudx.estate.dto.OpenApiSysProjectDeptDto;
import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.feign.OpenApiRemoteDeptService;
import com.aurine.cloudx.estate.mapper.SysProjectDeptMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectStaffService;
import com.aurine.cloudx.estate.service.OpenApiSysProjectDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/05/17 16:00
 * @Package: com.aurine.openv2.service.impl
 * @Version: 1.0
 * @Remarks: 部门管理的增删改同时改动pigx的部门表
 **/
@Service
public class OpenApiSysProjectDeptServiceImpl extends ServiceImpl<SysProjectDeptMapper, SysProjectDept> implements OpenApiSysProjectDeptService {

    @Resource
    private OpenApiRemoteDeptService openApiRemoteDeptService;

    @Resource
    private OpenApiProjectStaffService openApiProjectStaffService;

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiSysProjectDeptDto> deptSave(OpenApiSysProjectDeptDto deptDto) {
        String deptName = deptDto.getDeptName();

        // 新增数据到 pigxx.sysDept 表下
        SysDept dept = new SysDept();
        dept.setName(deptName);
        dept.setDeptTypeId(DeptTypeEnum.DEPT.getId());
        dept.setDeptTypeName(DeptTypeEnum.DEPT.getName());
        dept.setSort(SysDeptConstant.COMPANY_SORT);
        dept.setParentId(ProjectContextHolder.getProjectId());

        R<Integer> saveSysDeptResult = openApiRemoteDeptService.innerSaveRetId(dept, "Y");

        if (saveSysDeptResult.getCode() != CommonConstants.SUCCESS) {
            log.error(String.format("pigX - 部门[%s]保存失败", deptDto.getDeptName()));
            throw new OpenApiServiceException(String.format("部门[%s]保存异常", deptDto.getDeptName()));
        }

        // 新增数据到 aurine.sysProjectDept 表下,deptId为 pigxx.sysDept表的deptId
        SysProjectDept sysProjectDept = new SysProjectDept();
        sysProjectDept.setDeptName(deptName);
        sysProjectDept.setDeptDesc(deptName);
        sysProjectDept.setDeptId(saveSysDeptResult.getData());
        sysProjectDept.setProjectId(ProjectContextHolder.getProjectId());

        boolean saveSysProjectDeptResult = this.save(sysProjectDept);

        if (!saveSysProjectDeptResult) {
            log.error(String.format("openV2 - 部门[%s]保存失败", deptDto.getDeptName()));
            throw new OpenApiServiceException(String.format("部门[%s]保存失败", deptDto.getDeptName()));
        }

        OpenApiSysProjectDeptDto respSysProjectDeptDto = new OpenApiSysProjectDeptDto();
        BeanUtil.copyProperties(sysProjectDept, respSysProjectDeptDto);

        return R.ok(respSysProjectDeptDto);
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiSysProjectDeptDto> deptUpdate(OpenApiSysProjectDeptDto deptDto) {
        String deptName = deptDto.getDeptName();

        // 更新数据到 aurine.sysProjectDept 表下
        SysProjectDept sysProjectDept = this.getById(deptDto.getDeptId());
        sysProjectDept.setDeptName(deptName);

        boolean updateResult = this.updateById(sysProjectDept);

        if (!updateResult) {
            log.error(String.format("openV2 - 部门[%s]修改失败", deptDto.getDeptName()));
            throw new OpenApiServiceException(String.format("部门[%s]修改失败", deptDto.getDeptName()));
        }

        // 更新数据到 pigxx.sysDept 表下
        SysDept dept = new SysDept();

        dept.setDeptId(deptDto.getDeptId());
        dept.setName(deptName);
        dept.setSort(SysDeptConstant.COMPANY_SORT);

        R<Boolean> r = openApiRemoteDeptService.innerUpdate(dept, "Y");

        if (r.getCode() != CommonConstants.SUCCESS) {
            log.error(String.format("pigX - 部门[%s]修改失败", deptDto.getDeptName()));
            throw new OpenApiServiceException(String.format("部门[%s]修改失败", deptDto.getDeptName()));
        }

        OpenApiSysProjectDeptDto openApiSysProjectDeptDto = new OpenApiSysProjectDeptDto();
        BeanUtil.copyProperties(sysProjectDept, openApiSysProjectDeptDto);

        return R.ok(openApiSysProjectDeptDto);
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<Integer> deptRemove(OpenApiSysProjectDeptDto deptDto) {
        Integer deptId = deptDto.getDeptId();

        // 根据部门ID查询员工信息表是否有人员已绑定在部门下
        Integer staffCount = openApiProjectStaffService.getStaffCountByDeptId(deptId);

        if (staffCount > 0) {
            throw new OpenApiServiceException(String.format("部门Id为[%s]的部门下已有员工，请解绑后在进行操作", deptId));
        }

        // 删除 aurine.sysProjectDept 部门
        boolean removeSysProjectDeptResult = this.removeById(deptId);

        if (!removeSysProjectDeptResult) {
            throw new OpenApiServiceException(String.format("部门Id为[%s]的部门删除失败", deptId));
        }

        // 删除 pigxx sys_dept 部门
        R<Boolean> removeSysDeptResult = openApiRemoteDeptService.innerRemoveById(deptId, "Y");

        if (removeSysDeptResult.getCode() != CommonConstants.SUCCESS) {
            log.error(String.format("pigx Id为[%s]的部门删除失败", deptId));
            throw new OpenApiServiceException(String.format("Id为[%s]的部门删除失败", deptId));
        }

        return R.ok(deptId);
    }

}
