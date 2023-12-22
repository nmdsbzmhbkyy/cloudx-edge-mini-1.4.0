package com.aurine.cloudx.estate.open.house.fegin;

import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteSysServiceCfgService", value = "cloudx-estate-biz")
public interface RemoteSysServiceCfgService {
    /**
     * 查询所有增值服务
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务", notes = "查询所有增值服务")
    @GetMapping("/sysServiceCfg/page")
    R page(Page page);

    /**
     * 查询所有增值服务与厂商列表
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务与厂商列表", notes = "查询所有增值服务与厂商列表")
    @GetMapping("/sysServiceCfg/selectValueadd")
    R selectValueadd();

    /**
     * 查询所有增值服务无分页
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务无分页", notes = "查询所有增值服务无分页")
    @GetMapping("/sysServiceCfg/listService")
    R listService();

    /**
     * 查询当前项目所有增值服务,用于房屋增值服务
     *
     * @return R
     */
    @ApiOperation(value = "查询当前项目所有增值服务", notes = "查询当前项目所有增值服务")
    @GetMapping("/sysServiceCfg/houseServiceList")
    R getSysServiceHouseList();

    /**
     * 根据项目id查询增值服务
     *
     * @return R
     */
    @ApiOperation(value = "根据项目id查询增值服务", notes = "根据项目id查询增值服务")
    @GetMapping("/sysServiceCfg/getHouseServiceByProjectId/{projectId}")
    R getByProjectId(@PathVariable("projectId") Integer projectId);

    @ApiOperation(value = "根据房屋id查询增值服务", notes = "根据房屋id查询增值服务")
    @GetMapping("/sysServiceCfg/gethouseServiceByhouseId/{houseId}")
    R getByHouseId(@PathVariable("houseId") String houseId);

    /**
     * 通过id查询系统增值服务配置
     *
     * @param serviceId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/sysServiceCfg/{serviceId}")
    R getById(@PathVariable("serviceId") String serviceId);

    /**
     * 新增系统增值服务配置
     *
     * @param sysServiceCfg 系统增值服务配置
     * @return R
     */
    @ApiOperation(value = "新增系统增值服务配置", notes = "新增系统增值服务配置")
    @PostMapping("/sysServiceCfg")
    R save(@RequestBody SysServiceCfg sysServiceCfg);

    /**
     * 增值服务项目配置
     *
     * @param serviceProjectSaveVo 增值服务项目配置
     * @return R
     */
    @ApiOperation(value = "增值服务项目配置", notes = "增值服务项目配置")
    @PostMapping("/sysServiceCfg/saveByProject")
    R saveByProject(@RequestBody ServiceProjectSaveVo serviceProjectSaveVo);

    /**
     * 房屋信息增值服务
     *
     * @param serviceHouseSaveVo 房屋信息增值服务
     * @return R
     */
    @ApiOperation(value = "房屋信息增值服务", notes = "房屋信息增值服务")
    @PostMapping("/sysServiceCfg/saveByHouse")
    R saveByHouse(@RequestBody ServiceHouseSaveVo serviceHouseSaveVo);

    /**
     * 批量新增房屋增值服务
     *
     * @param serviceHouseIdsSaveVo 批量新增房屋增值服务
     * @return R
     */
    @ApiOperation(value = "批量新增房屋增值服务", notes = "批量新增房屋增值服务")
    @PostMapping("/sysServiceCfg/saveByHouseIds")
    R saveByHouseIds(@RequestBody ServiceHouseIdsSaveVo serviceHouseIdsSaveVo);

    /**
     * 修改系统增值服务配置
     *
     * @param sysServiceCfg 系统增值服务配置
     * @return R
     */
    @ApiOperation(value = "修改系统增值服务配置", notes = "修改系统增值服务配置")
    @PutMapping("/sysServiceCfg")
    R updateById(@RequestBody SysServiceCfg sysServiceCfg);

    /**
     * 通过id删除系统增值服务配置
     *
     * @param serviceId id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统增值服务配置", notes = "通过id删除系统增值服务配置")
    @DeleteMapping("/sysServiceCfg/{serviceId}")
    R removeById(@PathVariable("serviceId") String serviceId);

    /**
     * 通过serviceType查询信息
     *
     * @return
     */
    @ApiOperation(value = "通过serviceType查询信息", notes = "通过serviceType查询信息")
    @GetMapping("/sysServiceCfg/getServiceByType/{serviceType}")
    R getServiceByType(@PathVariable("serviceType") String serviceType);
}
