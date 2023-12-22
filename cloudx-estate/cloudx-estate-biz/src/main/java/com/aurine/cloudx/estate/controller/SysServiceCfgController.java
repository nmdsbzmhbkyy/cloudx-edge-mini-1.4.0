package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectService;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.entity.SysServiceDeviceClassify;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.intercom.factory.IntercomFactoryProducer;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 系统增值服务配置
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:16:57
 */
@RestController
@RequestMapping("/sysServiceCfg")
@Api(value = "sysServiceCfg", tags = "系统增值服务配置管理")
public class SysServiceCfgController {
    @Resource
    private SysServiceCfgService sysServiceCfgService;
    @Resource
    private ProjectServiceService projectServiceService;
    @Resource
    private SysServiceDeviceClassifyService sysServiceDeviceClassifyService;
    @Resource
    private ProjectHouseServiceService projectHouseServiceService;
    @Resource
    private SysDeviceClassifyService sysDeviceClassifyService;

    /**
     * 查询所有增值服务
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务", notes = "查询所有增值服务")
    @GetMapping("/page")
    public R page(Page page) {
        return R.ok(sysServiceCfgService.page(page));
    }


    /**
     * 查询所有增值服务与厂商列表
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务与厂商列表", notes = "查询所有增值服务与厂商列表")
    @GetMapping("/selectValueadd")
    public R selectValueadd() {
        return R.ok(sysServiceCfgService.selectValueadd());
    }

    /**
     * 查询所有增值服务无分页
     *
     * @return
     */
    @ApiOperation(value = "查询所有增值服务无分页", notes = "查询所有增值服务无分页")
    @GetMapping("/listService")
    public R listService() {
        return R.ok(sysServiceCfgService.list());
    }

    /**
     * 查询当前项目所有增值服务,用于房屋增值服务
     *
     * @return R
     */
    @ApiOperation(value = "查询当前项目所有增值服务", notes = "查询当前项目所有增值服务")
    @GetMapping("/houseServiceList")
    public R getSysServiceHouseList() {
        return R.ok(projectServiceService.getHouseServiceList());
    }

    /**
     * 根据项目id查询增值服务
     *
     * @return R
     */
    @ApiOperation(value = "根据项目id查询增值服务", notes = "根据项目id查询增值服务")
    @GetMapping("/getHouseServiceByProjectId/{projectId}")
    public R getByProjectId(@PathVariable("projectId") Integer projectId) {
        return R.ok(projectServiceService.getHouseServiceByProjectId(projectId));
    }

    @ApiOperation(value = "根据房屋id查询增值服务", notes = "根据房屋id查询增值服务")
    @GetMapping("/gethouseServiceByhouseId/{houseId}")
    public R getByHouseId(@PathVariable("houseId") String houseId) {
        return R.ok(projectHouseServiceService.getHouseServiceByHouseId(houseId));
    }

    /**
     * 查询业务设施类型配置
     * @param
     * @return R
     */
  /*  @ApiOperation(value = "查询业务设施类型配置", notes = "查询业务设施类型配置")
    @GetMapping("/pageDeviceClassifyService" )
    public R deviceClassifyList(){
        return R.ok(sysServiceDeviceClassifyService.list());
    }*/

    /**
     * 通过id查询系统增值服务配置
     *
     * @param serviceId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{serviceId}")
    public R getById(@PathVariable("serviceId") String serviceId) {
        List<SysServiceDeviceClassify> deviceClassify = sysServiceDeviceClassifyService.list(Wrappers.lambdaQuery(SysServiceDeviceClassify.class).eq(SysServiceDeviceClassify::getServiceId, serviceId).select(SysServiceDeviceClassify::getDeviceClassify));
        List<String> deviceClassifyIds = new ArrayList<>();
        deviceClassify.forEach(e -> {
            deviceClassifyIds.add(e.getDeviceClassify());
        });
        SysServiceCfg sysServiceCfg = sysServiceCfgService.getById(serviceId);
        SysServiceCfgClassifyVo sysServiceCfgClassifyVo = new SysServiceCfgClassifyVo();
        if (BeanUtil.isNotEmpty(sysServiceCfg)) {

        }
        BeanUtils.copyProperties(sysServiceCfg, sysServiceCfgClassifyVo);
        sysServiceCfgClassifyVo.setDeviceClassify(deviceClassifyIds);
        return R.ok(sysServiceCfgClassifyVo);
    }

    /**
     * 新增系统增值服务配置
     *
     * @param sysServiceCfg 系统增值服务配置
     * @return R
     */
    @ApiOperation(value = "新增系统增值服务配置", notes = "新增系统增值服务配置")
    @SysLog("新增系统增值服务配置")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_servicecfg_add')")
    public R save(@RequestBody SysServiceCfg sysServiceCfg) {
        return R.ok(sysServiceCfgService.save(sysServiceCfg));
    }

