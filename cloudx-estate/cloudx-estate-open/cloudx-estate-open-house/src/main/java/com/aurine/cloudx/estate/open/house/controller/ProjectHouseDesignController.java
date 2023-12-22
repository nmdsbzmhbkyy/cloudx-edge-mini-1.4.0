

package com.aurine.cloudx.estate.open.house.controller;

import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.aurine.cloudx.estate.open.house.bean.HouseDesignPage;
import com.aurine.cloudx.estate.open.house.fegin.RemoteProjectHouseDesignService;
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
@RequestMapping("/houseDesign")
@Api(value = "houseDesign", tags = "项目户型配置表管理")
public class ProjectHouseDesignController {

    @Resource
    private RemoteProjectHouseDesignService projectHouseDesignService;


    /**
     * 分页查询
     *
     * @param page       分页对象
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('houseDesign:get:page')")
    public R getHouseDesignPage(HouseDesignPage page) {
        return projectHouseDesignService.getHouseDesignPage(page);
    }


    /**
     * 通过id查询户型
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('houseDesign:get:getById')")
    public R getById(@PathVariable("id") String id) {
        return projectHouseDesignService.getById(id);
    }


    /**
     * 新增项目户型配置表
     *
     * @param projectHouseDesign 项目户型配置表
     * @return R
     */
    @ApiOperation(value = "新增项目户型配置表", notes = "新增项目户型配置表")
    @SysLog("新增项目户型配置表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('houseDesign:post:save')")
    public R save(@RequestBody ProjectHouseDesign projectHouseDesign) {
       return projectHouseDesignService.save(projectHouseDesign);
    }


    /**
     * 通过id删除项目户型配置表
     *
     * @param designId uuid
     * @return R
     */
    @ApiOperation(value = "通过id删除项目户型配置表", notes = "通过id删除项目户型配置表")
    @SysLog("通过id删除项目户型配置表")
    @DeleteMapping("/{designId}")
    @PreAuthorize("@pms.hasPermission('houseDesign:delete:removeById')")
    public R removeById(@PathVariable String designId) {
        return projectHouseDesignService.removeById(designId);
    }

    /**
     * 修改房型
     *
     * @param projectHouseDesign
     * @return R
     */
    @ApiOperation(value = "修改房型", notes = "修改房型")
    @SysLog("通过id修改房型")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('houseDesign:put:updateById')")
    public R updateById(@RequestBody ProjectHouseDesign projectHouseDesign) {
       return projectHouseDesignService.updateById(projectHouseDesign);
    }

}
