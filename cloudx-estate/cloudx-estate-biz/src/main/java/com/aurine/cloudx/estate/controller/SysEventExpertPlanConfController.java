package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import com.aurine.cloudx.estate.service.SysEventExpertPlanConfService;
import com.aurine.cloudx.estate.vo.SysEventExpertPlanConfVo;
import com.aurine.cloudx.estate.vo.SysEventTypeConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报警类型定义管理
 *
 * @author 谢泽毅
 * @date 2021-07-09 09:05:22
 */

@RestController
@AllArgsConstructor
@RequestMapping("/sysExpertPlan" )
@Api(value = "sysExpertPlan", tags = "预案管理")
public class SysEventExpertPlanConfController {

    private final SysEventExpertPlanConfService sysEventExpertPlanConfService;

    /**
     * 新增预案
     * @param sysEventExpertPlanConf
     * @return
     */
    @ApiOperation(value = "新增预案", notes = "新增预案")
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody SysEventExpertPlanConf sysEventExpertPlanConf) {
        return sysEventExpertPlanConfService.saveReturnId(sysEventExpertPlanConf);
    }


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<SysEventExpertPlanConfVo>> getExpertPlanPage(SysEventExpertPlanConfVo sysEventExpertPlanConfVo, Page page) {
        return R.ok(sysEventExpertPlanConfService.pageExpertPlan(sysEventExpertPlanConfVo,page));
    }

    @ApiOperation(value = "预案设备关联展示", notes = "预案设备关联展示")
    @GetMapping("/planEventRel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<SysEventTypeConfVo>> getExpertPlanEventTypeRelList(SysEventTypeConfVo sysEventTypeConfVo) {
        return R.ok(sysEventExpertPlanConfService.getExpertPlanEventTypeRelList(sysEventTypeConfVo));
    }


    @ApiOperation(value = "预案内容展示", notes = "预案内容展示")
    @GetMapping("/sysAlarmTypeByPlanId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<SysEventExpertPlanConf> getExpertPlanContentById(SysEventExpertPlanConf sysEventExpertPlanConf) {
        return R.ok(sysEventExpertPlanConfService.getExpertPlanContentById(sysEventExpertPlanConf));
    }

    @ApiOperation(value = "通过id删除预案", notes = "通过id删除预案")
    @DeleteMapping("/{planId}")
    public R<Boolean> removeById(@PathVariable String planId) {
        return R.ok(sysEventExpertPlanConfService.removeByPlanId(planId));
    }

    @ApiOperation(value = "通过预案id修改预案信息", notes = "通过预案id修改预案信息")
    @PutMapping("/planInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updataExpertPlan(SysEventExpertPlanConf sysEventExpertPlanConf) {
        return sysEventExpertPlanConfService.updataExpertPlanById(sysEventExpertPlanConf);
    }

    @ApiOperation(value = "通过事件类型id(eventTypeId)获取关联的预案", notes = "通过事件类型id(eventTypeId)获取关联的预案")
    @GetMapping("/planInfoList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<SysEventExpertPlanConf>> getExpertPlanListByEventTypeId(String eventTypeId) {
        return R.ok(sysEventExpertPlanConfService.getExpertPlanListByEventTypeId(eventTypeId));
    }

}
