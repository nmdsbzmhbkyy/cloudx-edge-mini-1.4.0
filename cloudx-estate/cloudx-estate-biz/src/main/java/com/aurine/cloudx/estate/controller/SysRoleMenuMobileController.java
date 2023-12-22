package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.SysRoleMenuMobile;
import com.aurine.cloudx.estate.service.SysRoleMenuMobileService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色菜单表(SysRoleMenuMobile)表控制层
 *
 * @author makejava
 * @since 2021-02-07 10:25:49
 */
@RestController
@RequestMapping("sys-role-menu-mobile")
@Api(value = "sysRoleMenuMobile", tags = "角色菜单表")
public class SysRoleMenuMobileController {
    /**
     * 服务对象
     */
    @Resource
    private SysRoleMenuMobileService sysRoleMenuMobileService;

    /**
     * 分页查询所有数据
     *
     * @param page              分页对象
     * @param sysRoleMenuMobile 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询sysRoleMenuMobile所有数据")
    public R selectAll(Page<SysRoleMenuMobile> page, SysRoleMenuMobile sysRoleMenuMobile) {
        return R.ok(this.sysRoleMenuMobileService.page(page, new QueryWrapper<>(sysRoleMenuMobile)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询sysRoleMenuMobile单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.sysRoleMenuMobileService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysRoleMenuMobile 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增sysRoleMenuMobile数据")
    public R insert(@RequestBody SysRoleMenuMobile sysRoleMenuMobile) {
        return R.ok(this.sysRoleMenuMobileService.save(sysRoleMenuMobile));
    }

    /**
     * 修改数据
     *
     * @param sysRoleMenuMobile 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改sysRoleMenuMobile数据")
    public R update(@RequestBody SysRoleMenuMobile sysRoleMenuMobile) {
        return R.ok(this.sysRoleMenuMobileService.updateById(sysRoleMenuMobile));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除sysRoleMenuMobile数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.sysRoleMenuMobileService.removeByIds(idList));
    }

    @GetMapping("/list/id/{roleId}")
    @ApiOperation(value = "根据角色id获取菜单id列表", notes = "根据角色id获取菜单id列表")
    public R listIdByRoleId(@PathVariable("roleId") Integer roleId) {
        return R.ok(this.sysRoleMenuMobileService.listIdByRoleId(roleId));
    }

    @GetMapping("/list/permission/{roleId}")
    @ApiOperation(value = "根据角色id获取权限列表", notes = "根据角色id获取权限列表")
    public R listPermissionByRoleId(@PathVariable("roleId") Integer roleId) {
        return R.ok(this.sysRoleMenuMobileService.listPermissionByRoleId(roleId));
    }

    @GetMapping("/tree/{roleId}")
    @ApiOperation(value = "根据角色id获取菜单树", notes = "根据角色id获取菜单树")
    public R treeRoleId(@PathVariable("roleId") Integer roleId) {
        return R.ok(this.sysRoleMenuMobileService.treeByRoleId(roleId));
    }

    @GetMapping("/tree/{roleId}/{type}")
    @ApiOperation(value = "根据角色id和类型获取菜单树", notes = "根据角色id和类型获取菜单树")
    public R treeTypeRoleId(@PathVariable("roleId") Integer roleId, @PathVariable("type") Integer type) {
        return R.ok(this.sysRoleMenuMobileService.treeTypeByRoleId(roleId, type));
    }

    @PutMapping("/save-or-update/{roleId}")
    public R saveOrUpdateIdByRoleId(@PathVariable("roleId") Integer roleId, @RequestBody(required = false) List<Integer> menuIds) {
        this.sysRoleMenuMobileService.updateByRoleId(roleId, menuIds);
        return R.ok();
    }
}