    /**
     * 增值服务项目配置
     *
     * @param serviceProjectSaveVo 增值服务项目配置
     * @return R
     */
    @ApiOperation(value = "增值服务项目配置", notes = "增值服务项目配置")
    @SysLog("增值服务项目配置")
    @PostMapping("saveByProject")
    public R saveByProject(@RequestBody ServiceProjectSaveVo serviceProjectSaveVo) {
        List<String> serviceIds = serviceProjectSaveVo.getServiceIds();
        //要添加的增值服务集合
        List<String> enableAddServiceIds = new ArrayList<>();
        List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(serviceProjectSaveVo.getProjectId());
        //如果项目中存在增值服务
        if (CollUtil.isNotEmpty(projectServiceInfoVos)) {
            serviceIds.forEach(serviceId -> {
                projectServiceInfoVos.forEach(projectService -> {
                    //判断哪些需要添加
                    if (!projectService.getServiceId().equals(serviceId)) {
                        enableAddServiceIds.add(serviceId);
                    }
                });
            });
        } else {
            enableAddServiceIds.addAll(serviceIds);
        }

        //关闭本地已经不存在的远端项目增值服务
        projectServiceService.removeRemoteProjectService(serviceProjectSaveVo.getProjectId(), serviceIds);
        //保存本地项目增值服务
        projectServiceService.saveByServiceIds(serviceProjectSaveVo);
        if (CollUtil.isNotEmpty(enableAddServiceIds)) {
            List<SysServiceCfg> sysServiceCfgs = sysServiceCfgService.listByIds(enableAddServiceIds);
            sysServiceCfgs.forEach(e -> {
                //开启前端添加的增值服务到远端
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addProject(serviceProjectSaveVo.getProjectId());
            });
        }
        return R.ok();
    }

