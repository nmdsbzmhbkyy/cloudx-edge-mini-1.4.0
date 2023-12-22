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

import com.aurine.cloudx.estate.feign.RemoteParCarRegisterService;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projectParCarRegister")
@Api(value = "projectParCarRegister", tags = "车辆管理")
public class ProjectParCarRegisterController {
    @Autowired
    private RemoteParCarRegisterService remoteParCarRegisterService;

    @PostMapping
    @ApiOperation(value = "车辆登记", notes = "车辆登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车场Id", required = true, paramType = "body"),
            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true, paramType = "body"),
            @ApiImplicitParam(name = "telephone", value = "手机号", required = true, paramType = "body"),
            @ApiImplicitParam(name = "personName", value = "车主名", required = true, paramType = "body"),
            @ApiImplicitParam(name = "parkPlaceName", value = "车位地址", required = true, paramType = "body"),
            @ApiImplicitParam(name = "relType", value = "车位类型(0 闲置(公共) 1 产权 2 租赁)", required = true, paramType = "body"),
            @ApiImplicitParam(name = "parkPlaceId", value = "车位地址", required = true, paramType = "body"),
            @ApiImplicitParam(name = "ruleId", value = "收费方式", required = true, paramType = "body"),
            @ApiImplicitParam(name = "startTime", value = "开始日期（格式 yyyy-mm-dd）", required = true, paramType = "body"),
            @ApiImplicitParam(name = "startTime", value = "结束日期（格式 yyyy-mm-dd）", required = true, paramType = "body")
    })
    public R register (@RequestBody ProjectParCarRegisterVo projectParCarRegister) {
        return remoteParCarRegisterService.register(projectParCarRegister);
    }

}
