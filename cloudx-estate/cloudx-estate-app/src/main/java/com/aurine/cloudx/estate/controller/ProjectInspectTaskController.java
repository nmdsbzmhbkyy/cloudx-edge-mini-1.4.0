package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.utils.StringUtils;
import com.aurine.cloudx.estate.entity.ProjectInspectCheckinDetail;
import com.aurine.cloudx.estate.entity.ProjectInspectDetailCheckItem;
import com.aurine.cloudx.estate.entity.ProjectInspectTaskDetail;
import com.aurine.cloudx.estate.service.ProjectInspectDetailCheckItemService;
import com.aurine.cloudx.estate.service.ProjectInspectDetailDeviceService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskDetailService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.aurine.cloudx.estate.service.impl.ProjectStaffServiceImpl;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * (ProjectInspectTaskController)设备巡检任务
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/23 16:40
 */
@RestController
@RequestMapping("inspect")
@Api(value = "inspect", tags = "设备巡检任务")
public class ProjectInspectTaskController {

    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectTaskService projectInspectTaskService;

    @Resource
    private ProjectStaffServiceImpl projectStaffService;


    @Resource
    private ProjectInspectTaskDetailService projectInspectTaskDetailService;

    @Resource
    private ProjectInspectDetailDeviceService projectInspectDetailDeviceService;

    @Resource
    private ProjectInspectDetailCheckItemService projectInspectDetailCheckItemService;


    @Resource
    private ImgConvertUtil imgConvertUtil;

    /**
     * 分页查询所有数据
     *
     * @param page  分页对象
     * @param query 查询vo对象
     * @return 所有数据
     */
    @GetMapping("/page")
    @SysLog("分页查询任务数据")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectTask所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    public R<Page<ProjectInspectTaskPageVo>> selectAll(Page page, ProjectInspectTaskSearchConditionVo query) {
        // 每次查询都检查是否超时并更新状态
        projectInspectTaskService.dealTimeOut();
        return R.ok(this.projectInspectTaskService.fetchList(page, query));
    }

