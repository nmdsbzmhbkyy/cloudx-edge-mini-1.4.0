package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.SysMenuMobile;
import com.aurine.cloudx.estate.service.SysMenuMobileService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.util.List;

/**
 * 移动端菜单权限表(SysMenuMobile)表控制层
 *
 * @author makejava
 * @since 2021-02-07 10:23:23
 */
@RestController
@RequestMapping("sys-menu-mobile")
@Api(value = "sysMenuMobile", tags = "移动端菜单权限表")
public class SysMenuMobileController {
    /**
     * 服务对象
     */
    @Resource
    private SysMenuMobileService sysMenuMobileService;

    /**
     * 分页查询所有数据
     *
     * @param page          分页对象
     * @param sysMenuMobile 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询sysMenuMobile所有数据")
    public R selectAll(Page<SysMenuMobile> page, SysMenuMobile sysMenuMobile) {
        return R.ok(this.sysMenuMobileService.page(page, new QueryWrapper<>(sysMenuMobile)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询sysMenuMobile单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.sysMenuMobileService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysMenuMobile 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增sysMenuMobile数据")
    public R insert(@RequestBody SysMenuMobile sysMenuMobile) {
        return R.ok(this.sysMenuMobileService.save(sysMenuMobile));
    }

    /**
     * 修改数据
     *
     * @param sysMenuMobile 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改sysMenuMobile数据")
    public R update(@RequestBody SysMenuMobile sysMenuMobile) {
        return R.ok(this.sysMenuMobileService.updateById(sysMenuMobile));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除sysMenuMobile数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.sysMenuMobileService.removeByIds(idList));
    }

    /**
     * 获取菜单树
     *
     * @return 菜单树
     */
    @GetMapping("/tree")
    @ApiOperation(value = "获取菜单树",notes = "获取菜单树")
    public R tree(@RequestParam(value = "type", required = false) String type) {
        if (StrUtil.isBlank(type)) {
            type = "0";
        }
        return R.ok(this.sysMenuMobileService.tree(type));
    }
}
