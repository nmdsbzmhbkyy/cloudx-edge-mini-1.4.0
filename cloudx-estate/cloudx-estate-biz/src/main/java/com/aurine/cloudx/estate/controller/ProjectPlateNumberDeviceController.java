package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectPlateNumberDeviceService;
import com.aurine.parking.entity.po.ProjectPlateNumberDevice;
import com.aurine.parking.entity.vo.ParkingDeviceCertDlstatusCountVo;
import com.aurine.parking.entity.vo.PlateNumberDeviceSearchCondition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 设备车牌下发情况控制器
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/26 15:25
 */
@RestController
@RequestMapping("/projectPlateNumberDevice")
public class ProjectPlateNumberDeviceController {

    @Resource
    ProjectPlateNumberDeviceService projectPlateNumberDeviceService;

    /**
     * <p>
     * 分页查询车牌号下发情况
     * </p>
     *
     * @param page  分页信息
     * @param query 查询条件
     * @return 分页数据
     * @author: 王良俊
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询车牌号下发记录数据")
    public R<Page<ProjectPlateNumberDevice>> page(Page page, PlateNumberDeviceSearchCondition query) {
        return projectPlateNumberDeviceService.selectPage(page, query);
    }

    /**
     * <p>
     * 根据设备id获取所有车牌号下发记录
     * </p>
     *
     * @param deviceId 设备ID
     * @return 该设备的车牌号下发记录
     * @author: 王良俊
     */
    @GetMapping("/list/{deviceId}")
    @ApiOperation(value = "分页查询", notes = "获取设备所有车牌号下发记录")
    public R<List<ProjectPlateNumberDevice>> listByDeviceId(@PathVariable("deviceId") String deviceId) {
        return projectPlateNumberDeviceService.listByDeviceId(deviceId);
    }

    /**
     * <p>
     * 获取车牌号下发情况的统计信息
     * </p>
     *
     * @return 当前项目车牌号下发情况统计信息
     * @author: 王良俊
     */
    @GetMapping("/countByProjectId")
    @ApiOperation(value = "分页查询", notes = "获取项目所有车道设备车牌下发情况统计信息")
    public R<List<ParkingDeviceCertDlstatusCountVo>> countByProject() {
        return projectPlateNumberDeviceService.countByProject();
    }

    @GetMapping("/exportExcel/{deviceId}/{deviceName}")
    @ApiOperation(value = "导出excel", notes = "导出excel")
    public void download(@PathVariable String deviceId, @PathVariable String deviceName, HttpServletResponse httpServletResponse) {
        projectPlateNumberDeviceService.exportExcel(deviceId, deviceName, httpServletResponse);
    }

    @PostMapping("/redownloadFailedPlateNumber")
    @ApiOperation(value = "重新下载失败车牌号", notes = "重新下载失败车牌号")
    public R<Boolean> redownloadFailedPlateNumber(@RequestBody List<String> deviceIdList) {
        return projectPlateNumberDeviceService.redownloadFailedPlateNumber(deviceIdList);
    }

}
