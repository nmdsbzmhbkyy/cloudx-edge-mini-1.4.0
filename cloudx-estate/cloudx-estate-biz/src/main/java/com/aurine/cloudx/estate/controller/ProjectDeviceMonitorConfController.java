package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.util.PinyinUtils;
import com.aurine.cloudx.estate.entity.ProjectDeviceMonitorConf;
import com.aurine.cloudx.estate.service.ProjectDeviceMonitorConfService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备监控项配置(ProjectDeviceMonitorConf)表控制层
 *
 * @author 王良俊
 * @since 2020-07-23 18:30:45
 */
@RestController
@RequestMapping("projectDeviceMonitorConf")
@Api(value = "projectDeviceMonitorConf", tags = "设备监控项配置")
public class ProjectDeviceMonitorConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceMonitorConfService projectDeviceMonitorConfService;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param projectDeviceMonitorConf 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectDeviceMonitorConf所有数据")
    public R selectAll(Page<ProjectDeviceMonitorConf> page, ProjectDeviceMonitorConf projectDeviceMonitorConf) {
        return R.ok(this.projectDeviceMonitorConfService.page(page, new QueryWrapper<>(projectDeviceMonitorConf).lambda()
                .eq(ProjectDeviceMonitorConf::getDeviceTypeId, projectDeviceMonitorConf.getDeviceTypeId())));
    }
    /**
     * 查询某个设备类型的所有检查项
     *
     * @param deviceTypeId 设备类型
     * @return 所有数据
     */
    @GetMapping("/list/{deviceTypeId}")
    @ApiOperation(value = "查询某设备类型的所有检查项", notes = "查询某设备类型的所有检查项")
    public R list(@PathVariable("deviceTypeId") String deviceTypeId) {
        return R.ok(this.projectDeviceMonitorConfService.list(new QueryWrapper<ProjectDeviceMonitorConf>().lambda()
                .eq(ProjectDeviceMonitorConf::getDeviceTypeId, deviceTypeId)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param monitorId 主键
     * @return 单条数据
     */
    @GetMapping("{monitorId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectDeviceMonitorConf单条数据")
    public R selectOne(@PathVariable("monitorId") String monitorId) {
        return R.ok(this.projectDeviceMonitorConfService.getById(monitorId));
    }

    /**
     * 新增数据
     *
     * @param projectDeviceMonitorConf 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectDeviceMonitorConf数据")
    public R insert(@RequestBody ProjectDeviceMonitorConf projectDeviceMonitorConf) {
        List<ProjectDeviceMonitorConf> monitorConfList = projectDeviceMonitorConfService.list(new QueryWrapper<ProjectDeviceMonitorConf>().lambda()
                .eq(ProjectDeviceMonitorConf::getDeviceTypeId, projectDeviceMonitorConf.getDeviceTypeId())
                .eq(ProjectDeviceMonitorConf::getMonitorName, projectDeviceMonitorConf.getMonitorName()));
        if (CollUtil.isNotEmpty(monitorConfList)){
            throw new RuntimeException("在该分类已有同名检查项无法添加");
        }
        projectDeviceMonitorConf.setMonitorCode(PinyinUtils.getPinYinHeadChar(projectDeviceMonitorConf.getMonitorName()));
        projectDeviceMonitorConf.setSeq(null);
        return R.ok(this.projectDeviceMonitorConfService.save(projectDeviceMonitorConf));
    }

    /**
     * 修改数据
     *
     * @param projectDeviceMonitorConf 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectDeviceMonitorConf数据")
    public R update(@RequestBody ProjectDeviceMonitorConf projectDeviceMonitorConf) {
        List<ProjectDeviceMonitorConf> monitorConfList = projectDeviceMonitorConfService.list(new QueryWrapper<ProjectDeviceMonitorConf>().lambda()
                .eq(ProjectDeviceMonitorConf::getDeviceTypeId, projectDeviceMonitorConf.getDeviceTypeId())
                .eq(ProjectDeviceMonitorConf::getMonitorName, projectDeviceMonitorConf.getMonitorName())
                .notIn(ProjectDeviceMonitorConf::getMonitorId, projectDeviceMonitorConf.getMonitorId()));
        if (CollUtil.isNotEmpty(monitorConfList)){
            throw new RuntimeException("在该分类已有同名检查项无法修改");
        }
        return R.ok(this.projectDeviceMonitorConfService.updateById(projectDeviceMonitorConf));
    }

    /**
     * 删除数据
     *
     * @param monitorId 检查项id
     * @return 删除结果
     */
    @DeleteMapping("/{monitorId}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectDeviceMonitorConf数据")
    public R delete(@PathVariable("monitorId") String monitorId) {
        return R.ok(this.projectDeviceMonitorConfService.removeById(monitorId));
    }
}