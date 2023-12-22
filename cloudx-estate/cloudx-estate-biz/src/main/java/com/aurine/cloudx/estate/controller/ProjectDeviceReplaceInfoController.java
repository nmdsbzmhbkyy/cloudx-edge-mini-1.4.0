package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectDeviceReplaceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceReplaceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备变更历史表，用于存储被替换掉的设备信息(ProjectDeviceReplaceInfo)表控制层
 *
 * @author 王良俊
 * @since 2021-01-11 11:18:42
 */
@RestController
@RequestMapping("projectDeviceReplaceInfo")
@Api(value = "projectDeviceReplaceInfo", tags = "设备变更历史表，用于存储被替换掉的设备信息")
public class ProjectDeviceReplaceInfoController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceReplaceInfoService projectDeviceReplaceInfoService;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param projectDeviceReplaceInfo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectDeviceReplaceInfo所有数据")
    public R selectAll(Page<ProjectDeviceReplaceInfo> page, ProjectDeviceReplaceInfo projectDeviceReplaceInfo) {
        return R.ok(this.projectDeviceReplaceInfoService.page(page, new QueryWrapper<>(projectDeviceReplaceInfo)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectDeviceReplaceInfo单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectDeviceReplaceInfoService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectDeviceReplaceInfo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectDeviceReplaceInfo数据")
    public R insert(@RequestBody ProjectDeviceReplaceInfo projectDeviceReplaceInfo) {
        return R.ok(this.projectDeviceReplaceInfoService.save(projectDeviceReplaceInfo));
    }

    /**
     * 修改数据
     *
     * @param projectDeviceReplaceInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectDeviceReplaceInfo数据")
    public R update(@RequestBody ProjectDeviceReplaceInfo projectDeviceReplaceInfo) {
        return R.ok(this.projectDeviceReplaceInfoService.updateById(projectDeviceReplaceInfo));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectDeviceReplaceInfo数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectDeviceReplaceInfoService.removeByIds(idList));
    }
}