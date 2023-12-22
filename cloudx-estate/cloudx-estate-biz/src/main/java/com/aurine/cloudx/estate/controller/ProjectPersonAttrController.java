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

import com.aurine.cloudx.estate.service.ProjectPersonAttrService;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrListVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 人员属性拓展表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 16:24:30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectPersonAttr" )
@Api(value = "ProjectPersonAttr", tags = "人员属性拓展表管理")
public class ProjectPersonAttrController {

    private final  ProjectPersonAttrService projectPersonAttrService;


    /**
     * 查询人员属性拓展表管理
     *
     * @param type 人员类型
     * @param personId   人员id
     * @param projectId  项目id
     * @return R
     */
    @ApiOperation(value = "通过类型查询", notes = "通过类型查询")
    @GetMapping("/{projectId}/{type}/{personId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "type", value = "人员类型", paramType = "path", required = true),
            @ApiImplicitParam(name = "personId", value = "用户id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectPersonAttrListVo>> get(@PathVariable("projectId") Integer projectId, @PathVariable("type") String type, @PathVariable(value = "personId",required = false)String personId) {
        return R.ok(projectPersonAttrService.getPersonAttrListVo(projectId, type,personId));
    }


    /**
     * 修改人员属性拓展表管理
     *
     * @param projectPersonAttrFormVo 人员属性拓展表管理
     * @return R
     */
    @ApiOperation(value = "修改人员属性拓展表管理", notes = "修改人员属性拓展表管理")
    @SysLog("修改人员属性拓展表管理")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_personattr_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R update(@RequestBody ProjectPersonAttrFormVo projectPersonAttrFormVo) {
        return R.ok(projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo));
    }


}
