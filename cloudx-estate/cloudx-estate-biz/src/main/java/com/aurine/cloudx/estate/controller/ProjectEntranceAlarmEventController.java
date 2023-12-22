

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectEntranceAlarmEvent;
import com.aurine.cloudx.estate.service.ProjectEntranceAlarmEventService;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 报警事件
 *
 * @author 黄阳光
 * @date 2020-06-04 08:30:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectentrancealarmevent")
@Api(value = "projectentrancealarmevent", tags = "警情记录")
public class ProjectEntranceAlarmEventController {

    private final ProjectEntranceAlarmEventService projectEntranceAlarmEventService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param vo   报警事件
     * @return
     */
    @ApiOperation(value = "分页查询警情记录", notes = "分页查询警情记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/page")
    public R getProjectEntranceAlarmEventPage(Page page, ProjectEntranceAlarmEventVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceAlarmEventService.page(page, vo));
    }

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param vo   报警事件
     * @return
     */
    @ApiOperation(value = "分页查询警情记录", notes = "分页查询警情记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "operator", value = "用户id", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/appPage")
    public R getProjectEntranceAlarmEventAppPage(Page page, ProjectEntranceAlarmEventVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceAlarmEventService.appPage(page, vo));
    }


    /**
     * 通过id查询报警事件
     *
     * @param eventId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{eventId}")
    public R getById(@PathVariable("eventId") String eventId) {
        return R.ok(projectEntranceAlarmEventService.getById(eventId));
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
    @PreAuthorize("@pms.hasPermission('estate_pass_alarm_event_add')")
    public R save(@RequestBody ProjectEntranceAlarmEventVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceAlarmEventService.save(vo));
    }

    /**
     * 修改报警事件
     * @param projectEntranceAlarmEvent 报警事件
     * @return R
     */
    /*@ApiOperation(value = "修改报警事件", notes = "修改报警事件")
    @SysLog("修改报警事件" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_pass_alarm_event_edit')" )
    public R updateById(@RequestBody ProjectEntranceAlarmEvent projectEntranceAlarmEvent) {
        return R.ok(projectEntranceAlarmEventService.updateById(projectEntranceAlarmEvent));
    }*/

    /**
     * 通过id删除报警事件
     * @param seq id
     * @return R
     */
    /*@ApiOperation(value = "通过id删除报警事件", notes = "通过id删除报警事件")
    @SysLog("通过id删除报警事件" )
    @DeleteMapping("/{seq}" )
    @PreAuthorize("@pms.hasPermission('estate_pass_alarm_event_del')" )
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectEntranceAlarmEventService.removeById(seq));
    }*/

    /**
     * 获取当月事件信息
     */
    @ApiModelProperty(value = "获取当月警报数量")
    @GetMapping("/countMonthAlarmNum")
    public R getNum() {
        return R.ok(projectEntranceAlarmEventService.num(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()));
    }

    /**
     * 获取所有事件信息
     */
    @ApiModelProperty(value = "获取所有报警数量")
    @GetMapping("/countAlarmNum")
    public R getAllNum() {
        return R.ok(projectEntranceAlarmEventService.allNum(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()));
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
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceAlarmEventService.setStatus(vo));
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
        if (vo.getResult() == null || "".equals(vo.getResult())) {
            throw new RuntimeException("请输入处理情况信息");
        }
//        if (vo.getLivePic() == null || vo.getLivePic().equals("")) {
//            throw new RuntimeException("请上传图片");
//        }
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceAlarmEventService.putStatus(vo));
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
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceAlarmEventService.putBatchById(vo));
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
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceAlarmEventService.allHandle(vo));
    }

    /**
     * 按月份进统计
     *
     * @return
     */
    @ApiModelProperty(value = "按月份进统计")
    @GetMapping("/countByMonth/{date}")
    public R countByMonth(@PathVariable("date") String date) {
        return R.ok(projectEntranceAlarmEventService.countByMonth(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId(), date));
    }

    /**
     * 按月份进统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "按月份进统计未处理报警")
    @GetMapping("/countByMonthOff/{date}")
    public R countByMonthOff(@PathVariable("date") String date) {
        return R.ok(projectEntranceAlarmEventService.countByMonthOff(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId(), date));
    }

    /**
     * 统计未处理报警
     *
     * @return
     */
    @ApiModelProperty(value = "统计未处理报警")
    @GetMapping("/countByOff")
    public R countByOff() {
        return R.ok(projectEntranceAlarmEventService.countByOff());
    }

}
