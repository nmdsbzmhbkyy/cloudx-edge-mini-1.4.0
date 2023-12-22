package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.SysExpertPlanEventTypeRelService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 谢泽毅
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysExpertPlanEventTypeRel" )
@Api(value = "sysExpertPlanEventTypeRel", tags = "预案关联事件类型")
public class SysExpertPlanEventTypeRelController {

    private final SysExpertPlanEventTypeRelService sysExpertPlanEventTypeRelService;

    @ApiOperation(value = "预案设备关联修改", notes = "预案设备关联修改")
    @PutMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateExpertPlanEventTypeRel(String deviceType, String eventTypeList, String planId) {
        return sysExpertPlanEventTypeRelService.updateExpertPlanEventTypeRel(deviceType,eventTypeList,planId);
    }
}
