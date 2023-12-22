package com.aurine.cloudx.estate.open.parking.controller;


import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.open.parking.bean.ProjectParkingPlaceConditionVoPage;
import com.aurine.cloudx.estate.open.parking.fegin.RemoteProjectParkingPlaceService;
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
@RequestMapping("/parking-place")
@Api(value = "parking-place", tags = "车位管理")
public class ProjectParkingPlaceController {

    @Resource
    private RemoteProjectParkingPlaceService projectParkingPlaceService;

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('parking-place:get:getProjectParkingPlacePage')")
    public R getProjectParkingPlacePage(ProjectParkingPlaceConditionVoPage page) {
        return projectParkingPlaceService.getProjectParkingPlacePage(page);
    }


    /**
     * 新增车位
     *
     * @param projectParkingPlace 车位
     * @return R
     */
    @ApiOperation(value = "新增车位", notes = "新增车位")
    @SysLog("新增车位")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('parking-place:post:save')")
    public R save(@RequestBody ProjectParkingPlace projectParkingPlace) {
        return projectParkingPlaceService.save(projectParkingPlace);
    }

    /**
     * 修改车位
     *
     * @param projectParkingPlace 车位
     * @return R
     */
    @ApiOperation(value = "修改车位", notes = "修改车位")
    @SysLog("修改车位")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('parking-place:put:updateById')")
    public R updateById(@RequestBody ProjectParkingPlace projectParkingPlace) {
        return projectParkingPlaceService.updateById(projectParkingPlace);
    }

    /**
     * 通过id删除车位
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除车位", notes = "通过id删除车位")
    @SysLog("通过id删除车位")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('parking-place:delete:removeById')")
    public R removeById(@PathVariable String id) {
       return projectParkingPlaceService.removeById(id);
    }

}
