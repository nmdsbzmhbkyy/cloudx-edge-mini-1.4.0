package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectTaskStaff;
import com.aurine.cloudx.estate.service.ProjectInspectTaskStaffService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 巡检任务人员列表，用于app推送和领取(ProjectInspectTaskStaff)表控制层
 *
 * @author  * @author 王良俊
 * @since 2020-10-26 11:41:50
 */
@RestController
@RequestMapping("projectInspectTaskStaff")
@Api(value = "projectInspectTaskStaff", tags = "巡检任务人员列表，用于app推送和领取")
public class ProjectInspectTaskStaffController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectTaskStaffService projectInspectTaskStaffService;

    /**
     * 分页查询所有数据
     *
     * @param page                    分页对象
     * @param projectInspectTaskStaff 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectTaskStaff所有数据")
    public R selectAll(Page<ProjectInspectTaskStaff> page, ProjectInspectTaskStaff projectInspectTaskStaff) {
        return R.ok(this.projectInspectTaskStaffService.page(page, new QueryWrapper<>(projectInspectTaskStaff)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectTaskStaff单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectTaskStaffService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectInspectTaskStaff 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectTaskStaff数据")
    public R insert(@RequestBody ProjectInspectTaskStaff projectInspectTaskStaff) {
        return R.ok(this.projectInspectTaskStaffService.save(projectInspectTaskStaff));
    }

    /**
     * 修改数据
     *
     * @param projectInspectTaskStaff 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectTaskStaff数据")
    public R update(@RequestBody ProjectInspectTaskStaff projectInspectTaskStaff) {
        return R.ok(this.projectInspectTaskStaffService.updateById(projectInspectTaskStaff));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectTaskStaff数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectTaskStaffService.removeByIds(idList));
    }
}