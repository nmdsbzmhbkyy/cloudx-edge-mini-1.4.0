

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectBuildingInfo;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.service.ProjectBuildingInfoService;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.vo.ProjectFrameInfoTreeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/baseBuildingFrame")
@Api(value = "baseBuildingFrame", tags = "框架管理")
public class ProjectFrameInfoController {
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;


    /**
     * 通过level和projectId查询框架信息
     *
     * @param level
     * @return R
     */
    @ApiOperation(value = "通过level和projectId查询框架信息(业主、物业)", notes = "通过level和projectId查询框架信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "level", value = "框架所处层级（4：组团，3：楼栋，2：单元，1：房屋）", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/list/{level}")
    public R<List<ProjectFrameInfo>> getList(@PathVariable("level") Integer level) {
        return R.ok(projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, level)));
    }

    /**
     * 获取项目下的楼栋列表
     *
     * @return
     */
    @ApiOperation(value = " 获取项目下的楼栋列表", notes = " 获取项目下的楼栋列表")
    @GetMapping("/listWithGroup")
    public R<List<ProjectBuildingInfo>> listWithGroup(@RequestParam(value = "name", required = false) String name) {
        return R.ok(projectBuildingInfoService.listBuildingWithGroup(name));
    }


    /**
     * 通过父级id查询下级框架信息
     *
     * @param puid
     * @return R
     */
    @ApiOperation(value = "通过父级id查询下级框架信息(业主、物业)", notes = "通过父级id查询下级框架信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "puid", value = "父级id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/listPuid/{puid}")
    public R<List<ProjectFrameInfo>> getListByPuid(@PathVariable("puid") String puid) {
        return R.ok(projectFrameInfoService.listByPuid(puid));
    }


    /**
     * 获取子系统树
     *
     * @return
     */
    @ApiOperation("获取框架树-LV4-7(业主、物业)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/tree")
    public R<List<ProjectFrameInfoTreeVo>> findTree() {
        return R.ok(projectFrameInfoService.findTree(""));
    }

    /**
     * 获取子系统树，带根节点
     *
     * @return
     */
    @ApiOperation("获取框架树-LV4-7,带有社区根节点(业主、物业)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/findTreeWithRoot")
    public R<List<ProjectFrameInfoTreeVo>> findTreeWithRoot() {
        return R.ok(projectFrameInfoService.findTree("小区"));
    }


}
