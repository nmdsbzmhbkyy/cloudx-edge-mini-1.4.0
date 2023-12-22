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

import com.aurine.cloudx.estate.feign.RemoteParkBillingRuleService;
import com.aurine.cloudx.estate.service.ProjectParkBillingRuleService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/projectParkingBillRule")
@Api(value = "projectParkingBillRule", tags = "车场计费规则管理")
public class ProjectParkBillingRuleController {

    private final ProjectParkBillingRuleService projectParkBillingRuleService;
    @Resource
    private RemoteParkBillingRuleService remoteParkBillingRuleService;

    /**
     * 查询可用的计费规则
     *
     * @param parkId 停车场ID
     * @return
     */
    @ApiOperation(value = "根据车场ID获取车场计费规则列表", notes = "根据车场ID获取车场计费规则列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车场ID", required = true, paramType = "path")
    })
    @GetMapping("/list/{parkId}")
    public R listUseable(@PathVariable("parkId") String parkId) {
        return remoteParkBillingRuleService.listUseable(parkId);
    }







}
