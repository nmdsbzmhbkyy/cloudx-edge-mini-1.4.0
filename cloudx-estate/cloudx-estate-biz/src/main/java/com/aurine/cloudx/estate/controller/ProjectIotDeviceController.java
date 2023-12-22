package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.IotEventCallback;
import com.aurine.cloudx.estate.service.ProjectDeviceEventCallbackService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 物联网设备事件控制器
 * </p>
 * @author : 王良俊
 * @date : 2021-07-20 10:25:31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectIotDevice")
@Api(value = "projectIotDevice", tags = "物联网设备")
public class ProjectIotDeviceController {

    @Resource
    ProjectDeviceEventCallbackService projectDeviceEventCallbackService;

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<IotEventCallback>> getProjectMediaAdPage(IotDevicePage page) {
        return R.ok(projectDeviceEventCallbackService.page(page.getDeviceId(), page.getPage(), page.getSize(), IotEventCallback.class));
    }


    @GetMapping("/exportExcel/{deviceId}/{deviceName}")
    @ApiOperation(value = "导出excel", notes = "导出excel")
    @Inner(false)
    public void download(@PathVariable String deviceId, @PathVariable String deviceName, HttpServletResponse httpServletResponse) {
        projectDeviceEventCallbackService.exportExcel(deviceId, deviceName, httpServletResponse);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class IotDevicePage {
        int page;
        int size;
        String deviceId;
    }
}
