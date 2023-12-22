package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectConfig;
import com.aurine.cloudx.estate.service.ProjectConfigService;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.wjy.constant.WyModuleType;
import com.aurine.cloudx.wjy.feign.RemoteProjectService;
import com.aurine.cloudx.wjy.feign.RemoteWebH5Service;
import com.aurine.cloudx.wjy.vo.ProjectVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 项目参数设置
 *
 * @author guhl.@aurine.cn
 * @date 2020-07-10 10:06:39
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectConfig")
@Api(value = "projectConfig", tags = "项目参数设置管理")
public class ProjectConfigController {

    private final ProjectConfigService projectConfigService;
    private final ProjectParCarRegisterService projectParCarRegisterService;
    @Resource
    private RemoteProjectService remoteProjectService;
    @Resource
    private RemoteWebH5Service remoteWebH5Service;


    /**
     * 修改项目参数设置
     *
     * @param projectConfig 项目参数设置
     * @return R
     */
    @ApiOperation(value = "修改项目参数设置", notes = "修改项目参数设置")
    @SysLog("修改项目参数设置")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_config_edit')")
    public R update(@RequestBody ProjectConfig projectConfig) {
        if (StrUtil.isNotEmpty(projectConfig.getMultiCarsPerPlace())
                && "0".equals(projectConfig.getMultiCarsPerPlace())
                && projectParCarRegisterService.isAlreadyAMultiCar()) {
           return R.failed("已有车位登记了多辆车，无法关闭一位多车");
        }
        return R.ok(projectConfigService.update(projectConfig, Wrappers.lambdaUpdate(ProjectConfig.class)
                .eq(ProjectConfig::getProjectId, ProjectContextHolder.getProjectId())));
    }

    /**
     * 查询项目配置参数
     *
     * @return
     */
    @ApiOperation(value = "查询项目配置参数", notes = "查询项目配置参数")
    @GetMapping("/getConfig")
    public R getConfig() {
        return R.ok(projectConfigService.getConfig());
    }


    /**
     * 查询一位多车  remote
     *
     * @return
     */
    @ApiOperation(value = "查询一位多车", notes = "查询一位多车")
    @GetMapping("/isEnableMultiCarsPerPlace/{projectId}")
    public R isEnableMultiCarsPerPlace(@PathVariable Integer projectId) {
        return R.ok(projectConfigService.isEnableMultiCarsPerPlace(projectId));
    }

    /**
     * 查询项目配置参数
     *
     * @return
     */
    @ApiOperation(value = "查询项目配置参数", notes = "查询项目配置参数")
    @GetMapping("/getByProjectId/{projectId}")
    public R getConfig(@PathVariable Integer projectId) {
        return R.ok(projectConfigService.getByProjectId(projectId));
    }

    /**
     * 修改财务配置
     *
     * @return
     */
    @ApiOperation(value = "修改财务配置", notes = "修改财务配置")
    @PutMapping("/updateFinanceConfig")
    public R UpdateFinanceConfig(@RequestBody ProjectVo projectVo) {
        return R.ok(remoteProjectService.projectConfig(projectVo));
    }

    /**
     * 获取财务配置
     *
     * @return
     */
    @ApiOperation(value = "获取财务配置", notes = "获取财务配置")
    @GetMapping("/getFinanceConfig/{projectId}")
    public R getFinanceConfig(@PathVariable Integer projectId) {
        return R.ok(remoteProjectService.projectInfo(projectId));
    }

    /**
     * 获取财务厂商URL
     *
     * @return
     */
    @ApiOperation(value = "获取财务厂商URL", notes = "获取财务厂商URL")
    @GetMapping("/getFinanceUrl/{projectId}")
    public R getFinanceUrl(@PathVariable Integer projectId) {
        return R.ok(remoteWebH5Service.getModule(projectId, WyModuleType.toMain.getType()));
    }

    /**
     * 启用或禁用财务服务
     *
     * @return
     */
    @ApiOperation(value = "启用金蝶财务服务", notes = "启用财务服务")
    @PutMapping("/enableFinance/{projectId}")
    public R enableFinance(@PathVariable Integer projectId) {
        return R.ok(remoteProjectService.projectEnable(projectId, true));
    }

    /**
     * 启用或禁用财务服务
     *
     * @return
     */
    @ApiOperation(value = "禁用金蝶财务服务", notes = "禁用财务服务")
    @PutMapping("/disableFinance/{projectId}")
    public R disableFinance(@PathVariable Integer projectId) {
        return R.ok(remoteProjectService.projectEnable(projectId, false));
    }
    @ApiOperation(value = "修改阿里对接设置设置", notes = "修改阿里对接设置设置")
    @PutMapping("/updateAliProjectCode")
    @PreAuthorize("@pms.hasPermission('estate_config_edit')")
    public R updateAliProjectCode(@RequestBody ProjectConfig projectConfig) {
        projectConfigService.updateAliProjectCode(projectConfig.getProjectId(),projectConfig.getAliProjectCode());
        return R.ok();
    }
    @ApiOperation(value = "修改视频接入设置", notes = "修改视频接入设置")
    @PutMapping("/updateTotalMonitorDevNo")
    @PreAuthorize("@pms.hasPermission('estate_config_edit')")
    public R updateTotalMonitorDevNo(@RequestBody ProjectConfig projectConfig) {
        projectConfigService.updateTotalMonitorDevNo(projectConfig.getProjectId(),projectConfig.getTotalMonitorDevNo());
        return R.ok();
    }

    @ApiOperation(value = "更新开放平台配置", notes = "更新开放平台配置")
    @PutMapping("/updateOpen")
    @PreAuthorize("@pms.hasPermission('estate_config_edit')")
    public R updateOpen(@RequestBody ProjectConfig projectConfig) {
        // 更新内容
        projectConfigService.updateOpen(projectConfig);

        return R.ok();
    }

    @ApiOperation(value = "修改梯控功能开关设置", notes = "修改梯控功能开关设置")
    @PutMapping("/updateLiftEnable")
    @PreAuthorize("@pms.hasPermission('estate_config_edit')")
    public R updateLiftEnable(@RequestBody ProjectConfig projectConfig) {
        projectConfigService.updateLiftEnable(projectConfig.getProjectId(),projectConfig.getLiftEnable());
        return R.ok();
    }
}
