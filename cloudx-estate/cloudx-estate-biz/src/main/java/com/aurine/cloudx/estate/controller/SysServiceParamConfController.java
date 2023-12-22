package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.entity.SysServiceParamConf;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.service.SysServiceParamConfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 平台设备参数定义表(SysServiceParamConf)表控制层
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:38
 */
@RestController
@RequestMapping("SysServiceParamConf")
@Api(value = "SysServiceParamConf", tags = "平台设备参数定义表")
public class SysServiceParamConfController {
    /**
     * 服务对象
     */
    @Resource
    private SysServiceParamConfService sysServiceParamConfService;

    /**
     * 分页查询所有数据
     *
     * @param page               分页对象
     * @param sysServiceParamConf 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询sysDeviceParamConf所有数据")
    public R selectAll(Page<SysServiceParamConf> page, SysServiceParamConf sysServiceParamConf) {
        return R.ok(this.sysServiceParamConfService.page(page, new QueryWrapper<>(sysServiceParamConf)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询sysDeviceParamConf单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.sysServiceParamConfService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysServiceParamConf 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增sysDeviceParamConf数据")
    public R insert(@RequestBody SysServiceParamConf sysServiceParamConf) {
        return R.ok(this.sysServiceParamConfService.save(sysServiceParamConf));
    }

    /**
     * 修改数据
     *
     * @param sysServiceParamConf 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改sysDeviceParamConf数据")
    public R update(@RequestBody SysServiceParamConf sysServiceParamConf) {
        return R.ok(this.sysServiceParamConfService.updateById(sysServiceParamConf));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除sysDeviceParamConf数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.sysServiceParamConfService.removeByIds(idList));
    }
}