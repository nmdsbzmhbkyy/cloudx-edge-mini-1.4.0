package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.service.ProjectDeviceModifyLogService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备修改记录表
 *
 * @author 邹宇
 * @date 2021-9-26 16:05:25
 */
@RestController
@RequestMapping("/projectDeviceModifyLog")
@Api(value = "projectDeviceModifyLog", tags = "设备修改记录表")
public class ProjectDeviceModifyLogController {

    @Resource
    private ProjectDeviceModifyLogService projectDeviceModifyLogService;


    @ApiOperation(value = "根据设备ID查询更新记录", notes = "根据设备ID查询更新记录")
    @SysLog("根据设备ID查询更新记录")
    @GetMapping("/getUpdateRecordByDeviceid/{deviceId}/{count}")
    public R getUpdateRecordByDeviceid(@PathVariable String deviceId,@PathVariable Integer count) {
        return R.ok(projectDeviceModifyLogService.getUpdateRecordByDeviceId(deviceId,count));
    }

    @ApiOperation(value = "校验设备ip4和设备编号以及mac", notes = "校验设备ip4和设备编号以及mac")
    @SysLog("校验设备ip4和设备编号以及mac")
    @GetMapping("/checkDeviceIp4AndDeviceCode/{deviceId}")
    public R checkDeviceIpv4AndDeviceCode(@PathVariable String deviceId) {
        return R.ok(projectDeviceModifyLogService.checkDeviceIp4AndDeviceCode(deviceId));
    }

    @ApiOperation(value = "添加设备时校验参数", notes = "添加设备时校验参数")
    @SysLog("添加设备时校验参数")
    @GetMapping("/checkDeviceParam/{ipv4}/{deviceCode}/{mac}/{deviceId}")
    public R checkDeviceParam(@PathVariable("ipv4") String ipv4,@PathVariable("deviceCode") String deviceCode,@PathVariable("mac") String mac,@PathVariable("deviceId") String deviceId) {
        return projectDeviceModifyLogService.checkDeviceParam(ipv4,deviceCode,mac,deviceId);
    }
    @ApiOperation(value = "添加设备时校验参数", notes = "添加设备时校验参数")
    @SysLog("添加设备时校验参数")
    @GetMapping("/checkDeviceParam/{ipv4}/{deviceCode}/{mac}/{deviceId}/{thirdpartyCode}")
    public R checkDeviceParam(@PathVariable("ipv4") String ipv4,@PathVariable("deviceCode") String deviceCode,@PathVariable("mac") String mac,@PathVariable("deviceId") String deviceId,@PathVariable("thirdpartyCode") String thirdpartyCode) {
        return projectDeviceModifyLogService.checkDeviceParam(ipv4,deviceCode,mac,deviceId,thirdpartyCode);
    }
}
