package com.aurine.cloudx.estate.open.device.controller;


import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.open.device.bean.ProjectProprietorDeviceSearchConditionPage;
import com.aurine.cloudx.estate.open.device.fegin.RemoteProjectProprietorDeviceService;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
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


/**
 * <p>住户-设备 权限管理</p>
 *
 * @ClassName: ProjectProprietorDeviceController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:49
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/service-proprietor-device")
@Api(value = "service-proprietor-device", tags = "人员设备权限管理")
public class ProjectProprietorDeviceController {
    @Resource
    private RemoteProjectProprietorDeviceService projectProprietorDeviceProxyService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询住户授权状态", notes = "分页查询住户授权状态")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('service-proprietor-device:get:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<Page<ProjectProprietorDeviceRecordVo>> getProjectPersonDevicePage(ProjectProprietorDeviceSearchConditionPage page) {
        return projectProprietorDeviceProxyService.getProjectPersonDevicePage(page);
    }


    /**
     * 通过住户ID查询通行权限信息
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过住户ID查询通行权限信息", notes = "通过住户ID查询通行权限信息")
    @GetMapping("/{personId}")
    @PreAuthorize("@pms.hasPermission('service-proprietor-device:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "path"),
    })
    public R<ProjectProprietorDeviceVo> getById(@PathVariable("personId") String personId) {
        return projectProprietorDeviceProxyService.getById(personId);
    }

    /**
     * 通过id查询人员方案下可以使用的设备
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/listDevice/{personId}/{planId}")
    @PreAuthorize("@pms.hasPermission('service-proprietor-device:get:list-device')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "path"),
    })
    public R<ProjectPassDeviceVo> getDeviceById(@PathVariable("personId") String personId, @PathVariable("planId") String planId) {
        return projectProprietorDeviceProxyService.getDeviceById(personId, planId);
    }

    /**
     * 新增人员设备权限
     *
     * @param projectProprietorDeviceVo 人员设备权限
     * @return R
     */
    @ApiOperation(value = "新增人员设备权限", notes = "新增人员设备权限")
    @SysLog("新增人员设备权限")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('service-proprietor-device:post:save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<Boolean> save(@Valid @RequestBody ProjectProprietorDeviceVo projectProprietorDeviceVo) {
        return projectProprietorDeviceProxyService.save(projectProprietorDeviceVo);
    }

    /**
     * 修改人员设备权限
     *
     * @param projectProprietorDeviceVo 人员设备权限
     * @return R
     */
    @ApiOperation(value = "修改人员设备权限", notes = "修改人员设备权限")
    @SysLog("修改人员设备权限")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('service-proprietor-device:put:update')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<Boolean> updateById(@Valid @RequestBody ProjectProprietorDeviceVo projectProprietorDeviceVo) {

        return projectProprietorDeviceProxyService.updateById(projectProprietorDeviceVo);
    }

    /**
     * 启用
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/enable/{personId}")
    @PreAuthorize("@pms.hasPermission('service-proprietor-device:get:enable')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "path"),
    })
    public R<Boolean> enablePassRight(@PathVariable("personId") String personId) {
        return projectProprietorDeviceProxyService.enablePassRight(personId);
    }

    /**
     * 禁用
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/disable/{personId}")
    @PreAuthorize("@pms.hasPermission('service-proprietor-device:get:disable')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "path"),
    })
    public R<Boolean> disablePassRight(@PathVariable("personId") String personId) {
        return projectProprietorDeviceProxyService.disablePassRight(personId);
    }

}
