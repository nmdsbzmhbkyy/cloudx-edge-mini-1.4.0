
package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectDeviceAttrService;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrListVo;
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
 * 设备拓展属性表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:18:23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDeviceAttr")
@Api(value = "ProjectDeviceAttr", tags = "设备拓展属性表管理")
public class ProjectDeviceAttrController {

    private final ProjectDeviceAttrService projectDeviceAttrService;


    /**
     * 查询项目设备拓展属性表
     *
     * @param deviceId deviceId
     * @return R
     */
    @ApiOperation(value = "通过类型查询项目设备拓展属性", notes = "通过类型查询项目设备拓展属性")
    @GetMapping("/{projectId}/{type}/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "type", value = "设备类型", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceAttrListVo>> get(@PathVariable("projectId") Integer projectId, @PathVariable("type") String type, @PathVariable(value = "deviceId",required = false) String deviceId) {
        return R.ok(projectDeviceAttrService.getDeviceAttrListVo(projectId, type,deviceId));
    }


    /**
     * 修改项目设备拓展属性表
     *
     * @param projectDeviceAttrFormVo 项目设备拓展属性表
     * @return R
     */
    @ApiOperation(value = "修改项目设备拓展属性表", notes = "修改项目设备拓展属性表")
    @SysLog("修改项目设备拓展属性表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_deviceattr_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R update(@RequestBody ProjectDeviceAttrFormVo projectDeviceAttrFormVo) {
        return R.ok(projectDeviceAttrService.updateDeviceAttrList(projectDeviceAttrFormVo));
    }

}
