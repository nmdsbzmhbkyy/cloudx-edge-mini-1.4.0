

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectDeviceLocation;
import com.aurine.cloudx.estate.service.ProjectDeviceLocationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 记录设备平面图位置打点信息
 *
 * @author lingang
 * @date 2020-06-15 16:07:41
 */
@RestController
@AllArgsConstructor
@RequestMapping("/deviceLocation")
@Api(value = "projectDeviceLocation", tags = "记录设备平面图位置打点信息管理")
public class ProjectDeviceLocationController {

    private final ProjectDeviceLocationService projectDeviceLocationService;

    /**
     * 通过id查询记录设备平面图位置打点信息
     *
     * @param deviceId
     *         id
     * @param picId
     *         图片地址
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/getPoint/{deviceId}/{picId}")
    public R getPoint(@PathVariable("deviceId") String deviceId, @PathVariable("picId") String picId) {
        QueryWrapper<ProjectDeviceLocation> queryWrapper = new QueryWrapper<ProjectDeviceLocation>();
        queryWrapper.eq("deviceId", deviceId);
        queryWrapper.eq("picId", picId);

        return R.ok(projectDeviceLocationService.getOne(queryWrapper));
    }

    /**
     * 新增记录设备平面图位置打点信息
     * 
     * @param projectDeviceLocation 记录设备平面图位置打点信息
     * @return R
     */
    @ApiOperation(value = "新增记录设备平面图位置打点信息", notes = "新增记录设备平面图位置打点信息")
    @SysLog("新增记录设备平面图位置打点信息")
    @PostMapping
    public R save(@RequestBody ProjectDeviceLocation projectDeviceLocation) {
        return R.ok(projectDeviceLocationService.save(projectDeviceLocation));
    }

    /**
     * 修改记录设备平面图位置打点信息
     * 
     * @param projectDeviceLocation 记录设备平面图位置打点信息
     * @return R
     */
    @ApiOperation(value = "修改记录设备平面图位置打点信息", notes = "修改记录设备平面图位置打点信息")
    @SysLog("修改记录设备平面图位置打点信息")
    @PutMapping
    public R update(@RequestBody ProjectDeviceLocation projectDeviceLocation) {
        QueryWrapper<ProjectDeviceLocation> queryWrapper = new QueryWrapper<ProjectDeviceLocation>();
        queryWrapper.eq("deviceId", projectDeviceLocation.getDeviceId());
        queryWrapper.eq("picId", projectDeviceLocation.getPicId());
        
        return R.ok(projectDeviceLocationService.update(projectDeviceLocation, queryWrapper));
    }

    /**
     * 通过id删除记录设备平面图位置打点信息
     *
     * @param deviceId
     *         设备id
     * @param picId
     *         图片id
     *
     * @return R
     */
    @ApiOperation(value = "通过id删除记录设备平面图位置打点信息", notes = "通过id删除记录设备平面图位置打点信息")
    @SysLog("通过id删除记录设备平面图位置打点信息")
    @DeleteMapping("/removePoint/{deviceId}/{picId}")
    public R removePoint(@PathVariable("deviceId") String deviceId, @PathVariable("picId") String picId) {
        QueryWrapper<ProjectDeviceLocation> queryWrapper = new QueryWrapper<ProjectDeviceLocation>();
        queryWrapper.eq("deviceId", deviceId);
        queryWrapper.eq("picId", picId);
        
        return R.ok(projectDeviceLocationService.remove(queryWrapper));
    }
    
    /**
     * 通过id查询记录设备平面图位置打点信息
     *
     * @param deviceId
     *         id
     * @param picId
     *         图片地址
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/getPoints/{picId}")
    public R getPoints(@PathVariable("picId") String picId) {
        return R.ok(projectDeviceLocationService.getPoints(picId));
    }
}
