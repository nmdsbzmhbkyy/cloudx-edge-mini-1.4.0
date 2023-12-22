package com.aurine.cloudx.estate.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.aurine.cloudx.estate.entity.ProjectPatrolPersonPoint;
import com.aurine.cloudx.estate.service.ProjectPatrolInfoService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonPointService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonService;
import com.aurine.cloudx.estate.service.impl.ProjectStaffServiceImpl;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (ProjectPatrolInfoController)项目巡更记录
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/24 9:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectPatrolInfo")
@Api(value = "projectPatrolInfo", tags = "项目巡更记录")
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
    private RemoteUserService remoteUserService;
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
    @GetMapping("/pageForMe")
    @ApiOperation(value = "分页查询待我认领巡更记录", notes = "分页查询待我认领巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
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
    @GetMapping("/pageToDo")
    @ApiOperation(value = "分页查询待我执行巡更记录", notes = "分页查询待我执行巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
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
     * @param date   日期
     * @return 所有数据
     */
    @GetMapping("/pageDateToDo")
    @ApiOperation(value = "分页查询待我完成巡更记录", notes = "分页查询待我完成巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
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
            @ApiImplicitParam(name = "patrolId", value = "巡更记录Id", paramType = "query", required = true),

            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
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
    @PostMapping("/claim/{id}")
    @ApiOperation(value = "认领任务", notes = "认领任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R claim(@PathVariable("id") String id) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolPersonService.assignPerson(id, projectStaffVo));
    }


    /**
     * <p>
     * 指派人员
     * </p>
     *
     * @param projectPatrolAssignVo 巡更指派VO对象 包含巡更记录ID和巡更指派人员对象列表
     * @return R
     */
    @PostMapping("/assignPerson")
    @ApiOperation(value = "指派人员", notes = "指派人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R assignPerson(@RequestBody ProjectPatrolAssignVo projectPatrolAssignVo) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPatrolPersonService.assignPerson(projectPatrolAssignVo.getPatrolId(), projectPatrolAssignVo.getStaffList()));
    }

    /**
     * <p>
     * 获取已指派人员ID集合
     * </p
     *
     * @param patrolId 巡更记录ID
     * @return R
     */
    @GetMapping("/getAssignStaffIdListByPatrolId/{patrolId}")
    @ApiOperation(value = "获取已指派人员ID集合", notes = "获取已指派人员ID集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patrolId", value = "巡更记录Id", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getAssignStaffIdListByPatrolId(@PathVariable("patrolId") String patrolId) {
        List<String> staffIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(patrolId)) {
            List<ProjectPatrolPerson> personList = projectPatrolPersonService.list(new QueryWrapper<ProjectPatrolPerson>()
                    .lambda().eq(ProjectPatrolPerson::getPatrolId, patrolId).eq(ProjectPatrolPerson::getStaffType,"2"));
            if (CollUtil.isNotEmpty(personList)) {
                staffIdList = personList.stream().map(ProjectPatrolPerson::getStaffId).collect(Collectors.toList());
            }
        }
        return R.ok(staffIdList);
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
    public R sign(@RequestBody  ProjectPatrolPersonPoint projectPatrolPersonPoint) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectPatrolPersonPointService.sign(projectPatrolPersonPoint);
        return R.ok();
    }








}