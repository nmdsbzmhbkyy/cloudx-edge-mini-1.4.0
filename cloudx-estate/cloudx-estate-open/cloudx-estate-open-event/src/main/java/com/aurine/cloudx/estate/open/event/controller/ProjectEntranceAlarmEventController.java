package com.aurine.cloudx.estate.open.event.controller;

import com.aurine.cloudx.estate.open.event.bean.ProjectEntranceAlarmEventVoPage;
import com.aurine.cloudx.estate.open.event.fegin.RemoteProjectEntranceAlarmEventService;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 报警事件
 *
 * @author 黄阳光
 * @date 2020-06-04 08:30:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/entrance-alarm-event")
@Api(value = "entrance-alarm-event", tags = "警情记录")
public class ProjectEntranceAlarmEventController {


    @Resource
    private RemoteProjectEntranceAlarmEventService projectEntranceAlarmEventService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询警情记录", notes = "分页查询警情记录")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('entrance-alarm-event:get:page')")
    public R getProjectEntranceAlarmEventPage(ProjectEntranceAlarmEventVoPage page) {
        return projectEntranceAlarmEventService.getProjectEntranceAlarmEventPage(page);
    }


    /**
     * 通过id查询报警事件
     *
     * @param eventId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{eventId}")
    @ApiImplicitParam(name = "eventId", value = "警情记录ID", paramType = "path", required = true)
    @PreAuthorize("@pms.hasPermission('entrance-alarm-event:get:getById')")
    public R getById(@PathVariable("eventId") String eventId) {
        return projectEntranceAlarmEventService.getById(eventId);
    }

    /**
     * 新增报警事件
     *
     * @param vo 报警事件
     * @return R
     */
    @ApiOperation(value = "新增报警事件", notes = "新增报警事件")
    @SysLog("新增报警事件")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('entrance-alarm-event:post:save')")
    public R save(@RequestBody ProjectEntranceAlarmEventVo vo) {
        return projectEntranceAlarmEventService.save(vo);
    }


    /**
     * 获取当月事件信息
     */
    @ApiModelProperty(value = "获取当月警报数量")
    @GetMapping("/countMonthAlarmNum")
    @PreAuthorize("@pms.hasPermission('entrance-alarm-event:get:getNum')")
    public R getNum() {
        return projectEntranceAlarmEventService.getNum();
    }

    /**
     * 获取所有事件信息
     */
    @ApiModelProperty(value = "获取所有报警数量")
    @GetMapping("/countAlarmNum")
    public R getAllNum() {
        return projectEntranceAlarmEventService.getAllNum();
    }

    /**
     * 修改处理状态为处理中并储存开始处理时间
     *
     * @param vo
     * @return
     */
    @ApiModelProperty(value = "修改处理状态为处理中并储存开始处理时间")
    @PutMapping("/confirm")
    public R confirmByStatus(@RequestBody ProjectEntranceAlarmEventVo vo) {

        return projectEntranceAlarmEventService.confirmByStatus(vo);
    }

    /**
     * 修改处理状态为已处理并储存事件信息
     *
     * @param vo
     * @return
     */
    @ApiModelProperty(value = "修改处理状态为已处理并储存事件信息")
    @PutMapping("/modify")
    public R modifyByStatus(@RequestBody ProjectEntranceAlarmEventVo vo) {

        return projectEntranceAlarmEventService.modifyByStatus(vo);
    }

    /**
     * 批量处理
     *
     * @param vo
     * @return
     */
    @ApiModelProperty(value = "批量处理")
    @PutMapping("/batchModify")
    public R batchModifyStatus(@RequestBody ProjectEntranceAlarmEventVo vo) {

        return projectEntranceAlarmEventService.batchModifyStatus(vo);
    }

    /**
     * 全部处理
     *
     * @param vo
     * @return
     */
    @ApiModelProperty(value = "全部处理")
    @PutMapping("/allHandle")
    public R allHandle(@RequestBody ProjectEntranceAlarmEventVo vo) {

        return projectEntranceAlarmEventService.allHandle(vo);
    }

    /**
     * 按月份进统计
     *
     * @return
     */
    @ApiModelProperty(value = "按月份进统计")
    @GetMapping("/countByMonth/{date}")
    @ApiImplicitParam(name = "date", value = "月份时间", paramType = "path", required = true)
    public R countByMonth(@PathVariable("date") String date) {
        return projectEntranceAlarmEventService.countByMonth(date);
    }

    /**
     * 按月份进统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "按月份进统计未处理报警")
    @GetMapping("/countByMonthOff/{date}")
    @ApiImplicitParam(name = "date", value = "月份时间", paramType = "path", required = true)
    public R countByMonthOff(@PathVariable("date") String date) {
        return projectEntranceAlarmEventService.countByMonthOff(date);
    }

    /**
     * 统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "统计未处理报警")
    @GetMapping("/countByOff")
    public R countByOff() {
        return projectEntranceAlarmEventService.countByOff();
    }
}
