

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectProprietorDeviceProxyService;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;


/**
 * <p>住户-设备 权限管理</p>
 *
 * @ClassName: ProjectProprietorDeviceController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:49
 * @Copyright:
 */
@RestController
@RequestMapping("/serviceProprietorDevice")
@Api(value = "serviceProprietorDevice", tags = "人员设备权限管理")
public class ProjectProprietorDeviceController {
    @Resource
    private ProjectProprietorDeviceProxyService projectProprietorDeviceProxyService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param searchCondition 人员设备权限
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectPersonDevicePage(Page page, ProjectProprietorDeviceSearchCondition searchCondition) {
//        return R.ok(projectProprietorDeviceProxyService.page(page, Wrappers.query(projectPersonDevice)));
        return R.ok(projectProprietorDeviceProxyService.findPage(page, searchCondition));
    }


    /**
     * 通过id查询人员设备权限
     * 烤猫说：通过人员设备表获取人员类型，另外获取该人员的通行模板/方案(每个人只有一个通行方案)的基本信息(如方案id、生效时间、是否启用等)
     *      这里返回的信息就是该人员id的人员类型、通行方案id、生效时间、是否启用等
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{personId}")
    public R getById(@PathVariable("personId") String personId) {
        return R.ok(projectProprietorDeviceProxyService.getVo(personId));
    }

    /**
     * 通过id查询人员电梯权限
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/lift/{personId}")
    public R getLiftById(@PathVariable("personId") String personId) {
        return R.ok(projectProprietorDeviceProxyService.getPersonLift(personId));
    }

    /**
     * 通过id查询人员方案下可以使用的设备
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/listDevice/{personId}/{planId}")
    public R getDeviceById(@PathVariable("personId") String personId, @PathVariable("planId") String planId) {
        return R.ok(projectPersonDeviceService.listDeviceByPersonIdAndPlanId(personId, planId));
    }

    /**
     * 新增人员设备权限
     *
     * @param projectProprietorDeviceVo 人员设备权限
     * @return R
     */
    @ApiOperation(value = "新增人员设备权限", notes = "新增人员设备权限")
    @SysLog("新增人员设备权限")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_proprietordevice_add')")
    public R save(@Valid @RequestBody ProjectProprietorDeviceVo projectProprietorDeviceVo) {
        projectProprietorDeviceVo.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        projectProprietorDeviceVo.setEffTime(LocalDateTime.now());
        return R.ok(projectProprietorDeviceProxyService.save(projectProprietorDeviceVo));
    }

    /**
     * 修改人员设备权限
     *
     * @param projectProprietorDeviceVo 人员设备权限
     * @return R
     */
    @ApiOperation(value = "修改人员设备权限", notes = "修改人员设备权限")
    @SysLog("修改人员设备权限")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_proprietordevice_edit')")
    public R updateById(@Valid @RequestBody ProjectProprietorDeviceVo projectProprietorDeviceVo) {
        projectProprietorDeviceVo.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        projectProprietorDeviceVo.setEffTime(LocalDateTime.now());
        return R.ok(projectProprietorDeviceProxyService.save(projectProprietorDeviceVo));
    }
//
//    /**
//     * 通过id删除人员设备权限
//     * @param seq id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除人员设备权限", notes = "通过id删除人员设备权限")
//    @SysLog("通过id删除人员设备权限" )
//    @DeleteMapping("/{seq}" )
//    @PreAuthorize("@pms.hasPermission('estate_proprietordevice_del')" )
//    public R removeById(@PathVariable Integer seq) {
//        return R.ok(projectProprietorDeviceProxyService.removeById(seq));
//    }

    /**
     * 启用
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/enable/{personId}")
    public R enablePassRight(@PathVariable("personId") String personId) {
        return R.ok(projectProprietorDeviceProxyService.enablePassRight(personId));
    }

    /**
     * 禁用
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/disable/{personId}")
    public R disablePassRight(@PathVariable("personId") String personId) {
        return R.ok(projectProprietorDeviceProxyService.disablePassRight(personId));
    }

}
