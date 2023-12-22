package com.aurine.cloudx.estate.open.staff.controller;

import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.open.staff.bean.SysProjectDeptPage;
import com.aurine.cloudx.estate.open.staff.fegin.RemoteProjectDeptService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Api(value = "sysProjectDept", tags = "项目内部门信息管理")
public class SysProjectDeptController {
    
    private final RemoteProjectDeptService remoteProjectDeptService;

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PreAuthorize("@pms.hasPermission('dept:get:page')")
    public R<Page<SysProjectDept>> getSysProjectDeptPage(SysProjectDeptPage page) {
        return remoteProjectDeptService.getSysProjectDeptPage(page);
    }

    /**
     * 通过id查询项目内部门信息
     *
     * @param id id
     * @return
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "部门id", paramType = "path", required = true)
    })
    @PreAuthorize("@pms.hasPermission('dept:get:getById')")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(remoteProjectDeptService.getById(id));
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PreAuthorize("@pms.hasPermission('dept:post:save')")
    public R save(@RequestBody SysProjectDept sysProjectDept) {
        return remoteProjectDeptService.save(sysProjectDept);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PreAuthorize("@pms.hasPermission('dept:put:updateById')")
    public R updateById(@RequestBody SysProjectDept sysProjectDept) {
        return remoteProjectDeptService.updateById(sysProjectDept);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "部门id", paramType = "path", required = true)
    })
    @PreAuthorize("@pms.hasPermission('dept:delete:removeById')")
    public R removeById(@PathVariable("id") Integer id) {
        return remoteProjectDeptService.removeById(id);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PreAuthorize("@pms.hasPermission('dept:get:list')")
    public R getSysProjectDeptList() {
        return remoteProjectDeptService.getSysProjectDeptList();
    }
    
}
