

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.service.ProjectHouseInfoService;
import com.aurine.cloudx.estate.vo.HouseDesginVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchVo;
import com.aurine.cloudx.estate.vo.ProjectHouseInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 房屋
 *
 * @author 王伟
 * @date 2020-05-08 16:29:18
 */
@RestController
@RequestMapping("/baseHouse")
@Api(value = "baseHouse", tags = "房屋管理")
public class ProjectHouseInfoController {
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;


    /**
     * 分页查询
     *
     * @param page             分页对象
     * @param projectHouseInfo 房屋
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectHouseInfoPage(Page page, ProjectHouseInfoVo projectHouseInfo) {
        return R.ok(projectHouseInfoService.findPage(page, projectHouseInfo));
    }

    /**
     * 根据单元查询房间列表
     *
     * @return
     */
    @ApiOperation(value = "根据单元查询房间列表", notes = "根据单元查询房间列表")
    @GetMapping("/inner/list/{projectId}/{unitId}")
    @Inner(value = false)
    public R innerListByUnit(@PathVariable Integer projectId, @PathVariable String unitId) {
        return R.ok(projectHouseInfoService.getByUnitId(unitId,projectId));
    }

    /**
     * 通过id查询房屋
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/inner/info/{id}")
    @Inner(value = false)
    public R innerInfoById(@PathVariable("id") String id) {
        return R.ok(projectHouseInfoService.getVoById(id));
    }

    /**
     * 根据单元查询房间数量
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/countUsed")
    public R countUsedNumber(String buildingId, String unitId) {
        return R.ok(projectHouseInfoService.countHouseUsed(buildingId, unitId));
    }

    /**
     * 根据单元查询房间数量
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/countUnused")
    public R countUnuseNumber(String buildingId, String unitId) {
        return R.ok(projectHouseInfoService.countHouseUnuse(buildingId, unitId));
    }


    /**
     * 通过id查询房屋
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectHouseInfoService.getVoById(id));
    }

    /**
     * 新增房屋
     *
     * @param projectHouseInfo 房屋
     * @return R
     */
    @ApiOperation(value = "新增房屋", notes = "新增房屋")
    @SysLog("新增房屋")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projecthouseinfo_add')")
    public R save(@RequestBody ProjectHouseInfoVo projectHouseInfo) {
        return R.ok(projectHouseInfoService.saveVo(projectHouseInfo));
    }




    /**
     * 批量新增房屋
     *
     * @param vo 楼栋批量添加VO
     * @return R
     */
    @ApiOperation(value = "批量新增楼栋", notes = "批量新增楼栋")
    @SysLog("批量新增楼栋")
    @PostMapping("/batch")
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_add')")
    public R saveBatch(@RequestBody ProjectBuildingBatchVo vo) {
        boolean result = projectHouseInfoService.saveBatchVo(vo);
        if (result) {
            return R.ok();
        } else {
            return R.failed("添加失败");
        }
    }

    /**
     * 修改房屋
     *
     * @param projectHouseInfo 房屋
     * @return R
     */
    @ApiOperation(value = "修改房屋", notes = "修改房屋")
    @SysLog("修改房屋")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projecthouseinfo_edit')")
    public R updateById(@RequestBody ProjectHouseInfoVo projectHouseInfo) {
        return R.ok(projectHouseInfoService.updateById(projectHouseInfo));
    }

    /**
     * 通过id删除房屋
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除房屋", notes = "通过id删除房屋")
    @SysLog("通过id删除房屋")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_projecthouseinfo_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(projectHouseInfoService.removeHouseAndFrameById(id));
    }

    /**
     * 通过id列表批量删除房屋
     *
     * @param houseIdList 房屋ID列表
     * @return R
     */
    @ApiOperation(value = "通过id列表批量删除房屋", notes = "通过id列表批量删除房屋")
    @SysLog("通过id列表批量删除房屋")
    @PostMapping("removeBatch")
    @PreAuthorize("@pms.hasPermission('estate_projecthouseinfo_del')")
    public R removeBatch(@RequestBody List<String> houseIdList) {
        return R.ok(projectHouseInfoService.removeBatch(houseIdList));
    }

    /**
     * 通过id修改房型
     * @param  houseDesginVo 房屋ID列表
     * @return R
     */
    @ApiOperation(value = "通过id列表修改房屋", notes = "通过id列表修改房屋")
    @SysLog("通过id修改房屋" )
    @PutMapping("/putBatch" )
    public R putBatch(@RequestBody HouseDesginVo houseDesginVo) {
        return R.ok(projectHouseInfoService.putBatch(houseDesginVo));
    }

    /**
     * <p>
     * 查询该房屋的所有住户
     * </p>
     *
     * @param houseId id
     * @return
     * @throws
     * @author: 王良俊
     */
    @ApiOperation(value = "房屋住户查询")
    @SysLog("房屋住户信息查看")
    @GetMapping("/getHouseResident")
    public R getHouseResident(Page page, String houseId) {
        return R.ok(projectHouseInfoService.getHouseResidents(page, houseId));
    }

    /**
     * <p>
     * 查询服务住户记录
     * </p>
     *
     * @param houseId id
     * @return
     * @throws
     * @author: 王良俊
     */
    @ApiOperation(value = "房屋住户查询")
    @SysLog("房屋住户信息查看")
    @GetMapping("/getHouseRecord")
    public R getHouseRecord(Page page, String houseId) {
        return R.ok(projectHouseInfoService.getHouseRecord(page, houseId));
    }

    /**
     * 统计房屋总数
     *
     * @return
     */
    @ApiOperation(value = "统计房屋总数")
    @GetMapping("/countHouse")
    public R countHouse() {
        return R.ok(projectHouseInfoService.countHouse());
    }

    /**
     * 根据房屋的用途进行分类统计
     *
     * @return
     */
    @ApiOperation(value = "根据房屋的用途进行分类统计")
    @GetMapping("/countByType")
    public R countByType() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("live", projectHouseInfoService.countLive());
        map.put("sublet", projectHouseInfoService.countSublet());
        map.put("commercial", projectHouseInfoService.countCommercial());
        map.put("free", projectHouseInfoService.countFree());

        return R.ok(map);
    }

    @ApiOperation(value = "根据房屋名称查询框架树信息")
    @GetMapping("/findIndoorByName")
    public R findIndoorByName(Page page, String name) {
        return R.ok(projectHouseInfoService.findIndoorByName(page,name));
    }

    /**
     * 根据项目获取楼层信息
     *
     * @return
     */
    @ApiOperation(value = "根据项目获取楼层信息")
    @GetMapping("/getFloors")
    public R getFloors() {
        return R.ok(projectHouseInfoService.getFloorByProjectId(ProjectContextHolder.getProjectId()));
    }

    /**
     * 根据楼栋获取楼层信息
     *
     * @return
     */
    @ApiOperation(value = "根据项目获取楼层信息")
    @GetMapping("/getFloorsByBuildingId")
    public R getFloorsByBuildingId(@RequestParam(required = true) String buildingId) {
        return R.ok(projectHouseInfoService.getFloorByBuildingId(buildingId));
    }

    /**
     * 根据楼栋获取楼层信息
     *
     * @return
     */
    @ApiOperation(value = "根据项目获取楼层信息")
    @GetMapping("/getListByBuildingId")
    public R getListByBuildingId(@RequestParam(required = true) String buildingId) {
        return R.ok(projectHouseInfoService.getByBuildingId(buildingId));
    }
}
