package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.vo.ProjectDeviceParamHisPageVo;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamHis;
import com.aurine.cloudx.estate.service.ProjectDeviceParamHisService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.util.List;

/**
 * 记录项目设备参数配置的历史记录(ProjectDeviceParamHis)表控制层
 *
 * @author makejava
 * @since 2020-12-23 09:31:51
 */
@RestController
@RequestMapping("projectDeviceParamHis")
@Api(value = "projectDeviceParamHis", tags = "记录项目设备参数配置的历史记录")
public class ProjectDeviceParamHisController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceParamHisService projectDeviceParamHisService;

    /**
     * 分页查询所有数据
     *
     * @param page                        分页对象
     * @param projectDeviceParamHisPageVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectDeviceParamHis所有数据")
    public R selectAll(Page page, ProjectDeviceParamHisPageVo projectDeviceParamHisPageVo) {
        return R.ok(this.projectDeviceParamHisService.pageHis(page, projectDeviceParamHisPageVo));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectDeviceParamHis单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectDeviceParamHisService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectDeviceParamHis 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectDeviceParamHis数据")
    public R insert(@RequestBody ProjectDeviceParamHis projectDeviceParamHis) {
        return R.ok(this.projectDeviceParamHisService.save(projectDeviceParamHis));
    }

    /**
     * 修改数据
     *
     * @param projectDeviceParamHis 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectDeviceParamHis数据")
    public R update(@RequestBody ProjectDeviceParamHis projectDeviceParamHis) {
        return R.ok(this.projectDeviceParamHisService.updateById(projectDeviceParamHis));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectDeviceParamHis数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectDeviceParamHisService.removeByIds(idList));
    }
}