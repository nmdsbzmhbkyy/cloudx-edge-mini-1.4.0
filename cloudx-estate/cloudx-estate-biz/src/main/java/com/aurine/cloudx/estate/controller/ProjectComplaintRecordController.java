package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectComplaintRecord;
import com.aurine.cloudx.estate.mapper.ProjectHousePersonRelMapper;
import com.aurine.cloudx.estate.service.ProjectComplaintRecordService;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.util.MessageTextUtil;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


/**
 * 项目投诉服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:34:42
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectComplaintRecord")
@Api(value = "projectComplaintRecord", tags = "项目投诉服务记录管理")
public class ProjectComplaintRecordController {

    private final ProjectComplaintRecordService projectComplaintRecordService;
    private final ProjectStaffService projectStaffService;
    private final NoticeUtil noticeUtil;
    private final ProjectHousePersonRelMapper projectHousePersonRelMapper;
    private final ProjectHousePersonRelService projectHousePersonRelService;
    /**
     * 报事投诉菜单ID
     */
    private final Integer carAuditMenuId = 10644;

    /**
     * 分页查询
     *
     * @param page                         分页对象
     * @param projectComplaintRecordPageVo 项目投诉服务记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectComplaintRecordPage(Page page, ProjectComplaintRecordPageVo projectComplaintRecordPageVo) {
        if (projectComplaintRecordPageVo.getStartTimeString() != null && !"".equals(projectComplaintRecordPageVo.getStartTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectComplaintRecordPageVo.setStartTime(LocalDate.parse(projectComplaintRecordPageVo.getStartTimeString(), fmt));
        }
        if (projectComplaintRecordPageVo.getEndTimeString() != null && !"".equals(projectComplaintRecordPageVo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectComplaintRecordPageVo.setEndTime(LocalDate.parse(projectComplaintRecordPageVo.getEndTimeString(), fmt));
        }
        return R.ok(projectComplaintRecordService.pageComplaintRecord(page, projectComplaintRecordPageVo));
    }


    /**
     * 通过id查询项目投诉服务记录
     *
     * @param complaintId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{complaintId}")
    public R<ProjectComplaintRecordInfoVo> getById(@PathVariable("complaintId") String complaintId) {
        return R.ok(projectComplaintRecordService.getByComplainId(complaintId));
    }

    /**
     * 新增项目投诉服务记录
     *
     * @param projectComplaintRecord 项目投诉服务记录
     * @return R
     */
    @ApiOperation(value = "新增项目投诉服务记录", notes = "新增项目投诉服务记录")
    @SysLog("新增项目投诉服务记录")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_complaintrecord_add')")
    public R save(@RequestBody ProjectComplaintRecord projectComplaintRecord) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectComplaintRecord.setComplaintId(uuid);
        ProjectHouseAddressVo projectHouseAddressVo = projectHousePersonRelMapper.getAddress(projectComplaintRecord.getHouseId());
        List<String> address = projectHousePersonRelService.getAddress(projectHouseAddressVo.getAddress());

        /**
         *投诉建议通知
         *
         * 有新的投诉工单等待接单
         * 联系人：【住户姓名】
         * 电话：【住户手机号】
         * 房号：【XXX小区XXX项目组团XX栋XX单元XXXX房屋】
         * 投诉建议内容：【住户所填写的投诉内容】
         */
        List<String> staffIdList = projectStaffService.getStaffIdListByMenuId(carAuditMenuId);
        noticeUtil.send(true, "投诉建议通知",
                MessageTextUtil.init()
                        .append("有新的投诉工单等待接单")
                        .p("联系人：%s", projectComplaintRecord.getPersonName())
                        .p("电话：%s", projectComplaintRecord.getPhoneNumber())
                        .p("房号：%s", address.get(0))
                        .p("投诉建议内容：%s", projectComplaintRecord.getContent())
                        .toString(),
                staffIdList
        );
        return R.ok(projectComplaintRecordService.save(projectComplaintRecord));
    }

    /**
     * 修改项目投诉服务记录
     *
     * @param projectComplaintRecord 项目投诉服务记录
     * @return R
     */
    @ApiOperation(value = "修改项目投诉服务记录", notes = "修改项目投诉服务记录")
    @SysLog("修改项目投诉服务记录")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_complaintrecord_edit')")
    public R updateById(@RequestBody ProjectComplaintRecord projectComplaintRecord) {
        return R.ok(projectComplaintRecordService.updateById(projectComplaintRecord));
    }

    /**
     * 通过id删除项目投诉服务记录
     *
     * @param complaintId id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目投诉服务记录", notes = "通过id删除项目投诉服务记录")
    @SysLog("通过id删除项目投诉服务记录")
    @DeleteMapping("/{complaintId}")
//    @PreAuthorize("@pms.hasPermission('estate_complaintrecord_del')")
    public R removeById(@PathVariable String complaintId) {
        return R.ok(projectComplaintRecordService.removeByComplainId(complaintId));
    }

    /**
     * 派单
     *
     * @param projectComplaintRecord
     * @return R
     */
    @ApiOperation(value = "派单", notes = "派单")
    @SysLog("派单")
    @PutMapping("/sendOrder")
    public R sendOrder(@RequestBody ProjectComplaintRecord projectComplaintRecord) {
        return R.ok(projectComplaintRecordService.setHandler(projectComplaintRecord.getComplaintId(),
                projectComplaintRecord.getHandler()));
    }

    /**
     * 抢单
     *
     * @param projectComplaintRecord
     * @return R
     */
    @ApiOperation(value = "抢单", notes = "抢单")
    @SysLog("抢单")
    @PutMapping("/robOrder")
    public R robOrder(@RequestBody ProjectComplaintRecord projectComplaintRecord) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        return R.ok(projectComplaintRecordService.receivingOrders(projectComplaintRecord.getComplaintId(), projectStaffVo.getStaffId()));
    }

    @ApiOperation(value = "完成订单", notes = "完成订单")
    @SysLog("完成订单")
    @PutMapping("/doneOrder")
    public R doneOrder(@RequestBody ProjectComplainRecordResultFormVo projectComplainRecordResultFormVo) {
        return projectComplaintRecordService.setResult(projectComplainRecordResultFormVo);
    }

    /**
     * 按月份进统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "按月份统计报警")
    @GetMapping("/countByMonth/{date}")
    public R countByMonth(@PathVariable("date") String date) {
        return R.ok(projectComplaintRecordService.countByMonth(date));
    }

    /**
     * 统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "统计未处理报警")
    @GetMapping("/countByOff")
    public R countByOff() {
        return R.ok(projectComplaintRecordService.countByOff());
    }
}
