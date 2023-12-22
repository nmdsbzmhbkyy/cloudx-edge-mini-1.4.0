package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectFocusPersonAttr;
import com.aurine.cloudx.estate.service.ProjectFocusPersonAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 重点人员拓展信息表(ProjectFocusPersonAttr)表控制层
 *
 * @author makejava
 * @since 2020-08-18 09:06:39
 */
@RestController
@RequestMapping("projectFocusPersonAttr")
@Api(value = "projectFocusPersonAttr", tags = "重点人员拓展信息表")
public class ProjectFocusPersonAttrController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectFocusPersonAttrService projectFocusPersonAttrService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param projectFocusPersonAttr 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectFocusPersonAttr所有数据")
    public R selectAll(Page<ProjectFocusPersonAttr> page, ProjectFocusPersonAttr projectFocusPersonAttr) {
        return R.ok(this.projectFocusPersonAttrService.page(page, new QueryWrapper<>(projectFocusPersonAttr)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectFocusPersonAttr单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectFocusPersonAttrService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectFocusPersonAttr 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectFocusPersonAttr数据")
    public R insert(@RequestBody ProjectFocusPersonAttr projectFocusPersonAttr) {
        return R.ok(this.projectFocusPersonAttrService.save(projectFocusPersonAttr));
    }

    /**
     * 修改数据
     *
     * @param projectFocusPersonAttr 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectFocusPersonAttr数据")
    public R update(@RequestBody ProjectFocusPersonAttr projectFocusPersonAttr) {
        return R.ok(this.projectFocusPersonAttrService.updateById(projectFocusPersonAttr));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectFocusPersonAttr数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectFocusPersonAttrService.removeByIds(idList));
    }
}