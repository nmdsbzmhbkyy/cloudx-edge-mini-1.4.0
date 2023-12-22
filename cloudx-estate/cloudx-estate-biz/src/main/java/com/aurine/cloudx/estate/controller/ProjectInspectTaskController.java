package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectTask;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskPageVo;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskSearchConditionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备巡检任务(ProjectInspectTask)表控制层
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:49
 */
@RestController
@RequestMapping("projectInspectTask")
@Api(value = "projectInspectTask", tags = "设备巡检任务")
public class ProjectInspectTaskController {

    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectTaskService projectInspectTaskService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param query 查询vo对象
     * @return 所有数据
     */
    @GetMapping("/page")
    @SysLog("分页查询任务数据")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectTask所有数据")
    public R selectAll(Page<ProjectInspectTask> page, ProjectInspectTaskSearchConditionVo query) {
        // 每次查询都检查是否超时并更新状态
        projectInspectTaskService.dealTimeOut();
        return R.ok(this.projectInspectTaskService.fetchList(page, query));
    }

    /**
     * app端获取分页数据
     *
     * @param page 分页对象
     * @param query 查询vo对象
     * @return 所有数据
     */
    @GetMapping("/taskPage")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectTask所有数据 app端使用")
    public Page<ProjectInspectTaskPageVo> selectAllApp(Page<ProjectInspectTask> page, ProjectInspectTaskSearchConditionVo query) {
        projectInspectTaskService.dealTimeOut();
        return this.projectInspectTaskService.fetchList(page, query);
    }

    /**
     * <p>
     *  取消任务
     * </p>
     *
     * @param taskId 任务ID
    */
    @GetMapping("/cancel/{taskId}")
    @SysLog("取消任务")
    @ApiOperation(value = "取消任务", notes = "取消任务")
    public R cancel(@PathVariable("taskId") String taskId) {
        return R.ok(this.projectInspectTaskService.cancel(taskId));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param taskId 主键
     * @return 单条数据
     */
    @GetMapping("{taskId}")
    @SysLog("获取任务数据")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectTask单条数据")
    public R selectOne(@PathVariable("taskId") String taskId) {
        return R.ok(this.projectInspectTaskService.getById(taskId));
    }

    /**
     * 新增数据
     *
     * @param task 任务实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectTask数据")
    public R insert(@RequestBody ProjectInspectTask task) {
        return R.ok(this.projectInspectTaskService.save(task));
    }

    /**
     * 修改数据
     *
     * @param task 任务实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectTask数据")
    public R update(@RequestBody ProjectInspectTask task) {
        return R.ok(this.projectInspectTaskService.updateById(task));
    }

    /**
     * 删除数据
     *
     * @param taskId 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/{taskId}")
    @SysLog("删除任务")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectTask数据")
    public R delete(@PathVariable("taskId") String taskId) {
        return R.ok(this.projectInspectTaskService.removeTaskById(taskId));
    }

    /**
     * 处理所有超时的任务
     */
    @GetMapping("dealTimeOutTask")
    @SysLog("超时任务批量处理")
    @ApiOperation(value = "处理所有超时的任务", notes = "处理所有超时的任务")
    public R dealTimeOutTask() {
        return R.ok(this.projectInspectTaskService.dealTimeOut());
    }

}