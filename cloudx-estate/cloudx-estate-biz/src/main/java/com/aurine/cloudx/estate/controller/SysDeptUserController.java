package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.service.SysDeptUserService;
import com.aurine.cloudx.estate.vo.SysUserVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Title: SysDeptUserController
 * Description: 根据部门添加用户
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/7 18:03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysDeptUser")
@Api(value = "sysDeptUser", tags = "集团管理管理")
public class SysDeptUserController {
    private final SysDeptUserService sysDeptUserService;
    private final ProjectStaffService projectStaffService;

    /**
     * 添加当前部门下的用户
     *
     * @param sysUserVo 新增用户视图
     * @return R
     */
    @ApiOperation(value = "添加当前部门下的用户", notes = "添加当前部门下的用户")
    @SysLog("添加当前部门下的用户")
    @PostMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody SysUserVo sysUserVo) {
        sysDeptUserService.addUserAndRole(sysUserVo);
        return R.ok();
    }

    /**
     * 根据当前部门id获取用户信息列表
     * @param deptId
     * @return
     */
    @ApiOperation(value = "根据当前部门id获取用户信息列表", notes = "根据当前部门id获取用户信息列表")
    @GetMapping("/getUsers/{deptId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<List<SysUserVo>> getUsersByDeptId(@PathVariable("deptId") Integer deptId) {
        return R.ok(projectStaffService.getUserVosByDeptId(deptId));
    }

    @ApiOperation(value = "更新当前部门下的用户", notes = "更新当前部门下的用户")
    @SysLog("更新当前部门下的用户")
    @PutMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R update(@RequestBody SysUserVo sysUserVo) {
        sysDeptUserService.updateUserAndRole(sysUserVo);
        return  R.ok();
    }

    @ApiOperation(value = "删除当前部门下的用户", notes = "删除当前部门下的用户")
    @SysLog("删除当前部门下的用户")
    @DeleteMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @Deprecated
    public R remove(@RequestBody SysUserVo sysUserVo) {
        return sysDeptUserService.removeDeptUser(sysUserVo);
    }


    @ApiOperation(value = "删除当前部门下的用户", notes = "删除当前部门下的用户")
    @SysLog("删除当前部门下的用户")
    @DeleteMapping("/remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeUserAndStaff(@RequestParam("userId") Integer userId,@RequestParam("deptId") Integer deptId) {
        return sysDeptUserService.removeUserAndStaff(userId,deptId);
    }


}
