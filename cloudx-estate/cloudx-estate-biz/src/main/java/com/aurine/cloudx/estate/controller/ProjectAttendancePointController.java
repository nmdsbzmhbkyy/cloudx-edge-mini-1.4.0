package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.vo.ProjectAttendancePointForm;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectAttendancePoint;
import com.aurine.cloudx.estate.service.ProjectAttendancePointService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.util.List;

/**
 * 考勤点设置(ProjectAttendancePoint)表控制层
 *
 * @author xull
 * @since 2021-03-03 10:52:24
 */
@RestController
@RequestMapping("project-attendance-point")
@Api(value = "projectAttendancePoint", tags = "考勤点设置")
public class ProjectAttendancePointController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectAttendancePointService projectAttendancePointService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param projectAttendancePoint 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectAttendancePoint所有数据")
    public R selectAll(Page<ProjectAttendancePoint> page, ProjectAttendancePoint projectAttendancePoint) {
        return R.ok(this.projectAttendancePointService.page(page, new QueryWrapper<>(projectAttendancePoint)));
    }


    /**
     * @description: 查询所有数据
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/5/7 13:52
     */
    @GetMapping("/getAll")
    @ApiOperation(value = "查询所有数据", notes = "查询projectAttendancePoint所有数据")
    public R getAll() {
        return R.ok(projectAttendancePointService.list());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectAttendancePoint单条数据")
    public R selectOne(@PathVariable String id) {
        return R.ok(this.projectAttendancePointService.getById(id));
    }

    /**
     * 批量添加考勤点数据
     *
     * @param form
     * @return
     */
    @PostMapping("/addBatch")
    public R addBatch(@RequestBody ProjectAttendancePointForm form) {
        if (form.getPlaceVo() == null || form.getPlaceVo().size() == 0) {
            return R.failed("考勤地址不能为空");
        }
        return R.ok(this.projectAttendancePointService.addBatch(form));
    }

    /**
     * 新增数据
     *
     * @param projectAttendancePoint 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectAttendancePoint数据")
    public R insert(@RequestBody ProjectAttendancePoint projectAttendancePoint) {
        return R.ok(this.projectAttendancePointService.save(projectAttendancePoint));
    }

    /**
     * 修改数据
     *
     * @param projectAttendancePoint 实体对象
     * @return 修改结果
     */
    @PutMapping("{id}")
    @ApiOperation(value = "修改数据", notes = "修改projectAttendancePoint数据")
    public R update(@PathVariable("id") String id, @RequestBody ProjectAttendancePoint projectAttendancePoint) {
        projectAttendancePoint.setPointId(id);
        return R.ok(this.projectAttendancePointService.updateById(projectAttendancePoint));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectAttendancePoint数据")
    public R delete(@PathVariable("id") String id) {
        return R.ok(this.projectAttendancePointService.removeById(id));
    }
}
