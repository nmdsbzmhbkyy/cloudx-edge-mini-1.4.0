package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.aurine.cloudx.estate.service.ProjectStaffNoticeObjectService;
import com.aurine.cloudx.estate.service.ProjectStaffNoticeService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.vo.ProjectStaffNoticeAddVo;
import com.aurine.cloudx.estate.vo.ProjectStaffNoticeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 员工通知发布
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:16:25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectStaffNotice")
@Api(value = "projectStaffNotice", tags = "员工通知发布管理")
public class ProjectStaffNoticeController {

    private final ProjectStaffNoticeService projectStaffNoticeService;
    private final ProjectStaffNoticeObjectService objectService;
    private final ProjectStaffService projectStaffService;

    /**
     * 分页查询
     *
     * @param page               分页对象
     * @param projectStaffNotice 员工通知发布
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectStaffNoticePage(Page page, ProjectStaffNotice projectStaffNotice) {
        LambdaQueryWrapper<ProjectStaffNotice> queryWrapper = Wrappers.lambdaQuery(ProjectStaffNotice.class);
        if (projectStaffNotice.getNoticeTitle() != null && !"".equals(projectStaffNotice.getNoticeTitle())) {
            queryWrapper.like(ProjectStaffNotice::getNoticeTitle, projectStaffNotice.getNoticeTitle());
        }
        queryWrapper.orderByDesc(ProjectStaffNotice::getPubTime);
        queryWrapper.eq(ProjectStaffNotice::getSource,"1");
        return R.ok(projectStaffNoticeService.page(page, queryWrapper));
    }

    /**
     * 通过id查询员工通知发布
     *
     * @param noticeId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{noticeId}")
    public R getById(@PathVariable("noticeId") String noticeId) {
        return R.ok(projectStaffNoticeService.getById(noticeId));
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
    @PreAuthorize("@pms.hasPermission('hr_staffnotice_add')")
    public R save(@RequestBody ProjectStaffNoticeAddVo projectStaffNoticeAddVo) {
        return R.ok(projectStaffNoticeService.saveByUserIds(projectStaffNoticeAddVo));
    }
    
    /**
     * 获取当前用户的消息
     *
     * @param page               分页对象
     * @param notice 员工通知发布
     * @return
     */
    @ApiOperation(value = "获取当前用户的消息", notes = "获取当前用户的消息")
    @GetMapping("/getByStaff")
    public R getByStaff(Page page, ProjectStaffNoticeVo notice) {
        return R.ok(projectStaffNoticeService.getByStaff(page, notice));
    }

    /**
     * 更新已读状态
     * @param notice
     * @return
     */
    @ApiOperation(value = "更新已读状态", notes = "更新已读状态")
    @SysLog("更新已读状态")
    @PutMapping("/updateStatus")
    public R updateStatus(@RequestBody ProjectStaffNoticeVo notice) {
        // 通过获ProjectId和登入用户id来获取对应的员工信息(包括了该员工的属性列表)
        ProjectStaff staff = projectStaffService.getStaffByOwner();
        // 利用员工id,对该登入用户的消息对象更新,并设置状态为1
        return R.ok(objectService.updateNoticeStatus(staff.getStaffId(),notice.getNoticeIds()));
    }
    
    /**
     * 统计未读信息
     * @return
     */
    @ApiOperation(value = "统计未读信息")
    @GetMapping("/countUnRead")
    public R countUnRead() {
        return R.ok(projectStaffNoticeService.countUnRead());
    }
}
