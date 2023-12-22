package com.aurine.cloudx.estate.controller;


import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.utils.StringUtils;
import com.aurine.cloudx.estate.entity.ProjectPatrolPersonPoint;
import com.aurine.cloudx.estate.service.ProjectPatrolInfoService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonPointService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonService;
import com.aurine.cloudx.estate.service.impl.ProjectStaffServiceImpl;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * (ProjectPatrolInfoController)项目巡更记录
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/24 9:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/patrol")
@Api(value = "patrol", tags = "项目巡更记录")
public class ProjectPatrolInfoController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPatrolInfoService projectPatrolInfoService;
    @Resource
    private ProjectPatrolPersonService projectPatrolPersonService;
    @Resource
    private ProjectStaffServiceImpl projectStaffService;
    @Resource
    private ProjectPatrolPersonPointService projectPatrolPersonPointService;
    @Resource
    private ImgConvertUtil imgConvertUtil;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param vo   查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询巡更记录", notes = "分页查询巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    public R<IPage<ProjectPatrolInfoVo>> selectAll(Page<ProjectPatrolInfoVo> page, ProjectPatrolInfoSearchCondition vo) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolInfoService.pageByPatrolInfo(page, vo));
    }

    /**
     * 分页查询待我认领巡更记录
     *
     * @param page 分页对象
     * @param vo   查询实体
     * @return 所有数据
     */
    @GetMapping("/claim/page")
    @ApiOperation(value = "分页查询待我认领巡更记录", notes = "分页查询待我认领巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
    })
    public R selectForMe(Page<ProjectPatrolInfoVo> page, ProjectPatrolInfoSearchCondition vo) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolInfoService.pageByPatrolInfoForMe(page, projectStaffVo.getStaffId(), vo));
    }

    /**
     * 分页查询待我执行巡更记录
     *
     * @param page 分页对象
     * @param vo   查询实体
     * @return 所有数据
     */
    @GetMapping("/execute/page")
    @ApiOperation(value = "分页查询待我执行巡更记录", notes = "分页查询待我执行巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
    })
    public R selectToDo(Page<ProjectPatrolInfoVo> page, ProjectPatrolInfoSearchCondition vo) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolInfoService.pageByPatrolInfoToDo(page, projectStaffVo.getStaffId(), vo));
    }

    /**
     * 分页查询待我完成巡更记录
     *
     * @param page 分页对象
     * @param date 日期
     * @return 所有数据
     */
    @GetMapping("/complete/page")
    @ApiOperation(value = "分页查询待我完成巡更记录", notes = "分页查询待我完成巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    public R<Page<ProjectPatrolInfoVo>> selectDateToDo(Page page, String date) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolInfoService.selectDateToDo(page, projectStaffVo.getStaffId(), date));
    }


    /**
     * 通过巡更记录Id查询巡更记录
     *
     * @param patrolId 主键
     * @return 单条数据
     */
    @GetMapping("/{patrolId}")
    @ApiOperation(value = "通过巡更记录Id查询巡更记录", notes = "通过巡更记录Id查询巡更记录")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "patrolId", value = "巡更记录Id", paramType = "query", required = true)
    })
    public R<ProjectPatrolInfoOnDetailVo> selectOne(@PathVariable("patrolId") String patrolId) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolInfoService.getVoById(patrolId));
    }

    /**
     * <p>
     * 认领任务
     * </p>
     *
     * @return R
     */
    @PostMapping("/claim/{taskId}")
    @ApiOperation(value = "认领任务", notes = "认领任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "taskId", value = "任务id", paramType = "path")
    })
    public R claim(@PathVariable("taskId") String taskId) {

        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolPersonService.assignPerson(taskId, projectStaffVo));
    }


    /**
     * 巡更点签到
     *
     * @param projectPatrolPersonPoint 实体对象
     * @return 新增结果
     */
    @PostMapping("/sign")
    @ApiOperation(value = "巡更点签到", notes = "巡更点签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R sign(@RequestBody ProjectPatrolPersonPoint projectPatrolPersonPoint) {

        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectPatrolPersonPointService.sign(projectPatrolPersonPoint);
        return R.ok();
    }

    /**
     * 巡更点签到
     *
     * @param projectPatrolPersonPoint 实体对象
     * @return 新增结果
     */
    @PostMapping("/appSign")
    @ApiOperation(value = "巡更点签到", notes = "巡更点签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R appSign(@RequestBody ProjectPatrolPersonPoint projectPatrolPersonPoint) {

        if (!StringUtils.isEmpty(projectPatrolPersonPoint.getPatrolResultPic1())) {
            try {
                projectPatrolPersonPoint.setPatrolResultPic1(imgConvertUtil.base64ToMinio(projectPatrolPersonPoint.getPatrolResultPic1()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(projectPatrolPersonPoint.getPatrolResultPic2())) {
            try {
                projectPatrolPersonPoint.setPatrolResultPic2(imgConvertUtil.base64ToMinio(projectPatrolPersonPoint.getPatrolResultPic2()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(projectPatrolPersonPoint.getPatrolResultPic3())) {
            try {
                projectPatrolPersonPoint.setPatrolResultPic3(imgConvertUtil.base64ToMinio(projectPatrolPersonPoint.getPatrolResultPic3()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(projectPatrolPersonPoint.getCheckInPic())) {
            try {
                projectPatrolPersonPoint.setCheckInPic(imgConvertUtil.base64ToMinio(projectPatrolPersonPoint.getCheckInPic()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectPatrolPersonPointService.sign(projectPatrolPersonPoint);
        return R.ok();
    }


}