package com.aurine.cloudx.estate.open.parking.controller;


import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.open.parking.bean.ProjectParkRegionSeachConditionVoPage;
import com.aurine.cloudx.estate.open.parking.fegin.RemoteProjectParkRegionService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/park-region" )
@Api(value = "park-Region", tags = "车位区域管理")
public class ProjectParkRegionController {

    @Resource
    private RemoteProjectParkRegionService projectParkRegionService;


    /**
     * 分页查询
     * @param page
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('park-region:get:page')")
    public R getProjectParkRegionPage(ProjectParkRegionSeachConditionVoPage page) {
        return projectParkRegionService.getProjectParkRegionPage(page);
    }

    /**
     * 新增车位区域
     * @param projectParkRegion 车位区域對对象
     * @return R
     */
    @ApiOperation(value = "新增车位区域", notes = "新增车位区域")
    @SysLog("新增车位区域" )
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectParkRegion_add')" )
    @PreAuthorize("@pms.hasPermission('park-region:post:save')")
    public R save(@RequestBody ProjectParkRegion projectParkRegion) {
        return projectParkRegionService.save(projectParkRegion);
    }
    /**
     * 修改车位区域
     * @param projectParkRegion 车位区域
     * @return R
     */
    @ApiOperation(value = "修改车位区域", notes = "修改车位区域")
    @SysLog("修改车位区域" )
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectParkRegion_edit')" )
    @PreAuthorize("@pms.hasPermission('park-region:put:updateById')")
    public R updateById(@RequestBody ProjectParkRegion projectParkRegion) {
       return projectParkRegionService.updateById(projectParkRegion);
    }
    /**
     * 通过id删除车位区域
     * @param parkRegionId 车位区域id
     * @return R
     */
//    @PreAuthorize("@pms.hasPermission('estate_projectParkRegion_del')" )
    @ApiOperation(value = "通过id删除车位区域", notes = "通过id删除车位区域")
    @SysLog("通过id删除车位区域" )
    @DeleteMapping("/{parkRegionId}" )
    @PreAuthorize("@pms.hasPermission('park-region:delete:removeById')")
    public R removeById(@PathVariable String parkRegionId) {
        return projectParkRegionService.removeById(parkRegionId);
    }

}
