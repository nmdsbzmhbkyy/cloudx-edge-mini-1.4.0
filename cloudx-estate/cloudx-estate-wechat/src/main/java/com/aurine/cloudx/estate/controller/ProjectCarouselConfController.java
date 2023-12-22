package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.feign.RemoteCarouselConfService;
import com.aurine.cloudx.estate.service.ProjectCarouselConfService;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfQuery;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(value = "projectCarouselConf", tags = "社区资讯")
@RequestMapping("/projectCarouselConf")
public class ProjectCarouselConfController {
    @Resource
    private ProjectCarouselConfService projectCarouselConfService;
    @Resource
    private RemoteCarouselConfService remoteCarouselConfService;

    @ApiOperation(value = "获取资讯列表（资讯）", notes = "获取资讯列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
    })
    @GetMapping("/page")
    public R<Page<ProjectCarouselConfVo>> carouselConfPage (Page page, ProjectCarouselConfQuery carouselConfQuery) {
        return R.ok(projectCarouselConfService.fetchList(page, carouselConfQuery));
    }

    @ApiOperation(value = "获取资讯详情（资讯）", notes = "获取资讯详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "infoId", value = "资讯ID", required = true, paramType = "path"),
    })
    @GetMapping("/{infoId}")
    public R<ProjectCarouselConf> carouselConfPage (@PathVariable("infoId") String infoId) {
        return remoteCarouselConfService.selectOne(infoId);
    }
}
