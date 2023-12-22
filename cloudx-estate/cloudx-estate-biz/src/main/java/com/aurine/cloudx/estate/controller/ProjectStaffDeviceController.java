

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectStaffDeviceProxyService;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceVo;
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
 * <p>员工-设备 权限管理</p>
 *
 * @ClassName: ProjectStaffDeviceController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:49
 * @Copyright:
 */
@RestController
@RequestMapping("/serviceStaffDevice")
@Api(value = "serviceStaffDevice", tags = "员工设备权限管理")
public class ProjectStaffDeviceController {
    @Resource
    private ProjectStaffDeviceProxyService projectStaffDeviceProxyService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    /**
     * 分页查询
     *
     * @param page              分页对象
     * @param searchConditionVo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectStaffDevicePage(Page page, ProjectStaffDeviceSearchConditionVo searchConditionVo) {
//        return R.ok(projectStaffDeviceProxyService.page(page, Wrappers.query(projectStaffDevice)));
        return R.ok(projectStaffDeviceProxyService.findPage(page, searchConditionVo));
    }


    /**
     * 通过id查询人员设备权限
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{personId}")
    public R getById(@PathVariable("personId") String personId) {
        return R.ok(projectStaffDeviceProxyService.getVo(personId));
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
     * @param projectStaffDevice 人员设备权限
     * @return R
     */
    @ApiOperation(value = "新增人员设备权限", notes = "新增人员设备权限")
    @SysLog("新增人员设备权限")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectstaffdevice_add')")
    public R save(@Valid @RequestBody ProjectStaffDeviceVo projectStaffDevice) {
        projectStaffDevice.setPersonType(PersonTypeEnum.STAFF.code);
        projectStaffDevice.setEffTime(LocalDateTime.now());
        return R.ok(projectStaffDeviceProxyService.save(projectStaffDevice));
    }

    /**
     * 启用
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/enable/{personId}")
    public R enablePassRight(@PathVariable("personId") String personId) {
        return R.ok(projectStaffDeviceProxyService.enablePassRight(personId));
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
        return R.ok(projectStaffDeviceProxyService.disablePassRight(personId));
    }

//
//    /**
//     * 修改人员设备权限
//     *
//     * @param projectStaffDevice 人员设备权限
//     * @return R
//     */
//    @ApiOperation(value = "修改人员设备权限", notes = "修改人员设备权限")
//    @SysLog("修改人员设备权限")
//    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectstaffdevice_edit')")
//    public R updateById(@RequestBody ProjectPersonDevice projectStaffDevice) {
//        return R.ok(projectStaffDeviceProxyService.updateById(projectStaffDevice));
//    }

//    /**
//     * 通过id删除人员设备权限
//     *
//     * @param seq id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除人员设备权限", notes = "通过id删除人员设备权限")
//    @SysLog("通过id删除人员设备权限")
//    @DeleteMapping("/{seq}")
//    @PreAuthorize("@pms.hasPermission('estate_projectstaffdevice_del')")
//    public R removeById(@PathVariable Integer seq) {
//        return R.ok(projectStaffDeviceProxyService.removeById(seq));
//    }

}
