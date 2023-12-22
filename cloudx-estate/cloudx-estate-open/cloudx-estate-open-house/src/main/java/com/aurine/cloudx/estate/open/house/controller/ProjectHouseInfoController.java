package com.aurine.cloudx.estate.open.house.controller;

import com.aurine.cloudx.estate.open.house.bean.HousePage;
import com.aurine.cloudx.estate.open.house.bean.ProjectHouseInfoPage;
import com.aurine.cloudx.estate.open.house.fegin.RemoteProjectHouseInfoService;
import com.aurine.cloudx.estate.vo.ProjectHouseInfoVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/houses")
@Api(value = "houses", tags = "房屋管理")
public class ProjectHouseInfoController {
    private RemoteProjectHouseInfoService remoteProjectHouseInfoService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PreAuthorize("@pms.hasPermission('houses:get:getProjectHouseInfoPage')")
    public R getProjectHouseInfoPage(ProjectHouseInfoPage page) {
        return remoteProjectHouseInfoService.getProjectHouseInfoPage(page);
    }

    /**
     * 通过id查询房屋
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过房屋id查询", notes = "通过房屋id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('houses:get:getById')")
    public R getById(@PathVariable("id") String id) {
        return remoteProjectHouseInfoService.getById(id);
    }

    /**
     * 新增房屋
     *
     * @param projectHouseInfo 房屋
     * @return R
     */
    @ApiOperation(value = "新增房屋", notes = "新增房屋")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('houses:post:save')")
    public R save(@RequestBody ProjectHouseInfoVo projectHouseInfo) {
        return remoteProjectHouseInfoService.save(projectHouseInfo);
    }

    /**
     * 修改房屋
     *
     * @param projectHouseInfo 房屋
     * @return R
     */
    @ApiOperation(value = "修改房屋", notes = "修改房屋")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('houses:put:updateById')")
    public R updateById(@RequestBody ProjectHouseInfoVo projectHouseInfo) {
        return remoteProjectHouseInfoService.updateById(projectHouseInfo);
    }

    /**
     * 通过id删除房屋
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除房屋", notes = "通过id删除房屋")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('houses:put:updateById')")
    public R removeById(@PathVariable String id) {
        return remoteProjectHouseInfoService.removeById(id);
    }

    /**
     * <p>
     * 查询该房屋的所有住户
     * </p>
     *
     * @param page page
     * @return
     * @throws
     * @author: 王良俊
     */
    @ApiOperation(value = "房屋住户查询")
    @SysLog("房屋住户信息查看")
    @GetMapping("/resident")
    @PreAuthorize("@pms.hasPermission('houses:get:getHouseResident')")
    public R getHouseResident(HousePage page) {
        return remoteProjectHouseInfoService.getHouseResident(page);
    }
}
