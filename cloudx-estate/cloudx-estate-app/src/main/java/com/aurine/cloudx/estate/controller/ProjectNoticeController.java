package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.entity.ProjectNotice;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ProjectPersonInfoController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 15:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/notice")
@Api(value = "notice", tags = "通知")
@Slf4j
public class ProjectNoticeController {

    @Resource
    private final ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private final ProjectStaffNoticeService projectStaffNoticeService;
    @Resource
    private final ProjectNoticeService projectNoticeService;
    @Resource
    private final ProjectStaffNoticeObjectService projectStaffNoticeObjectService;
    @Resource
    private ProjectStaffService projectStaffService;

    /**
     * 业主获取消息通知列表
     *
     * @param size
     * @param current
     * @return
     */
    @ApiOperation(value = "业主获取消息通知列表(消息)", notes = "业主获取消息通知列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "total", value = "总条数", paramType = "query")
    })
    @GetMapping("/page")
    public R<Page<ProjectPersonNoticeVo>> getProjectNoticePage(Long size, Long current) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }

        AppPage page = new AppPage(current, size);

        return R.ok(projectNoticeService.pageByPerson(page, projectPersonInfo.getPersonId(), "3"));
    }

    /**
     * 业主获取未读消息数量
     *
     * @return
     */
    @ApiOperation(value = "业主获取未读消息数量(消息)", notes = "业主获取未读消息数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/count")
    public R<Integer> getCount() {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectStaffNoticeService.countByPersonId(projectPersonInfo.getPersonId(), "3"));
    }

    /**
     * 获取消息信息
     *
     * @param noticeId 消息id
     * @return
     */
    @ApiOperation(value = "获取消息详情(消息)", notes = "获取消息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "noticeId", value = "消息id", required = true, paramType = "path")
    })
    @GetMapping("/{noticeId}")
    public R<ProjectNotice> getProjectNoticePage(@PathVariable("noticeId") String noticeId) {
        ProjectNotice projectNotice = projectNoticeService.getById(noticeId);
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectStaffNoticeObjectService.updateNoticeStatus(projectPersonInfo.getPersonId(), ListUtil.toList(noticeId));
        return R.ok(projectNotice);
    }

    /**
     * 分页查询消息通知
     *
     * @return
     */
    @ApiOperation(value = "分页查询物业人员消息通知(物业)", notes = "分页查询物业人员消息通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
    })
    @GetMapping("/staff/page")
    public R<Page<ProjectStaffNoticeVo>> getProjectStaffNoticePage(@RequestParam(value = "size", required = false) Long size,
                                                                   @RequestParam(value = "current", required = false) Long current) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        AppPage page = new AppPage(current, size);
        return R.ok(projectStaffNoticeService.pageByPerson(page, projectStaffVo.getStaffId()));
    }

    /**
     * 获取消息信息
     *
     * @param noticeId 消息id
     * @return
     */
    @ApiOperation(value = "获取消息详情(物业)", notes = "获取消息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "noticeId", value = "消息id", required = true, paramType = "path")
    })
    @GetMapping("/staff/{noticeId}")
    public R<ProjectStaffNotice> getStaffNoticePage(@PathVariable("noticeId") String noticeId) {
        ProjectStaffNotice projectStaffNotice = projectStaffNoticeService.getById(noticeId);
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectStaffNoticeObjectService.updateNoticeStatus(projectStaffVo.getStaffId(), ListUtil.toList(noticeId));
        return R.ok(projectStaffNotice);
    }


    /**
     * 查询人员消息通知未读汇总
     *
     * @return
     */
    @ApiOperation(value = "查询物业人员消息通知未读数量(物业)", notes = "查询物业人员消息通知未读数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/staff/count")
    public R<Integer> getStaffCount() {

        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectStaffNoticeService.countByStaffId(projectStaffVo.getStaffId()));
    }


    /**
     * 分页查询当前用户创建发布的消息
     *
     * @param page               分页对象
     * @param projectStaffNotice 员工通知发布
     * @return
     */
    @ApiOperation(value = "分页查询当前用户创建发布的消息（物业）", notes = "分页查询当前用户创建发布的消息")
    @GetMapping("/staff/publish/page")
    public R getProjectStaffNoticePage(Page page, ProjectStaffNotice projectStaffNotice) {

        LambdaQueryWrapper<ProjectStaffNotice> queryWrapper = Wrappers.lambdaQuery(ProjectStaffNotice.class);
        if (projectStaffNotice.getNoticeTitle() != null && !"".equals(projectStaffNotice.getNoticeTitle())) {
            queryWrapper.like(ProjectStaffNotice::getNoticeTitle, projectStaffNotice.getNoticeTitle());
        }
        queryWrapper.orderByDesc(ProjectStaffNotice::getPubTime);
        queryWrapper.eq(ProjectStaffNotice::getOperator, SecurityUtils.getUser().getId());
        return R.ok(projectStaffNoticeService.page(page, queryWrapper));
    }

    /**
     * 新增员工通知发布
     *
     * @param projectStaffNoticeAddVo 员工通知发布
     * @return R
     */
    @ApiOperation(value = "员工发布通知消息(物业)", notes = "员工发布通知消息")
    @SysLog("新增员工通知发布")
    @PostMapping("/staff/publish")
    public R save(@RequestBody ProjectStaffNoticeAddVo projectStaffNoticeAddVo) {
        projectStaffNoticeAddVo.setContentType("2");
        return R.ok(projectStaffNoticeService.saveByUserIds(projectStaffNoticeAddVo));
    }
}
