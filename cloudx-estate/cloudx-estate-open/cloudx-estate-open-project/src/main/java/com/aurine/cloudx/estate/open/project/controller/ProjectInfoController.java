package com.aurine.cloudx.estate.open.project.controller;

import com.aurine.cloudx.estate.feign.RemoteFrameInfoService;
import com.aurine.cloudx.estate.open.core.UnCheckProject;
import com.aurine.cloudx.estate.open.project.bean.ProjectInfoVo;
import com.aurine.cloudx.estate.open.project.fegin.RemoteProjectUserService;
import com.pig4cloud.pigx.admin.api.vo.UserDeptVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 住户管理 (ProjectInfoController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/3 10:13
 */
@RestController
@RequestMapping("/info")
@Api(value = "info", tags = "住户管理")
public class ProjectInfoController {
    @Resource
    private RemoteProjectUserService remoteProjectUserService;

    @Resource
    private RemoteFrameInfoService remoteFrameInfoService;

    /**
     * 获取用户项目列表
     *
     * @return
     */
    @ApiOperation("获取用户项目列表")
    @SysLog("获取用户项目列表")
    @GetMapping("/list")
    @UnCheckProject
    public R<List<ProjectInfoVo>> getUserDeptRole() {
        R r = remoteProjectUserService.getUserDeptRole();

        if (r.getCode() == 0) {
            List<Map> list = (List) r.getData();
            List data = new ArrayList<UserDeptVo>();
            Map keys = new HashMap();

            for (Map map : list) {
                if ("3".equals(map.get("deptTypeId"))) {
                    int deptId = (int) map.get("deptId");

                    if (!keys.containsKey(deptId)) {
                        ProjectInfoVo info = new ProjectInfoVo();
                        info.setProjectId(deptId);
                        info.setProjectName((String) map.get("name"));

                        data.add(info);
                        keys.put(deptId, info);
                    }
                }
            }

            return R.ok(data);
        }

        return r;
    }

    /**
     * 获取子系统树，带根节点
     *
     * @return
     */
    @ApiOperation("获取框架树-LV4-7,带有社区根节点")
    @GetMapping("/frame-tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R findTreeWithRoot() {
        return remoteFrameInfoService.findTreeWithRoot();
    }
}
