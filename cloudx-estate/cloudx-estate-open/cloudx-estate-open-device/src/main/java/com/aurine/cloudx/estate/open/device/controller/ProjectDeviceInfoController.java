package com.aurine.cloudx.estate.open.device.controller;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.estate.open.device.bean.ProjectDeviceInfoGateVo;
import com.aurine.cloudx.estate.open.device.bean.ProjectDeviceInfoLadderVo;
import com.aurine.cloudx.estate.open.device.bean.ProjectDeviceSearchFormVo;
import com.aurine.cloudx.estate.open.device.fegin.RemoteProjectDeviceInfoService;
import com.aurine.cloudx.estate.vo.DeviceAdminPassword;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoPageVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/devices")
@Api(value = "devices", tags = "设备管理")
public class ProjectDeviceInfoController {

    private RemoteProjectDeviceInfoService remoteProjectDeviceInfoService;

    /**
     * 区口机分页查询
     *
     * @return
     */
    @ApiOperation(value = "区口机分页查询", notes = "区口机分页查询")
    @GetMapping("/gate/page")
    @PreAuthorize("@pms.hasPermission('device:get:gate-page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectDeviceInfoPageVo>> getProjectDeviceGateInfoPage(ProjectDeviceSearchFormVo page) {
        page.setDeviceTypeId("3");
        return remoteProjectDeviceInfoService.getProjectDeviceInfoPage(page);
    }

    /**
     * 梯口机分页查询
     *
     * @return
     */
    @ApiOperation(value = "梯口机分页查询", notes = "梯口机分页查询")
    @GetMapping("/ladder/page")
    @PreAuthorize("@pms.hasPermission('device:get:ladder-page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectDeviceInfoPageVo>> getProjectDeviceLadderInfoPage(ProjectDeviceSearchFormVo page) {
        page.setDeviceTypeId("2");
        return remoteProjectDeviceInfoService.getProjectDeviceInfoPage(page);
    }

    /**
     * 监控设备分页查询
     *
     * @return
     */
    @ApiOperation(value = "监控设备分页查询", notes = "监控设备分页查询")
    @GetMapping("/monitor/page")
    @PreAuthorize("@pms.hasPermission('device:get:monitor-page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectDeviceInfoPageVo>> getProjectDeviceMonitorInfoPage(ProjectDeviceSearchFormVo page) {
        page.setDeviceTypeId("6");
        return remoteProjectDeviceInfoService.getProjectDeviceInfoPage(page);
    }



    /**
     * 通过id查询设备信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('device:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "设备ID", required = true, paramType = "path")
    })
    public R<ProjectDeviceInfoVo> getById(@PathVariable("id") String id) {
        return remoteProjectDeviceInfoService.getById(id);
    }


    /**
     * 新增区口设备
     *
     * @param projectDeviceInfo 新增区口设备
     * @return R
     */
    @ApiOperation(value = "新增区口设备", notes = "新增区口设备")
    @SysLog("新增区口设备")
    @PostMapping("/gate")
    @PreAuthorize("@pms.hasPermission('device:post:gate-save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R saveGate(@RequestBody @Valid ProjectDeviceInfoGateVo projectDeviceInfo) {
        projectDeviceInfo.setDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode());
        //TODO：设置为公共区域
        return remoteProjectDeviceInfoService.save(projectDeviceInfo);
    }


    /**
     * 修改区口设备信息
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @ApiOperation(value = "修改区口设备信息", notes = "修改区口设备信息")
    @SysLog("修改设备信息表")
    @PutMapping("/gate")
    @PreAuthorize("@pms.hasPermission('device:put:gate-info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateGateById(@RequestBody @Valid ProjectDeviceInfoGateVo projectDeviceInfo) {
        projectDeviceInfo.setDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode());
        return remoteProjectDeviceInfoService.updateById(projectDeviceInfo);
    }



    /**
     * 新增梯口设备
     *
     * @param projectDeviceInfo 新增梯口设备
     * @return R
     */
    @ApiOperation(value = "新增梯口设备", notes = "新增梯口设备")
    @SysLog("新增梯口设备")
    @PostMapping("/ladder")
    @PreAuthorize("@pms.hasPermission('device:post:ladder-save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R saveLadder(@RequestBody @Valid ProjectDeviceInfoLadderVo projectDeviceInfo) {
        projectDeviceInfo.setDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode());
        //TODO：设置为公共区域
        return remoteProjectDeviceInfoService.save(projectDeviceInfo);
    }


    /**
     * 修改设备信息表
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @ApiOperation(value = "修改梯口设备信息", notes = "修改梯口设备信息")
    @SysLog("修改梯口设备信息")
    @PutMapping("/ladder")
    @PreAuthorize("@pms.hasPermission('device:put:ladder-info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateLadderById(@RequestBody @Valid ProjectDeviceInfoLadderVo projectDeviceInfo) {
        projectDeviceInfo.setDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode());
        return remoteProjectDeviceInfoService.updateById(projectDeviceInfo);
    }


//    /**
//     * 新增监控设备 监控设备由于扩展属性字段问题，不考虑使用api添加
//     *
//     * @param projectDeviceInfo 新增监控设备
//     * @return R
//     */
//    @ApiOperation(value = "新增监控设备", notes = "新增监控设备")
//    @SysLog("新增监控设备")
//    @PostMapping("/monitor")
//    @PreAuthorize("@pms.hasPermission('device:post:monitor-save')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
//    })
//    public R saveMonitor(@RequestBody @Valid ProjectDeviceInfoMonitorVo projectDeviceInfo) {
//        projectDeviceInfo.setDeviceType(DeviceTypeEnum.MONITOR_DEVICE.getCode());
//        //设备扩展属性转换
//        List<ProjectDeviceAttrListVo> deviceAttrs = new ArrayList<>();
//        projectDeviceInfo.setDeviceAttrs(deviceAttrs);
//
//        //TODO：设置为公共区域
//        return remoteProjectDeviceInfoService.save(projectDeviceInfo);
//    }

