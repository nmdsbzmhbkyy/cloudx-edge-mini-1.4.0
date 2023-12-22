package com.aurine.cloudx.estate.open.device.controller;

import com.aurine.cloudx.estate.open.device.fegin.RemoteProjectRightDeviceService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rightDevice")
@Api(value = "rightDevice", tags = "设备维护")
public class ProjectRightDeviceController {

    @Resource
    private RemoteProjectRightDeviceService projectRightDeviceService;

    /**
     * 根据介质类型重新下载该设备上这个类型的介质
     *
     * @param deviceId 要操作的设备ID
     * @param certType 介质类型
     * @return R
     */
    @ApiOperation(value = "根据介质类型重新下载该设备上这个类型的介质", notes = "根据介质类型重新下载该设备上这个类型的介质")
    @SysLog("根据介质类型重新下载该设备上这个类型的介质")
    @GetMapping("/reDownloadCertByType/{deviceId}/{certType}")
    @PreAuthorize("@pms.hasPermission('rightDevice:get:reDownloadCertByType')")
    public R reDownloadCertByType(@PathVariable("deviceId") String deviceId, @PathVariable("certType") String certType) {
        return projectRightDeviceService.reDownloadCertByType(deviceId, certType);
    }

    /**
     * 根据介质类型清空该设备上这个类型的介质
     *
     * @param deviceId 要操作的设备ID
     * @param certType 介质类型
     * @return R
     */
    @ApiOperation(value = "根据介质类型清空该设备上这个类型的介质", notes = "根据介质类型清空该设备上这个类型的介质")
    @SysLog("根据介质类型清空该设备上这个类型的介质")
    @GetMapping("/clearCertByType/{deviceId}/{certType}")
    @PreAuthorize("@pms.hasPermission('rightDevice:get:clearCertByType')")
    public R clearCertByType(@PathVariable("deviceId") String deviceId, @PathVariable("certType") String certType) {
        return projectRightDeviceService.clearCertByType(deviceId,certType);
    }


    /**
     * 根据deviceId重新下载失败凭证
     *
     * @param deviceIdList 设备权限ID列表
     * @return
     */
    @PostMapping("/resentFailCert")
    @PreAuthorize("@pms.hasPermission('rightDevice:post:resentFailCert')")
    public R resendFailCert(@RequestBody List<String> deviceIdList) {
       return projectRightDeviceService.resendFailCert(deviceIdList);
    }

    /**
     * 重新下发
     *
     * @param rightDeviceIdList 设备权限ID列表
     * @return
     */
    @ApiOperation(value = "重新下发", notes = "重新下发")
    @PostMapping("/resendBatch")
    @PreAuthorize("@pms.hasPermission('rightDevice:post:resendBatch')")
    public R resendBatch(@RequestBody List<String> rightDeviceIdList) {
       return projectRightDeviceService.resendBatch(rightDeviceIdList);
    }
}
