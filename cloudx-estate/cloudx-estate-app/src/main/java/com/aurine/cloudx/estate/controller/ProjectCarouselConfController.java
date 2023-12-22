package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.feign.RemoteCarouselConfService;
import com.aurine.cloudx.estate.service.ProjectCarouselConfService;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "carousel", tags = "社区资讯")
@RequestMapping("/carousel")
public class ProjectCarouselConfController {
    @Resource
    private RemoteCarouselConfService remoteCarouselConfService;

    @ApiOperation(value = "获取资讯列表（资讯）", notes = "获取资讯列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题名称", paramType = "query"),
            @ApiImplicitParam(name = "timeRange", value = "查询时间范围(yyyy-mm-dd hh:mm:ss格式,该类型为字符串数组，非空时必传两个值，开始时间与结束时间如：timeRange=2021-01-14%2000%3A00%3A00&timeRange=2021-02-24%2000%3A00%3A00)", paramType = "query")
    })
    @GetMapping("/page")
    public R<Page<ProjectCarouselConfVo>> carouselConfPage(
            @RequestParam(value = "size",required = false) Integer size,
            @RequestParam(value = "current",required = false) Integer current,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "timeRange", required = false) LocalDateTime[] timeRange
    ) {
        Map<String, Object> query = new HashMap<>();
        query.put("size", size);
        query.put("current", current);
        query.put("title", title);
        query.put("timeRange", timeRange);
        return remoteCarouselConfService.carouselConfPage(query);
    }

    @ApiOperation(value = "获取资讯详情（资讯）", notes = "获取资讯详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "carouselId", value = "资讯ID", required = true, paramType = "path"),
    })
    @GetMapping("/{carouselId}")
    public R<ProjectCarouselConf> carouselConfPage(@PathVariable("carouselId") String infoId) {
        return remoteCarouselConfService.selectOne(infoId);
    }
}
