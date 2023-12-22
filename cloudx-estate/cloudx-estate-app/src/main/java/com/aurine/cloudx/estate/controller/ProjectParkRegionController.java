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

import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.feign.RemoteParkingPlaceService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/Park-region" )
@Api(value = "Park-region", tags = "车位管理")
public class ProjectParkRegionController {
    @Resource
    private RemoteParkingPlaceService remoteParkingPlaceService;

    /**
     * 获取这个用户所拥有的车位类型
     * @author: 王良俊
     * @return
     */
    @ApiOperation(value = "获取这个用户所拥有的车位类型")
    @GetMapping("/type" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "parkId", value = "停车场ID", required = true, paramType = "param"),
            @ApiImplicitParam(name = "personId", value = "人员ID", required = true, paramType = "param"),
    })
    public R<List<String>> getPlaceRelTypeByPersonId(@RequestParam("parkId") String parkId, @RequestParam("personId") String personId) {
        Map<String, Object> query = new HashMap<>();
        query.put("personId", personId);
        query.put("parkId", parkId);

        return remoteParkingPlaceService.getPlaceRelTypeByPersonId(query);
    }


    /**
     * <p>
     *  获取车位列表
     * </p>
     *
     * @param personId
     * @param parkRegionId
     * @param relType
     * @return
     * @throws
    */
    @ApiOperation(value = "获取车位列表", notes = "获取车位列表")
    @SysLog("获取车位列表")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "param"),
            @ApiImplicitParam(name = "parkRegionId", value = "车位区域id", required = true, paramType = "param"),
            @ApiImplicitParam(name = "relType", value = "关系类型 0 闲置 1 产权 2 租赁", required = true, paramType = "param"),
    })
    public R<List<ProjectParkingPlace>> listParkPlaceByRelType(@RequestParam("personId") String personId,
                                              @RequestParam("parkRegionId") String parkRegionId,
                                              @RequestParam("relType") String relType) {
        Map<String, Object> query = new HashMap<>();
        query.put("personId", personId);
        query.put("parkRegionId", parkRegionId);
        query.put("relType", relType);

        return remoteParkingPlaceService.listParkPlaceByRelType(query);
    }

    /**
     * 通过车位区域id和人员id获取到这个人在这个区域拥有的车位列表
     * @author: 王良俊
     * @param personId
     * @param parkId
     * @param relType
     * @return
     */
    @ApiOperation(value = "获取车位区域列表")
    @GetMapping("/region/list" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "param"),
            @ApiImplicitParam(name = "parkId", value = "车场id", required = true, paramType = "param"),
            @ApiImplicitParam(name = "relType", value = "关系类型 0 闲置 1 产权 2 租赁", required = true, paramType = "param"),
    })
    public R<List<ProjectParkRegion>> getList(@RequestParam("personId") String personId,
                                              @RequestParam("parkId") String parkId,
                                              @RequestParam("relType") String relType) {
        Map<String, Object> query = new HashMap<>();
        query.put("personId", personId);
        query.put("parkId", parkId);
        query.put("relType", relType);

        return remoteParkingPlaceService.getList(query);
    }
}
