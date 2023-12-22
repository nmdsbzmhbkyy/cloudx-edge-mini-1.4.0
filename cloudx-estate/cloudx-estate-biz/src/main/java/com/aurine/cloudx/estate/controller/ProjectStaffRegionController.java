package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.ProjectStaffRegion;
import com.aurine.cloudx.estate.service.ProjectStaffRegionService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工管辖区域设置(ProjectStaffRegion)表控制层
 *
 * @author 王良俊
 * @since 2020-12-09 15:04:23
 */
@RestController
@RequestMapping("projectStaffRegion")
@Api(value = "projectStaffRegion", tags = "员工管辖区域设置")
public class ProjectStaffRegionController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectStaffRegionService projectStaffRegionService;
    @Resource
    private ProjectStaffService projectStaffService;

    /**
     * 分页查询所有数据
     *
     * @param page               分页对象
     * @param projectStaffRegion 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectStaffRegion所有数据")
    public R selectAll(Page<ProjectStaffRegion> page, ProjectStaffRegion projectStaffRegion) {
        return R.ok(this.projectStaffRegionService.page(page, new QueryWrapper<>(projectStaffRegion)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectStaffRegion单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectStaffRegionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectStaffRegion 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectStaffRegion数据")
    public R insert(@RequestBody ProjectStaffRegion projectStaffRegion) {
        return R.ok(this.projectStaffRegionService.save(projectStaffRegion));
    }

    /**
     * 判断员工是否管辖一个区域 (如果员工有管辖这个区域则返回true)
     *
     */
    @GetMapping("/isManagerRegion")
    @ApiOperation(value = "判断员工是否管辖一个区域", notes = "判断员工是否管辖一个区域")
    public R isManagerRegion(@RequestParam("staffId") String staffId, @RequestParam("regionId") String regionId) {
        int count = projectStaffRegionService.count(new QueryWrapper<ProjectStaffRegion>().lambda()
                .eq(ProjectStaffRegion::getStaffId, staffId).eq(ProjectStaffRegion::getRegionId, regionId));
        return R.ok(count != 0);
    }

    /**
     * 获取当前员工管辖的区域列表 (如果没有管辖任何区域就返回空集合)
     *
     */
    @GetMapping("/getManagerRegionList")
    @ApiOperation(value = "获取当前员工管辖的区域列表", notes = "获取当前员工管辖的区域列表")
    public R getManagerRegionList() {
        Integer userId = SecurityUtils.getUser().getId();
        List<ProjectStaff> staffList = projectStaffService.list(new QueryWrapper<ProjectStaff>().lambda()
                .eq(ProjectStaff::getUserId, userId)
                .eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId()));
        if (CollUtil.isNotEmpty(staffList)) {
            ProjectStaff staff = staffList.get(0);
            return R.ok(projectStaffRegionService.list(new QueryWrapper<ProjectStaffRegion>().lambda().eq(ProjectStaffRegion::getStaffId, staff.getStaffId())));
        }
        return R.ok(new ArrayList<>());
    }

    /**
     * 修改数据
     *
     * @param projectStaffRegion 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectStaffRegion数据")
    public R update(@RequestBody ProjectStaffRegion projectStaffRegion) {
        return R.ok(this.projectStaffRegionService.updateById(projectStaffRegion));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectStaffRegion数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectStaffRegionService.removeByIds(idList));
    }

    /**
     * 指派员工管辖对应区域
     *
     * @param staffIdList 员工ID集合
     * @param
     * @return 删除结果
     */
    @PostMapping("/assignStaff")
    @ApiOperation(value = "指派员工管辖对应区域", notes = "指派员工管辖对应区域")
    public R assignStaff(@RequestParam("staffIdList") List<String> staffIdList, @RequestParam("regionId") String regionId) {

        return R.ok();
    }


}