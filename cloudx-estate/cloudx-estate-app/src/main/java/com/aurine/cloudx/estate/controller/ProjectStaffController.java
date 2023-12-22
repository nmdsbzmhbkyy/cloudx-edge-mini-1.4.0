package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.feign.RemoteStaffService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (ProjectPersonInfoController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 15:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/staff")
@Api(value = "staff", tags = "员工管理")
@Slf4j
public class ProjectStaffController {

    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private RemoteStaffService remoteStaffService;

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
     * 更改业主手机号
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "更改员工手机号", notes = "更改员工手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path"),

    })
    @PutMapping("/updatePhoneById/{phone}")
    public R<Boolean> getNewPhoneQRCode(@PathVariable("phone") String phone) {

        return  remoteStaffService.updatePhoneById(phone);
    }



    /**
     * 分页查询当前小区下其他员工信息
     *
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "分页查询当前小区下其他员工信息(物业)", notes = "分页查询当前小区下其他员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "员工名称", paramType = "query"),
            @ApiImplicitParam(name = "deptId", value = "部门id", paramType = "query"),
    })
    @GetMapping("/page")
    public R<Page<ProjectStaffListVo>> pageProjectByPerson(@RequestParam(value = "size", required = false) Long size,
                                                           @RequestParam(value = "current", required = false) Long current,
                                                           @RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "deptId", required = false) String deptId

    ) {
        AppPage page = new AppPage(current, size);
        return R.ok(projectStaffService.pageAll(page, name,deptId));
    }

    /**
     * 查询当前小区全部员工ID
     *
     * @return 查询当前小区全部员工ID
     */
    @ApiOperation(value = "查询当前小区全部员工ID", notes = "查询当前小区全部员工ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @GetMapping("/selectId")
    public R<List<String>> selectId () {

        Integer projectId = ProjectContextHolder.getProjectId();
        List<String> projectStaffListVoPage = projectStaffService.selectId(projectId);
        return R.ok(projectStaffListVoPage);
    }


    /**
     * 获取员工信息
     *
     * @return
     */
    @ApiOperation(value = "获取当前小区的员工信息(物业)", notes = "获取当前小区的员工信息")

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
    public R saveEmployeeAvatar(@RequestBody AppFaceHeadPortraitVo appFaceHeadPortraitVo) {

        return remoteStaffService.saveEmployeeAvatar(appFaceHeadPortraitVo);
    }

}
