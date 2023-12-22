package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectTaskDetail;
import com.aurine.cloudx.estate.service.ProjectInspectTaskDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备巡检任务明细(ProjectInspectTaskDetail)表控制层
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:58
 */
@RestController
@RequestMapping("projectInspectTaskDetail")
@Api(value = "projectInspectTaskDetail", tags = "设备巡检任务明细")
public class ProjectInspectTaskDetailController {

    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectTaskDetailService projectInspectTaskDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param taskDetail 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectTaskDetail所有数据")
    public R selectAll(Page<ProjectInspectTaskDetail> page, ProjectInspectTaskDetail taskDetail) {
        return R.ok(this.projectInspectTaskDetailService.page(page, new QueryWrapper<>(taskDetail)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectTaskDetail单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectTaskDetailService.getById(id));
    }

    /**
     * 根据任务id获取任务明细列表
     *
     * @param taskId 巡检任务主键
     */
    @GetMapping("listByTaskId/{taskId}")
    @SysLog("根据任务id获取任务明细列表")
    @ApiOperation(value = "根据任务id获取任务明细列表", notes = "根据任务id获取任务明细列表")
    public R listByTaskId(@PathVariable("taskId") String taskId) {
        return R.ok(this.projectInspectTaskDetailService.listByTaskId(taskId));
    }

    /**
     * 新增数据
     *
     * @param taskDetail 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectTaskDetail数据")
    public R insert(@RequestBody ProjectInspectTaskDetail taskDetail) {
        this.projectInspectTaskDetailService.saveInspectTaskDetail(taskDetail);
        return R.ok();
    }

    /**
     * 修改数据
     *
     * @param taskDetail 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectTaskDetail数据")
    public R update(@RequestBody ProjectInspectTaskDetail taskDetail) {
        return R.ok(this.projectInspectTaskDetailService.updateById(taskDetail));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectTaskDetail数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectTaskDetailService.removeByIds(idList));
    }

}