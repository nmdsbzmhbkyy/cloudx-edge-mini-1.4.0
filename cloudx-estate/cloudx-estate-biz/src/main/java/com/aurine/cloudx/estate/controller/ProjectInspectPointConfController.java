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

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.qrcode.QRCodeGenerateUtil;
import com.aurine.cloudx.estate.entity.ProjectInspectPointConf;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfVo;
import com.aurine.cloudx.estate.vo.QRCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.aurine.cloudx.estate.service.ProjectInspectPointConfService;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 设备巡检点配置
 *
 * @author 王良俊
 * @date 2020-07-23 16:26:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectInspectPointConf")
@Api(value = "projectInspectPointConf", tags = "设备巡检点配置管理")
public class ProjectInspectPointConfController {

    @Resource
    private final ProjectInspectPointConfService projectInspectPointConfService;
    @Resource
    ProjectInspectTaskService projectInspectTaskService;

    /**
     * 分页查询
     *
     * @param page              分页对象
     * @param searchConditionVo 查询vo对象
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @SysLog("巡检点分页查询")
    @GetMapping("/page")
    public R getProjectInspectPointConfPage(Page page, ProjectInspectPointConfSearchConditionVo searchConditionVo) {
        return R.ok(projectInspectPointConfService.fetchList(page, searchConditionVo));
    }

    /**
     * 根据路线id查询巡更点列表
     *
     * @param inspectRouteId 路线id
     */
    @ApiOperation(value = "根据路线id查询巡更点列表", notes = "根据路线id查询巡更点列表")
    @SysLog("根据巡检路线id获取巡检点列表")
    @GetMapping("/listByInspectRouteId/{inspectRouteId}")
    public R getInspectPointListByInspectRouteId(@PathVariable("inspectRouteId") String inspectRouteId) {
        return R.ok(projectInspectPointConfService.listByInspectPointByRouteId(inspectRouteId));
    }

    /**
     * 通过id查询设备巡检点配置
     *
     * @param pointId uid
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @SysLog("根据巡检点id获取巡检点信息")
    @GetMapping("/{pointId}")
    public R getById(@PathVariable("pointId") String pointId) {
        return R.ok(projectInspectPointConfService.getById(pointId));
    }

    /**
     * 新增设备巡检点配置
     *
     * @param pointConfVo 设备巡检点配置VO对象
     * @return R
     */
    @ApiOperation(value = "新增设备巡检点配置", notes = "新增设备巡检点配置")
    @SysLog("新增设备巡检点配置")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectinspectpointconf_add')" )
    public R save(@RequestBody ProjectInspectPointConfVo pointConfVo) {
        return R.ok(projectInspectPointConfService.saveInspectPoint(pointConfVo));
    }

    /**
     * 修改设备巡检点配置
     *
     * @param pointConfVo 设备巡检点配置Vo对象
     * @return R
     */
    @ApiOperation(value = "修改设备巡检点配置", notes = "修改设备巡检点配置")
    @SysLog("修改设备巡检点配置")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectinspectpointconf_edit')" )
    public R updateById(@RequestBody ProjectInspectPointConfVo pointConfVo) {
        return R.ok(projectInspectPointConfService.updateInspectPoint(pointConfVo));
    }

    /**
     * 通过id删除设备巡检点配置
     *
     * @param pointId id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备巡检点配置", notes = "通过id删除设备巡检点配置")
    @SysLog("通过id删除设备巡检点配置")
    @DeleteMapping("/{pointId}")
//    @PreAuthorize("@pms.hasPermission('estate_projectinspectpointconf_del')" )
    public R removeById(@PathVariable String pointId) {
        return R.ok(projectInspectPointConfService.removeInspectPoint(pointId));
    }

    /**
     * <p>
     * 生成巡检点二维码并返回给前端下载
     * </p>
     *
     * @param qrCodeVo 巡检点二维码vo对象存放二维码内容、二维码宽高
     * @return
     * @throws
     */
    @ApiOperation(value = "生成巡检点二维码并返回给前端下载", notes = "生成巡检点二维码并返回给前端下载")
    @SysLog("生成巡检点二维码并返回给前端下载")
    @PostMapping("/downloadQrCode")
    @Inner(false)
    public void downloadQrCode(QRCode qrCodeVo, HttpServletResponse response) {
        if (CollUtil.isNotEmpty(qrCodeVo.getPointIdList())) {
            String rgbStr = qrCodeVo.getRgbStr();
            String[] rgbArr = rgbStr.substring(4, rgbStr.length() - 1).split(", ");
            int qrCodeColor = new Color(Integer.parseInt(rgbArr[0]), Integer.parseInt(rgbArr[1]), Integer.parseInt(rgbArr[2])).getRGB();
            List<ProjectInspectPointConf> pointConfList = projectInspectPointConfService.list(new QueryWrapper<ProjectInspectPointConf>()
                    .lambda().in(ProjectInspectPointConf::getPointId, qrCodeVo.getPointIdList()));
            Map<String, String> pointMap = pointConfList.stream().collect(Collectors.toMap(ProjectInspectPointConf::getPointId,
                    ProjectInspectPointConf::getPointName));
            QRCodeGenerateUtil.getInstance().genQRCodeZip(response, pointMap, qrCodeVo.getWidth(), qrCodeVo.getHeight(),
                    qrCodeVo.getLogoFile(), qrCodeColor);
        }
    }
}
