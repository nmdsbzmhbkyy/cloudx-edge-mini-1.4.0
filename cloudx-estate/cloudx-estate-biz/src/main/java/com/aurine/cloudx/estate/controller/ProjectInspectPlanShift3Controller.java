package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectPlanShift3;
import com.aurine.cloudx.estate.service.ProjectInspectPlanShift3Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备巡检计划班次信息（自定义）(ProjectInspectPlanShift3)表控制层
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:26
 */
@RestController
@RequestMapping("projectInspectPlanShift3")
@Api(value = "projectInspectPlanShift3", tags = "设备巡检计划班次信息（自定义）")
public class ProjectInspectPlanShift3Controller {

    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectPlanShift3Service projectInspectPlanShift3Service;

    /**
     * 分页查询所有数据
     *
     * @param page  分页对象
     * @param planShift3 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectPlanShift3所有数据")
    public R selectAll(Page<ProjectInspectPlanShift3> page, ProjectInspectPlanShift3 planShift3) {
        return R.ok(this.projectInspectPlanShift3Service.page(page, new QueryWrapper<>(planShift3)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectPlanShift3单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectPlanShift3Service.getById(id));
    }

    /**
     * 新增数据
     *
     * @param planShift3 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectPlanShift3数据")
    public R insert(@RequestBody ProjectInspectPlanShift3 planShift3) {
        return R.ok(this.projectInspectPlanShift3Service.save(planShift3));
    }

    /**
     * 修改数据
     *
     * @param planShift3 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectPlanShift3数据")
    public R update(@RequestBody ProjectInspectPlanShift3 planShift3) {
        return R.ok(this.projectInspectPlanShift3Service.updateById(planShift3));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectPlanShift3数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectPlanShift3Service.removeByIds(idList));
    }

}