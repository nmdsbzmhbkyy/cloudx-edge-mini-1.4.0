package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectNoticeCategoryConf;
import com.aurine.cloudx.estate.service.ProjectNoticeCategoryConfService;
import com.aurine.cloudx.estate.vo.ProjectNoticeCategoryConfVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 推送模板类型配置（信息分类），该类型可应用于媒体通知和推送中心(ProjectNoticeCategoryConf)表控制层
 *
 * @author makejava
 * @since 2020-12-14 10:06:42
 */
@RestController
@RequestMapping("projectNoticeCategoryConf")
@Api(value = "projectNoticeCategoryConf", tags = "推送模板类型配置（信息分类），该类型可应用于媒体通知和推送中心")
public class ProjectNoticeCategoryConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectNoticeCategoryConfService projectNoticeCategoryConfService;

    /**
     * 分页查询所有数据
     *
     * @param page                      分页对象
     * @param projectNoticeCategoryConf 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectNoticeCategoryConf所有数据")
    public R selectAll(Page<ProjectNoticeCategoryConfVo> page, ProjectNoticeCategoryConf projectNoticeCategoryConf) {
        return R.ok(this.projectNoticeCategoryConfService.pageVo(page, projectNoticeCategoryConf));
    }
    /**
     * 查询所有可用类型数据
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有可用类型数据", notes = "查询所有可用类型数据")
    public R selectAll() {
        return R.ok(this.projectNoticeCategoryConfService.list(Wrappers.lambdaUpdate(ProjectNoticeCategoryConf.class).eq(ProjectNoticeCategoryConf::getIsActive, "0")));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectNoticeCategoryConf单条数据")
    public R selectOne(@PathVariable String id) {
        return R.ok(this.projectNoticeCategoryConfService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectNoticeCategoryConf 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectNoticeCategoryConf数据")
    public R insert(@RequestBody ProjectNoticeCategoryConf projectNoticeCategoryConf) {
        return R.ok(this.projectNoticeCategoryConfService.save(projectNoticeCategoryConf));
    }

    /**
     * 修改数据
     *
     * @param projectNoticeCategoryConf 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectNoticeCategoryConf数据")
    public R update(@RequestBody ProjectNoticeCategoryConf projectNoticeCategoryConf) {
        return R.ok(this.projectNoticeCategoryConfService.updateById(projectNoticeCategoryConf));
    }

    /**
     * 启用禁用
     *
     * @param id 实体对象
     * @return 修改结果
     */
    @PutMapping("/active/{id}/{isActive}")
    @ApiOperation(value = "启用禁用", notes = "启用禁用projectNoticeCategoryConf数据")
    public R active(@PathVariable("id") String id, @PathVariable("isActive") String isActive) {
        return R.ok(this.projectNoticeCategoryConfService.update(Wrappers.lambdaUpdate(ProjectNoticeCategoryConf.class)
                .set(ProjectNoticeCategoryConf::getIsActive, isActive).eq(ProjectNoticeCategoryConf::getTypeId, id)));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectNoticeCategoryConf数据")
    public R delete(@PathVariable("id") String id) {
        return R.ok(this.projectNoticeCategoryConfService.removeTypeById(id));
    }

    /**
     * @description:
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/2/24 17:02
     */
    @GetMapping("/getConfigByTypeName")
    @ApiOperation(value = "通过资源类型名称查询", notes = "通过模板名称查询模板")
    public R getTemplateByTitle(String typeName) {

        return R.ok(this.projectNoticeCategoryConfService.getVoListByTypeName(typeName));
    }

}