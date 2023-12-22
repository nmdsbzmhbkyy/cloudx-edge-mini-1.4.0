package com.aurine.cloudx.estate.open.person.controller;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.open.person.fegin.RemoteProjectFaceResourcesService;
import com.aurine.cloudx.estate.vo.ProjectFaceResourcesAppVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/face")
@Api(value = "projectFaceResources", tags = "面部管理")
public class ProjectFaceResourcesController {

    @Resource
    private RemoteProjectFaceResourcesService projectFaceResourcesService;

    /**
     * 查询人脸列表通过人员id
     *
     * @param personId 人员id
     * @return
     */
    @ApiOperation(value = "查询人脸列表通过人员id", notes = "查询人脸列表通过人员id")
    @GetMapping("/list/{personId}")
    @PreAuthorize("@pms.hasPermission('face:get:list')")
    public R listProjectFaceResourcesByPersonId(@PathVariable("personId") String personId) {
        return projectFaceResourcesService.listProjectFaceResourcesByPersonId(personId);
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
    @PreAuthorize("@pms.hasPermission('face:post:save')")
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectFaceResources_add')")
    public R save(@RequestBody ProjectFaceResources projectFaceResources) {
        return projectFaceResourcesService.save(projectFaceResources);
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
    @PreAuthorize("@pms.hasPermission('face:delete:removeById')")
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectfaceresources_del')")
    public R removeById(@PathVariable String faceId) {
        return projectFaceResourcesService.removeById(faceId);
    }
}
