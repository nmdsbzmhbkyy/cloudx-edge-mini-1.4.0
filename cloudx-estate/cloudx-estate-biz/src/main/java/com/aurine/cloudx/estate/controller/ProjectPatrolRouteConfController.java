package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.service.ProjectPatrolRouteConfService;
import com.aurine.cloudx.estate.vo.ProjectPatrolRouteConfVo;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 项目巡更路线设置(ProjectPatrolRouteConf)表控制层
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-24 12:00:08
 */
@RestController
@RequestMapping("/projectPatrolRouteConf")
@Api(value = "projectPatrolRouteConf", tags = "项目巡更路线设置")
public class ProjectPatrolRouteConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPatrolRouteConfService projectPatrolRouteConfService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param vo 查询实体
     * @return 所有数据
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R selectAll(Page page, ProjectPatrolRouteConfVo vo) {
        return R.ok(projectPatrolRouteConfService.page(page, vo));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param patrolRouteId 主键
     * @return 单条数据
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{patrolRouteId}")
    public R selectOne(@PathVariable String patrolRouteId) {
        return R.ok(projectPatrolRouteConfService.getVoById(patrolRouteId));
    }

    /**
     * 新增数据
     *
     * @param vo 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @PostMapping
    public R save(@RequestBody ProjectPatrolRouteConfVo vo) {
        return R.ok(projectPatrolRouteConfService.save(vo));
    }

    /**
     * 修改数据
     *
     * @param vo 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @PutMapping
    public R update(@RequestBody ProjectPatrolRouteConfVo vo) {
        return R.ok(projectPatrolRouteConfService.updatePatrolRouteConfById(vo));
    }

    /**
     * 删除数据
     *
     * @param patrolRouteId 主键
     * @return 删除结果
     */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @DeleteMapping("/{patrolRouteId}")
    public R delete(@PathVariable String patrolRouteId) {
        return R.ok(projectPatrolRouteConfService.removeById(patrolRouteId));
    }

    /**
     * 切换巡更路线状态
     */
    @ApiOperation(value = "切换巡更路线状态", notes = "切换巡更路线状态")
    @SysLog("切换巡更路线状态")
    @PutMapping("/updateStatusById/{routeId}" )
    public R updateByStatus(@PathVariable("routeId") String routeId) {
        return R.ok(projectPatrolRouteConfService.updateStatusById(routeId));
    }

    /**
     *
     * @param routeId 主键
     * @return 查询结果
     */
    @ApiOperation(value = "获取当前路线下的所有参与人姓名", notes = "获取当前路线下的所有参与人姓名")
    @SysLog("获取当前路线下的所有参与人姓名")
    @GetMapping("/getStaffByName/{routeId}" )
    public R getStaffByName(@PathVariable("routeId") String routeId) {
        return R.ok(projectPatrolRouteConfService.getPersonNameList(routeId));
    }
}