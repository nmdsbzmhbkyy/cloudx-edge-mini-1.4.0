

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.entity.ProjectFingerprints;
import com.aurine.cloudx.estate.service.ProjectFingerprintsService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 记录项目辖区内允许通行的指纹信息，供辖区内已开放通行权限的指纹识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:20:22
 */
@RestController
@RequestMapping("/projectFingerPrints")
@Api(value = "projectFingerPrints", tags = "记录项目辖区内允许通行的指纹信息，供辖区内已开放通行权限的指纹识别设备下载管理")
public class ProjectFingerprintsController {
    @Resource
    private ProjectFingerprintsService projectFingerprintsService;
    @Resource
    private   ProjectRightDeviceService projectRightDeviceService;

    /**
     * 分页查询
     *
     * @param page                分页对象
     * @param projectFingerprints 指纹对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectFingerprints projectFingerprints) {
        return R.ok(projectFingerprintsService.page(page, Wrappers.query(projectFingerprints)));
    }


    /**
     * 通过id查询记录项目辖区内允许通行的指纹信息
     *
     * @param id 指纹id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectFingerprintsService.getById(id));
    }

    /**
     * 查询指纹列表通过人员id
     *
     * @param personId 人员id
     * @return
     */
    @ApiOperation(value = "查询指纹列表通过人员id", notes = "查询指纹列表通过人员id")
    @GetMapping("/list/{personId}")
    public R listProjectFingerprintByPersonId(@PathVariable("personId")  String personId) {
        return R.ok(projectFingerprintsService.list(new QueryWrapper<ProjectFingerprints>().lambda().eq(ProjectFingerprints::getPersonId,personId)));
    }

    /**
     * 新增记录项目辖区内允许通行的指纹信息
     *
     * @param projectFingerprints 指纹对象
     * @return R
     */
    @ApiOperation(value = "新增指纹", notes = "新增指纹")
    @SysLog("通过人员id新增指纹")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectfingerprints_add')")
    public R save(@RequestBody ProjectFingerprints projectFingerprints) {
        projectFingerprints.setStatus(PassRightTokenStateEnum.USED.code);
        return R.ok(projectFingerprintsService.save(projectFingerprints));
    }

    /**
     * 修改记录项目辖区内允许通行的指纹信息
     *
     * @param projectFingerprints 指纹对象
     * @return R
     */
    @ApiOperation(value = "通过指纹id修改", notes = "通过指纹id修改")
    @SysLog("通过指纹id修改")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectfingerprints_edit')")
    public R updateById(@RequestBody ProjectFingerprints projectFingerprints) {
        return R.ok(projectFingerprintsService.updateById(projectFingerprints));
    }

    /**
     * 通过id删除记录项目辖区内允许通行的指纹信息
     *
     * @param fingerprintId id
     * @return R
     */
    @ApiOperation(value = "通过指纹id删除", notes = "通过指纹id删除")
    @SysLog("通过指纹id删除")
    @DeleteMapping("/{fingerprintId}")
//    @PreAuthorize("@pms.hasPermission('com.aurine.cloudx_projectfingerprints_del')")
    public R removeById(@PathVariable String fingerprintId) {
        projectRightDeviceService.removeCertmedia(fingerprintId);
        return R.ok(projectFingerprintsService.removeById(fingerprintId));
    }

}
