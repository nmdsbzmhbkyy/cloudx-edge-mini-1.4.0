package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.aurine.cloudx.estate.feign.RemoteStaffService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProjectStaffController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 14:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectStaff")
@Api(value = "projectStaff", tags = "员工管理")
public class ProjectStaffController {

    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private ProjectStaffNoticeService projectStaffNoticeService;

    @Resource
    private ProjectStaffNoticeObjectService projectStaffNoticeObjectService;

    @Resource
    private ProjectComplaintRecordService projectComplaintRecordService;

    @Resource
    private ProjectRepairRecordService projectRepairRecordService;

    @Resource
    private ProjectInspectTaskService projectInspectTaskService;

    @Resource
    private ProjectPatrolInfoService projectPatrolInfoService;
    @Resource
    private RemoteStaffService remoteStaffService;

    /**
     * 分页查询当前小区下其他员工信息
     *
     * @param page 分页查询对象
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "分页查询当前小区下其他员工信息(物业)", notes = "分页查询当前小区下其他员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "员工名称", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/page")
    public R<Page<ProjectStaffListVo>> pageProjectByPerson(Page page, @RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "deptId", required = false) String deptId) {
        return R.ok(projectStaffService.pageAll(page, name,deptId));
    }

    /**
     * 分页查询当前小区下其他员工信息
     *
     * @param page 分页查询对象
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "根据员工ID获取当前小区下职位内员工信息列表(物业)", notes = "根据员工ID获取当前小区下职位内员工信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "staffId", value = "员工ID", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "name", value = "员工名称", paramType = "query"),
    })
    @GetMapping("/page/{staffId}")
    public R<Page<ProjectStaffListVo>> staffPage(Page page, @PathVariable("staffId") String staffId, @RequestParam(value = "name", required = false) String name) {
        return R.ok(projectStaffService.staffPage(page, staffId, name));
    }


    /**
     * 分页查询消息通知
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询人员消息通知(物业)", notes = "分页查询人员消息通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/notice/page")
    public R<Page<ProjectStaffNoticeVo>> getProjectStaffNoticePage(Page page) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectStaffNoticeService.pageByPerson(page, projectStaffVo.getStaffId()));
    }

    /**
     * 查询人员消息通知未读汇总
     *
     * @return
     */
    @ApiOperation(value = "查询人员消息通知未读汇总(物业)", notes = "查询人员消息通知未读汇总")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/notice/count")
    public R<Integer> getCount() {

        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectStaffNoticeService.countByStaffId(projectStaffVo.getStaffId()));
    }

    /**
     * 获取消息信息
     *
     * @param noticeId 消息id
     * @return
     */
    @ApiOperation(value = "获取消息详细信息(物业)", notes = "获取消息详细信息")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeId", value = "消息id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/notice/{noticeId}")
    public R<ProjectStaffNotice> getProjectStaffNoticePage(@PathVariable("noticeId") String noticeId) {
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
     * 获取员工信息
     *
     * @return
     */
    @ApiOperation(value = "获取员工信息(物业)", notes = "获取员工信息")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping
    public R<ProjectStaffVo> getInfo() {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();


        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectStaffVo);
    }


    /**
     * 获取员工信息
     *
     * @return
     */
    @ApiOperation(value = "获取员工信息(物业)", notes = "获取员工信息")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/getInfoByPhone/{phone}")
    public R<ProjectStaff> getInfoByPhone(@PathVariable String phone) {
        List<ProjectStaff> projectStaff = projectStaffService.list(Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getMobile, phone));
        if (projectStaff.size() > 0) {
            return R.ok(projectStaff.get(0));
        } else {
            return R.ok();
        }
    }

    /**
     * 获取员工工作情况
     */
    @ApiOperation(value = "统计员工工作情况(物业)", notes = "统计员工工作情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "staffId", value = "员工ID", required = true, paramType = "param"),
            @ApiImplicitParam(name = "date", value = "日期（格式为 yyyy-MM）", required = true, paramType = "param")
    })
    @GetMapping("/getWorkInfo")
    public R<ProjectStaffAllWorkVo> getWorkInfo(@RequestParam(value = "staffId", required = false) String staffId, @RequestParam(value = "date", required = false) String date) {
        ProjectStaffAllWorkVo staffAllWorkVo = new ProjectStaffAllWorkVo();
        staffAllWorkVo.setStaffId(staffId);
        staffAllWorkVo.setComplainRecord(projectComplaintRecordService.getCount(staffId, date));
        staffAllWorkVo.setRepairRecord(projectRepairRecordService.getCount(staffId, date));
        staffAllWorkVo.setInspectTask(projectInspectTaskService.getCount(staffId, date));
        staffAllWorkVo.setPatrolInfo(projectPatrolInfoService.getCount(staffId, date));
        return R.ok(staffAllWorkVo);
    }


    /**
     * 员工上传头像
     *
     * @return
     */
    @ApiOperation(value = "员工上传头像", notes = "员工上传头像")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/save-employee-avatar")
    public R<AppFaceHeadPortraitVo> saveEmployeeAvatar(@RequestBody AppFaceHeadPortraitVo appFaceHeadPortraitVo) {
        return remoteStaffService.saveEmployeeAvatar(appFaceHeadPortraitVo);
    }




    /**
     * 更改员工手机号
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "更改员工手机号", notes = "更改员工手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path"),
    })
    @PutMapping("/updatePhoneById/{phone}")
    public R<Boolean> getNewPhoneQRCode(@PathVariable("phone") String phone) {

        return  remoteStaffService.updatePhoneById(phone);
    }

    /**
     * 获取当前员工的信息
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/getStaff")
    public R getByUserId() {
        return R.ok(projectStaffService.getStaffByOwner());
    }
}
