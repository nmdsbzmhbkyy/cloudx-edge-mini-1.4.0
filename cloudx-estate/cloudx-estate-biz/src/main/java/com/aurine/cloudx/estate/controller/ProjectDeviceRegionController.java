

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectBuildingInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceRegion;
import com.aurine.cloudx.estate.entity.ProjectFloorPic;
import com.aurine.cloudx.estate.service.ProjectBuildingInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceRegionService;
import com.aurine.cloudx.estate.service.ProjectFloorPicService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonObject;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


/**
 * 设备区域表
 *
 * @author xull@aurine.cn
 * @date 2020-05-25 08:50:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDeviceRegion")
@Api(value = "projectDeviceRegion", tags = "设备区域表管理")
public class ProjectDeviceRegionController {

    private final ProjectDeviceRegionService projectDeviceRegionService;

    @Autowired
    private final ProjectFloorPicService projectFloorPicService;
    @Autowired
    private final ProjectDeviceInfoService projectDeviceInfoService;
    @Autowired
    private final ProjectBuildingInfoService projectBuildingInfoService;



    /**
     * 分页查询
     *
     * @param page                分页对象
     * @param projectDeviceRegion 设备区域表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectDeviceRegion>> getProjectDeviceRegionPage(Page page, ProjectDeviceRegion projectDeviceRegion) {
        return R.ok(projectDeviceRegionService.page(page, Wrappers.query(projectDeviceRegion)));
    }


    /**
     * 通过id查询设备区域表
     *
     * @param regionId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{regionId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectDeviceRegion> getById(@PathVariable("regionId") Integer regionId) {
        return R.ok(projectDeviceRegionService.getById(regionId));
    }

    /**
     * 新增设备区域表
     *
     * @param projectDeviceRegion 设备区域表
     * @return R
     */
    @ApiOperation(value = "新增设备区域表", notes = "新增设备区域表")
    @SysLog("新增设备区域表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_deviceregion_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody ProjectDeviceRegion projectDeviceRegion) {
        return R.ok(projectDeviceRegionService.saveRegion(projectDeviceRegion));
    }

    /**
     * 修改设备区域表
     *
     * @param projectDeviceRegion 设备区域表
     * @return R
     */
    @ApiOperation(value = "修改设备区域表", notes = "修改设备区域表")
    @SysLog("修改设备区域表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_deviceregion_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateById(@RequestBody ProjectDeviceRegion projectDeviceRegion) {
        return R.ok(projectDeviceRegionService.updateById(projectDeviceRegion));
    }

    /**
     * 通过id删除设备区域表
     *
     * @param regionId id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备区域表", notes = "通过id删除设备区域表")
    @SysLog("通过id删除设备区域表")
    @DeleteMapping("/{regionId}")
    @PreAuthorize("@pms.hasPermission('estate_deviceregion_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String regionId) {
        String childRegionIds = projectDeviceRegionService.getChildRegionIds(regionId);
        if (StrUtil.isNotEmpty(childRegionIds)) {
            String[] childRegionIdArr = childRegionIds.split(",");
            List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
                    .in(ProjectDeviceInfo::getDeviceRegionId, childRegionIdArr));
            List<ProjectBuildingInfo> buildingList = projectBuildingInfoService.list(new QueryWrapper<ProjectBuildingInfo>().lambda()
                    .in(ProjectBuildingInfo::getRegionId, childRegionIdArr));
            if (deviceInfoList.size() > 0 || CollUtil.isNotEmpty(buildingList)) {
                return R.failed("区域下已有楼栋或设备不可删除");
            } else {
                // 这里删除区域下所有的平面图
                projectFloorPicService.remove(new QueryWrapper<ProjectFloorPic>().lambda().in(ProjectFloorPic::getRegionId, childRegionIdArr));
                return R.ok(projectDeviceRegionService.remove(new QueryWrapper<ProjectDeviceRegion>().lambda().in(ProjectDeviceRegion::getRegionId, childRegionIdArr)));
            }
        }
        return R.ok(true);
    }


    /**
     * 列表查询
     *
     * @param projectDeviceRegion 设备区域表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceRegion>> getProjectDeviceRegionPage(ProjectDeviceRegion projectDeviceRegion) {
        return R.ok(projectDeviceRegionService.list(Wrappers.query(projectDeviceRegion)));
    }

    /**
     * 获取子系统树
     *
     * @return
     */
    @ApiOperation("获取子系统树")
    @GetMapping("/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceRegionTreeVo>> findTree(String name) {
        return R.ok(projectDeviceRegionService.findTree(name));
    }

    /**
     * 根据设备类型获取子系统树详情
     *
     * @param name
     * @param type
     * @return
     */
    @ApiModelProperty("根据设备类型获取子系统树详情")
    @GetMapping("/treeByDeviceType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceRegionDetailTreeVo>> findTreeByDeviceType(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "deviceRegionId", required = false) String deviceRegionId) {
        return R.ok(projectDeviceRegionService.findTreeByDeviceType(name, type, deviceRegionId));
    }

    /**
     * 根据设备类型获取子系统树详情
     *
     * @return
     */
    @ApiModelProperty("根据设备类型获取子系统树详情")
    @GetMapping("/monitorTreeByDeviceType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R< List<ProjectMonitorNodeVo>> findMonitorTreeByDeviceType() {
        return R.ok(projectDeviceRegionService.findMonitorTreeByDeviceType(null));
    }

    /**
     * 获取楼栋区域信息分页数据
     *
     */
    @ApiModelProperty("获取楼栋区域信息分页数据")
    @GetMapping("/pageBuildingRegionInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R pageBuildingRegionInfo(Page page, String buildingName, String regionId, String regionName) {
        return R.ok(projectDeviceRegionService.pageBuildingRegionInfo(page, buildingName,regionId,regionName));
    }

    /**
     * 获取区域设置分页数据
     *
     */
    @ApiModelProperty("获取区域设置分页数据")
    @GetMapping("/pageRegionManager")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R pageRegionManager(Page page) {
        return R.ok(projectDeviceRegionService.pageRegionManager(page));
    }

    /**
     * 获取区域设置分页数据
     *
     */
    @ApiModelProperty("获取区域设置分页数据")
    @GetMapping("/listBuildingByRegionId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listBuildingByRegionId(@RequestParam("regionId") String regionId) {
        return R.ok(projectDeviceRegionService.listBuildingByRegionId(regionId));
    }

    /**
     * 获取区口设备列表根据区域ID
     *
     */
    @ApiModelProperty("获取区域设置分页数据")
    @GetMapping("/listRegionDeviceByRegionId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listRegionDeviceByRegionId(@RequestParam("regionId") String regionId) {
        return R.ok(projectDeviceRegionService.listRegionDeviceByRegionId(regionId));
    }

    /**
     * 移动楼栋以及楼栋下的设备到新的区域
     *
     */
    @ApiModelProperty("移动楼栋以及楼栋下的设备到新的区域")
    @PostMapping("/moveBuildingsRegion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R moveBuildingsRegion(@RequestBody ProjectBuildingRegionVo projectBuildingRegionVo) {
        return R.ok(projectDeviceRegionService.moveBuildingsRegion(projectBuildingRegionVo.getBuildingIdList(),
                projectBuildingRegionVo.getRegionId()));
    }

    /**
     * 更新区域的管辖人
     *
     */
    @ApiModelProperty("更新区域的管辖人")
    @PostMapping("/updateRegionManager")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateRegionManager(@RequestBody ProjectStaffRegionVo projectStaffRegionVo) {
        return R.ok(projectDeviceRegionService.updateRegionManager(projectStaffRegionVo.getStaffIdList(),
                projectStaffRegionVo.getRegionId()));
    }


}
