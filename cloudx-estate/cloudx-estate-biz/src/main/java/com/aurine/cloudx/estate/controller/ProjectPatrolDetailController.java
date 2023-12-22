package com.aurine.cloudx.estate.controller;
import com.aurine.cloudx.estate.service.ProjectPatrolDetailService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 巡更明细(ProjectPatrolDetail)表控制层
 *
 * @author 王良俊
 * @since 2020-09-17 10:29:21
 */
@RestController
@RequestMapping("projectPatrolDetail")
@Api(value = "projectPatrolDetail", tags = "巡更明细")
public class ProjectPatrolDetailController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPatrolDetailService projectPatrolDetailService;


    /**
     * 根据巡更详细ID获取到巡更名细列表
     */
    @GetMapping("getDetailListByPatrolId/{patrolId}")
    @ApiOperation(value = "通过id查询", notes = "根据巡更详细ID获取到巡更名细列表")
    public R selectOne(@PathVariable String patrolId) {
        return R.ok(this.projectPatrolDetailService.getDetailListByPatrolId(patrolId));
    }


}