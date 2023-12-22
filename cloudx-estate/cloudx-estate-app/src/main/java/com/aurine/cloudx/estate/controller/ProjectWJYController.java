

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.service.SysServiceCfgService;
import com.aurine.cloudx.wjy.constant.GjModuleType;
import com.aurine.cloudx.wjy.constant.WjModuleType;
import com.aurine.cloudx.wjy.constant.WyModuleType;
import com.aurine.cloudx.wjy.feign.RemoteAppEngineerH5Service;
import com.aurine.cloudx.wjy.feign.RemoteAppUserH5Service;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/wjy")
@Api(value = "wjy", tags = "我家云管理")
public class ProjectWJYController {

    @Resource
    private RemoteAppUserH5Service remoteAppUserH5Service;
    @Resource
    private RemoteAppEngineerH5Service remoteAppEngineerH5Service;
    @Resource
    private SysServiceCfgService sysServiceCfgService;

    /**
     * 获取我家云地址
     *
     * @return
     */
    @ApiOperation(value = "获取我家云地址", notes = "获取我家云地址")
    @GetMapping("/url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<String> getUrl() {
        String phone = SecurityUtils.getUser().getPhone();
        Integer projectId = ProjectContextHolder.getProjectId();
        if (sysServiceCfgService.getWJYStatus(projectId)) {
            return remoteAppUserH5Service.getModule(projectId, WjModuleType.Bill.getType(), phone);
        }
        return R.failed("未开启我家云服务");
    }

    /**
     * 获取我家云地址
     *
     * @return
     */
    @ApiOperation(value = "获取我家云地址(物业)", notes = "获取我家云地址（物业）")
    @GetMapping("/staff/url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<String> getStaffUrl() {
        String phone = SecurityUtils.getUser().getPhone();
        Integer projectId = ProjectContextHolder.getProjectId();
        if (sysServiceCfgService.getWJYStatus(projectId)) {
            return remoteAppEngineerH5Service.getModule(projectId, GjModuleType.Query.getType(), phone);
        }
        return R.failed("未开启我家云服务");
    }
}
