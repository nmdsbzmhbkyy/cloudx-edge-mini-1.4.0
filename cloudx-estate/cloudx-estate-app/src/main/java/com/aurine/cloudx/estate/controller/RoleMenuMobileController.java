package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.estate.feign.RemoteRoleMenuMobileService;
import com.aurine.cloudx.estate.vo.SysMenuMobileTreeVo;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
@Api(value = "/menu", tags = "菜单权限")
public class RoleMenuMobileController {
    @Resource
    private RemoteRoleMenuMobileService remoteRoleMenuMobileService;
    @Resource
    private RemoteUserService remoteUmsUserService;

    @GetMapping("/staff/tree")
    @ApiOperation(value = "获取菜单结构树(物业)", notes = "获取菜单结构树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<SysMenuMobileTreeVo>> treeStaffRoleId(@RequestHeader("PROJECT-ID") Integer projectId) {
        R<List<Object>> userDeptRole = remoteUmsUserService.getUserDeptRole();
        if (ObjectUtil.isNotEmpty(userDeptRole.getData())){
            for (Object datum : userDeptRole.getData()) {
                JSONObject jsonObject = JSONUtil.parseObj(datum);
                if (jsonObject.get("deptId",Integer.class).equals(projectId)) {
                    return remoteRoleMenuMobileService.treeRoleId(jsonObject.get("roleId",Integer.class));
                }
            }
        }
        return R.ok();
    }

    @GetMapping("/tree/{roleId}")
    @ApiOperation(value = "获取菜单结构树（业主）", notes = "获取菜单结构树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "roleId", value = "角色Id (从当前业主信息中获取)", required = true, paramType = "path"),
    })
    public R<List<SysMenuMobileTreeVo>> treeRoleId(@PathVariable Integer roleId) {
        return remoteRoleMenuMobileService.treeTypeRoleId(roleId, 1);
    }
}