    /**
     * 房屋信息增值服务
     *
     * @param serviceHouseSaveVo 房屋信息增值服务
     * @return R
     */
    @ApiOperation(value = "房屋信息增值服务", notes = "房屋信息增值服务")
    @SysLog("房屋信息增值服务")
    @PostMapping("saveByHouse")
    public R saveByHouse(@RequestBody ServiceHouseSaveVo serviceHouseSaveVo) {
        List<String> serviceIds = serviceHouseSaveVo.getServiceIds();
        //关闭本地已经不存在的远端房屋增值服务
        projectHouseServiceService.removeRemoteHouseService(serviceHouseSaveVo.getHouseId(), serviceIds);
        List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(serviceHouseSaveVo.getHouseId());
        //要添加的增值服务集合
        List<String> enableAddServiceIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(projectHouseServiceInfoVos)) {
            serviceIds.forEach(serviceId -> {
                projectHouseServiceInfoVos.forEach(houseService -> {
                    if (!houseService.getServiceId().equals(serviceId)) {
                        enableAddServiceIds.add(serviceId);
                    }
                });
            });
        } else {
            enableAddServiceIds.addAll(serviceIds);
        }
        //保存本地房屋增值服务
        projectHouseServiceService.saveByHouse(serviceHouseSaveVo);
        if (CollUtil.isNotEmpty(enableAddServiceIds)) {
            List<SysServiceCfg> sysServiceCfgs = sysServiceCfgService.listByIds(enableAddServiceIds);
            sysServiceCfgs.forEach(e -> {
                //开启前端添加的增值服务到远端
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addByHouse(serviceHouseSaveVo.getHouseId(), ProjectContextHolder.getProjectId());
            });
        }
        return R.ok();
    }

    /**
     * 房屋信息增值服务
     *
     * @param serviceHouseSaveVo 房屋信息增值服务
     * @return R
     */
    @ApiOperation(value = "房屋信息增值服务", notes = "房屋信息增值服务")
    @SysLog("房屋信息增值服务")
    @PostMapping("/inner/saveByHouse")
    public R innerSaveByHouse(@RequestBody ServiceHouseSaveVo serviceHouseSaveVo) {
        List<String> serviceIds = serviceHouseSaveVo.getServiceIds();
        //关闭本地已经不存在的远端房屋增值服务
        projectHouseServiceService.removeRemoteHouseService(serviceHouseSaveVo.getHouseId(), serviceIds);
        List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(serviceHouseSaveVo.getHouseId());
        //要添加的增值服务集合
        List<String> enableAddServiceIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(projectHouseServiceInfoVos)) {
            serviceIds.forEach(serviceId -> {
                projectHouseServiceInfoVos.forEach(houseService -> {
                    if (!houseService.getServiceId().equals(serviceId)) {
                        enableAddServiceIds.add(serviceId);
                    }
                });
            });
        } else {
            enableAddServiceIds.addAll(serviceIds);
        }
        //保存本地房屋增值服务
        projectHouseServiceService.saveByHouse(serviceHouseSaveVo);
        if (CollUtil.isNotEmpty(enableAddServiceIds)) {
            List<SysServiceCfg> sysServiceCfgs = sysServiceCfgService.listByIds(enableAddServiceIds);
            sysServiceCfgs.forEach(e -> {
                //开启前端添加的增值服务到远端
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addByHouse(serviceHouseSaveVo.getHouseId(), ProjectContextHolder.getProjectId());
            });
        }
        return R.ok();
    }


    /**
     * 批量新增房屋增值服务
     *
     * @param serviceHouseIdsSaveVo 批量新增房屋增值服务
     * @return R
     */
    @ApiOperation(value = "批量新增房屋增值服务", notes = "批量新增房屋增值服务")
    @SysLog("批量新增房屋增值服务")
    @PostMapping("saveByHouseIds")
    public R saveByHouseIds(@RequestBody ServiceHouseIdsSaveVo serviceHouseIdsSaveVo) {
        //保存本地房屋增值服务
        projectHouseServiceService.saveByHouseIds(serviceHouseIdsSaveVo);
        List<String> serviceIds = serviceHouseIdsSaveVo.getServiceIds();
        List<String> houseIds = serviceHouseIdsSaveVo.getHouseIds();
        //关闭本地已经不存在的远端房屋增值服务
        houseIds.forEach(houseId -> {
            projectHouseServiceService.removeRemoteHouseService(houseId, serviceIds);
            List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(houseId);
            //要添加的增值服务集合
            List<String> enableAddServiceIds = new ArrayList<>();
            if (CollUtil.isNotEmpty(projectHouseServiceInfoVos)) {
                serviceIds.forEach(serviceId -> {
                    projectHouseServiceInfoVos.forEach(houseService -> {
                        if (!houseService.getServiceId().equals(serviceId)) {
                            enableAddServiceIds.add(serviceId);
                        }
                    });
                });
            } else {
                enableAddServiceIds.addAll(serviceIds);
            }
            if (CollUtil.isNotEmpty(enableAddServiceIds)) {
                List<SysServiceCfg> sysServiceCfgs = sysServiceCfgService.listByIds(enableAddServiceIds);
                sysServiceCfgs.forEach(e -> {
                    //开启前端添加的增值服务到远端
                    IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addByHouse(houseId, ProjectContextHolder.getProjectId());
                });
            }
        });

        return R.ok();
    }

    /**
     * 修改系统增值服务配置
     *
     * @param sysServiceCfg 系统增值服务配置
     * @return R
     */
    @ApiOperation(value = "修改系统增值服务配置", notes = "修改系统增值服务配置")
    @SysLog("修改系统增值服务配置")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_servicecfg_edit')")
    public R updateById(@RequestBody SysServiceCfg sysServiceCfg) {
        return R.ok(sysServiceCfgService.updateById(sysServiceCfg));
    }

    /**
     * 通过id删除系统增值服务配置
     *
     * @param serviceId id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统增值服务配置", notes = "通过id删除系统增值服务配置")
    @SysLog("通过id删除系统增值服务配置")
    @DeleteMapping("/{serviceId}")
    @PreAuthorize("@pms.hasPermission('estate_servicecfg_del')")
    public R removeById(@PathVariable String serviceId) {
        List<ProjectService> projectServices = projectServiceService.list(Wrappers.lambdaQuery(ProjectService.class)
                .eq(ProjectService::getServiceId, serviceId));
        if (projectServices == null || projectServices.size() == 0) {
            return R.ok(sysServiceCfgService.removeById(serviceId));
        } else {
            throw new RuntimeException("该增值服务已开放给集团,无法删除");
        }
    }
    /**
     * 通过serviceType查询信息
     *
     * @return
     */
    @ApiOperation(value = "通过serviceType查询信息", notes = "通过serviceType查询信息")
    @GetMapping("/getServiceByType/{serviceType}")
    public R getServiceByType(@PathVariable("serviceType") String serviceType){
        return R.ok(this.sysServiceCfgService.list(new LambdaQueryWrapper<SysServiceCfg>().eq(SysServiceCfg::getServiceType,serviceType)));
    }

}
