
package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttrConf;
import com.aurine.cloudx.estate.service.ProjectDeviceAttrConfService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 设备拓展属性配置表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:19:06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDeviceAttrConf" )
@Api(value = "ProjectDeviceAttrConf", tags = "设备拓展属性配置表管理")
public class ProjectDeviceAttrConfController {

    private final  ProjectDeviceAttrConfService projectDeviceAttrConfService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectDeviceAttrConf 设备拓展属性配置表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectDeviceAttrConf>> getProjectDeviceAttrConfPage(Page page, ProjectDeviceAttrConf projectDeviceAttrConf) {
        return R.ok(projectDeviceAttrConfService.page(page, Wrappers.query(projectDeviceAttrConf)));
    }


    /**
     * 通过id查询设备拓展属性配置表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备属性拓展id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectDeviceAttrConf> getById(@PathVariable("id" ) String id) {
        return R.ok(projectDeviceAttrConfService.getById(id));
    }

    /**
     * 新增设备拓展属性配置表(私有属性)
     * @param projectDeviceAttrConf 设备拓展属性配置表
     * @return R
     */
    @ApiOperation(value = "新增设备拓展属性配置表", notes = "新增设备拓展属性配置表")
    @SysLog("新增设备拓展属性配置表" )
    @PostMapping("/savePrivate")
    @PreAuthorize("@pms.hasPermission('estate_deviceattrconf_add')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R savePrivate(@RequestBody ProjectDeviceAttrConf projectDeviceAttrConf) {
        projectDeviceAttrConf.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectDeviceAttrConfService.save(projectDeviceAttrConf));
    }

    /**
     * 修改设备拓展属性配置表(私有属性)
     * @param projectDeviceAttrConf 设备拓展属性配置表
     * @return R
     */
    @ApiOperation(value = "修改设备拓展属性配置表", notes = "修改设备拓展属性配置表")
    @SysLog("修改设备拓展属性配置表" )
    @PutMapping("/updatePrivate")
    @PreAuthorize("@pms.hasPermission('estate_deviceattrconf_edit')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updatePrivate(@RequestBody ProjectDeviceAttrConf projectDeviceAttrConf) {
        projectDeviceAttrConf.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectDeviceAttrConfService.updateById(projectDeviceAttrConf));
    }
    /**
     * 新增设备拓展属性配置表(公有属性)
     * @param projectDeviceAttrConf 设备拓展属性配置表
     * @return R
     */
    @ApiOperation(value = "新增设备拓展属性配置表", notes = "新增设备拓展属性配置表")
    @SysLog("新增设备拓展属性配置表" )
    @PostMapping("/savePublic")
    @PreAuthorize("@pms.hasPermission('estate_deviceattrconf_add')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R savePublic(@RequestBody ProjectDeviceAttrConf projectDeviceAttrConf) {
        //公共属性项目id为null
        projectDeviceAttrConf.setProjectId(null);
        return R.ok(projectDeviceAttrConfService.save(projectDeviceAttrConf));
    }

    /**
     * 修改设备拓展属性配置表(公有属性)
     * @param projectDeviceAttrConf 设备拓展属性配置表
     * @return R
     */
    @ApiOperation(value = "修改设备拓展属性配置表", notes = "修改设备拓展属性配置表")
    @SysLog("修改设备拓展属性配置表" )
    @PutMapping("/updatePublic")
    @PreAuthorize("@pms.hasPermission('estate_deviceattrconf_edit')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updatePublic(@RequestBody ProjectDeviceAttrConf projectDeviceAttrConf) {
        //公共属性项目id为null
        projectDeviceAttrConf.setProjectId(null);
        return R.ok(projectDeviceAttrConfService.updateById(projectDeviceAttrConf));
    }

    /**
     * 通过id删除设备拓展属性配置表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备拓展属性配置表", notes = "通过id删除设备拓展属性配置表")
    @SysLog("通过id删除设备拓展属性配置表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('estate_deviceattrconf_del')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备属性拓展id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        return R.ok(projectDeviceAttrConfService.removeById(id));
    }




}
