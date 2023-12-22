package com.aurine.cloudx.estate.open.device.controller;

import com.aurine.cloudx.estate.open.device.bean.ProjectStaffDeviceSearchConditionVoPage;
import com.aurine.cloudx.estate.open.device.fegin.RemoteProjectStaffDeviceService;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceRecordVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/service-staff-device")
@Api(value = "serviceStaffDevice", tags = "员工设备权限管理")
public class ProjectStaffDeviceController {
    @Resource
    private RemoteProjectStaffDeviceService projectStaffDeviceProxyService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('service-staff-device:get:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<Page<ProjectStaffDeviceRecordVo>> getProjectStaffDevicePage(ProjectStaffDeviceSearchConditionVoPage page) {
        return projectStaffDeviceProxyService.getProjectStaffDevicePage(page);
    }


    /**
     * 通过id查询人员设备权限
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{personId}")
    @PreAuthorize("@pms.hasPermission('service-staff-device:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "员工ID", required = true, paramType = "path"),
    })
    public R<ProjectStaffDeviceVo> getById(@PathVariable("personId") String personId) {
        return projectStaffDeviceProxyService.getById(personId);
    }

    /**
     * 通过id查询人员方案下可以使用的设备
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/listDevice/{personId}/{planId}")
    @PreAuthorize("@pms.hasPermission('service-staff-device:get:list-device')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "员工ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "planId", value = "计划ID", required = true, paramType = "path"),
    })
    public R<List<ProjectPassDeviceVo>> getDeviceById(@PathVariable("personId") String personId, @PathVariable("planId") String planId) {
        return projectStaffDeviceProxyService.getDeviceById(personId, planId);
    }

    /**
     * 新增人员设备权限
     *
     * @param projectStaffDevice 人员设备权限
     * @return R
     */
    @ApiOperation(value = "新增人员设备权限", notes = "新增人员设备权限")
    @SysLog("新增人员设备权限")
    @PreAuthorize("@pms.hasPermission('service-staff-device:post:save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PostMapping
    public R<Boolean> save(@Valid @RequestBody ProjectStaffDeviceVo projectStaffDevice) {

        return projectStaffDeviceProxyService.save(projectStaffDevice);
    }

    /**
     * 启用
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/enable/{personId}")
    @PreAuthorize("@pms.hasPermission('service-staff-device:get:enable')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "员工ID", required = true, paramType = "path"),
    })
    public R<Boolean> enablePassRight(@PathVariable("personId") String personId) {
        return projectStaffDeviceProxyService.enablePassRight(personId);
    }

    /**
     * 禁用
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/disable/{personId}")
    @PreAuthorize("@pms.hasPermission('service-staff-device:get:disable')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "员工ID", required = true, paramType = "path"),
    })
    public R<Boolean> disablePassRight(@PathVariable("personId") String personId) {
        return projectStaffDeviceProxyService.disablePassRight(personId);
    }


}
