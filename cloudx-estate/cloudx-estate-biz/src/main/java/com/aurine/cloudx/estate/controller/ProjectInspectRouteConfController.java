package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.InspectStatusConstants;
import com.aurine.cloudx.estate.entity.ProjectInspectRouteConf;
import com.aurine.cloudx.estate.service.ProjectInspectPlanService;
import com.aurine.cloudx.estate.service.ProjectInspectRouteConfService;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备巡检路线设置(ProjectInspectRouteConf)表控制层
 *
 * @author 王良俊
 * @since 2020-07-23 18:33:18
 */
@RestController
@RequestMapping("projectInspectRouteConf")
@Api(value = "projectInspectRouteConf", tags = "设备巡检路线设置")
public class ProjectInspectRouteConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectRouteConfService projectInspectRouteConfService;
    @Resource
    private ProjectInspectPlanService projectInspectPlanService;

    /**
     * 分页查询所有数据
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 所有数据
     */
    @GetMapping("/page")
    @SysLog("分页查询巡检路线")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectRouteConf所有数据")
    public R selectAll(Page page, ProjectInspectRouteConfSearchConditionVo query) {
        return R.ok(this.projectInspectRouteConfService.fetchList(page, query));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param inspectRouteId 路线uid
     * @return 单条数据
     */
    @GetMapping("{inspectRouteId}")
    @SysLog("使用巡检路线id获取巡检信息")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectRouteConf单条数据")
    public R selectOne(@PathVariable String inspectRouteId) {
        return R.ok(this.projectInspectRouteConfService.getById(inspectRouteId));
    }

    /**
     * 新增数据
     *
     * @param routeConfVo 路线vo对象
     * @return 新增结果
     */
    @PostMapping
    @SysLog("新增巡检路线")
    @ApiOperation(value = "新增数据", notes = "新增projectInspectRouteConf数据")
    public R insert(@RequestBody ProjectInspectRouteConfVo routeConfVo) {
        return R.ok(this.projectInspectRouteConfService.saveInspectRoute(routeConfVo));
    }

    /**
     * 修改数据
     *
     * @param routeConfVo 路线vo对象
     * @return 修改结果
     */
    @PutMapping
    @SysLog("修改巡检路线信息")
    @ApiOperation(value = "修改数据", notes = "修改projectInspectRouteConf数据")
    public R update(@RequestBody ProjectInspectRouteConfVo routeConfVo) {
        return R.ok(this.projectInspectRouteConfService.updateInspectRoute(routeConfVo));
    }

    /**
     * 获取巡检路线列表（全部用于下拉框）
     *
     * @return 巡检路线列表
     */
    @GetMapping("/list")
    @SysLog("获取巡检路线列表（下拉框）")
    @ApiOperation(value = "修改数据", notes = "获取巡检路线列表")
    public R list() {
        return R.ok(projectInspectRouteConfService.list(new QueryWrapper<ProjectInspectRouteConf>().lambda()
                .eq(ProjectInspectRouteConf::getStatus, InspectStatusConstants.ACTIVITY)));
    }

    /**
     * 获取巡检路线列表（全部用于下拉框）
     *
     * @return 巡检路线列表
     */
    @DeleteMapping("/{inspectRouteId}")
    @SysLog("删除巡检路线")
    @ApiOperation(value = "删除巡检路线", notes = "删除巡检路线")
    public R delete(@PathVariable("inspectRouteId") String inspectorRouteId) {
        if (projectInspectPlanService.routeIsUsed(inspectorRouteId)){
            throw new RuntimeException("该路线已与计划绑定无法删除");
        }
        return R.ok(projectInspectRouteConfService.removeInspectRoute(inspectorRouteId));
    }

}