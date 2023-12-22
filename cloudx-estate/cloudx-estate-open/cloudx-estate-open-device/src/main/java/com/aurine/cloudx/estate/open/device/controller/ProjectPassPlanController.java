package com.aurine.cloudx.estate.open.device.controller;


import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.open.device.bean.ProjectPassPlanPage;
import com.aurine.cloudx.estate.open.device.fegin.RemoteProjectPassPlanService;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoPageVo;
import com.aurine.cloudx.estate.vo.ProjectPassPlanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>通行方案视图控制器</p>
 *
 * @ClassName: ProjectPassPlanController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/21 14:16
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/service-pass-plan")
@Api(value = "servicePassPlan", tags = "通行方案管理")
public class ProjectPassPlanController {
    @Resource
    private RemoteProjectPassPlanService projectPassPlanService;

//    /**
//     * 分页查询
//     *
//     * @param page            分页对象
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    @GetMapping("/page")
//    public R getProjectPassPlanPage(ProjectPassPlanPage page) {
//        return projectPassPlanService.getProjectPassPlanPage(page);
//    }


    /**
     * 通过id查询通行方案
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('service-pass-plan:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "通行方案管理id", required = true, paramType = "path"),
    })
    public R<ProjectPassPlanVo> getById(@PathVariable("id") String id) {
        return projectPassPlanService.getById(id);
    }

//    /**
//     * 新增通行方案
//     *
//     * @param projectPassPlanVo 通行方案
//     * @return R
//     */
//    @ApiOperation(value = "新增通行方案", notes = "新增通行方案")
//    @SysLog("新增通行方案")
//    @PostMapping
//    public R save(@RequestBody ProjectPassPlanVo projectPassPlanVo) {
//
//        return projectPassPlanService.save(projectPassPlanVo);
//    }
//
//    /**
//     * 修改通行方案
//     *
//     * @param projectPassPlanVo 通行方案
//     * @return R
//     */
//    @ApiOperation(value = "修改通行方案", notes = "修改通行方案")
//    @SysLog("修改通行方案")
//    @PutMapping
//    public R updateById(@RequestBody ProjectPassPlanVo projectPassPlanVo) {
//        return projectPassPlanService.updateById(projectPassPlanVo);
//    }
//
//    /**
//     * 通过id删除通行方案
//     *
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除通行方案", notes = "通过id删除通行方案")
//    @SysLog("通过id删除通行方案")
//    @DeleteMapping("/{id}")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "通行方案管理id", required = true, paramType = "path"),
//    })
//    public R removeById(@PathVariable String id) {
//        return projectPassPlanService.removeById(id);
//    }


    /**
     * 通过类型获取通行方案
     *
     * @param planObject planObject
     * @return R
     */
    @ApiOperation(value = "通过类型获取通行方案", notes = "通过类型获取通行方案")
    @GetMapping("/listByType/{planObject}")
    @PreAuthorize("@pms.hasPermission('service-pass-plan:get:list-by-type')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "planObject", value = "通行方案类型", required = true, paramType = "path"),
    })
    public R<List<ProjectPassPlan>> list(@PathVariable("planObject") String planObject) {
        return projectPassPlanService.list(planObject);
    }

    /**
     * 通过通行方案ID获取可选设备状态列表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过类型获取通行方案", notes = "通过类型获取通行方案")
    @GetMapping("/listDevice/{id}")
    @PreAuthorize("@pms.hasPermission('service-pass-plan:get:list-device')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "通行方案管理id", required = true, paramType = "path"),
    })
    public R listDevice(@PathVariable("id") String id) {
        return this.projectPassPlanService.listDevice(id);
    }

}
