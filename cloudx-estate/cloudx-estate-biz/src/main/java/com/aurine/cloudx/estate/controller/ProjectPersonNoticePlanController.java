package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonNoticePlan;
import com.aurine.cloudx.estate.service.ProjectPersonNoticePlanService;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticePlanFormVo;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticePlanPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 住户通知计划(ProjectPersonNoticePlan)表控制层
 *
 * @author makejava
 * @since 2020-12-14 10:08:23
 */
@RestController
@RequestMapping("projectPersonNoticePlan")
@Api(value = "projectPersonNoticePlan", tags = "住户通知计划")
public class ProjectPersonNoticePlanController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPersonNoticePlanService projectPersonNoticePlanService;

    /**
     * 分页查询所有数据
     *
     * @param page                          分页对象
     * @param projectPersonNoticePlanPageVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectPersonNoticePlan所有数据")
    public R selectAll(Page page, ProjectPersonNoticePlanPageVo projectPersonNoticePlanPageVo) {
        String createStartTimeString = projectPersonNoticePlanPageVo.getCreateStartTimeString();
        String createEndTimeString = projectPersonNoticePlanPageVo.getCreateEndTimeString();
        if (StrUtil.isNotEmpty(createStartTimeString) && StrUtil.isNotEmpty(createEndTimeString)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime createStartTime = LocalDateTime.parse(createStartTimeString, df);
            LocalDateTime createEndTime = LocalDateTime.parse(createEndTimeString, df);
            projectPersonNoticePlanPageVo.setCreateStartTime(createStartTime);
            projectPersonNoticePlanPageVo.setCreateEndTime(createEndTime);
        }
        return R.ok(this.projectPersonNoticePlanService.pageNoticePlan(page, projectPersonNoticePlanPageVo));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param planId 主键
     * @return 单条数据
     */
    @GetMapping("{planId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectPersonNoticePlan单条数据")
    public R selectOne(@PathVariable("planId") String planId) {
        return R.ok(this.projectPersonNoticePlanService.getByPlanId(planId));
    }

    /**
     * 切换状态
     *
     * @param projectPersonNoticePlan 实体对象
     * @return 修改结果
     */
    @PutMapping("switchAvtive")
    @ApiOperation(value = "切换状态", notes = "切换状态")
    public R switchAvtive(@RequestBody ProjectPersonNoticePlan projectPersonNoticePlan) {
        return R.ok(this.projectPersonNoticePlanService.updateById(projectPersonNoticePlan));
    }


    /**
     * 新增数据
     *
     * @param projectPersonNoticePlanFormVo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectPersonNoticePlan数据")
    public R insert(@RequestBody ProjectPersonNoticePlanFormVo projectPersonNoticePlanFormVo) {
        return R.ok(this.projectPersonNoticePlanService.saveNoticePlan(projectPersonNoticePlanFormVo));
    }

    /**
     * 修改数据
     *
     * @param projectPersonNoticePlanFormVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectPersonNoticePlan数据")
    public R update(@RequestBody ProjectPersonNoticePlanFormVo projectPersonNoticePlanFormVo) {
        return R.ok(this.projectPersonNoticePlanService.updateNoticePlan(projectPersonNoticePlanFormVo));
    }

    /**
     * 删除数据
     *
     * @param planId 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/{planId}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectPersonNoticePlan数据")
    public R delete(@PathVariable("planId") String planId) {
        return R.ok(this.projectPersonNoticePlanService.removeByPlanId(planId));
    }
}