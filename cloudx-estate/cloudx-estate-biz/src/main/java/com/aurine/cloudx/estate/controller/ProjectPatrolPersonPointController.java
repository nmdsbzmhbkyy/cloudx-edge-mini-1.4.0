package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectPatrolPersonPoint;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonPointService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 人员巡更巡点记录(ProjectPatrolPersonPoint)表控制层
 *
 * @author 王良俊
 * @since 2020-09-17 10:29:21
 */
@RestController
@RequestMapping("projectPatrolPersonPoint")
@Api(value = "projectPatrolPersonPoint", tags = "人员巡更巡点记录")
public class ProjectPatrolPersonPointController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPatrolPersonPointService projectPatrolPersonPointService;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param projectPatrolPersonPoint 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectPatrolPersonPoint所有数据")
    public R selectAll(Page<ProjectPatrolPersonPoint> page, ProjectPatrolPersonPoint projectPatrolPersonPoint) {
        return R.ok(this.projectPatrolPersonPointService.page(page, new QueryWrapper<>(projectPatrolPersonPoint)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectPatrolPersonPoint单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectPatrolPersonPointService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectPatrolPersonPoint 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectPatrolPersonPoint数据")
    public R insert(@RequestBody ProjectPatrolPersonPoint projectPatrolPersonPoint) {
        return R.ok(this.projectPatrolPersonPointService.save(projectPatrolPersonPoint));
    }

    /**
     * 修改数据
     *
     * @param projectPatrolPersonPoint 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectPatrolPersonPoint数据")
    public R update(@RequestBody ProjectPatrolPersonPoint projectPatrolPersonPoint) {
        return R.ok(this.projectPatrolPersonPointService.updateById(projectPatrolPersonPoint));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectPatrolPersonPoint数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectPatrolPersonPointService.removeByIds(idList));
    }
}