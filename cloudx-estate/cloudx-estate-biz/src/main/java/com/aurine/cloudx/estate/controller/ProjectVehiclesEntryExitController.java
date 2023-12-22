package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectVehiclesEntryExit;
import com.aurine.cloudx.estate.service.ProjectVehiclesEntryExitService;
import com.aurine.cloudx.estate.vo.ProjectVehiclesEntryExitVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 车辆出入口信息表(ProjectVehiclesEntryExit)表控制层
 *
 * @author 王良俊
 * @since 2020-08-17 10:08:53
 */
@RestController
@RequestMapping("projectVehiclesEntryExit")
@Api(value = "projectVehiclesEntryExit", tags = "车辆出入口信息表")
public class ProjectVehiclesEntryExitController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectVehiclesEntryExitService projectVehiclesEntryExitService;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param projectVehiclesEntryExit 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectVehiclesEntryExit所有数据")
    public R selectAll(Page<ProjectVehiclesEntryExit> page, ProjectVehiclesEntryExit projectVehiclesEntryExit) {
        return R.ok(this.projectVehiclesEntryExitService.page(page, new QueryWrapper<>(projectVehiclesEntryExit)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param entryId 出入口主键
     * @return 单条数据
     */
    @GetMapping("{entryId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectVehiclesEntryExit单条数据")
    public R selectOne(@PathVariable("entryId") String entryId) {
        return R.ok(this.projectVehiclesEntryExitService.getByEntryId(entryId));
    }

    /**
     * 新增数据
     *
     * @param entryExitVo 出入口信息vo对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectVehiclesEntryExit数据")
    public R insert(@RequestBody ProjectVehiclesEntryExitVo entryExitVo) {
        return R.ok(this.projectVehiclesEntryExitService.saveEntryExit(entryExitVo));
    }

    /**
     * 修改数据
     *
     * @param entryExitVo vo对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectVehiclesEntryExit数据")
    public R update(@RequestBody ProjectVehiclesEntryExitVo entryExitVo) {
        return R.ok(this.projectVehiclesEntryExitService.updateEntryExit(entryExitVo));
    }

    /**
     * 删除数据
     *
     * @param entryId 出入口ID
     * @return 删除结果
     */
    @DeleteMapping("{entryId}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectVehiclesEntryExit数据")
    public R delete(@PathVariable("entryId") String entryId) {
        return R.ok(this.projectVehiclesEntryExitService.removeEntryExit(entryId));
    }

    /**
     * 根据停车场ID获取出入口树形数据
     *
     * @param parkId 停车场ID
     */
    @GetMapping("/getTree/{parkId}")
    @ApiOperation(value = "根据停车场ID获取出入口树形数据", notes = "根据停车场ID获取出入口树形数据")
    public R getTree(@PathVariable("parkId") String parkId) {
        return R.ok(this.projectVehiclesEntryExitService.getEntryExitTreeByParkId(parkId));
    }
}