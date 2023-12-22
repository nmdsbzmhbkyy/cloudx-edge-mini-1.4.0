

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectDeviceCollectService;
import com.aurine.cloudx.estate.vo.ProjectDeviceCollectFormVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceCollectListVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 项目设备采集参数
 *
 * @author xull@aurine.cn
 * @date 2020-06-12 11:43:43
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDeviceCollect")
@Api(value = "projectDeviceCollect", tags = "项目设备采集参数管理")
public class ProjectDeviceCollectController {

    private final ProjectDeviceCollectService projectDeviceCollectService;


    /**
     * 查询项目设备采集参数
     *
     * @param type
     *         type
     *
     * @return R
     */
    @ApiOperation(value = "查询项目设备采集参数", notes = "查询项目设备采集参数")
    @GetMapping("/{projectId}/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "type", value = "设备类型", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceCollectListVo>> get(@PathVariable("projectId") Integer projectId, @PathVariable("type") String type) {
        return R.ok(projectDeviceCollectService.getDeviceCollectListVo(projectId, type));
    }


    /**
     * 修改项目设备采集参数
     *
     * @param projectDeviceCollectFormVo
     *         项目设备采集参数
     *
     * @return R
     */
    @ApiOperation(value = "修改项目设备采集参数", notes = "修改项目设备采集参数")
    @SysLog("修改项目设备采集参数")
    @PutMapping
    // @PreAuthorize("@pms.hasPermission('estate_devicecollect_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R update(@RequestBody ProjectDeviceCollectFormVo projectDeviceCollectFormVo) {
        return R.ok(projectDeviceCollectService.updateDeviceCollectList(projectDeviceCollectFormVo));
    }

    /**
     * 获取公安状态
     *
     * @return R
     */
    @ApiOperation(value = "获取公安状态", notes = "获取公安状态")
    @GetMapping("/getPoliceEnable/{projectId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<String> getPoliceEnable(@PathVariable("projectId") Integer projectId){
        return R.ok(projectDeviceCollectService.getPoliceEnable(projectId));
    }
}
