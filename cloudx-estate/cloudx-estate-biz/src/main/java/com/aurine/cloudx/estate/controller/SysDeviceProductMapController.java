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
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.estate.constant.DeviceInfoConstant;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.vo.ProjectDeviceSelectTreeVo;
import com.aurine.cloudx.estate.vo.SysDeviceProductMapVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 产品设备关联
 *
 * @ClassName: SysDeviceProductMapController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-12 9:23
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysDeviceProductMapController")
@Api(value = "SysDeviceProductMapController", tags = "产品设备关联")
public class SysDeviceProductMapController {

    private final SysDeviceProductMapService sysDeviceProductMapService;


    /**
     * 同步产品信息
     *
     * @param projectId 要同步的项目编号
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/sync")
    @Inner(false)
    @SysLog("调用设备产品同步接口")
    public R syncProduct(int projectId) {
        DeviceFactoryProducer.getFactory(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode(), projectId, 1).getDeviceService().syncProduces(projectId, 1);
        return R.ok();
    }

    @ApiOperation(value = "门控制器信息", notes = "门控制器信息")
    @GetMapping("/doorControllers")
    public R doorControllerInfos() {
        return R.ok(DoorControllerEnum.getList());
    }


    /**
     * 产品树
     *
     * @return
     */
    @ApiOperation(value = "产品树", notes = "产品树")
    @GetMapping("/productTree")
    public R productTree() {
        List<ProjectDeviceSelectTreeVo> projectDeviceSelectTreeVoList = new ArrayList<>();
        List<SysDeviceProductMapVo> sysDeviceProductMaps = sysDeviceProductMapService.getProductList();
        String uid = UUID.randomUUID().toString().replace("-", "");
        //设置项目信息 xull@aurine.cn 2020/5/25 10:42

        ProjectDeviceSelectTreeVo accessControlSystem = new ProjectDeviceSelectTreeVo();
        accessControlSystem.setName("门禁系统");
        //设置根节点 将id设为0 pid设为uid是为了预防后面产品重构productId可能为1的情况 guhl@aurine.cn 2020/12/22 11:47
        accessControlSystem.setId("type:" + DeviceTypeConstants.LADDER_WAY_DEVICE + "-" + DeviceTypeConstants.GATE_DEVICE + "-"
                + DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE);
        accessControlSystem.setParentId(uid);
        accessControlSystem.setType(DeviceInfoConstant.IS_NOT_DEVICE);

        ProjectDeviceSelectTreeVo levelGauge = new ProjectDeviceSelectTreeVo();
        levelGauge.setName("液位计");
        levelGauge.setId("type:" + DeviceTypeConstants.LEVEL_GAUGE);
        levelGauge.setParentId(uid);
        levelGauge.setType(DeviceInfoConstant.IS_NOT_DEVICE);

        ProjectDeviceSelectTreeVo smartManholeCover = new ProjectDeviceSelectTreeVo();
        smartManholeCover.setName("智能井盖");
        smartManholeCover.setId("type:" + DeviceTypeConstants.SMART_MANHOLE_COVER);
        smartManholeCover.setParentId(uid);
        smartManholeCover.setType(DeviceInfoConstant.IS_NOT_DEVICE);

        ProjectDeviceSelectTreeVo waterMeter = new ProjectDeviceSelectTreeVo();
        waterMeter.setName("智能水表");
        waterMeter.setId("type:" + DeviceTypeConstants.SMART_WATER_METER);
        waterMeter.setParentId(uid);
        waterMeter.setType(DeviceInfoConstant.IS_NOT_DEVICE);

        ProjectDeviceSelectTreeVo smartStreetLight = new ProjectDeviceSelectTreeVo();
        smartStreetLight.setName("智能路灯");
        smartStreetLight.setId("type:" + DeviceTypeConstants.SMART_STREET_LIGHT);
        smartStreetLight.setParentId(uid);
        smartStreetLight.setType(DeviceInfoConstant.IS_NOT_DEVICE);


        ProjectDeviceSelectTreeVo deviceDrivers = new ProjectDeviceSelectTreeVo();
        deviceDrivers.setName("设备服务引擎");
        deviceDrivers.setId("type:" + DeviceTypeConstants.DEVICE_DRIVER);
        deviceDrivers.setParentId(uid);
        deviceDrivers.setType(DeviceInfoConstant.IS_NOT_DEVICE);
        /*  20230704 V1.3.0.0（人行版）去除车场相关信息
        ProjectDeviceSelectTreeVo vehicleRoadGateDevice = new ProjectDeviceSelectTreeVo();
        vehicleRoadGateDevice.setName("车道一体机");
        vehicleRoadGateDevice.setId("type:" + DeviceTypeConstants.VEHICLE_BARRIER_DEVICE);
        vehicleRoadGateDevice.setParentId(uid);
        vehicleRoadGateDevice.setType(DeviceInfoConstant.IS_DEVICE);*/

        projectDeviceSelectTreeVoList.add(accessControlSystem);
        projectDeviceSelectTreeVoList.add(deviceDrivers);
//        projectDeviceSelectTreeVoList.add(levelGauge);
//        projectDeviceSelectTreeVoList.add(smartManholeCover);
//        projectDeviceSelectTreeVoList.add(waterMeter);
//        projectDeviceSelectTreeVoList.add(smartStreetLight);
        // 20230704 V1.3.0.0（人行版）去除车场相关信息
//        projectDeviceSelectTreeVoList.add(vehicleRoadGateDevice);
        if (CollUtil.isNotEmpty(sysDeviceProductMaps)) {
            projectDeviceSelectTreeVoList.addAll(getProductTree(sysDeviceProductMaps));
        }
        //构造结构树
        return R.ok(TreeUtil.build(projectDeviceSelectTreeVoList, uid));
    }

    private List<ProjectDeviceSelectTreeVo> getProductTree(List<SysDeviceProductMapVo> sysDeviceProductMapsList) {
        Map<String, String> productRepeatCheckMap = new HashMap<>();
        List<ProjectDeviceSelectTreeVo> treeList = sysDeviceProductMapsList.stream()
                .map(item -> {
                    ProjectDeviceSelectTreeVo node = new ProjectDeviceSelectTreeVo();
                    node.setId(item.getProductId());
                    switch (item.getDeviceType()) {
                        case DeviceTypeConstants.LADDER_WAY_DEVICE:
                        case DeviceTypeConstants.GATE_DEVICE:
                        case DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE:
                            if (productRepeatCheckMap.get(item.getProductId()) == null) {
                                node.setParentId("type:" + DeviceTypeConstants.LADDER_WAY_DEVICE + "-" + DeviceTypeConstants.GATE_DEVICE + "-" + DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE);
                                productRepeatCheckMap.put(item.getProductId(), "");
                            }
                            break;
                        default:
                            node.setParentId("type:" + item.getDeviceType());
                    }
                    node.setName(item.getProductName());
                    node.setType(DeviceInfoConstant.IS_DEVICE);
                    return node;
                }).collect(Collectors.toList());
        return treeList;
    }
}
