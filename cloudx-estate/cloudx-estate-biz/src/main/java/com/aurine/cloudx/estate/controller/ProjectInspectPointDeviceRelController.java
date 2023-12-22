package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectPointDeviceRel;
import com.aurine.cloudx.estate.service.ProjectInspectPointDeviceRelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备巡检点与设备关联表(ProjectInspectPointDeviceRel)表控制层
 *
 * @author 王良俊
 * @since 2020-07-23 18:26:37
 */
@RestController
@RequestMapping("projectInspectPointDeviceRel")
@Api(value = "projectInspectPointDeviceRel", tags = "设备巡检点与设备关联表")
public class ProjectInspectPointDeviceRelController {

    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectPointDeviceRelService projectInspectPointDeviceRelService;

    /**
     * 分页查询所有数据
     *
     * @param page  分页对象
     * @param pointDeviceRel 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectPointDeviceRel所有数据")
    public R selectAll(Page<ProjectInspectPointDeviceRel> page, ProjectInspectPointDeviceRel pointDeviceRel) {
        return R.ok(this.projectInspectPointDeviceRelService.page(page, new QueryWrapper<>(pointDeviceRel)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectPointDeviceRel单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectPointDeviceRelService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param pointDeviceRel 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectPointDeviceRel数据")
    public R insert(@RequestBody ProjectInspectPointDeviceRel pointDeviceRel) {
        return R.ok(this.projectInspectPointDeviceRelService.save(pointDeviceRel));
    }

    /**
     * 修改数据
     *
     * @param pointDeviceRel 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectPointDeviceRel数据")
    public R update(@RequestBody ProjectInspectPointDeviceRel pointDeviceRel) {
        return R.ok(this.projectInspectPointDeviceRelService.updateById(pointDeviceRel));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectPointDeviceRel数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectPointDeviceRelService.removeByIds(idList));
    }

}