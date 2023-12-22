package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectCarousel;
import com.aurine.cloudx.estate.service.ProjectCarouselService;
import com.aurine.cloudx.estate.vo.ProjectCarouselVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * ProjectCarouselController
 *
 * @author DW
 * @version 1.0.0
 * @date 2021/1/12 15:11
 */
@RestController
@RequestMapping("/projectCarousel")
@Api(value = "projectCarousel", tags = "轮播图管理")
public class ProjectCarouselController {

    @Resource
    private ProjectCarouselService projectCarouselService;
    
    /**
     * 获取轮播图列表
     *
     * @return 轮播图列表
     */
    @GetMapping("/list")
    public R listCarousel(@RequestParam("type")String type){
        return R.ok(projectCarouselService.listVo(type));
    }
    
    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param projectCarouselVo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @SysLog("分页查询" )
    @GetMapping("/page")
    public R pageVo(Page page, ProjectCarouselVo projectCarouselVo) {
        return R.ok(projectCarouselService.pageVo(page, projectCarouselVo));
    }

    /**
     * 获取轮播图信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取轮播图信息", notes = "获取轮播图信息")
    @GetMapping("/{id}")
    public R get(@PathVariable("id")String id) {
        return R.ok(projectCarouselService.getById(id));
    }

    /**
     * 新增轮播图信息
     * @param projectCarousel 新增轮播图信息
     * @return R
     */
    @ApiOperation(value = "新增轮播图信息", notes = "新增轮播图信息")
    @SysLog("新增轮播图信息" )
    @PostMapping
    public R save(@RequestBody ProjectCarousel projectCarousel) {
        return R.ok(projectCarouselService.save(projectCarousel));
    }
    
    /**
     * 修改轮播图信息
     *
     * @param projectCarousel
     * @return
     */
    @ApiOperation(value = "修改轮播图信息", notes = "修改轮播图信息")
    @SysLog("修改轮播图信息" )
    @PutMapping
    public R update(@RequestBody ProjectCarousel projectCarousel) {
        return R.ok(projectCarouselService.updateById(projectCarousel));
    }


    /**
     * 上移轮播图信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "上移轮播图信息", notes = "上移轮播图信息")
    @SysLog("上移轮播图信息" )
    @PutMapping("/up/{id}")
    public R upObj(@PathVariable("id")String id) {
        projectCarouselService.upObj(id);
        return R.ok();
    }

    /**
     * 下移轮播图信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "下移轮播图信息", notes = "下移轮播图信息")
    @SysLog("下移轮播图信息" )
    @PutMapping("/down/{id}")
    public R downObj(@PathVariable("id")String id) {
        projectCarouselService.downObj(id);
        return R.ok();
    }


    /**
     * 删除轮播图信息
     *
     * @param carouselId 轮播图id
     * @return
     */
    @ApiOperation(value = "删除轮播图信息", notes = "删除轮播图信息")
    @SysLog("删除轮播图信息" )
    @DeleteMapping("/remove/{carouselId}")
    public R deleteCarousel(@PathVariable("carouselId") String carouselId){
        return R.ok(projectCarouselService.removeById(carouselId));
    }

}
