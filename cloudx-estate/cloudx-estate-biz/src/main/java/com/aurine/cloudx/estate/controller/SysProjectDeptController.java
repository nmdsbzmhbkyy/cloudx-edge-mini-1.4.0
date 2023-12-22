package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.vo.SysProjectDeptVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.service.SysProjectDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import java.util.List;


/**
 * 项目内部门信息
 *
 * @author lingang
 * @date 2020-05-07 18:44:46
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysProjectDept")
@Api(value = "sysProjectDept", tags = "项目内部门信息管理")
public class SysProjectDeptController {

    private final SysProjectDeptService sysProjectDeptService;
    private final ProjectStaffService projectStaffService;

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param  项目内部门信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getSysProjectDeptPage(Page page, SysProjectDeptVo sysProjectDeptVo) {
        sysProjectDeptVo.setProjectId(ProjectContextHolder.getProjectId());
        sysProjectDeptVo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(sysProjectDeptService.page(page,sysProjectDeptVo));
    }

    /**
     * 通过id查询项目内部门信息
     *
     * @param id id
     * @return
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(sysProjectDeptService.getById(id));
    }

    /**
     * 新增项目内部门信息
     *
     * @param sysProjectDept 项目内部门信息
     * @return R
     */
    @ApiOperation(value = "新增项目内部门信息", notes = "新增项目内部门信息")
    @SysLog("新增项目内部门信息")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hr_group_dept_add')")
    public R save(@RequestBody SysProjectDept sysProjectDept) {
        return R.ok(sysProjectDeptService.save(sysProjectDept));
    }

    /**
     * 修改项目内部门信息
     *
     * @param sysProjectDept 项目内部门信息
     * @return R
     */
    @ApiOperation(value = "修改项目内部门信息", notes = "修改项目内部门信息")
    @SysLog("修改项目内部门信息")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hr_group_dept_edit')")
    public R updateById(@RequestBody SysProjectDept sysProjectDept) {
        return R.ok(sysProjectDeptService.updateById(sysProjectDept));
    }

    /**
     * 通过id删除项目内部门信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目内部门信息", notes = "通过id删除项目内部门信息")
    @SysLog("通过id删除项目内部门信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hr_group_dept_del')")
    public R removeById(@PathVariable Integer id) {

        List<ProjectStaff> projectStaffList = projectStaffService.list(Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getDepartmentId, id));
        if (ObjectUtil.isNotEmpty(projectStaffList) && projectStaffList.size() > 0) {
            return R.failed("删除失败，该部门下存在人员");
        }
        return R.ok(sysProjectDeptService.removeById(id));
    }

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    public R getSysProjectDeptList() {
        return R.ok(sysProjectDeptService.list(Wrappers.lambdaQuery(SysProjectDept.class)
                .eq(SysProjectDept::getProjectId, ProjectContextHolder.getProjectId())));
    }
}
