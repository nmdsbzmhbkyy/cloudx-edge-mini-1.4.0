package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.service.SysProjectDeptService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 项目内部门信息
 *
 * @author lingang
 * @date 2020-05-07 18:44:46
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Api(value = "dept", tags = "项目内部门信息管理")
public class SysProjectDeptController {

    @Resource
    private final SysProjectDeptService sysProjectDeptService;
    @GetMapping("/list")
    @ApiOperation(value = "获取所有部门", notes = "获取所有部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R list() {
        return R.ok(sysProjectDeptService.list(Wrappers.lambdaQuery(SysProjectDept.class)
                .eq(SysProjectDept::getProjectId, ProjectContextHolder.getProjectId())));
    }
}
