package com.aurine.cloudx.estate.open.house.controller;

import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.open.house.fegin.RemoteSysServiceCfgService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 系统增值服务配置
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:16:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/service")
@Api(value = "service", tags = "系统增值服务配置管理")
public class SysServiceCfgController {

    private RemoteSysServiceCfgService remoteSysServiceCfgService;

    /**
     * 查询所有增值服务
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务", notes = "查询所有增值服务")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('service:get:page')")
    public R page(Page page) {
        return remoteSysServiceCfgService.page(page);
    }

    /**
     * 查询所有增值服务与厂商列表
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务与厂商列表", notes = "查询所有增值服务与厂商列表")
    @GetMapping("/select")
    @PreAuthorize("@pms.hasPermission('service:get:select')")
    public R selectValueadd() {
        return remoteSysServiceCfgService.selectValueadd();
    }

    /**
     * 查询所有增值服务无分页
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务无分页", notes = "查询所有增值服务无分页")
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('service:get:list')")
    public R listService() {
        return remoteSysServiceCfgService.listService();
    }

    /**
     * 查询当前项目所有增值服务,用于房屋增值服务
     *
     * @return R
     */
    @ApiOperation(value = "查询当前项目所有增值服务", notes = "查询当前项目所有增值服务")
    @GetMapping("/house-list")
    @PreAuthorize("@pms.hasPermission('service:get:house-list')")
    public R getSysServiceHouseList() {
        return remoteSysServiceCfgService.getSysServiceHouseList();
    }

    /**
     * 根据项目id查询增值服务
     *
     * @return R
     */
    @ApiOperation(value = "根据项目id查询增值服务", notes = "根据项目id查询增值服务")
    @GetMapping("/service/project/{projectId}")
    @PreAuthorize("@pms.hasPermission('service:get:getByProjectId')")
    public R getByProjectId(@PathVariable("projectId") Integer projectId) {
        return remoteSysServiceCfgService.getByProjectId(projectId);
    }

    @ApiOperation(value = "根据房屋id查询增值服务", notes = "根据房屋id查询增值服务")
    @GetMapping("/service/house/{houseId}")
    @PreAuthorize("@pms.hasPermission('service:get:getByHouseId')")
    public R getByHouseId(@PathVariable("houseId") String houseId) {
        return remoteSysServiceCfgService.getByHouseId(houseId);
    }

    /**
     * 通过id查询系统增值服务配置
     *
     * @param serviceId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{serviceId}")
    @PreAuthorize("@pms.hasPermission('service:get:getById')")
    public R getById(@PathVariable("serviceId") String serviceId) {
        return remoteSysServiceCfgService.getById(serviceId);
    }

    /**
     * 新增系统增值服务配置
     *
     * @param sysServiceCfg 系统增值服务配置
     * @return R
     */
    @ApiOperation(value = "新增系统增值服务配置", notes = "新增系统增值服务配置")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('service:post:save')")
    public R save(@RequestBody SysServiceCfg sysServiceCfg) {
        return remoteSysServiceCfgService.save(sysServiceCfg);
    }

    /**
     * 增值服务项目配置
     *
     * @param serviceProjectSaveVo 增值服务项目配置
     * @return R
     */
    @ApiOperation(value = "增值服务项目配置", notes = "增值服务项目配置")
    @SysLog("增值服务项目配置")
    @PostMapping("/project")
    @PreAuthorize("@pms.hasPermission('service:post:saveByProject')")
    public R saveByProject(@RequestBody ServiceProjectSaveVo serviceProjectSaveVo) {
        return remoteSysServiceCfgService.saveByProject(serviceProjectSaveVo);
    }

    /**
     * 房屋信息增值服务
     *
     * @param serviceHouseSaveVo 房屋信息增值服务
     * @return R
     */
    @ApiOperation(value = "房屋信息增值服务", notes = "房屋信息增值服务")
    @SysLog("房屋信息增值服务")
    @PostMapping("/house")
    @PreAuthorize("@pms.hasPermission('service:post:saveByHouse')")
    public R saveByHouse(@RequestBody ServiceHouseSaveVo serviceHouseSaveVo) {
        return remoteSysServiceCfgService.saveByHouse(serviceHouseSaveVo);
    }

    /**
     * 批量新增房屋增值服务
     *
     * @param serviceHouseIdsSaveVo 批量新增房屋增值服务
     * @return R
     */
    @ApiOperation(value = "批量新增房屋增值服务", notes = "批量新增房屋增值服务")
    @SysLog("批量新增房屋增值服务")
    @PostMapping("/houses")
    @PreAuthorize("@pms.hasPermission('service:post:houses')")
    public R saveByHouseIds(@RequestBody ServiceHouseIdsSaveVo serviceHouseIdsSaveVo) {
        return remoteSysServiceCfgService.saveByHouseIds(serviceHouseIdsSaveVo);
    }

    /**
     * 修改系统增值服务配置
     *
     * @param sysServiceCfg 系统增值服务配置
     * @return R
     */
    @ApiOperation(value = "修改系统增值服务配置", notes = "修改系统增值服务配置")
    @SysLog("修改系统增值服务配置")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('service:put:updateById')")
    public R updateById(@RequestBody SysServiceCfg sysServiceCfg) {
        return remoteSysServiceCfgService.updateById(sysServiceCfg);
    }

    /**
     * 通过id删除系统增值服务配置
     *
     * @param serviceId id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统增值服务配置", notes = "通过id删除系统增值服务配置")
    @SysLog("通过id删除系统增值服务配置")
    @DeleteMapping("/{serviceId}")
    @PreAuthorize("@pms.hasPermission('service:delete:removeById')")
    public R removeById(@PathVariable String serviceId) {
        return remoteSysServiceCfgService.removeById(serviceId);
    }

    /**
     * 通过serviceType查询信息
     *
     * @return
     */
    @ApiOperation(value = "通过serviceType查询信息", notes = "通过serviceType查询信息")
    @GetMapping("/getServiceByType/{serviceType}")
    @PreAuthorize("@pms.hasPermission('service:get:getServiceByType')")
    public R getServiceByType(@PathVariable("serviceType") String serviceType){
        return remoteSysServiceCfgService.getServiceByType(serviceType);
    }
}
