package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import com.aurine.cloudx.estate.service.ProjectRepairRecordService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.util.MessageTextUtil;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.vo.ProjectRepairRecordFromVo;
import com.aurine.cloudx.estate.vo.ProjectRepairRecordPageVo;
import com.aurine.cloudx.estate.vo.ProjectRepairRecordResultFormVo;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


/**
 * 项目报修服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:36:01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectRepairRecord")
@Api(value = "projectRepairRecord", tags = "项目报修服务记录管理")
public class ProjectRepairRecordController {

    private final ProjectRepairRecordService projectRepairRecordService;

    private final ProjectStaffService projectStaffService;

    private final NoticeUtil noticeUtil;
    /**
     * 报修服务菜单ID
     */
    private final Integer carAuditMenuId = 10645;

    /**
     * 分页查询
     *
     * @param page                      分页对象
     * @param projectRepairRecordPageVo 项目报修服务记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectRepairRecordPage(Page page, ProjectRepairRecordPageVo projectRepairRecordPageVo) {
        if (projectRepairRecordPageVo.getStartTimeString() != null && !"".equals(projectRepairRecordPageVo.getStartTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectRepairRecordPageVo.setStartTime(LocalDate.parse(projectRepairRecordPageVo.getStartTimeString(), fmt));
        }
        if (projectRepairRecordPageVo.getEndTimeString() != null && !"".equals(projectRepairRecordPageVo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectRepairRecordPageVo.setEndTime(LocalDate.parse(projectRepairRecordPageVo.getEndTimeString(), fmt));
        }
        return R.ok(projectRepairRecordService.pageRepairRecord(page, projectRepairRecordPageVo));
    }


    /**
     * 通过id查询项目报修服务记录
     *
     * @param repairId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{repairId}")
    public R getById(@PathVariable("repairId") String repairId) {
        return R.ok(projectRepairRecordService.getByRepairId(repairId));
    }

    /**
     * 新增项目报修服务记录
     *
     * @param projectRepairRecordFromVo 项目报修服务记录
     * @return R
     */
    @ApiOperation(value = "新增项目报修服务记录", notes = "新增项目报修服务记录")
    @SysLog("新增项目报修服务记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_repairrecord_add')")
    public R save(@RequestBody ProjectRepairRecordFromVo projectRepairRecordFromVo) {
        if (projectRepairRecordFromVo.getReserveTimeBeginString() != null && !"".equals(projectRepairRecordFromVo.getReserveTimeBeginString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectRepairRecordFromVo.setReserveTimeBegin(LocalDateTime.parse(projectRepairRecordFromVo.getReserveTimeBeginString(), fmt));
        }
        if (projectRepairRecordFromVo.getReserveTimeEndString() != null && !"".equals(projectRepairRecordFromVo.getReserveTimeEndString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectRepairRecordFromVo.setReserveTimeEnd(LocalDateTime.parse(projectRepairRecordFromVo.getReserveTimeEndString(), fmt));
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectRepairRecordFromVo.setRepairId(uuid);
        ProjectRepairRecord projectRepairRecord = new ProjectRepairRecord();
        BeanUtils.copyProperties(projectRepairRecordFromVo, projectRepairRecord);

        /**
         * 维修任务提醒
         *
         * 有新的报修工单等待接单
         * 联系人：【住户姓名】
         * 联系电话：【住户手机号】
         * 派单时间：
         * 报修内容：【住户所填写的报修内容】
         */
        List<String> staffIdList = projectStaffService.getStaffIdListByMenuId(carAuditMenuId);
        noticeUtil.send(true, "维修任务提醒",
                MessageTextUtil.init()
                        .append("有新的报修工单等待接单")
                        .p("联系人：%s", projectRepairRecordFromVo.getPersonName())
                        .p("联系电话：%s", projectRepairRecordFromVo.getPhoneNumber())
                        .p("派单时间：")
                        .p("报修内容：%s", projectRepairRecordFromVo.getContent())
                        .toString(),
                staffIdList
        );
        return R.ok(projectRepairRecordService.save(projectRepairRecord));
    }

    /**
     * 修改项目报修服务记录
     *
     * @param projectRepairRecordFromVo 项目报修服务记录
     * @return R
     */
    @ApiOperation(value = "修改项目报修服务记录", notes = "修改项目报修服务记录")
    @SysLog("修改项目报修服务记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_repairrecord_edit')")
    public R updateById(@RequestBody ProjectRepairRecordFromVo projectRepairRecordFromVo) {
        if (projectRepairRecordFromVo.getReserveTimeBeginString() != null && !"".equals(projectRepairRecordFromVo.getReserveTimeBeginString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectRepairRecordFromVo.setReserveTimeBegin(LocalDateTime.parse(projectRepairRecordFromVo.getReserveTimeBeginString(), fmt));
        }
        if (projectRepairRecordFromVo.getReserveTimeEndString() != null && !"".equals(projectRepairRecordFromVo.getReserveTimeEndString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectRepairRecordFromVo.setReserveTimeEnd(LocalDateTime.parse(projectRepairRecordFromVo.getReserveTimeEndString(), fmt));
        }
        ProjectRepairRecord projectRepairRecord = new ProjectRepairRecord();
        BeanUtils.copyProperties(projectRepairRecordFromVo, projectRepairRecord);
        return R.ok(projectRepairRecordService.updateById(projectRepairRecord));
    }

    /**
     * 通过id删除项目报修服务记录
     *
     * @param repairId id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目报修服务记录", notes = "通过id删除项目报修服务记录")
    @SysLog("通过id删除项目报修服务记录")
    @DeleteMapping("/{repairId}")
    @PreAuthorize("@pms.hasPermission('estate_repairrecord_del')")
    public R removeById(@PathVariable("repairId") String repairId) {
        return R.ok(projectRepairRecordService.removeById(repairId));
    }

    /**
     * 派单
     *
     * @param projectRepairRecord
     * @return R
     */
    @ApiOperation(value = "派单", notes = "派单")
    @SysLog("派单")
    @PutMapping("/sendOrder")
    public R sendOrder(@RequestBody ProjectRepairRecord projectRepairRecord) {
        return R.ok(projectRepairRecordService.setHandler(projectRepairRecord.getRepairId(), projectRepairRecord.getHandler()));
    }

    /**
     * 抢单
     *
     * @param projectRepairRecord
     * @return R
     */
    @ApiOperation(value = "抢单", notes = "抢单")
    @SysLog("抢单")
    @PutMapping("/robOrder")
    public R robOrder(@RequestBody ProjectRepairRecord projectRepairRecord) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        return R.ok(projectRepairRecordService.receivingOrders(projectRepairRecord.getRepairId(), projectStaffVo.getStaffId()));
    }

    /**
     * 完成订单
     *
     * @param projectRepairRecordResultFormVo
     * @return R
     */
    @ApiOperation(value = "完成订单", notes = "完成订单")
    @SysLog("完成订单")
    @PutMapping("/doneOrder")
    public R doneOrder(@RequestBody ProjectRepairRecordResultFormVo projectRepairRecordResultFormVo) {
        return projectRepairRecordService.setResult(projectRepairRecordResultFormVo);
    }

    /**
     * 按月份进统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "按月份统计报警")
    @GetMapping("/countByMonth/{date}")
    public R countByMonth(@PathVariable("date") String date) {
        return R.ok(projectRepairRecordService.countByMonth(date));
    }

    /**
     * 统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "统计未处理报警")
    @GetMapping("/countByOff")
    public R countByOff() {
        return R.ok(projectRepairRecordService.countByOff());
    }
}
