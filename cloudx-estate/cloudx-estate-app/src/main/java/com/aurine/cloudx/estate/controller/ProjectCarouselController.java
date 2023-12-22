package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.service.ProjectCarouselService;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.AppCarouselVo;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private ImgConvertUtil imgConvertUtil;

    /**
     * 获取轮播图列表
     *
     * @return 轮播图列表
     */
    @GetMapping("/list/{type}")
    @ApiOperation(value = "获取轮播图列表", notes = "获取轮播图列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "类型（1 业主端 2 物业端）", required = true, paramType = "path")
    })
    public R<List<AppCarouselVo>> listCarousel(@PathVariable String type) {
        List<AppCarouselVo> carouselVoList = projectCarouselService.listByType(type).stream()
            .map(e -> {
                AppCarouselVo carouselVo = new AppCarouselVo();
                BeanUtil.copyProperties(e, carouselVo);
                return carouselVo;
            })
            .collect(Collectors.toList());
        return R.ok(carouselVoList);
    }



}
