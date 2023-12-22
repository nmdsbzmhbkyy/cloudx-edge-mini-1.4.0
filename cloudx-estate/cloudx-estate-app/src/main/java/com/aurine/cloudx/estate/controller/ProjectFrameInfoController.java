

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.vo.ProjectFrameInfoTreeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 楼栋框架 (ProjectFrameInfoController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/3 15:13
 */
@RestController
@RequestMapping("/frame")
@Api(value = "frame", tags = "框架管理")
public class ProjectFrameInfoController {
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;


    /**
     * 获取子系统树
     *
     * @return
     */
    @ApiOperation(value = "获取当前小区树结构（小区）", notes = "获取当前小区树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/tree")
    public R<List<ProjectFrameInfoTreeVo>> findTree() {
        return R.ok(projectFrameInfoService.findTree(""));
    }

    /**
     * 通过父级id查询下级框架信息
     *
     * @param puid
     * @return R
     */
    @ApiOperation(value = "通过父级id查询下级列表（小区）", notes = "传楼栋ID获取单元列表，传单元ID获取房屋列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "puid", value = "父级id", required = true, paramType = "path")
    })
    @GetMapping("/list/puid/{puid}")
    public R<List<ProjectFrameInfo>> getListByPuid(@PathVariable("puid") String puid) {
        return R.ok(projectFrameInfoService.listByPuid(puid));
    }

    /**
     * 通过level和projectId查询框架信息
     *
     * @param level
     * @return R
     */
    @ApiOperation(value = "通过level查询小区信息(小区)", notes = "通过level和projectId查询框架信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "level", value = "框架所处层级（4：组团，3：楼栋，2：单元，1：房屋）", required = true, paramType = "path")
    })
    @GetMapping("/list/level/{level}")
    public R<List<ProjectFrameInfo>> getList(@PathVariable("level") Integer level) {
        return R.ok(projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, level)));
    }

    /**
     * 获取子系统树
     *
     * @return
     */
    @ApiOperation(value = "获取level级以上的小区树结构（小区）", notes = "获取level级以上的小区树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "level", value = "框架所处层级（7、6、5、4：组团，3：楼栋，2：单元，1：房屋）", required = true, paramType = "path")
    })
    @GetMapping("/tree/{level}")
    public R<List<ProjectFrameInfoTreeVo>> findTreeByLevel(@PathVariable("level") Integer level) {
        return R.ok(projectFrameInfoService.findTreeByLevel(level));
    }
}
