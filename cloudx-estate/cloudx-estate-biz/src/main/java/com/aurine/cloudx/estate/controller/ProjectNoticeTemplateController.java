package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PlaceRelTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectNoticeCategoryConf;
import com.aurine.cloudx.estate.entity.ProjectNoticeTemplate;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.entity.ProjectPersonNoticePlan;
import com.aurine.cloudx.estate.service.ProjectNoticeTemplateService;
import com.aurine.cloudx.estate.service.ProjectPersonNoticePlanService;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateForm;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 推送中心消息模板配置(ProjectNoticeTemplate)表控制层
 *
 * @author makejava
 * @since 2020-12-14 09:34:16
 */
@RestController
@RequestMapping("projectNoticeTemplate")
@Api(value = "projectNoticeTemplate", tags = "推送中心消息模板配置")
public class ProjectNoticeTemplateController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectNoticeTemplateService projectNoticeTemplateService;
    @Resource
    private ProjectPersonNoticePlanService projectPersonNoticePlanService;
    /**
     * 分页查询所有数据
     *
     * @param page                  分页对象
     * @param projectNoticeTemplate 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectNoticeTemplate所有数据")
    public R page(Page<ProjectNoticeTemplateVo> page, ProjectNoticeTemplateForm projectNoticeTemplate) {

        return R.ok(this.projectNoticeTemplateService.pageVo(page, projectNoticeTemplate));
    }

    /**
     * 启用禁用
     *
     * @param id 实体对象
     * @return 修改结果
     */
    @PutMapping("/active/{id}/{isActive}")
    @ApiOperation(value = "启用禁用", notes = "启用禁用ProjectNoticeTemplate数据")
    public R active(@PathVariable("id") String id, @PathVariable("isActive") String isActive) {
        return R.ok(this.projectNoticeTemplateService.updateActiveById(id,isActive));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectNoticeTemplate单条数据")
    public R selectOne(@PathVariable String id) {
        return R.ok(this.projectNoticeTemplateService.getVoById(id));
    }


    /**
     * @description:
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/2/24 17:02
     */
    @GetMapping("/getTemplateByTitle")
    @ApiOperation(value = "通过模板查询", notes = "通过模板名称查询模板")
    public R getTemplateByTitle(ProjectNoticeTemplate projectNoticeTemplate) {
        if (ProjectContextHolder.getProjectId() == 1) {
            List<ProjectNoticeTemplate> list = projectNoticeTemplateService.list(new QueryWrapper<ProjectNoticeTemplate>().lambda()
                    .eq(ProjectNoticeTemplate::getTitle, projectNoticeTemplate.getTitle())
                    .eq(ProjectNoticeTemplate::getTypeId, projectNoticeTemplate.getTypeId()));
            return R.ok(list);
        }else {
            ProjectNoticeTemplateVo projectNoticeTemplateVo=new ProjectNoticeTemplateVo();
            projectNoticeTemplateVo.setTitle(projectNoticeTemplate.getTitle());
            projectNoticeTemplateVo.setTypeId(projectNoticeTemplate.getTypeId());
            projectNoticeTemplateVo.setProjectId(ProjectContextHolder.getProjectId());
            return R.ok(projectNoticeTemplateService.getVoBytitle(projectNoticeTemplateVo));
        }

    }
    /**
     * 新增数据
     *
     * @param projectNoticeTemplate 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectNoticeTemplate数据")
    public R insert(@RequestBody ProjectNoticeTemplate projectNoticeTemplate) {
        return R.ok(this.projectNoticeTemplateService.save(projectNoticeTemplate));
    }

    /**
     * 修改数据
     *
     * @param projectNoticeTemplate 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectNoticeTemplate数据")
    public R update(@RequestBody ProjectNoticeTemplate projectNoticeTemplate) {
        return R.ok(this.projectNoticeTemplateService.updateById(projectNoticeTemplate));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectNoticeTemplate数据")
    public R delete(@PathVariable("id") String id) {
        ProjectNoticeTemplate byId = projectNoticeTemplateService.getById(id);
        int count = projectPersonNoticePlanService.count(new QueryWrapper<ProjectPersonNoticePlan>().lambda()
                .eq(ProjectPersonNoticePlan::getTemplateId, byId.getTemplateId())
        );
        if(count > 0){
            return  R.failed("已被住户通知计划使用，不允许删除");
        }
        return R.ok(this.projectNoticeTemplateService.removeById(id));
    }
}