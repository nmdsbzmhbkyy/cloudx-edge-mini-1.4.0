

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.dto.OpenApiProjectAddBlacklistFaceDto;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectFaceResourcesService;
import com.aurine.cloudx.estate.vo.ProjectFaceResourcesAppVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 项目人脸库，用于项目辖区内的人脸识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:06
 */
@RestController
@RequestMapping("/projectFaceResources")
@Api(value = "projectFaceResources", tags = "项目人脸库，用于项目辖区内的人脸识别设备下载管理")
public class ProjectFaceResourcesController {

//    @Resource
//    private WebProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private AbstractProjectFaceResourcesService adapterWebProjectFaceResourcesServiceImpl;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    /**
     * 分页查询
     *
     * @param page                 分页对象
     * @param projectFaceResources 项目人脸库，用于项目辖区内的人脸识别设备下载
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectFaceResources projectFaceResources) {
        return R.ok(adapterWebProjectFaceResourcesServiceImpl.page(page, Wrappers.query(projectFaceResources)));
    }


    /**
     * 通过id查询项目人脸库，用于项目辖区内的人脸识别设备下载
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(adapterWebProjectFaceResourcesServiceImpl.getById(id));
    }

    /**
     * 查询人脸列表通过人员id
     *
     * @param personId 人员id
     * @return
     */
    @ApiOperation(value = "查询人脸列表通过人员id", notes = "查询人脸列表通过人员id")
    @GetMapping("/list/{personId}")
    public R listProjectFaceResourcesByPersonId(@PathVariable("personId") String personId) {
        return R.ok(adapterWebProjectFaceResourcesServiceImpl.list(new QueryWrapper<ProjectFaceResources>().lambda().eq(ProjectFaceResources::getPersonId, personId)));
    }


    @ApiOperation(value = "保存人脸数据", notes = "保存人脸数据")
    @SysLog("保存人脸数据")
    @PostMapping("/saveFaceByApp")
    public R saveFaceByApp(@RequestBody ProjectFaceResourcesAppVo projectFaceResources) {

        /**
         * 调用WR20接口，执行添加人脸
         * @since 2020-08-07
         * @author: 王伟
         */
//        WR20Factory.getFactoryInstance().getRightService(ProjectContextHolder.getProjectId()).addFace(ProjectContextHolder.getProjectId(), projectFaceResources);

        return R.ok(adapterWebProjectFaceResourcesServiceImpl.addFaceFromApp(projectFaceResources));
    }


    /**
     * 新增项目人脸库，用于项目辖区内的人脸识别设备下载
     *
     * @param projectFaceResources 项目人脸库，用于项目辖区内的人脸识别设备下载
     * @return R
     */
    @ApiOperation(value = "保存人脸数据", notes = "保存人脸数据")
    @SysLog("保存人脸数据")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectFaceResources_add')")
    public R save(@RequestBody ProjectFaceResources projectFaceResources) {

//        /**
//         * 调用WR20接口，执行添加人脸
//         * @since 2020-08-07
//         * @author: 王伟
//         */
//        WR20Factory.getFactoryInstance().getRightService(ProjectContextHolder.getProjectId()).addFace(ProjectContextHolder.getProjectId(), projectFaceResources);

        return R.ok(adapterWebProjectFaceResourcesServiceImpl.saveFace(projectFaceResources));
    }

    /**
     * 修改项目人脸库，用于项目辖区内的人脸识别设备下载
     *
     * @param projectFaceResources 项目人脸库，用于项目辖区内的人脸识别设备下载
     * @return R
     */
    @ApiOperation(value = "修改人脸数据", notes = "修改人脸数据")
    @SysLog("修改人脸数据")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectfaceresources_edit')")
    public R updateById(@RequestBody ProjectFaceResources projectFaceResources) {
        return R.ok(adapterWebProjectFaceResourcesServiceImpl.updateById(projectFaceResources));
    }

    /**
     * 通过id删除项目人脸库，用于项目辖区内的人脸识别设备下载
     *
     * @param faceId 人脸id
     * @return R
     */
    @ApiOperation(value = "删除人脸数据", notes = "删除人脸数据")
    @SysLog("删除人脸数据")
    @DeleteMapping("/{faceId}")
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectfaceresources_del')")
    public R removeById(@PathVariable String faceId) {
//        webProjectRightDeviceService.removeCertmedia(faceId);
//
//        /**
//         * 调用WR20接口，删除全部人脸
//         * @since 2020-08-10
//         * @author: 王伟
//         */
//        ProjectFaceResources projectFaceResources = projectFaceResourcesService.getById(faceId);
//        WR20Factory.getFactoryInstance().getRightService(ProjectContextHolder.getProjectId()).delFace(ProjectContextHolder.getProjectId(), projectFaceResources);

        return R.ok(adapterWebProjectFaceResourcesServiceImpl.removeFace(faceId));
    }


}
