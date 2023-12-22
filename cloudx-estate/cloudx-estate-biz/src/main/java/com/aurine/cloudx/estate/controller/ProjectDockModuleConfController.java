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

import com.aurine.cloudx.estate.constant.enums.DockModuleEnum;
import com.aurine.cloudx.estate.entity.ProjectDockModuleConf;
import com.aurine.cloudx.estate.service.ProjectDockModuleConfService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfWR20Vo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 第三方系统配置
 *
 * @author 王伟
 * @date 2020-12-15 13:39:38
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDockModuleConf")
@Api(value = "projectDockModuleConf", tags = "第三方系统配置管理")
public class ProjectDockModuleConfController {

    private final ProjectDockModuleConfService projectDockModuleConfService;
    @Resource
    private final DockModuleConfigUtil dockModuleConfigUtil;

    /**
     * 分页查询
     *
     * @param page                  分页对象
     * @param projectDockModuleConf 第三方系统配置
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectDockModuleConfPage(Page page, ProjectDockModuleConf projectDockModuleConf) {
        return R.ok(projectDockModuleConfService.page(page, Wrappers.query(projectDockModuleConf)));
    }


    /**
     * 通过id查询第三方系统配置
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}")
    public R getById(@PathVariable("seq") Integer seq) {
        return R.ok(projectDockModuleConfService.getById(seq));
    }

    /**
     * 通过id查询第三方系统配置
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/wr20/{projectId}")
    public R getWR20(@PathVariable("projectId") Integer projectId) {
        return R.ok(projectDockModuleConfService.getConfigByProjectId(projectId, DockModuleEnum.WR20.code, ProjectDockModuleConfWR20Vo.class));
    }

    /**
     * 新增第三方系统配置
     *
     * @param projectDockModuleConf 第三方系统配置
     * @return R
     */
    @ApiOperation(value = "新增第三方系统配置", notes = "新增第三方系统配置")
    @SysLog("新增第三方系统配置")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectdockmoduleconf_add')" )
    public R save(@RequestBody ProjectDockModuleConf projectDockModuleConf) {
        return R.ok(projectDockModuleConfService.save(projectDockModuleConf));
    }

    /**
     * 删除第三方系统配置
     *
     * @return R
     */
    @ApiOperation(value = "新增第三方系统配置", notes = "新增第三方系统配置")
    @SysLog("新增WR20第三方系统配置")
    @DeleteMapping("/wr20/{projectId}")
//    @PreAuthorize("@pms.hasPermission('estate_projectdockmoduleconf_add')" )
    public R delWR20(@PathVariable("projectId") Integer projectId) {
        return R.ok(projectDockModuleConfService.delWR20(projectId));
    }

    /**
     * 删除第三方系统配置
     *
     * @param projectDockModuleConfWR20Vo 第三方系统配置
     * @return R
     */
    @ApiOperation(value = "新增第三方系统配置", notes = "新增第三方系统配置")
    @SysLog("新增WR20第三方系统配置")
    @PostMapping("/wr20")
//    @PreAuthorize("@pms.hasPermission('estate_projectdockmoduleconf_add')" )
    public R saveWR20(@RequestBody ProjectDockModuleConfWR20Vo projectDockModuleConfWR20Vo) {
        return R.ok(projectDockModuleConfService.saveWR20(projectDockModuleConfWR20Vo));
    }

    /**
     * 通过id查询第三方系统配置
     *
     * @param type type
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/sync/wr20/{projectId}/{type}")
    public R syncWr20(@PathVariable("projectId") Integer projectId, @PathVariable("type") String type) {
        return R.ok(projectDockModuleConfService.syncWr20(projectId, type));
    }

    /**
     * 修改第三方系统配置
     *
     * @param projectDockModuleConf 第三方系统配置
     * @return R
     */
    @ApiOperation(value = "修改第三方系统配置", notes = "修改第三方系统配置")
    @SysLog("修改第三方系统配置")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectdockmoduleconf_edit')" )
    public R updateById(@RequestBody ProjectDockModuleConf projectDockModuleConf) {
        return R.ok(projectDockModuleConfService.updateById(projectDockModuleConf));
    }

    /**
     * 通过id删除第三方系统配置
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除第三方系统配置", notes = "通过id删除第三方系统配置")
    @SysLog("通过id删除第三方系统配置")
    @DeleteMapping("/{seq}")
//    @PreAuthorize("@pms.hasPermission('estate_projectdockmoduleconf_del')" )
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectDockModuleConfService.removeById(seq));
    }



    /**
     * 判断项目是不是WR2
     *
     * @return R
     */
    @ApiOperation(value = "判断项目是不是WR2.0", notes = "判断项目是不是WR2.0")
    @GetMapping("/isWr20/{projectId}")
    public R<Boolean> isWr20(@PathVariable("projectId") Integer projectId) {
        return R.ok(dockModuleConfigUtil.isWr20(projectId));
    }
}
