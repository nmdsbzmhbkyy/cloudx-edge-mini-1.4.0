

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 人员设备权限关系表
 *
 * @author 王良俊
 * @date 2020-05-22 11:32:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectPersonDevice")
@Api(value = "projectPersonDevice", tags = "人员设备权限关系表管理")
public class ProjectPersonDeviceController {

    private final ProjectPersonDeviceService projectPersonDeviceService;

    /**
     * 分页查询
     *
     * @param page                分页对象
     * @param projectPersonDevice 人员设备权限关系表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectPersonDevice projectPersonDevice) {
        return R.ok(projectPersonDeviceService.page(page, Wrappers.query(projectPersonDevice)));
    }


    /**
     * 通过id查询人员设备权限关系表
     *
     * @param seq 自增序列id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}")
    public R getById(@PathVariable("seq") Integer seq) {
        return R.ok(projectPersonDeviceService.getById(seq));
    }

    /**
     * 新增人员设备权限关系表
     *
     * @param projectPersonDevice 人员设备权限关系表
     * @return R
     */
    @ApiOperation(value = "新增人员设备权限关系表", notes = "新增人员设备权限关系表")
    @SysLog("新增人员设备权限关系表")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectPersonDevice_add')")
    public R save(@RequestBody ProjectPersonDevice projectPersonDevice) {
        return R.ok(projectPersonDeviceService.save(projectPersonDevice));
    }

    /**
     * 修改人员设备权限关系表
     *
     * @param projectPersonDevice 人员设备权限关系表
     * @return R
     */
    @ApiOperation(value = "修改人员设备权限关系表", notes = "修改人员设备权限关系表")
    @SysLog("修改人员设备权限关系表")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectPersonDevice_edit')")
    public R updateById(@RequestBody ProjectPersonDevice projectPersonDevice) {
        return R.ok(projectPersonDeviceService.updateById(projectPersonDevice));
    }

    /**
     * 通过id删除人员设备权限关系表
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除人员设备权限关系表", notes = "通过id删除人员设备权限关系表")
    @SysLog("通过id删除人员设备权限关系表")
    @DeleteMapping("/{seq}")
//    @PreAuthorize("@pms.hasPermission('estate_projectPersonDevice_del')")
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectPersonDeviceService.removeById(seq));
    }


    @ApiOperation(value = "重载该用户的权限", notes = "重载该用户的权限")
    @SysLog("重载该用户的权限")
    @GetMapping("/refreshByPersonId")
    public boolean refreshByPersonId(@RequestParam("personId")String personId, @RequestParam("personTypeEnum")PersonTypeEnum personTypeEnum) {
        return projectPersonDeviceService.refreshByPersonId(personId,personTypeEnum);
    }

}
