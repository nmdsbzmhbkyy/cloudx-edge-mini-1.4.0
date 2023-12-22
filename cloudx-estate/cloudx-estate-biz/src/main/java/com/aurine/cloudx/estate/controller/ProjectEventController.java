

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.service.ProjectEntranceEventService;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 通行事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/event" )
@Api(value = "event", tags = "通行事件记录管理")
public class ProjectEventController {

    private final ProjectEntranceEventService projectEntranceEventService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectEventSearchCondition 通行事件记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectEventPage(Page page,@ModelAttribute ProjectEventSearchCondition projectEventSearchCondition) {
        projectEventSearchCondition.setProjectId(ProjectContextHolder.getProjectId());
        projectEventSearchCondition.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceEventService.page(page, projectEventSearchCondition));
    }


    /**
     * 通过id查询通行事件记录
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}" )
    public R getById(@PathVariable("seq" ) Long seq) {
        return R.ok(projectEntranceEventService.getById(seq));
    }

    /**
     * 新增通行事件记录
     * @param projectEventVo 通行事件记录
     * @return R
     */
    @ApiOperation(value = "新增通行事件记录", notes = "新增通行事件记录")
    @SysLog("新增通行事件记录" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectevent_add')" )
    public R save(@RequestBody ProjectEventVo projectEventVo) {
        projectEventVo.setProjectId(ProjectContextHolder.getProjectId());
        projectEventVo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntranceEventService.addEvent(projectEventVo));
    }

    @ApiOperation(value = "事件数量统计")
    @SysLog("事件数量统计")
    @GetMapping("/num")
    public R find() {
        return R.ok(projectEntranceEventService.getEventTypeNum(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()));
    }
    /**
     * 修改通行事件记录
     * @param projectEntranceEvent 通行事件记录
     * @return R
     */
    @ApiOperation(value = "修改通行事件记录", notes = "修改通行事件记录")
    @SysLog("修改通行事件记录" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectevent_edit')" )
    public R updateById(@RequestBody ProjectEntranceEvent projectEntranceEvent) {
        return R.ok(projectEntranceEventService.updateById(projectEntranceEvent));
//        return R.ok(projectEventService.updateByEventStatus(projectEventVo));
    }

    /**
     * 通过id删除通行事件记录
     * @param seq id
     * @return R
     */
    /*@ApiOperation(value = "通过id删除通行事件记录", notes = "通过id删除通行事件记录")
    @SysLog("通过id删除通行事件记录" )
    @DeleteMapping("/{seq}" )
    @PreAuthorize("@pms.hasPermission('estate_projectevent_del')" )
    public R removeById(@PathVariable Long seq) {
        return R.ok(projectEventService.removeById(seq));
    }*/

}
