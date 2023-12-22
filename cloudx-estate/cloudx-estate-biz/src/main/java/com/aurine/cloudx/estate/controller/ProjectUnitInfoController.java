

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import com.aurine.cloudx.estate.service.ProjectUnitInfoService;
import com.aurine.cloudx.estate.vo.ProjectUnitListVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 单元
 *
 * @author 王伟
 * @date 2020-06-10 11:10:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseUnitInfo" )
@Api(value = "baseUnitInfo", tags = "单元管理")
public class ProjectUnitInfoController {

    private final  ProjectUnitInfoService projectUnitInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectUnitInfo 单元
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectUnitInfoPage(Page page, ProjectUnitInfo projectUnitInfo) {
        return R.ok(projectUnitInfoService.page(page, Wrappers.query(projectUnitInfo)));
    }

    /**
     * list查询
     * @param buildingId 单元
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list/{buildingId}" )
    public R listUnit(@PathVariable("buildingId" ) String buildingId) {
        return R.ok(projectUnitInfoService.listUnit(buildingId));
    }
    /**
     * list查询
     * @param buildingId 楼栋
     * @return
     */
    @ApiOperation(value = "根据楼栋id查询单元集合", notes = "根据楼栋id查询单元集合")
    @GetMapping("/inner/list/{buildingId}" )
    @Inner(false)
    public R innerListUnit(@PathVariable("buildingId" ) String buildingId) {
        return R.ok(projectUnitInfoService.listUnit(buildingId));
    }

    /**
     * list查询
     * @param unitId 单元
     * @return
     */
    @ApiOperation(value = "查询单元信息", notes = "查询单元信息")
    @GetMapping("/inner/info/{unitId}" )
    @Inner(false)
    public R innerUnitInfo(@PathVariable("unitId" ) String unitId) {
        return R.ok(projectUnitInfoService.getById(unitId));
    }

    /**
     * 查询楼栋id
     * @param unitId 单元
     * @return
     */
    @ApiOperation(value = "查询单元信息", notes = "查询单元信息")
    @GetMapping("/inner/getBuildingId/{unitId}" )
    @Inner(false)
    public R innerGetBuildingId(@PathVariable("unitId" ) String unitId) {
        return R.ok(projectUnitInfoService.getBuildingIdByUnitId(unitId));
    }

    /**
     * 查询单元统计信息
     * @param buildingId
     * @return
     */
    @GetMapping("/list/info/{buildingId}")
    public R listUnitInfo(@PathVariable("buildingId") String buildingId) {
        return R.ok(projectUnitInfoService.listUnitInfo(buildingId));
    }


//    /**
//     * 通过id查询单元
//     * @param seq id
//     * @return R
//     */
//    @ApiOperation(value = "通过id查询", notes = "通过id查询")
//    @GetMapping("/{seq}" )
//    public R getById(@PathVariable("seq" ) Integer seq) {
//        return R.ok(projectUnitInfoService.getById(seq));
//    }

//    /**
//     * 新增单元
//     * @param projectUnitInfo 单元
//     * @return R
//     */
//    @ApiOperation(value = "新增单元", notes = "新增单元")
//    @SysLog("新增单元" )
//    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectunitinfo_add')" )
//    public R save(@RequestBody ProjectUnitInfo projectUnitInfo) {
//        return R.ok(projectUnitInfoService.save(projectUnitInfo));
//    }

    /**
     * 修改单元
     *
     * @param unitListVo
     *         单元列表VO
     *
     * @return R
     */
    @ApiOperation(value = "修改单元", notes = "修改单元")
    @SysLog("修改单元" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectunitinfo_edit')" )
    public R updateById(@RequestBody ProjectUnitListVo unitListVo) {
        return R.ok(projectUnitInfoService.updateBatchUnit(unitListVo.getUnitList()));
    }

//    /**
//     * 通过id删除单元
//     * @param seq id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除单元", notes = "通过id删除单元")
//    @SysLog("通过id删除单元" )
//    @DeleteMapping("/{seq}" )
//    @PreAuthorize("@pms.hasPermission('estate_projectunitinfo_del')" )
//    public R removeById(@PathVariable Integer seq) {
//        return R.ok(projectUnitInfoService.removeById(seq));
//    }

}
