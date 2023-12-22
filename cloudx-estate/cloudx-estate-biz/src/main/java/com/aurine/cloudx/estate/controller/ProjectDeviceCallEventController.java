package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.log.annotation.ProjSysLog;
import com.aurine.cloudx.estate.service.ProjectDeviceCallEventService;
import com.aurine.cloudx.estate.vo.ProjectDeviceCallEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 项目呼叫事件(ProjectDeviceCallEvent)Controller
 *
 * @author : Qiu
 * @date : 2020 12 16 11:41
 */
@RestController
@RequestMapping("/projectDeviceCallEvent")
@Api(value = "projectDeviceCallEvent", tags = "项目呼叫事件")
public class ProjectDeviceCallEventController {

    @Resource
    private ProjectDeviceCallEventService projectDeviceCallEventService;

    @GetMapping(value = "/pageCallEvent")
    @ProjSysLog(value = "项目呼叫事件 - 分页查询")
    @ApiOperation(value = "分页查询项目呼叫事件", notes = "分页查询项目呼叫事件")
    public R pageCallEvent(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        return R.ok(projectDeviceCallEventService.pageCallEvent(page, projectDeviceCallEventVo));
    }

    @GetMapping(value = "/pageCallEventByProject")
    @ProjSysLog(value = "项目呼叫事件 - 分页查询项目呼叫事件(查询当前登录用户所在项目的呼叫记录)")
    @ApiOperation(value = "分页查询项目呼叫事件(查询当前登录用户所在项目的呼叫记录)", notes = "分页查询项目呼叫事件(查询当前登录用户所在项目的呼叫记录)")
    public R pageCallEventByProject(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        return R.ok(projectDeviceCallEventService.pageCallEventByProject(page, projectDeviceCallEventVo));
    }

    @GetMapping(value = "/pageCallEventByStaff")
    @ProjSysLog(value = "项目呼叫事件 - 分页查询呼叫记录(查询当前登录用户的员工ID为接收方的呼叫记录)")
    @ApiOperation(value = "分页查询呼叫记录(查询当前登录用户的员工ID为接收方的呼叫记录)", notes = "分页查询呼叫记录(查询当前登录用户的员工ID为接收方的呼叫记录)")
    public R pageCallEventByStaff(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        return R.ok(projectDeviceCallEventService.pageCallEventByStaff(page, projectDeviceCallEventVo));
    }

    @PostMapping(value = "/saveCallEvent")
    @ApiOperation(value = "添加呼叫记录,针对室内机,中心机,住户,管家的", notes = "添加呼叫记录,针对室内机,中心机,住户,管家的")
    @Inner(value = false)
    public R saveCallEvent(@RequestBody JSONObject jsonObject){
        return projectDeviceCallEventService.saveCallEvent(jsonObject);
    }
}