    /**
     * 分页查询待我认领的巡检任务
     *
     * @param page 分页对象
     * @return 所有数据
     */
    @GetMapping("/claim/page")
    @SysLog("分页查询待我认领的巡检任务")
    @ApiOperation(value = "分页查询待我认领的巡检任务", notes = "分页查询待我认领的巡检任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
    })
    public R<Page<ProjectInspectTaskPageVo>> selectForMe(Page page) {
        projectInspectTaskService.dealTimeOut();
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(this.projectInspectTaskService.selectForMe(page, projectStaffVo.getStaffId()));
    }

    /**
     * 分页查询待我执行的巡检任务
     *
     * @param page 分页对象
     * @return 所有数据
     */
    @GetMapping("/execute/page")
    @SysLog("分页查询待我执行的巡检任务")
    @ApiOperation(value = "分页查询待我执行的巡检任务", notes = "分页查询待我执行的巡检任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    public R<Page<ProjectInspectTaskPageVo>> selectToDo(Page page) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectInspectTaskService.dealTimeOut();
        return R.ok(this.projectInspectTaskService.selectToDo(page, projectStaffVo.getStaffId()));
    }

    /**
     * 分页查询我完成的巡检任务
     *
     * @param page 分页对象
     * @return 所有数据
     */
    @GetMapping("/complete/page")
    @SysLog("分页查询我完成的巡检任务")
    @ApiOperation(value = "分页查询我完成的巡检任务", notes = "分页查询我完成的巡检任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    public R<Page<ProjectInspectTaskPageVo>> selecDatetToDo(Page page, String date) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectInspectTaskService.dealTimeOut();
        return R.ok(this.projectInspectTaskService.selectDateToDo(page, projectStaffVo.getStaffId(), date));
    }


    /**
     * 认领任务
     *
     * @return
     */
    @PutMapping("/claim/{taskId}")
    @ApiOperation(value = "认领任务", notes = "认领任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "taskId", value = "任务id", paramType = "path")
    })
    public R claim(@PathVariable("taskId") String id) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        this.projectInspectTaskService.claimTask(id, projectStaffVo.getStaffId());
        return R.ok();
    }

    /**
     * 通过主键查询单条数据
     *
     * @param taskId 主键
     * @return 单条数据
     */
    @GetMapping("{taskId}")
    @SysLog("获取任务数据")
    @ApiOperation(value = "通过id查询任务详情", notes = "通过id查询任务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<ProjectInspectTaskAndDetailVo> selectOne(@PathVariable("taskId") String taskId) {

        return R.ok(this.projectInspectTaskService.getTaskAndDetailById(taskId));
    }


    /**
     * 根据明细id查询设备列表
     *
     * @param detailId 巡检任务明细主键
     */
    @GetMapping("device/{detailId}")
    @SysLog("根据明细id查询设备列表")
    @ApiOperation(value = "根据明细id查询设备列表", notes = "根据明细id查询设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "明细Id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<List<ProjectInspectDetailDeviceVo>> listByDetailId(@PathVariable String detailId) {
        return R.ok(this.projectInspectDetailDeviceService.listDetailDeviceByDetailId(detailId));
    }

    /**
     * 根据设备明细id获取到该设备的检查项
     *
     * @param deviceDetailId 设备明细id
     */
    @GetMapping("check-item/{deviceDetailId}")
    @SysLog("根据明细设备id获取设备检查项列表")
    @ApiOperation(value = "根据设备明细id获取设备检查项列表", notes = "根据设备明细id获取设备检查项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "deviceDetailId", value = "设备明细Id", required = true, paramType = "path"),
    })
    public R<List<ProjectInspectDetailCheckItem>> listByDeviceDetailId(@PathVariable("deviceDetailId") String deviceDetailId) {
        List<ProjectInspectDetailCheckItem> list = this.projectInspectDetailCheckItemService.list(new QueryWrapper<ProjectInspectDetailCheckItem>().lambda()
                .eq(ProjectInspectDetailCheckItem::getDeviceDetailId, deviceDetailId));
        return R.ok(list);
    }


    /**
     * 根据任务明细id获取签到方式
     *
     * @param detailId 巡检任务主键
     */
    @GetMapping("sign-type/{detailId}")
    @SysLog("根据任务明细id获取签到方式")
    @ApiOperation(value = "根据任务明细id获取签到方式", notes = "根据任务明细id获取签到方式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "detailId", value = "任务Id", required = true, paramType = "path"),
    })
    public R<List<ProjectInspectCheckinDetail>> listCheckInDetailById(@PathVariable("detailId") String detailId) {
        return R.ok(this.projectInspectTaskDetailService.listCheckInDetailById(detailId));
    }

    /**
     * 巡检点签到
     *
     * @param taskVo 实体对象
     * @return 新增结果
     */
    @PutMapping("/sign")
    @ApiOperation(value = "巡检点签到", notes = "巡检点签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R sign(@RequestBody ProjectInspectVo taskVo) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectInspectTaskDetail projectInspectTaskDetail = projectInspectTaskDetailService.getById(taskVo.getDetailId());
        //如果不是未巡则无法签到
        if (!(ObjectUtil.isEmpty(projectInspectTaskDetail.getResult()) || "0".equals(projectInspectTaskDetail.getResult()))) {
            return R.failed("该巡检点已签到");
        }
        taskVo.setExecStaffName(projectStaffVo.getStaffName());
        this.projectInspectTaskDetailService.sign(taskVo);
        return R.ok();
    }


    /**
     * 巡检点签到
     *
     * @param taskVo 实体对象
     * @return 新增结果
     */
    @PutMapping("/appSign")
    @ApiOperation(value = "巡检点签到", notes = "巡检点签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R appSign(@RequestBody ProjectInspectVo taskVo) {


        List<ProjectInspectCheckinDetail> checkinDetails = taskVo.getCheckinDetails();
        List<ProjectInspectDetailDeviceFormVo> devices = taskVo.getDevices();

        if (CollUtil.isNotEmpty(checkinDetails)){
            checkinDetails.forEach(data->{
                if (!StringUtils.isEmpty(data.getPicUrl())){
                    try {
                        data.setPicUrl(imgConvertUtil.base64ToMinio(data.getPicUrl()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (CollUtil.isNotEmpty(devices)){
            devices.forEach(data->{

                if (!StringUtils.isEmpty(data.getPicUrl())){
                    try {
                        data.setPicUrl(imgConvertUtil.base64ToMinio(data.getPicUrl()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!StringUtils.isEmpty(data.getPicUrl2())){
                    try {
                        data.setPicUrl2(imgConvertUtil.base64ToMinio(data.getPicUrl2()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!StringUtils.isEmpty(data.getPicUrl3())){
                    try {
                        data.setPicUrl3(imgConvertUtil.base64ToMinio(data.getPicUrl3()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!StringUtils.isEmpty(data.getPicUrl4())){
                    try {
                        data.setPicUrl4(imgConvertUtil.base64ToMinio(data.getPicUrl4()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectInspectTaskDetail projectInspectTaskDetail = projectInspectTaskDetailService.getById(taskVo.getDetailId());
        //如果不是未巡则无法签到
        if (!(ObjectUtil.isEmpty(projectInspectTaskDetail.getResult()) || "0".equals(projectInspectTaskDetail.getResult()))) {
            return R.failed("该巡检点已签到");
        }
        taskVo.setExecStaffName(projectStaffVo.getStaffName());

        this.projectInspectTaskDetailService.sign(taskVo);
        return R.ok();
    }


}