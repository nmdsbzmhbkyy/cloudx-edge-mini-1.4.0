package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectPersonDeviceDto;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonDeviceService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/06/28 14:31
 * @Package: com.aurine.cloudx.estate.controller
 * @Version: 1.0
 * @Remarks: openApi内部调用方法，解决分布式事务
 **/
@RestController
@RequestMapping("/open/pass/inner")
@Api(value = "openApiProjectPersonDevice", tags = "openApi项目通信权限管理", hidden = true)
public class OpenApiProjectPersonDeviceController {

    @Resource
    private OpenApiProjectPersonDeviceService openApiProjectPersonDeviceService;

    /**
     * 员工通行权限新增
     *
     * @param openApiProjectPersonDeviceDto
     * @return R
     */
    @ApiOperation(value = "员工通行权限新增/更新", notes = "指定员工Id列表，设备id列表，新增/更新在这些设备的通行权限")
    @SysLog("员工通行权限新增/更新")
    @PostMapping("/staff")
    @Inner
    public R<List<OpenApiProjectPersonDeviceDto>> saveStaffPass(@RequestBody OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {
        try {
            return openApiProjectPersonDeviceService.staffSaveList(openApiProjectPersonDeviceDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 员工通行权限删除
     *
     * @param openApiProjectPersonDeviceDto
     * @return R
     */
    @ApiOperation(value = "员工通行权限删除", notes = "指定员工Id列表、设备id列表，删除在这些设备的通行权限，通行方案自带的设备不允许被删除")
    @SysLog("员工通行权限删除")
    @DeleteMapping("/staff")
    @Inner
    public R<List<String>> removeStaffPass(@RequestBody OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {
        try {
            return openApiProjectPersonDeviceService.staffRemoveList(openApiProjectPersonDeviceDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 住户通行权限新增
     *
     * @param openApiProjectPersonDeviceDto
     * @return R
     */
    @ApiOperation(value = "住户通行权限新增/更新", notes = "指定住户Id列表，设备id列表，新增/更新在这些设备的通行权限")
    @SysLog("住户通行权限新增/更新")
    @PostMapping("/household")
    @Inner
    public R<List<OpenApiProjectPersonDeviceDto>> saveHouseholdPass(@RequestBody OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {
        try {
            return openApiProjectPersonDeviceService.proprietorSaveList(openApiProjectPersonDeviceDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 住户通行权限删除
     *
     * @param openApiProjectPersonDeviceDto
     * @return R
     */
    @ApiOperation(value = "住户通行权限删除", notes = "指定住户Id列表、设备id列表，删除在这些设备的通行权限，通行方案自带的设备不允许被删除")
    @SysLog("住户通行权限删除")
    @DeleteMapping("/household")
    @Inner
    public R<List<String>> removeHouseholdPass(@RequestBody OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {
        try {
            return openApiProjectPersonDeviceService.proprietorRemoveList(openApiProjectPersonDeviceDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }
}
