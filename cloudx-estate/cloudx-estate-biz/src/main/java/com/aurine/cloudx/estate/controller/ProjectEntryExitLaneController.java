package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.aurine.cloudx.estate.service.ProjectEntryExitLaneService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 出入口车道信息(ProjectEntryExitLane)表控制层
 *
 * @author 王良俊
 * @since 2020-08-17 11:58:43
 */
@RestController
@RequestMapping("projectEntryExitLane")
@Api(value = "projectEntryExitLane", tags = "出入口车道信息")
public class ProjectEntryExitLaneController {
    /**
     * 服务对象
     */


    @Resource
    private ProjectEntryExitLaneService projectEntryExitLaneService;

    /**
     * 分页查询所有数据
     *
     * @param page                 分页对象
     * @param projectEntryExitLane 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectEntryExitLane所有数据")
    public R selectAll(Page<ProjectEntryExitLane> page, ProjectEntryExitLane projectEntryExitLane) {
        return R.ok(this.projectEntryExitLaneService.page(page, new QueryWrapper<>(projectEntryExitLane)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param laneId 车道主键
     * @return 单条数据
     */
    @GetMapping("{laneId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectEntryExitLane单条数据")
    public R selectOne(@PathVariable("laneId") String laneId) {
        return R.ok(this.projectEntryExitLaneService.getById(laneId));
    }

    /**
     * 通过车场ID查询车道列表
     *
     * @param parkId 车场ID
     * @return 列表数据
     */
    @GetMapping("/listByParkId/{parkId}")
    @ApiOperation(value = "通过车场ID查询车道列表", notes = "通过车场ID查询车道列表")
    public R listByParkId(@PathVariable("parkId") String parkId) {
        return R.ok(this.projectEntryExitLaneService.listByParkId(parkId));
    }

    /**
     * 新增数据
     *
     * @param projectEntryExitLane 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectEntryExitLane数据")
    public R insert(@RequestBody ProjectEntryExitLane projectEntryExitLane) {
        return R.ok(this.projectEntryExitLaneService.save(projectEntryExitLane));
    }

    /**
     * 修改数据
     *
     * @param projectEntryExitLane 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectEntryExitLane数据")
    public R update(@RequestBody ProjectEntryExitLane projectEntryExitLane) {
        return R.ok(this.projectEntryExitLaneService.updateById(projectEntryExitLane));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectEntryExitLane数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectEntryExitLaneService.removeByIds(idList));
    }

    /**
     * 更新车道数据
     *
     * @param parkId 车场ID
     * @return 删除结果
     */
    @GetMapping("/updateLane/{parkId}")
    @ApiOperation(value = "更新车道数据", notes = "更新车道数据")
    public R updateLane(@PathVariable("parkId") String parkId) {
        return R.ok(projectEntryExitLaneService.syncLane(parkId));
    }

}