//

//    /**
//     * 修改监控设备信息表
//     *
//     * @param projectDeviceInfo 设备信息表
//     * @return R
//     */
//    @ApiOperation(value = "修改监控设备信息", notes = "修改监控设备信息")
//    @SysLog("修改监控设备信息")
//    @PutMapping("/monitor")
//    @PreAuthorize("@pms.hasPermission('device:put:monitor-info')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
//    })
//    public R updateMonitorById(@RequestBody @Valid ProjectDeviceInfoMonitorVo projectDeviceInfo) {
//        projectDeviceInfo.setDeviceType(DeviceTypeEnum.MONITOR_DEVICE.getCode());
//        return remoteProjectDeviceInfoService.updateById(projectDeviceInfo);
//    }
    /**
     * 通过id删除设备信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备信息表", notes = "通过id删除设备信息表")
    @SysLog("通过id删除设备信息表")
    @PreAuthorize("@pms.hasPermission('device:delete:info')")
    @DeleteMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        return remoteProjectDeviceInfoService.removeById(id);
    }
//
//    /**
//     * 通过id批量删除设备信息表
//     *
//     * @param ids ids
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除设备信息表", notes = "通过id删除设备信息表")
//    @SysLog("通过id删除设备信息表")
//    @DeleteMapping("/removeAll")
//    @PreAuthorize("@pms.hasPermission('estate_device_del')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
//    })
//    public R removeById(@RequestBody List<String> ids) {
//        return remoteProjectDeviceInfoService.removeById(ids);
//    }


    @ApiOperation("开门")
    @SysLog("开门")
    @GetMapping("/open/{id}")
    @PreAuthorize("@pms.hasPermission('device:get:open')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R open(@PathVariable("id") String id) {
        return remoteProjectDeviceInfoService.open(id);
    }

    @ApiOperation("用户开门")
    @SysLog("用户开门")
    @GetMapping("/open-by-person/{id}/{personType}/{personId}")
    @PreAuthorize("@pms.hasPermission('device:get:open-by-person')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personType", value = "用户类型,1=住户，2=员工，3=访客", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personId", value = "人员的UUID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R openByPerson(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId) {
        return remoteProjectDeviceInfoService.openByPerson(id, personType, personId);
    }

    @ApiOperation("重启")
    @SysLog("重启")
    @GetMapping("/restart/{id}")
    @PreAuthorize("@pms.hasPermission('device:get:restart')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R restart(@PathVariable("id") String id) {
        return remoteProjectDeviceInfoService.restart(id);
    }

    @ApiOperation("恢复出厂")
    @SysLog("恢复出厂")
    @GetMapping("/reset/{id}")
    @PreAuthorize("@pms.hasPermission('device:get:reset')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> reset(@PathVariable("id") String id) {
        return remoteProjectDeviceInfoService.reset(id);
    }

    @ApiOperation("设置管理员密码")
    @SysLog("设置管理员密码")
    @PostMapping("/setAdminPwd")
    @PreAuthorize("@pms.hasPermission('device:post:set-admin-pwd')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> setAdminPwd(@RequestBody DeviceAdminPassword adminPassword) {
        return remoteProjectDeviceInfoService.setAdminPwd(adminPassword);
    }


    /**
     * 获取视频直播流的地址
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "获取视频直播流的地址", notes = "获取视频直播流的地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PreAuthorize("@pms.hasPermission('device:get:getLiveUrl')")
    @GetMapping("/getLiveUrl/{deviceId}")
    public R<String> getLiveUrl(@PathVariable("deviceId") String deviceId) {
        return remoteProjectDeviceInfoService.getLiveUrl(deviceId);
    }

    /**
     * 获取视频录播流的地址
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "获取视频录播流的地址", notes = "获取视频录播流的地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "path", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PreAuthorize("@pms.hasPermission('device:get:getVideoUrl')")
    @GetMapping("/getVideoUrl/{deviceId}/{startTime}/{endTime}")
    public R<String> getVideoUrl(@PathVariable("deviceId") String deviceId, @PathVariable("startTime") Long startTime, @PathVariable("endTime") Long endTime) {
        return remoteProjectDeviceInfoService.getVideoUrl(deviceId, startTime, endTime);
    }

}
