package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.enums.ApproveStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import com.aurine.cloudx.estate.feign.RemoteRepairRecordService;
import com.aurine.cloudx.estate.service.ProjectRepairRecordService;
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
 * (ProjectRepairRecordController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 11:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectRepairRecord")
@Api(value = "projectRepairRecord", tags = "报修处理")
public class ProjectRepairRecordController {

    @Resource
    ProjectRepairRecordService projectRepairRecordService;


    @Resource
    ProjectStaffService projectStaffService;

    @Resource
    private RemoteRepairRecordService remoteRepairRecordService;

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
            @ApiImplicitParam(name = "type", value = "报修类型 0.水电煤气 1.家电家具 2.电梯 3.门禁 4.公共设施 5.物业设备 6.其他报修 参考字典类型 repair_type", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 0.待抢单 1.待完成 2.待评价 3.已完成 参考字典类型 maintain_status ", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "handler", value = "处理人ID", required = false, paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "报修人手机号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    @GetMapping("/page")
    public R<Page<ProjectRepairRecordVo>> page(Page page,
                                               @RequestParam(value = "type", required = false) String type,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "handler", required = false) String handler,
                                               @RequestParam(value = "phone", required = false) String phone,
                                               @RequestParam(value = "date", required = false) String date) {
        return R.ok(projectRepairRecordService.pageByType(page, type,status, handler, phone, date));
    }

    /**
     * 分页查询待我处理的报修
     *
     * @param page 分页对象
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询待我处理的报修(物业)", notes = "分页查询待我处理的报修")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "报修类型 0.水电煤气 1.家电家具 2.电梯 3.门禁 4.公共设施 5.物业设备 6.其他报修 参考字典类型 repair_type", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 0.待抢单 1.待完成 2.待评价 3.已完成 参考字典类型 maintain_status ", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")

    })
    @GetMapping("/pageByHandler")
    public R<Page<ProjectRepairRecordVo>> pageByHandler(Page page, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "status", required = false) String status) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        return R.ok(projectRepairRecordService.pageByType(page, type,status, projectStaffVo.getStaffId(), null, null));
    }

    /**
     * 分页查询由我创建的报修
     *
     * @param page 分页对象
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询由我创建的报修(业主)", notes = "分页查询由我创建的报修")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "报修类型 0.水电煤气 1.家电家具 2.电梯 3.门禁 4.公共设施 5.物业设备 6.其他报修 参考字典类型 repair_type", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 0.待抢单 1.待完成 2.待评价 3.已完成 参考字典类型 maintain_status ", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")

    })
    @GetMapping("/pageByOperator")
    public R<Page<ProjectRepairRecordVo>> pageByOperator(Page page, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "status", required = false) String status) {
        return R.ok(projectRepairRecordService.pageByType(page, type, status,null, SecurityUtils.getUser().getPhone(), null));
    }


    /**
     * 指派处理人
     *
     * @param id      id
     * @param handler 类型
     * @return
     */
    @ApiOperation(value = "指派处理人(物业)", notes = "指派处理人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单据id", paramType = "query", required = true),
            @ApiImplicitParam(name = "handler", value = "处理人", paramType = "query", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PutMapping("/setHandler")
    public R setHandler(@RequestParam("id") String id, @RequestParam("handler") String handler) {
        ProjectRepairRecord projectRepairRecord = new ProjectRepairRecord();
        projectRepairRecord.setRepairId(id);
        projectRepairRecord.setHandler(handler);
        return remoteRepairRecordService.sendOrder(projectRepairRecord);
    }

    /**
     * 获取报修单据详细信息
     *
     * @param id 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "获取报修单据详细信息(业主、物业)", notes = "获取报修单据详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单据id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/{id}")
    public R<ProjectRepairRecordInfoVo> get(@PathVariable("id") String id) {
        return R.ok(projectRepairRecordService.getByRepairId(id));
    }

    /**
     * 接单
     *
     * @param id 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "接单(物业)", notes = "接单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单据id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @GetMapping("/order/{id}")
    public R order(@PathVariable("id") String id) {
        ProjectRepairRecord projectRepairRecord = new ProjectRepairRecord();
        projectRepairRecord.setRepairId(id);
        remoteRepairRecordService.robOrder(projectRepairRecord);
        return R.ok(projectRepairRecordService.getByRepairId(id));
    }

    /**
     * 处理完成
     *
     * @param formVo 表单对象
     * @return
     */
    @ApiOperation(value = "处理完成(物业)", notes = "处理完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @PostMapping("/setResult")
    public R setResult(@RequestBody ProjectRepairRecordResultFormVo formVo) {
        return remoteRepairRecordService.doneOrder(formVo);
    }

    /**
     * 评价
     *
     * @param id 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "评价(业主)", notes = "接单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单据id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @PutMapping("/appraise/{id}")
    public R appraise(@PathVariable("id") String id,@RequestParam Integer scope) {

        ProjectRepairRecord projectRepairRecord = projectRepairRecordService.getById(id);
        if (!SecurityUtils.getUser().getPhone().equals(projectRepairRecord.getPhoneNumber())) {
//            非创建人无法评价
            return R.failed("非创建人无法评价");
        }
        projectRepairRecord.setScore(scope);
        projectRepairRecord.setStatus(ApproveStatusEnum.finish.code);
        projectRepairRecordService.updateById(projectRepairRecord);
        return R.ok();
    }
    /**
     * 登记
     *
     * @param formVo 表单对象
     * @return
     */
    @ApiOperation(value = "登记(业主)", notes = "登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @PostMapping("/request")
    public R request(@RequestBody ProjectRepairRecordRequestFormVo formVo) {
        return projectRepairRecordService.request(formVo);
    }

}
