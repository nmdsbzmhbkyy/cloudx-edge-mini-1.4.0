package com.aurine.cloudx.estate.open.event.controller;

import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.open.event.bean.ProjectEventSearchConditionPage;
import com.aurine.cloudx.estate.open.event.fegin.RemoteProjectEventService;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 通行事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/event" )
@Api(value = "event", tags = "人行事件记录管理")
public class ProjectEventController {

    @Resource
    private RemoteProjectEventService projectEntranceEventService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('event:get:page')")
    public R getProjectEventPage(ProjectEventSearchConditionPage page) {

        return projectEntranceEventService.getProjectEventPage(page);
    }


    /**
     * 通过id查询通行事件记录
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}" )
    @ApiImplicitParam(name = "seq",value = "通行事件记录ID",paramType = "path",required = true)
    public R getById(@PathVariable("seq" ) Long seq) {
        return projectEntranceEventService.getById(seq);
    }


    @ApiOperation(value = "新增通行事件记录", notes = "新增通行事件记录")
    @SysLog("新增通行事件记录" )
    @PostMapping
    public R save(@RequestBody ProjectEventVo projectEventVo) {

        return projectEntranceEventService.save(projectEventVo);
    }

    @ApiOperation(value = "事件数量统计")
    @SysLog("事件数量统计")
    @GetMapping("/num")
    public R find() {
        return projectEntranceEventService.find();
    }
    /**
     * 修改通行事件记录
     * @param projectEntranceEvent 通行事件记录
     * @return R
     */
    @ApiOperation(value = "修改通行事件记录", notes = "修改通行事件记录")
    @SysLog("修改通行事件记录" )
    @PutMapping
    public R updateById(@RequestBody ProjectEntranceEvent projectEntranceEvent) {
        return projectEntranceEventService.updateById(projectEntranceEvent);
    }


}
