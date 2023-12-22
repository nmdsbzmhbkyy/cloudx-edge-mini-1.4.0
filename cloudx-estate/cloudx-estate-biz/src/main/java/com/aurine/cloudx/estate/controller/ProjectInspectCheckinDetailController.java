package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectInspectCheckinDetail;
import com.aurine.cloudx.estate.service.ProjectInspectCheckinDetailService;
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
 * 巡检点签到明细(ProjectInspectCheckinDetail)表控制层
 *
 * @author 王良俊
 * @since 2020-08-04 10:08:52
 */
@RestController
@RequestMapping("projectInspectCheckinDetail")
@Api(value = "projectInspectCheckinDetail", tags = "巡检点签到明细")
public class ProjectInspectCheckinDetailController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectInspectCheckinDetailService projectInspectCheckinDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page                        分页对象
     * @param projectInspectCheckinDetail 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectInspectCheckinDetail所有数据")
    public R selectAll(Page<ProjectInspectCheckinDetail> page, ProjectInspectCheckinDetail projectInspectCheckinDetail) {
        return R.ok(this.projectInspectCheckinDetailService.page(page, new QueryWrapper<>(projectInspectCheckinDetail)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectInspectCheckinDetail单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectInspectCheckinDetailService.getById(id));
    }


    /**
     * 通过巡检任务明细id获取其签到详情
     *
     * @param detailId 主键
     * @return 签到详情对象列表
     */
    @GetMapping("listByDetailId/{detailId}")
    @SysLog("使用巡检任务明细id获取其签到详情列表")
    @ApiOperation(value = "通过id查询", notes = "使用巡检任务明细id获取其签到详情列表")
    public R listByDetailId(@PathVariable("detailId") String detailId) {
        return R.ok(this.projectInspectCheckinDetailService.list(new QueryWrapper<ProjectInspectCheckinDetail>().lambda()
                .eq(ProjectInspectCheckinDetail::getDetailId, detailId)));
    }

    /**
     * 新增数据
     *
     * @param projectInspectCheckinDetail 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectInspectCheckinDetail数据")
    public R insert(@RequestBody ProjectInspectCheckinDetail projectInspectCheckinDetail) {
        return R.ok(this.projectInspectCheckinDetailService.save(projectInspectCheckinDetail));
    }

    /**
     * 修改数据
     *
     * @param projectInspectCheckinDetail 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectInspectCheckinDetail数据")
    public R update(@RequestBody ProjectInspectCheckinDetail projectInspectCheckinDetail) {
        return R.ok(this.projectInspectCheckinDetailService.updateById(projectInspectCheckinDetail));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectInspectCheckinDetail数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectInspectCheckinDetailService.removeByIds(idList));
    }

}