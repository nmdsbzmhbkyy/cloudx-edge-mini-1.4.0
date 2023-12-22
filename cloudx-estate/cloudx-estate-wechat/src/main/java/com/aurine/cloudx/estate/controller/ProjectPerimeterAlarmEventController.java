package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.dto.ProjectBillingInfoFormDTO;
import com.aurine.cloudx.estate.feign.RemoteBillingInfoService;
import com.aurine.cloudx.estate.feign.RemotePayingService;
import com.aurine.cloudx.estate.feign.RemotePaymentRecordService;
import com.aurine.cloudx.estate.feign.RemotePerimeterAlarmEventService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 安防预紧
 *
 * @author yz
 * @since 2021-07-8 16:43:48
 */
@RestController
@RequestMapping("perimeterAlarmEvent")
@Api(value = "perimeterAlarmEvent", tags = "安防预警")
public class ProjectPerimeterAlarmEventController {


    @Resource
    private RemotePerimeterAlarmEventService remotePerimeterAlarmEventService;


    /**
     * 分页查询
     *
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
    })
    public R<Page<ProjectPerimeterAlarmEventVo>> findPage(AppPerimeterAlarmEventFromVo appPerimeterAlarmEventFromVo) {

        return remotePerimeterAlarmEventService.findAll(appPerimeterAlarmEventFromVo);
    }

    @ApiOperation(value = "消除报警", notes = "消除报警")
    @DeleteMapping("projectPerimeterAlarmEvent/{eventId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "eventId", value = "报警id", paramType = "path"),
    })
    public R deleteByEventId(@PathVariable("eventId") String eventId) {

        return remotePerimeterAlarmEventService.deleteByEventId(eventId);
    }
    @ApiOperation(value = "获取视频流", notes = "获取视频流")
    @GetMapping("/getLiveUrl/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "报警主机设备id", paramType = "path"),
    })
   public R<String> getLiveUrl(@PathVariable("deviceId") String deviceId){
        return remotePerimeterAlarmEventService.getLiveUrl(deviceId);
    }
    @ApiOperation(value = "获取平面图", notes = "获取平面图")
    @GetMapping("/baseFloorPlan/findList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "报警主机设备id", paramType = "path"),
    })
    public R findList(AppFloorPicSearchConditionVo appFloorPicSearchConditionVo){
        return remotePerimeterAlarmEventService.findList(appFloorPicSearchConditionVo);
    }

    @ApiOperation(value = "获取设备列表", notes = "获取设备列表")
    @GetMapping("/getMonitoring")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "报警主机设备id", paramType = "path"),
    })
    public R getMonitoring(@RequestParam("deviceId") String deviceId) {

        return remotePerimeterAlarmEventService.getMonitoring(deviceId);
    }

    @ApiOperation(value = "获取设备打点的平面图", notes = "获取设备打点的平面图")
    @GetMapping("/baseFloorPlan/getPicLocation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "报警主机设备id", paramType = "path"),
    })
    public R<Page<ProjectFloorPicVo>> getPicLocation(AppFloorPicSearchConditionVo appFloorPicSearchConditionVo) {
        return remotePerimeterAlarmEventService.getPicLocation(appFloorPicSearchConditionVo);
    }

}