package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectRoutePointConf;
import com.aurine.cloudx.estate.service.ProjectInspectRoutePointConfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备巡检路线与巡更点关系表(ProjectInspectRoutePointConf)表控制层
 *
 * @author 王良俊
 * @since 2020-07-23 18:32:49
 */
@RestController
@RequestMapping("projectInspectRoutePointConf")
@Api(value = "projectInspectRoutePointConf", tags = "设备巡检路线与巡更点关系表")
public class ProjectInspectRoutePointConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectRoutePointConfService projectInspectRoutePointConfService;

    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param routePointConf 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectRoutePointConf所有数据")
    public R selectAll(Page<ProjectInspectRoutePointConf> page, ProjectInspectRoutePointConf routePointConf) {
        return R.ok(this.projectInspectRoutePointConfService.page(page, new QueryWrapper<>(routePointConf)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectRoutePointConf单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectRoutePointConfService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param routePointConf 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectRoutePointConf数据")
    public R insert(@RequestBody ProjectInspectRoutePointConf routePointConf) {
        return R.ok(this.projectInspectRoutePointConfService.save(routePointConf));
    }

    /**
     * 修改数据
     *
     * @param routePointConf 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectRoutePointConf数据")
    public R update(@RequestBody ProjectInspectRoutePointConf routePointConf) {
        return R.ok(this.projectInspectRoutePointConfService.updateById(routePointConf));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectRoutePointConf数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectRoutePointConfService.removeByIds(idList));
    }

}