package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.SysEventTypeConf;
import com.aurine.cloudx.estate.entity.SysExpertPlanEventTypeRel;
import com.aurine.cloudx.estate.service.SysEventTypeConfService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报警类型定义管理
 *
 * @author 谢泽毅
 * @date 2021-07-08 08:49:33
 */

@RestController
@AllArgsConstructor
@RequestMapping("/sysAlarmType" )
@Api(value = "sysAlarmType", tags = "报警类型定义管理")
public class SysEventTypeConfController {

    private final SysEventTypeConfService sysEventTypeConfService;

    /**
     * 通过设备类型(deviceType)和报警类型名称(eventTypeName)获取报警类型定义列表(sys_event_type_conf)
     * @param page
     * @param sysEventTypeConf
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<SysEventTypeConf>> getAlarmTypePage(Page page, SysEventTypeConf sysEventTypeConf) {
        return R.ok(sysEventTypeConfService.pageAlarmType(page, sysEventTypeConf));
    }


    /**
     *
     * @param sysEventTypeConf
     * @return
     */
    @ApiOperation(value = "修改", notes = "修改")
    @PutMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateById(SysEventTypeConf sysEventTypeConf) {
        return sysEventTypeConfService.updateAlarmType(sysEventTypeConf);
    }


    @ApiOperation(value = "根据预案id分页查询", notes = "根据预案id分页查询")
    @GetMapping("/sysAlarmTypeByPlanId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R< Page<SysEventTypeConf>> getAlarmTypeByPlanId(Page page, SysExpertPlanEventTypeRel sysExpertPlanEventTypeRel) {
        return R.ok(sysEventTypeConfService.pageAlarmTypeByPlanId(page, sysExpertPlanEventTypeRel));
    }

}
