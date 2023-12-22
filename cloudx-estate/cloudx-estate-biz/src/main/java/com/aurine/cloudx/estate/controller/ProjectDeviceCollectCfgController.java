

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceCollectCfg;
import com.aurine.cloudx.estate.service.ProjectDeviceCollectCfgService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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


/**
 * 项目采集设备参数配置
 *
 * @author xull@aurine.cn
 * @date 2020-06-12 11:43:55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDeviceCollectCfg")
@Api(value = "projectDeviceCollectCfg", tags = "项目采集设备参数配置管理")
public class ProjectDeviceCollectCfgController {

    private final ProjectDeviceCollectCfgService projectDeviceCollectCfgService;

    /**
     * 分页查询
     *
     * @param page
     *         分页对象
     * @param projectDeviceCollectCfg
     *         项目采集设备参数配置
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getProjectDeviceCollectCfgPage(Page page, ProjectDeviceCollectCfg projectDeviceCollectCfg) {
        return R.ok(projectDeviceCollectCfgService.page(page, Wrappers.query(projectDeviceCollectCfg)));
    }


    /**
     * 通过id查询项目采集设备参数配置
     *
     * @param id
     *         id
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目采集设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectDeviceCollectCfgService.getById(id));
    }

    /**
     * 新增项目采集设备参数配置(公共属性)
     *
     * @param projectDeviceCollectCfg
     *         项目采集设备参数配置(公共属性)
     *
     * @return R
     */
    @ApiOperation(value = "新增项目采集设备参数配置(公共属性)", notes = "新增项目采集设备参数配置(公共属性)")
    @SysLog("新增项目采集设备参数配置(公共属性)")
    @PostMapping("/savePublic")
    @PreAuthorize("@pms.hasPermission('estate_devicecollectcfg_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R savePublic(@RequestBody ProjectDeviceCollectCfg projectDeviceCollectCfg) {
        //公共属性项目id为null
        projectDeviceCollectCfg.setProjectId(null);
        return R.ok(projectDeviceCollectCfgService.save(projectDeviceCollectCfg));
    }

    /**
     * 新增项目采集设备参数配置(私有属性)
     *
     * @param projectDeviceCollectCfg
     *         项目采集设备参数配置
     *
     * @return R
     */
    @ApiOperation(value = "新增项目采集设备参数配置(私有属性)", notes = "新增项目采集设备参数配置(私有属性)")
    @SysLog("新增项目采集设备参数配置(私有属性)")
    @PostMapping("/savePrivate")
    @PreAuthorize("@pms.hasPermission('estate_devicecollectcfg_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R savePrivate(@RequestBody ProjectDeviceCollectCfg projectDeviceCollectCfg) {
        //私有属性项目id为当前编辑项目
        projectDeviceCollectCfg.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectDeviceCollectCfgService.save(projectDeviceCollectCfg));
    }

    /**
     * 修改项目采集设备参数配置(公共属性)
     *
     * @param projectDeviceCollectCfg
     *         项目采集设备参数配置(公共属性)
     *
     * @return R
     */
    @ApiOperation(value = "修改项目采集设备参数配置(公共属性)", notes = "修改项目采集设备参数配置(公共属性)")
    @SysLog("修改项目采集设备参数配置(公共属性)")
    @PutMapping("/updatePublic")
    @PreAuthorize("@pms.hasPermission('estate_devicecollectcfg_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updatePublic(@RequestBody ProjectDeviceCollectCfg projectDeviceCollectCfg) {
        projectDeviceCollectCfg.setProjectId(null);
        return R.ok(projectDeviceCollectCfgService.updateById(projectDeviceCollectCfg));
    }

    /**
     * 修改项目采集设备参数配置(私有属性)
     *
     * @param projectDeviceCollectCfg
     *         项目采集设备参数配置(私有属性)
     *
     * @return R
     */
    @ApiOperation(value = "修改项目采集设备参数配置(私有属性)", notes = "修改项目采集设备参数配置(私有属性)")
    @SysLog("修改项目采集设备参数配置(私有属性)")
    @PutMapping("/updatePrivate")
    @PreAuthorize("@pms.hasPermission('estate_devicecollectcfg_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateById(@RequestBody ProjectDeviceCollectCfg projectDeviceCollectCfg) {
        projectDeviceCollectCfg.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectDeviceCollectCfgService.updateById(projectDeviceCollectCfg));
    }

    /**
     * 通过id删除项目采集设备参数配置
     *
     * @param id
     *         id
     *
     * @return R
     */
    @ApiOperation(value = "通过id删除项目采集设备参数配置", notes = "通过id删除项目采集设备参数配置")
    @SysLog("通过id删除项目采集设备参数配置")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_devicecollectcfg_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目采集设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        return R.ok(projectDeviceCollectCfgService.removeById(id));
    }


}
