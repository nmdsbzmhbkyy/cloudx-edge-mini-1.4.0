package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectPlanShiftStaff;
import com.aurine.cloudx.estate.service.ProjectInspectPlanShiftStaffService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 巡检计划班次执行人员列表(ProjectInspectPlanShiftStaff)表控制层
 *
 * @author 王良俊
 * @since 2020-07-27 10:37:22
 */
@RestController
@RequestMapping("projectInspectPlanShiftStaff")
@Api(value = "projectInspectPlanShiftStaff", tags = "巡检计划班次执行人员列表")
public class ProjectInspectPlanShiftStaffController {

    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectPlanShiftStaffService projectInspectPlanShiftStaffService;

    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param planShiftStaff 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectPlanShiftStaff所有数据")
    public R selectAll(Page<ProjectInspectPlanShiftStaff> page, ProjectInspectPlanShiftStaff planShiftStaff) {
        return R.ok(this.projectInspectPlanShiftStaffService.page(page, new QueryWrapper<>(planShiftStaff)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectPlanShiftStaff单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectPlanShiftStaffService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param planShiftStaff 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectPlanShiftStaff数据")
    public R insert(@RequestBody ProjectInspectPlanShiftStaff planShiftStaff) {
        return R.ok(this.projectInspectPlanShiftStaffService.save(planShiftStaff));
    }

    /**
     * 修改数据
     *
     * @param planShiftStaff 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectPlanShiftStaff数据")
    public R update(@RequestBody ProjectInspectPlanShiftStaff planShiftStaff) {
        return R.ok(this.projectInspectPlanShiftStaffService.updateById(planShiftStaff));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectPlanShiftStaff数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectPlanShiftStaffService.removeByIds(idList));
    }

}