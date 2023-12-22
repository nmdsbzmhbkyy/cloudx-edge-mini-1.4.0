package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectDetailDevice;
import com.aurine.cloudx.estate.service.ProjectInspectDetailDeviceService;
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
 * 巡检任务明细设备列表(ProjectInspectDetailDevice)表控制层
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:31
 */
@RestController
@RequestMapping("projectInspectDetailDevice")
@Api(value = "projectInspectDetailDevice", tags = "巡检任务明细设备列表")
public class ProjectInspectDetailDeviceController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectDetailDeviceService projectInspectDetailDeviceService;

    /**
     * 分页查询所有数据
     *
     * @param page         分页对象
     * @param detailDevice 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectDetailDevice所有数据")
    public R selectAll(Page<ProjectInspectDetailDevice> page, ProjectInspectDetailDevice detailDevice) {
        return R.ok(this.projectInspectDetailDeviceService.page(page, new QueryWrapper<>(detailDevice)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectDetailDevice单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectDetailDeviceService.getById(id));
    }


    /**
     * 根据明细id查询设备列表
     *
     * @param detailId 巡检任务明细主键
     */
    @GetMapping("listByDetailId/{detailId}")
    @SysLog("根据明细id查询设备列表")
    @ApiOperation(value = "根据明细id查询设备列表", notes = "根据明细id查询设备列表")
    public R listByDetailId(@PathVariable String detailId) {
        return R.ok(this.projectInspectDetailDeviceService.listDetailDeviceByDetailId(detailId));
    }

    /**
     * 新增数据
     *
     * @param detailDevice 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectDetailDevice数据")
    public R insert(@RequestBody ProjectInspectDetailDevice detailDevice) {
        return R.ok(this.projectInspectDetailDeviceService.save(detailDevice));
    }

    /**
     * 修改数据
     *
     * @param detailDevice 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectDetailDevice数据")
    public R update(@RequestBody ProjectInspectDetailDevice detailDevice) {
        return R.ok(this.projectInspectDetailDeviceService.updateById(detailDevice));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectDetailDevice数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectDetailDeviceService.removeByIds(idList));
    }

}