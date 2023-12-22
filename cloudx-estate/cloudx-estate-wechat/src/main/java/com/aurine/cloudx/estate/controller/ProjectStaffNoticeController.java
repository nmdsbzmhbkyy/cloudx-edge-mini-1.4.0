package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.aurine.cloudx.estate.service.ProjectStaffNoticeService;
import com.aurine.cloudx.estate.vo.ProjectStaffNoticeAddVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ProjectStaffNoticeController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/23 14:40
 */
@RestController
@RequestMapping("/projectStaffNotice")
@Api(value = "projectStaffNotice", tags = "员工通知发布管理")
public class ProjectStaffNoticeController {
    @Resource
    private ProjectStaffNoticeService projectStaffNoticeService;

    /**
     * 分页查询当前用户创建发布的消息
     *
     * @param page               分页对象
     * @param projectStaffNotice 员工通知发布
     * @return
     */
    @ApiOperation(value = "分页查询当前用户创建发布的消息", notes = "分页查询当前用户创建发布的消息")
    @GetMapping("/page")
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
     * 分页查询所有
     *
     * @param page               分页对象
     * @param projectStaffNotice 员工通知发布
     * @return
     */
    @ApiOperation(value = "分页查询所有", notes = "分页查询所有")
    @GetMapping("/pageAll")
    public R pageAll(Page page, ProjectStaffNotice projectStaffNotice) {
        LambdaQueryWrapper<ProjectStaffNotice> queryWrapper = Wrappers.lambdaQuery(ProjectStaffNotice.class);
        if (projectStaffNotice.getNoticeTitle() != null && !"".equals(projectStaffNotice.getNoticeTitle())) {
            queryWrapper.like(ProjectStaffNotice::getNoticeTitle, projectStaffNotice.getNoticeTitle());
        }
        queryWrapper.eq(ProjectStaffNotice::getContentType, "2").orderByDesc(ProjectStaffNotice::getPubTime);
        return R.ok(projectStaffNoticeService.page(page, queryWrapper));
    }

    /**
     * 新增员工通知发布
     *
     * @param projectStaffNoticeAddVo 员工通知发布
     * @return R
     */
    @ApiOperation(value = "新增员工通知发布", notes = "新增员工通知发布")
    @SysLog("新增员工通知发布")
    @PostMapping
    public R save(@RequestBody ProjectStaffNoticeAddVo projectStaffNoticeAddVo) {
        projectStaffNoticeAddVo.setContentType("2");
        return R.ok(projectStaffNoticeService.saveByUserIds(projectStaffNoticeAddVo));
    }


}
