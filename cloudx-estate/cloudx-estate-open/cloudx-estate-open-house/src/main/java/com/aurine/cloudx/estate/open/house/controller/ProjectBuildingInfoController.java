package com.aurine.cloudx.estate.open.house.controller;

import com.aurine.cloudx.estate.open.house.bean.BuildingPage;
import com.aurine.cloudx.estate.open.house.fegin.RemoteProjectBuildingInfoService;
import com.aurine.cloudx.estate.vo.BuildingPublicFloorVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/building")
@Api(value = "building", tags = "楼栋管理")
public class ProjectBuildingInfoController {

    private RemoteProjectBuildingInfoService buildingInfoService;

    /**
     * 分页查询
     * @param page         分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('building:get:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getBuildingInfoPage(BuildingPage page) {
        return buildingInfoService.getBuildingInfoPage(page);
    }

    /**
     * 获取项目下的楼栋列表
     * @return
     */
    @ApiOperation(value = "获取项目下的楼栋列表", notes = "获取项目下的楼栋列表")
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('building:get:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R list() {
        return buildingInfoService.list();
    }

    /**
     * 获取项目下的楼栋列表
     * @return
     */
    @ApiOperation(value = "通过名称获取项目下的楼栋列表", notes = "通过名称获取项目下的楼栋列表")
    @GetMapping("/list-group/{name}")
    @PreAuthorize("@pms.hasPermission('building:get:list-group')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listWithGroup(@PathVariable("name") String name) {
        return buildingInfoService.listWithGroup(name);
    }

    /**
     * 通过楼栋ID获取到楼栋所属区域的信息
     * @return
     */
    @ApiOperation(value = "通过楼栋ID获取到楼栋所属区域的信息", notes = "通过楼栋ID获取到楼栋所属区域的信息")
    @GetMapping("/region/{buildingId}")
    @PreAuthorize("@pms.hasPermission('building:get:region')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R regionByBuildingId(@PathVariable("buildingId") String buildingId) {
        return buildingInfoService.regionByBuildingId(buildingId);
    }

    /**
     * 通过id查询楼栋
     *
     * @param buildingId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{buildingId}")
    @PreAuthorize("@pms.hasPermission('building:get')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getById(@PathVariable("buildingId") String buildingId) {
        return buildingInfoService.getById(buildingId);
    }

    /**
     * 查询当前项目所有组团列表
     *
     * @return R
     * @author:王良俊
     */
    @ApiOperation(value = "查询组团列表")
    @GetMapping("/frame-list")
    @PreAuthorize("@pms.hasPermission('building:get:frame-list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getFrameList() {
        return buildingInfoService.getFrameList();
    }

    /**
     * 新增楼栋
     *
     * @param building 楼栋
     * @return R
     */
    @ApiOperation(value = "新增楼栋", notes = "新增楼栋")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('building:post')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody ProjectBuildingInfoVo building) {
        return buildingInfoService.save(building);
    }

    /**
     * 批量新增楼栋
     *
     * @param vo 楼栋批量添加VO
     * @return R
     */
    @ApiOperation(value = "批量新增楼栋", notes = "批量新增楼栋")
    @SysLog("批量新增楼栋")
    @PostMapping("/batch")
    @PreAuthorize("@pms.hasPermission('building:post:batch')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R saveBatch(@RequestBody ProjectBuildingBatchVo vo) {
        return buildingInfoService.saveBatch(vo);
    }

    /**
     * 修改楼栋
     *
     * @param building 楼栋
     * @return R
     */
    @ApiOperation(value = "修改楼栋", notes = "修改楼栋")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('building:put')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateById(@RequestBody ProjectBuildingInfoVo building) {
        return buildingInfoService.updateById(building);
    }

    /**
     * 通过id删除楼栋
     *
     * @param buildingId id
     * @return R
     */
    @ApiOperation(value = "通过id删除楼栋", notes = "通过id删除楼栋")
    @DeleteMapping("/{buildingId}")
    @PreAuthorize("@pms.hasPermission('building:delete')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String buildingId) {
        return buildingInfoService.removeById(buildingId);
    }

    /**
     * 统计楼栋总数
     *
     * @return
     */
    @ApiOperation(value = "统计楼栋总数", notes = "统计楼栋总数")
    @GetMapping("/count")
    @PreAuthorize("@pms.hasPermission('building:get:count')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R countBuilding() {
        return buildingInfoService.countBuilding();
    }

    /**
     * 根据楼栋id获取其下的房屋名称
     *
     * @return
     */
    @ApiOperation(value = "根据楼栋id获取其下的房屋名称", notes = "根据楼栋id获取其下的房屋名称")
    @GetMapping("/list-house/{buildingId}")
    @PreAuthorize("@pms.hasPermission('building:get:list-house')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listHouseNameByBuildingId(@PathVariable("buildingId") String buildingId) {
        return buildingInfoService.listHouseNameByBuildingId(buildingId);
    }

    @ApiOperation(value = "根据层级编号获取到对应的位数", notes = "根据层级编号获取到对应的位数")
    @GetMapping("/getCodeRuleByLevel/{level}")
    public R getCodeRuleByLevel(@PathVariable String level){
        return buildingInfoService.getCodeRuleByLevel(level);
    }

    /**
     * 批量新增楼栋公共楼层
     *
     * @param vo 楼栋批量添加VO
     * @return R
     */
    @ApiOperation(value = "批量新增楼栋公共楼层", notes = "批量新增楼栋公共楼层")
    @SysLog("批量新增楼栋公共楼层")
    @PutMapping("/public-floor/batch")
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_edit')")
    public R savePublicFloorsBatch(@RequestBody BuildingPublicFloorVo list)  {
        return buildingInfoService.savePublicFloorsBatch(list);
    }
}
