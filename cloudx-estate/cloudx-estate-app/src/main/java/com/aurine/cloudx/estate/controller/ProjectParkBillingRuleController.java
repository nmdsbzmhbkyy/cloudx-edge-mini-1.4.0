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

import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.feign.RemoteParkBillingRuleService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/car-billing")
@Api(value = "car-billing", tags = "车场计费规则管理")
public class ProjectParkBillingRuleController {
    @Resource
    private RemoteParkBillingRuleService remoteParkBillingRuleService;

    /**
     * 查询可用的计费规则
     *
     * @param parkId 停车场ID
     * @RETURN
     */
    @ApiOperation(value = "根据车场ID获取车场计费规则列表", notes = "根据车场ID获取车场计费规则列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "parkId", value = "车场ID", required = true, paramType = "path")
    })
    @GetMapping("/list/{parkId}")
    public R<List<ProjectParkBillingRule>> listUseable(@PathVariable("parkId") String parkId) {
        return remoteParkBillingRuleService.listUseable(parkId);
    }
}
