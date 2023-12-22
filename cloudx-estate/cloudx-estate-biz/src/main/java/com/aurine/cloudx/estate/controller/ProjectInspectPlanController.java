package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.estate.entity.ProjectInspectPlan;
import com.aurine.cloudx.estate.service.ProjectInspectPlanService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备巡检计划设置(ProjectInspectPlan)表控制层
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:04
 */
@RestController
@RequestMapping("projectInspectPlan")
@Api(value = "projectInspectPlan", tags = "设备巡检计划设置")
public class ProjectInspectPlanController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectPlanService projectInspectPlanService;

    /**
     * 分页查询所有数据
     *
     * @param page  分页对象
     * @param query 查询条件vo对象
     * @return 所有数据
     */
    @GetMapping("/page")
    @SysLog("巡检计划分页查询")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectPlan所有数据")
    public R selectAll(Page<ProjectInspectPlan> page, ProjectInspectPlanSearchConditionVo query) {
        projectInspectPlanService.dealOutdatedPlan();
        return R.ok(this.projectInspectPlanService.fetchList(page, query));
    }

    /**
     * 通过主键获取到计划vo对象
     *
     * @param planId 主键
     * @return 单条数据
     */
    @GetMapping("{planId}")
    @SysLog("通过id获取巡检计划")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectPlan单条数据")
    public R selectOne(@PathVariable String planId) {
        return R.ok(this.projectInspectPlanService.getPlanById(planId));
    }

    /**
     * 新增数据
     *
     * @param planVo 计划vo对象
     * @return 新增结果
     */
    @PostMapping
    @SysLog("添加巡检计划")
    @ApiOperation(value = "新增数据", notes = "新增projectInspectPlan数据")
    public R insert(@RequestBody ProjectInspectPlanVo planVo) {
        String[] checkInTypeArr = planVo.getCheckInTypeArr();
        if (ArrayUtil.isNotEmpty(checkInTypeArr)) {
            planVo.setCheckInType(StringUtils.join(checkInTypeArr, ","));
        }
        int count = projectInspectPlanService.count(new QueryWrapper<ProjectInspectPlan>().lambda()
                .eq(ProjectInspectPlan::getPlanName, planVo.getPlanName()));
        if (count != 0) {
            throw new RuntimeException("计划名称重复");
        }
        return R.ok(this.projectInspectPlanService.saveOrUpdatePlan(planVo));
    }

    /**
     * 修改数据
     *
     * @param planVo 计划vo对象
     * @return 修改结果
     */
    @PutMapping
    @SysLog("修改巡检计划")
    @ApiOperation(value = "修改数据", notes = "修改projectInspectPlan数据")
    public R update(@RequestBody ProjectInspectPlanVo planVo) {
        return R.ok(this.projectInspectPlanService.saveOrUpdatePlan(planVo));
    }

    /**
     * 删除巡检计划
     *
     * @param planId 计划id
     * @return 删除结果
     */
    @DeleteMapping("/{planId}")
    @SysLog("删除巡检计划")
    @ApiOperation(value = "删除巡检计划", notes = "通过id删除巡检计划")
    public R delete(@PathVariable("planId") String planId) {
        return R.ok(this.projectInspectPlanService.removePlan(planId));
    }

    /**
     * 删除数据
     *
     * @param planId 计划id
     * @return 删除结果
     */
    @GetMapping("/changeStatus/{planId}/{status}")
    @SysLog("启用/禁用巡检计划")
    @ApiOperation(value = "启用或禁用计划", notes = "启用或禁用计划")
    public R changeStatus(@PathVariable("planId") String planId, @PathVariable("status") char status) {
        return R.ok(this.projectInspectPlanService.changeStatus(planId, status));
    }

}