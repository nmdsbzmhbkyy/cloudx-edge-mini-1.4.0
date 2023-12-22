package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceParamInfoService;
import com.aurine.cloudx.estate.vo.ElevatorFloorInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)表控制层
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:15
 */
@RestController
@RequestMapping("projectDeviceParamInfo")
@Api(value = "projectDeviceParamInfo", tags = "设备参数信息表，存储设备的参数信息")
public class ProjectDeviceParamInfoController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param projectDeviceParamInfo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectDeviceParamInfo所有数据")
    public R selectAll(Page<ProjectDeviceParamInfo> page, ProjectDeviceParamInfo projectDeviceParamInfo) {
        return R.ok(this.projectDeviceParamInfoService.page(page, new QueryWrapper<>(projectDeviceParamInfo)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectDeviceParamInfo单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.projectDeviceParamInfoService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectDeviceParamInfo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectDeviceParamInfo数据")
    public R insert(@RequestBody ProjectDeviceParamInfo projectDeviceParamInfo) {
        return R.ok(this.projectDeviceParamInfoService.save(projectDeviceParamInfo));
    }

    /**
     * 修改数据
     *
     * @param projectDeviceParamInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectDeviceParamInfo数据")
    public R update(@RequestBody ProjectDeviceParamInfo projectDeviceParamInfo) {
        return R.ok(this.projectDeviceParamInfoService.updateById(projectDeviceParamInfo));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectDeviceParamInfo数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.projectDeviceParamInfoService.removeByIds(idList));
    }

    /**
     * 根据设备ID获取到这台设备拥有的所有参数数据
     *
     * @param deviceId 设备ID
     * @return 获取到的参数数据
     */
    @GetMapping("/getParamByDeviceId")
    @ApiOperation(value = "根据设备ID获取到这台设备拥有的所有参数数据", notes = "根据设备ID获取到这台设备拥有的所有参数数据")
    public R getParamByDeviceId(@RequestParam("deviceId") String deviceId) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceId, deviceId));
        return R.ok(this.projectDeviceParamInfoService.getParamByDeviceId(deviceId, deviceInfo.getProductId()));
    }


    /**
     * 根据设备ID获取到这个设备除在线状态外的状态
     *
     * @param deviceId 设备ID
     * @return 获取到的参数数据
     */
    @GetMapping("/getDeviceOtherStatus")
    @ApiOperation(value = "根据设备ID获取到这个设备除在线状态外的状态", notes = "根据设备ID获取到这个设备除在线状态外的状态")
    public R getDeviceOtherStatus(@RequestParam("deviceId") String deviceId) {
        return R.ok(this.projectDeviceParamInfoService.getDeviceOtherStatus(deviceId));
    }

    /**
     * 根据设备ID获取到这台设备拥有的所有参数数据
     *
     * @param deviceId 设备ID
     * @return 获取到的参数数据
     */
    @GetMapping("/refreshParam")
    @ApiOperation(value = "刷新设备参数", notes = "刷新设备参数")
    public R refreshParam(@RequestParam("deviceId") String deviceId) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda()
                .eq(ProjectDeviceInfo::getDeviceId, deviceId));
        return R.ok(this.projectDeviceParamInfoService.refreshParam(deviceInfo));
    }

    /**
     * 根据设备ID获取到这台设备拥有的所有参数数据
     *
     * @param floorSet 前端的分层设置
     * @return 获取到的参数数据
     */
    @GetMapping("/getFloorInfo/{floorSet}")
    @ApiOperation(value = "刷新设备参数", notes = "刷新设备参数")
    public R getFloorInfo(@PathVariable("floorSet") String floorSet) {
        ElevatorFloorInfo elevatorFloorInfo = projectDeviceParamInfoService.getFloorInfo(floorSet);
        return R.ok(elevatorFloorInfo);
    }

}