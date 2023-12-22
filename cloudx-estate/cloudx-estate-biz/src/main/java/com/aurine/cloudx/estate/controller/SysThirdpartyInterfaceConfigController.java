/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.SysThirdPartyInterfaceConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.vo.SysThirdPartyInterfaceConfigVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 第三方接口配置
 *
 * @author 王伟
 * @date 2020-08-13 14:20:04
 */
@RestController
@RequestMapping("/thirdpartyInterfaceConfig")
@Api(value = "SysThirdpartyInterfaceConfigController", tags = "第三方接口配置管理")
public class SysThirdpartyInterfaceConfigController {
    @Resource
    private SysThirdPartyInterfaceConfigService sysThirdpartyInterfaceConfigService;

    /**
     * 列表
     *
     * @return
     */
    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("/list")
    public R list() {
        return R.ok(sysThirdpartyInterfaceConfigService.listInterfaceConfig(null, TenantContextHolder.getTenantId()));
    }

    /**
     * 根据设备类型获取列表
     * 预留接口
     *
     * @return
     */
    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("/list/{deviceType}")
    public R listByDeviceType(@PathVariable("deviceType") String deviceType) {
        return R.ok(sysThirdpartyInterfaceConfigService.listInterfaceConfig(null, TenantContextHolder.getTenantId()));
    }


    /**
     * 通过id查询第三方接口配置
     * 注意，如果是项目级，会返回当前项目的配置，uid会发生改变
     *
     * @param uid uid
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/get/{projectId}/{uid}")
    public R getById(@PathVariable("uid") String uid, @PathVariable("projectId") int projectId) {
        return R.ok(sysThirdpartyInterfaceConfigService.getConfig(projectId, uid));
    }

    /**
     * 通过id查询第三方接口配置
     * 注意，如果是项目级，会返回当前项目的配置，uid会发生改变
     *
     * @param deviceType deviceType
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/getByDeviceType/{projectId}/{deviceType}")
    public R getByDeviceType(@PathVariable("deviceType") String deviceType, @PathVariable("projectId") int projectId) {
        return R.ok(sysThirdpartyInterfaceConfigService.getConfigByDeviceType(projectId, deviceType));
    }

    /**
     * 通过id查询第三方接口配置
     * 注意，如果是项目级，会返回当前项目的配置，uid会发生改变
     *
     * @param deviceType deviceType
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/subscribe/{projectId}/{deviceType}")
    @PreAuthorize("@pms.hasPermission('estate_thirdpartyconfig_sub')")
    @SysLog("订阅第三方接口")
    public R subscribe(@PathVariable("deviceType") String deviceType, @PathVariable("projectId") int projectId) {
        return R.ok(DeviceFactoryProducer.getFactory(deviceType, projectId, TenantContextHolder.getTenantId()).getDeviceService().subscribe(deviceType, projectId, TenantContextHolder.getTenantId()));
    }


    /**
     * 保存第三方接口配置
     *
     * @param sysThirdpartyInterfaceConfig 第三方接口配置
     * @return R
     */
    @ApiOperation(value = "新增第三方接口配置", notes = "新增第三方接口配置")
    @SysLog("编辑第三方接口配置")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_thirdpartyconfig_add')")
    public R save(@RequestBody SysThirdPartyInterfaceConfigVo sysThirdpartyInterfaceConfig) {
        return R.ok(sysThirdpartyInterfaceConfigService.save(sysThirdpartyInterfaceConfig));
    }

}
