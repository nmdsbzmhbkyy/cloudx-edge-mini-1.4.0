package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectPropertyContact;
import com.aurine.cloudx.estate.service.ProjectPropertyContactService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目物业联系方式表(ProjectPropertyContact)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-27 15:38:48
 */
@RestController
@RequestMapping("projectPropertyContact")
@Api(value = "projectPropertyContact", tags = "项目物业联系方式表")
public class ProjectPropertyContactController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPropertyContactService projectPropertyContactService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param projectPropertyContact 查询实体
     * @return 所有数据
     */
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询projectPropertyContact所有数据")
    public R selectAll(Page<ProjectPropertyContact> page, ProjectPropertyContact projectPropertyContact) {
        LambdaQueryWrapper<ProjectPropertyContact> queryWrapper = Wrappers.lambdaQuery(ProjectPropertyContact.class);
        if (projectPropertyContact.getName() != null && !"".equals(projectPropertyContact.getName())) {
            queryWrapper.like(ProjectPropertyContact::getName, projectPropertyContact.getName());
        }
        return success(this.projectPropertyContactService.page(page, queryWrapper));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectPropertyContact单条数据")
    public R selectOne(@PathVariable Long id) {
        return success(this.projectPropertyContactService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectPropertyContact 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectPropertyContact数据")
    public R insert(@RequestBody ProjectPropertyContact projectPropertyContact) {
        int count = projectPropertyContactService.count(Wrappers.lambdaQuery(ProjectPropertyContact.class)
                .eq(ProjectPropertyContact::getName, projectPropertyContact.getName()));
        if (count != 0) {
            throw new RuntimeException("此名称已存在");
        }
        return success(this.projectPropertyContactService.save(projectPropertyContact));
    }

    /**
     * 修改数据
     *
     * @param projectPropertyContact 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectPropertyContact数据")
    public R update(@RequestBody ProjectPropertyContact projectPropertyContact) {
        int count = projectPropertyContactService.count(Wrappers.lambdaQuery(ProjectPropertyContact.class)
                .eq(ProjectPropertyContact::getName, projectPropertyContact.getName())
                .ne(ProjectPropertyContact::getSeq, projectPropertyContact.getSeq()));
        if (count != 0) {
            throw new RuntimeException("此名称已存在");
        }
        return success(this.projectPropertyContactService.updateById(projectPropertyContact));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectPropertyContact数据")
    public R delete(@PathVariable Long id) {
        return success(this.projectPropertyContactService.removeById(id));
    }
}