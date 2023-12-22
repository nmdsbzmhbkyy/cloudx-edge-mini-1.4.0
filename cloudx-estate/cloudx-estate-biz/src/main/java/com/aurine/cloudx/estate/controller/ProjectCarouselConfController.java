package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.entity.ProjectNoticeTemplate;
import com.aurine.cloudx.estate.service.ProjectCarouselConfService;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfQuery;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 轮播图资讯管理(projectCarouselConf)表控制层
 *
 * @author 王良俊
 * @since 2021-01-12 11:43:03
 */
@RestController
@RequestMapping("projectCarouselConf")
@Api(value = "projectCarouselConf", tags = "轮播图资讯管理")
public class ProjectCarouselConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectCarouselConfService projectCarouselConfService;

    /**
     * 分页查询所有数据
     *
     * @param page              分页对象
     * @param carouselConfQuery 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectCarouselConf所有数据")
    public R selectAll(Page<ProjectCarouselConf> page, ProjectCarouselConfQuery carouselConfQuery) {
        return R.ok(projectCarouselConfService.fetchList(page, carouselConfQuery));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param infoId 主键
     * @return 单条数据
     */
    @GetMapping("{infoId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectCarouselConf单条数据")
    public R selectOne(@PathVariable String infoId) {
        return R.ok(this.projectCarouselConfService.getById(infoId));
    }

    /**
     * 新增数据
     *
     * @param projectCarouselConf 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectCarouselConf数据")
    public R insert(@RequestBody ProjectCarouselConf projectCarouselConf) {
        projectCarouselConf.setInfoId("");
        return R.ok(this.projectCarouselConfService.save(projectCarouselConf));
    }


    /**
     * 修改数据
     *
     * @param projectCarouselConf 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectCarouselConf数据")
    public R update(@RequestBody ProjectCarouselConf projectCarouselConf) {
        return R.ok(this.projectCarouselConfService.updateById(projectCarouselConf));
    }

    /**
     * 删除数据
     *
     * @param infoId 资讯ID
     * @return 删除结果
     */
    @DeleteMapping("/remove/{infoId}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectCarouselConf数据")
    public R delete(@PathVariable("infoId") String infoId) {
        return R.ok(this.projectCarouselConfService.removeById(infoId));
    }
    @GetMapping("/getCarouselByTitle")
    @ApiOperation(value = "通过标题查询", notes = "通过标题查询")
    public R getCarouselByTitle(String title) {
        List<ProjectCarouselConf> list = projectCarouselConfService.list(new QueryWrapper<ProjectCarouselConf>().lambda()
                .eq(ProjectCarouselConf::getTitle, title));
        return R.ok(list);
    }
}