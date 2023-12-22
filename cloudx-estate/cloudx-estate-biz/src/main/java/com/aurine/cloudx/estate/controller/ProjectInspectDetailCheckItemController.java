package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectDetailCheckItem;
import com.aurine.cloudx.estate.service.ProjectInspectDetailCheckItemService;
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
 * 巡检任务明细检查项列表(ProjectInspectDetailCheckItem)表控制层
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:12
 */
@RestController
@RequestMapping("projectInspectDetailCheckItem")
@Api(value = "projectInspectDetailCheckItem", tags = "巡检任务明细检查项列表")
public class ProjectInspectDetailCheckItemController {

    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectDetailCheckItemService projectInspectDetailCheckItemService;

    /**
     * 分页查询所有数据
     *
     * @param page            分页对象
     * @param detailCheckItem 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectDetailCheckItem所有数据")
    public R selectAll(Page<ProjectInspectDetailCheckItem> page, ProjectInspectDetailCheckItem detailCheckItem) {
        return R.ok(this.projectInspectDetailCheckItemService.page(page, new QueryWrapper<>(detailCheckItem)));
    }

    /**
     * 新增数据
     *
     * @param detailCheckItem 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectDetailCheckItem数据")
    public R insert(@RequestBody ProjectInspectDetailCheckItem detailCheckItem) {
        return R.ok(this.projectInspectDetailCheckItemService.save(detailCheckItem));
    }

    /**
     * 根据设备明细id获取到该设备的检查项
     *
     * @param deviceDetailId 设备明细id
     */
    @GetMapping("listByDeviceDetailId/{deviceDetailId}")
    @SysLog("根据明细设备id获取设备检查项列表")
    @ApiOperation(value = "根据明细设备id获取设备检查项列表", notes = "根据明细设备id获取设备检查项列表")
    public R listByDeviceDetailId(@PathVariable("deviceDetailId") String deviceDetailId) {
        List<ProjectInspectDetailCheckItem> list = this.projectInspectDetailCheckItemService.list(new QueryWrapper<ProjectInspectDetailCheckItem>().lambda()
                .eq(ProjectInspectDetailCheckItem::getDeviceDetailId, deviceDetailId));
        return R.ok(list);
    }

    /**
     * 修改数据
     *
     * @param detailCheckItem 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectDetailCheckItem数据")
    public R update(@RequestBody ProjectInspectDetailCheckItem detailCheckItem) {
        return R.ok(this.projectInspectDetailCheckItemService.updateById(detailCheckItem));
    }

}