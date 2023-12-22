package com.aurine.cloudx.estate.open.staff.controller;

import com.aurine.cloudx.common.log.annotation.ProjSysLog;
import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.open.staff.bean.ProjectStaffPage;
import com.aurine.cloudx.estate.open.staff.fegin.RemoteProjectStaffService;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
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
@RequestMapping("/staff")
@Api(value = "staff", tags = "项目员工信息表管理")
public class ProjectStaffController {
    
    private final RemoteProjectStaffService remoteProjectStaffService;
    
    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ProjSysLog("员工管理 - 分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PreAuthorize("@pms.hasPermission('staff:get:page')")
    public R<Page<ProjectStaffVo>> getProjectStaffPage(ProjectStaffPage page) {
        return remoteProjectStaffService.getProjectStaffPage(page);
    }
    
    /**
     * 通过id查询项目员工信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "员工id", paramType = "path", required = true)
    })
    @PreAuthorize("@pms.hasPermission('staff:get:getById')")
    public R getById(@PathVariable("id") String id) {
        return remoteProjectStaffService.getById(id);
    }
    
    /**
     * 获取当前员工的信息
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询当前员工的信息", notes = "通过id查询当前员工的信息")
    @GetMapping("/get/staff")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PreAuthorize("@pms.hasPermission('staff:get:getStaff')")
    public R getStaff() {
        return remoteProjectStaffService.getStaff();
    }
    
    /**
     * 新增项目员工信息表
     *
     * @param projectStaff 项目员工信息表
     * @return R
     */
    @ApiOperation(value = "新增项目员工信息表", notes = "新增项目员工信息表")
    @SysLog("新增项目员工信息表")
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PreAuthorize("@pms.hasPermission('staff:post:save')")
    public R save(@RequestBody ProjectStaffDTO projectStaff) {
        return remoteProjectStaffService.save(projectStaff);
    }
    
    /**
     * 修改项目员工信息表
     *
     * @param projectStaff 项目员工信息表
     * @return R
     */
    @ApiOperation(value = "修改项目员工信息表", notes = "修改项目员工信息表")
    @SysLog("修改项目员工信息表")
    @PutMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PreAuthorize("@pms.hasPermission('staff:put:updateById')")
    public R updateById(@RequestBody ProjectStaffDTO projectStaff) {
        return remoteProjectStaffService.updateById(projectStaff);
    }
    
    /**
     * 通过id删除项目员工信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目员工信息表", notes = "通过id删除项目员工信息表")
    @ApiImplicitParam(name = "id", value = "员工id", paramType = "path", required = true)
    @SysLog("通过id删除项目员工信息表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('staff:delete:removeById')")
    public R removeById(@PathVariable("id") String id) {
        return remoteProjectStaffService.removeById(id);
    }
    
    /**
     * 通过userId删除员工
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "通过员工Id和项目id删除项目员工信息表", notes = "通过userId删除项目员工信息表")
    @SysLog("通过userId删除项目员工信息表")
    @DeleteMapping("/remove/staff/{userId}/{projectId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "userId", value = "员工id", paramType = "path", required = true),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "path", required = true),
    })
    public R removeByUserId(@PathVariable("userId") Integer userId, @PathVariable("projectId") Integer projectId) {
        return remoteProjectStaffService.removeByUserId(userId, projectId);
    }
    
    /**
     * 通过手机号获取员工
     *
     * @param mobile
     * @return
     */
    @ApiOperation(value = "通过手机号获取员工信息", notes = "通过手机号获取员工信息")
    @GetMapping("/get/staff/on/mobile/{mobile}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "path", required = true)
    })
    public R getStaffOnMobile(@PathVariable("mobile") String mobile) {
        return R.ok(remoteProjectStaffService.getProjectStaffPage(mobile));
    }
    
}
