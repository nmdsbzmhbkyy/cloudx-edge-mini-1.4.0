package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectCarousel;
import com.aurine.cloudx.estate.service.ProjectCarouselService;
import com.aurine.cloudx.estate.vo.ProjectCarouselVo;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    @ApiOperation(value = "获取轮播图列表", notes = "获取轮播图列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "类型（1 业主端 2 物业端）", required = true, paramType = "query")
    })
    public R<List<ProjectCarouselVo>> listCarousel(@RequestParam("type")String type){
        return R.ok(projectCarouselService.listVo(type));
    }



    /**
     * 获取轮播图信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取轮播图信息", notes = "获取轮播图信息")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "轮播图ID", required = true, paramType = "path")
    })
    public R<ProjectCarousel> get(@PathVariable("id")String id) {
        return R.ok(projectCarouselService.getById(id));
    }



}
