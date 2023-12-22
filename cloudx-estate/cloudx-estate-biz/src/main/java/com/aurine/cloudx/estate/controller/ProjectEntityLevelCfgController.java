

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceParamInfoService;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.vo.ProjectEntityLevelCfgVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 配置项目区域层级
 *
 * @author pigx code generator
 * @date 2020-05-06 13:49:41
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/baseBuildingFrameCfg")
@Api(value = "buildingFrameCfgController", tags = "配置项目区域层级管理")
public class ProjectEntityLevelCfgController {

    private final ProjectEntityLevelCfgService projectEntityLevelCfgService;
    private final ProjectDeviceInfoService projectDeviceInfoService;
    private final ProjectDeviceParamInfoService projectDeviceParamInfoService;

    /**
     * 分页查询
     *
     * @param page                   分页对象
     * @param buildingEntityLevelCfg 配置项目区域层级
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getBuildingEntityLevelCfgPage(Page page, ProjectEntityLevelCfg buildingEntityLevelCfg) {
        buildingEntityLevelCfg.setProjectId(ProjectContextHolder.getProjectId());
        /**
         * 修正多租户情况下，无法正常获取默认组团数据的问题
         * @author: 王伟 2020-07-07
         */
        buildingEntityLevelCfg.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectEntityLevelCfgService.page(page, buildingEntityLevelCfg));
    }


    /**
     * 通过id查询配置项目区域层级
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}")
    public R getById(@PathVariable("seq") Integer seq) {
        return R.ok(projectEntityLevelCfgService.getById(seq));
    }

    /**
     * 新增配置项目区域层级
     *
     * @param projectEntityLevelCfg 配置项目区域层级
     * @return R
     */
    @ApiOperation(value = "新增配置项目区域层级", notes = "新增配置项目区域层级")
    @SysLog("新增配置项目区域层级")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_buildinglevelcfg_add')")
    public R save(@RequestBody ProjectEntityLevelCfg projectEntityLevelCfg) {
        return R.ok(projectEntityLevelCfgService.save(projectEntityLevelCfg));
    }

    /**
     * 修改配置项目区域层级
     *
     * @param projectEntityLevelCfg 配置项目区域层级
     * @return R
     */
    @ApiOperation(value = "修改配置项目区域层级", notes = "修改配置项目区域层级")
    @SysLog("修改配置项目区域层级")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_buildinglevelcfg_edit')")
    public R updateById(@RequestBody ProjectEntityLevelCfg projectEntityLevelCfg) {
        return R.ok(projectEntityLevelCfgService.updateById(projectEntityLevelCfg));
    }

    /**
     * 通过id删除配置项目区域层级
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除配置项目区域层级", notes = "通过id删除配置项目区域层级")
    @SysLog("通过id删除配置项目区域层级")
    @DeleteMapping("/{seq}")
    @PreAuthorize("@pms.hasPermission('estate_buildinglevelcfg_del')")
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectEntityLevelCfgService.removeById(seq));
    }

    /**
     * 通过id 开关配置
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id配置开关", notes = "通过id配置开关")
    @GetMapping("/{seq}/switch/{val}")
    @PreAuthorize("@pms.hasPermission('estate_buildinglevelcfg_switch')")
    public R swtichById(@PathVariable("seq") Integer seq, @PathVariable("val") Integer val) {
        System.err.println(seq + "," + val);
        boolean result = projectEntityLevelCfgService.swithFrame(seq, val);
        return result ? R.ok() : R.failed("当前层级已存在数据，无法禁用");
    }

    /**
     * 切换最高层级
     *
     * @param level level
     * @return R
     * @author: 王伟
     */
    @ApiOperation(value = "切换最高层级", notes = "切换最高层级")
    @GetMapping("/changeMaxLevel/{level}")
    public R changeMaxLevel(@PathVariable("level") Integer level, String type) {
        if (StringUtils.isNotEmpty(type) && "clear".equals(type)) {
            //关闭组团

            return this.projectEntityLevelCfgService.disableAllLevel();
        } else {
            //切换层级
            return this.projectEntityLevelCfgService.activeLevel(level);
        }
    }

    @Inner(value = false)
    @ApiOperation(value = "判断组团是否启用", notes = "判断组团是否启用")
    @GetMapping("/checkIsEnable")
    public R<Boolean> checkIsEnable() {
        return R.ok(projectEntityLevelCfgService.checkIsEnabled());
    }


    /**
     * 修改配置项目区域层级
     *
     * @param projectEntityLevelCfgVo 配置项目区域层级
     * @return R
     */
    @ApiOperation(value = "修改框架号", notes = "修改框架号")
    @SysLog("修改框架号")
    @PutMapping("/updateFrame")
    public R updateFrame(@RequestBody ProjectEntityLevelCfgVo projectEntityLevelCfgVo) {
        R result = projectEntityLevelCfgService.updateFrame(projectEntityLevelCfgVo);
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceType, DeviceTypeConstants.DEVICE_DRIVER));
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            deviceInfoList.forEach(deviceInfo -> {
                try {
                    boolean b = projectDeviceParamInfoService.refreshParam(deviceInfo);
                    if (!b) {
                        log.info("修改项目框架时自动设置驱动设备设备编号参数失败： 设备ID {}", deviceInfo.getDeviceId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("修改项目框架时自动设置驱动设备设备编号参数失败： 设备ID {}", deviceInfo.getDeviceId());
                }
            });
        }
        return result;
    }


    @ApiOperation(value = "根据层级编号获取到对应的位数", notes = "根据层级编号获取到对应的位数")
    @GetMapping("/getCodeRuleByLevel/{level}")
    public R getCodeRuleByLevel(@PathVariable String level) {
        return R.ok(projectEntityLevelCfgService.getCodeRuleByLevel(level));
    }

    @ApiOperation(value = "获取当前项目框架", notes = "根据层级编号获取到对应的位数")
    @GetMapping("/getProjectSubSection")
    public R getProjectSubSection() {
        return R.ok(projectEntityLevelCfgService.getProjectSubSection(ProjectContextHolder.getProjectId()));
    }

}
