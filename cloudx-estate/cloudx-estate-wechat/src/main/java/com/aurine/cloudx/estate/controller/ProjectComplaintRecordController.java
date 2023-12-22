package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.constant.enums.ApproveStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectComplaintRecord;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.feign.RemoteComplaintRecordService;
import com.aurine.cloudx.estate.service.ProjectComplaintRecordService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.vo.*;
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

/**
 * (ProjectComplaintRecordController)投诉处理
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 16:20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectComplainRecord")
@Api(value = "projectComplainRecord", tags = "投诉处理")
public class ProjectComplaintRecordController {

    @Resource
    ProjectComplaintRecordService projectComplaintRecordService;

    @Resource
    ProjectStaffService projectStaffService;

    @Resource
    ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private RemoteComplaintRecordService remoteComplaintRecordService;

    /**
     * 分页查询所有
     *
     * @param page 分页对象
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询所有(物业)", notes = "分页查询所有")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型(不传为所有，其他值参考字典类型 complaint_type)", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(不传为所有，0: 待抢单 1: 待完成 2: 已完成 其他值参考字典类型 complaint_status)", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "handler", value = "处理人ID", required = false, paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "投诉人手机号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    @GetMapping("/page")
    public R<Page<ProjectComplaintRecordVo>> page(Page page,
                                                  @RequestParam(value = "type", required = false) String type,
                                                  @RequestParam(value = "status", required = false) String status,
                                                  @RequestParam(value = "handler", required = false) String handler,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  @RequestParam(value="date", required = false) String date) {

        return R.ok(projectComplaintRecordService.pageByType(page, type, status,handler, phone, date));
    }

    /**
     * 分页查询待我处理的投诉
     *
     * @param page 分页对象
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询待我处理的投诉(物业)", notes = "分页查询待我处理的投诉")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型(不传为所有，其他值参考字典类型 complaint_type)", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(不传为所有，0: 待抢单 1: 待完成 2: 已完成 其他值参考字典类型 complaint_status)", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true,  paramType = "header")
    })
    @GetMapping("/pageByHandler")
    public R<Page<ProjectComplaintRecordVo>> pageByHandler(Page page, @RequestParam(value = "type",required = false) String type,@RequestParam(value = "status",required = false) String status) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        return R.ok(projectComplaintRecordService.pageByType(page, type,status, projectStaffVo.getStaffId(),null, null));
    }

    /**
     * 分页查询我提交的投诉
     *
     * @param page 分页对象
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询我提交的投诉(业主)", notes = "分页查询我提交的投诉")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型(不传为所有，其他值参考字典类型 complaint_type)", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(不传为所有，0: 待抢单 1: 待完成 2: 已完成 其他值参考字典类型 complaint_status)", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header")
    })
    @GetMapping("/pageByOperator")
    public R<Page<ProjectComplaintRecordVo>> pageByOperator(Page page, @RequestParam(value = "type",required = false) String type,@RequestParam(value = "status",required = false) String status) {
        return R.ok(projectComplaintRecordService.pageByType(page, type, status,null, SecurityUtils.getUser().getPhone(), null));
    }

    /**
     * 指派处理人
     *
     * @param complaintId 分页对象
     * @param handler     类型
     * @return
     */
    @ApiOperation(value = "指派处理人(物业)", notes = "指派处理人")
    @PutMapping("/setHandler")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "complaintId", value = "单据id", paramType = "query",required = true),
            @ApiImplicitParam(name = "handler", value = "处理人id", paramType = "query",required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id",  paramType = "header")
    })
    public R setHandler(@RequestParam("complaintId") String complaintId, @RequestParam("handler") String handler) {
        ProjectComplaintRecord projectComplaintRecord = new ProjectComplaintRecord();
        projectComplaintRecord.setComplaintId(complaintId);
        projectComplaintRecord.setHandler(handler);
        return remoteComplaintRecordService.sendOrder(projectComplaintRecord);
    }

    /**
     * 获取投诉单据详细信息
     *
     * @param complaintId 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "获取投诉单据详细信息(业主、物业)", notes = "获取投诉单据详细信息")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "id", value = "单据id", paramType = "path",required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header")
    })
    @GetMapping("/{id}")
    public R<ProjectComplaintRecordInfoVo> get(@PathVariable("id") String complaintId) {
        return R.ok(projectComplaintRecordService.getByComplainId(complaintId));
    }

    /**
     * 接单
     *
     * @param id 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "接单(物业)", notes = "接单")
    @GetMapping("/order/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单据id", paramType = "path",required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header")
    })
    public R<ProjectComplaintRecordInfoVo> order(@PathVariable("id") String id) {
        ProjectComplaintRecord projectComplaintRecord = new ProjectComplaintRecord();
        projectComplaintRecord.setComplaintId(id);
        return remoteComplaintRecordService.robOrder(projectComplaintRecord);
    }

    /**
     * 处理完成
     *
     * @param formVo 表单对象
     * @return
     */
    @ApiOperation(value = "处理完成(物业)", notes = "处理完成")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header")
    })
    @PutMapping("/setResult")
    public R setResult(@RequestBody ProjectComplainRecordResultFormVo formVo) {

        return remoteComplaintRecordService.doneOrder(formVo);
    }

    /**
     * 评价
     *
     * @return
     */
    @ApiOperation(value = "评价(业主)", notes = "评价")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header")
    })
    @PutMapping("/appraise/{id}")
    public R appraise(@PathVariable String id,@RequestParam Integer scope) {

        ProjectComplaintRecord projectComplaintRecord= projectComplaintRecordService.getById(id);
        if (!SecurityUtils.getUser().getId().equals(projectComplaintRecord.getOperator())) {
//            非创建人无法评价
            return R.failed("非创建人无法评价");
        }
        projectComplaintRecord.setScore(scope);
        projectComplaintRecord.setStatus(ApproveStatusEnum.finish.code);
        projectComplaintRecordService.updateById(projectComplaintRecord);
        return R.ok();
    }
    /**
     * 登记
     *
     * @param formVo 表单对象
     * @return
     */
    @ApiOperation(value = "登记(业主)", notes = "登记(业主、物业)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header")
    })
    @PutMapping("/request")
    public R request(@RequestBody ProjectComplainRecordRequestFormVo formVo) {

        return projectComplaintRecordService.request(formVo);
    }

}
