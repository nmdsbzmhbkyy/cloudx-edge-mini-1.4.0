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

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.vo.ProjectNoticeDeviceVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.aurine.cloudx.estate.entity.ProjectPersonAttrConf;
import com.aurine.cloudx.estate.service.ProjectPersonAttrConfService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 人员属性拓展配置表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 16:24:19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectPersonAttrConf" )
@Api(value = "projectPersonAttrConf", tags = "人员属性拓展配置表管理")
public class ProjectPersonAttrConfController {

    private final  ProjectPersonAttrConfService projectPersonAttrConfService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectPersonAttrConf 人员属性拓展配置表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectPersonAttrConf>> getProjectPersonAttrConfPage(Page page, ProjectPersonAttrConf projectPersonAttrConf) {
        return R.ok(projectPersonAttrConfService.page(page, Wrappers.query(projectPersonAttrConf)));
    }


    /**
     * 通过id查询人员属性拓展配置表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户拓展属性id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectPersonAttrConf> getById(@PathVariable("id" ) String id) {
        return R.ok(projectPersonAttrConfService.getById(id));
    }

    /**
     * 新增人员属性拓展配置表(私有属性)
     * @param projectPersonAttrConf 人员属性拓展配置表
     * @return R
     */
    @ApiOperation(value = "新增人员属性拓展配置表", notes = "新增人员属性拓展配置表")
    @SysLog("新增人员属性拓展配置表" )
    @PostMapping("/savePrivate")
    @PreAuthorize("@pms.hasPermission('estate_personattrconf_add')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R savePrivate(@RequestBody ProjectPersonAttrConf projectPersonAttrConf) {
        projectPersonAttrConf.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectPersonAttrConfService.save(projectPersonAttrConf));
    }

    /**
     * 修改人员属性拓展配置表(私有属性)
     * @param projectPersonAttrConf 人员属性拓展配置表
     * @return R
     */
    @ApiOperation(value = "修改人员属性拓展配置表", notes = "修改人员属性拓展配置表")
    @SysLog("修改人员属性拓展配置表" )
    @PutMapping("/updatePrivate")
    @PreAuthorize("@pms.hasPermission('estate_personattrconf_edit')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updatePrivate(@RequestBody ProjectPersonAttrConf projectPersonAttrConf) {
        projectPersonAttrConf.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectPersonAttrConfService.updateById(projectPersonAttrConf));
    }
    /**
     * 新增人员属性拓展配置表(公有属性)
     * @param projectPersonAttrConf 人员属性拓展配置表
     * @return R
     */
    @ApiOperation(value = "新增人员属性拓展配置表", notes = "新增人员属性拓展配置表")
    @SysLog("新增人员属性拓展配置表" )
    @PostMapping("/savePublic")
    @PreAuthorize("@pms.hasPermission('estate_personattrconf_add')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R savePublic(@RequestBody ProjectPersonAttrConf projectPersonAttrConf) {
        //公共属性项目id为null
        projectPersonAttrConf.setProjectId(null);
        return R.ok(projectPersonAttrConfService.save(projectPersonAttrConf));
    }

    /**
     * 修改人员属性拓展配置表(公有属性)
     * @param projectPersonAttrConf 人员属性拓展配置表
     * @return R
     */
    @ApiOperation(value = "修改人员属性拓展配置表", notes = "修改人员属性拓展配置表")
    @SysLog("修改人员属性拓展配置表" )
    @PutMapping("/updatePublic")
    @PreAuthorize("@pms.hasPermission('estate_personattrconf_edit')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updatePublic(@RequestBody ProjectPersonAttrConf projectPersonAttrConf) {
        //公共属性项目id为null
        projectPersonAttrConf.setProjectId(null);
        return R.ok(projectPersonAttrConfService.updateById(projectPersonAttrConf));
    }

    /**
     * 通过id删除人员属性拓展配置表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除人员属性拓展配置表", notes = "通过id删除人员属性拓展配置表")
    @SysLog("通过id删除人员属性拓展配置表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('estate_personattrconf_del')" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户拓展属性id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        return R.ok(projectPersonAttrConfService.removeById(id));
    }

}
