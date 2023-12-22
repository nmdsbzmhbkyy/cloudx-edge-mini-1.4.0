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

import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.aurine.cloudx.estate.entity.SysDeviceTypeThirdPartyConfig;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 第三方接口设备配置
 *
 * @author 王伟
 * @date 2020-08-13 14:23:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/devicetypeThirdpartyConfig")
@Api(value = "SysDevicetypeThirdpartyConfigController", tags = "第三方接口设备配置管理")
public class SysDevicetypeThirdpartyConfigController {

    private final SysDeviceTypeThirdPartyConfigService sysDevicetypeThirdpartyConfigService;

    /**
     * 分页查询
     *
     * @param page                          分页对象
     * @param sysDevicetypeThirdpartyConfig 第三方接口设备配置
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getSysDevicetypeThirdpartyConfigPage(Page page, SysDeviceTypeThirdPartyConfig sysDevicetypeThirdpartyConfig) {
        return R.ok(sysDevicetypeThirdpartyConfigService.page(page, Wrappers.query(sysDevicetypeThirdpartyConfig)));
    }

    /**
     * 根据项目编号，设备类型 获取配置信息
     *
     * @param deviceType 设备类型
     * @param projectId  项目编号
     * @return
     */
    @ApiOperation(value = "根据项目编号，设备类型 获取配置信息", notes = "根据项目编号，设备类型 获取配置信息")
    @GetMapping("/getConfig/{deviceType}/{projectId}")
    public R getConfig(@PathVariable("deviceType") String deviceType, @PathVariable("projectId") Integer projectId) {
        return R.ok(sysDevicetypeThirdpartyConfigService.getConfigByDeviceType(deviceType, projectId, 1));
    }


    /**
     * 通过id查询第三方接口设备配置
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}")
    public R getById(@PathVariable("seq") Integer seq) {
        return R.ok(sysDevicetypeThirdpartyConfigService.getById(seq));
    }

    /**
     * 新增第三方接口设备配置
     *
     * @param sysDevicetypeThirdpartyConfig 第三方接口设备配置
     * @return R
     */
    @ApiOperation(value = "新增第三方接口设备配置", notes = "新增第三方接口设备配置")
    @SysLog("新增第三方接口设备配置")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_sysdevicetypethirdpartyconfig_add')")
    public R save(@RequestBody SysDeviceTypeThirdPartyConfig sysDevicetypeThirdpartyConfig) {
        return R.ok(sysDevicetypeThirdpartyConfigService.save(sysDevicetypeThirdpartyConfig));
    }

    /**
     * 修改第三方接口设备配置
     *
     * @param sysDevicetypeThirdpartyConfig 第三方接口设备配置
     * @return R
     */
    @ApiOperation(value = "修改第三方接口设备配置", notes = "修改第三方接口设备配置")
    @SysLog("修改第三方接口设备配置")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_sysdevicetypethirdpartyconfig_edit')")
    public R updateById(@RequestBody SysDeviceTypeThirdPartyConfig sysDevicetypeThirdpartyConfig) {
        return R.ok(sysDevicetypeThirdpartyConfigService.updateById(sysDevicetypeThirdpartyConfig));
    }

    /**
     * 通过id删除第三方接口设备配置
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除第三方接口设备配置", notes = "通过id删除第三方接口设备配置")
    @SysLog("通过id删除第三方接口设备配置")
    @DeleteMapping("/{seq}")
    @PreAuthorize("@pms.hasPermission('estate_sysdevicetypethirdpartyconfig_del')")
    public R removeById(@PathVariable Integer seq) {
        return R.ok(sysDevicetypeThirdpartyConfigService.removeById(seq));
    }

}
