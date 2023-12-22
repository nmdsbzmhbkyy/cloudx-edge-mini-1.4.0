package com.aurine.cloudx.wjy.controller;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import com.aurine.cloudx.wjy.service.WjyH5Service;
import com.aurine.cloudx.wjy.service.impl.WjyH5ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ： huangjj
 * @date ： 2021/4/16
 * @description： 移动端工程人员h5页面
 */
@RestController
@RequestMapping("/engineer")
@Api(value = "engineer", tags = "工程人员接口")
public class AppEngineerH5Controller {
    @Resource
    WjyH5Service wjyH5ServiceImpl;
    @Resource
    ProjectService projectServiceImpl;
    /**
     * 功能描述: 移动端工程人员H5页面URL获取
     *
     * @author huangjj
     * @date 2021/4/16
     * @param projectId 项目id
     * @param moduleType 业务类型
     * @param phone 手机号码
     * @return 返回URL
     */
    @GetMapping("/module")
    @Inner(value = false)
    @ApiOperation(value = "移动端工程人员H5页面URL获取", notes = "移动端工程人员H5页面URL获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "moduleType", value = "模块类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "query"),
    })
    public R getModule(
            @RequestParam(value = "projectId") Integer projectId,
            @RequestParam(value = "moduleType") String moduleType,
            @RequestParam(value = "phone") String phone){
        Project project = projectServiceImpl.getByProjectId(projectId);
        if(project == null){
            return R.failed("未找到项目信息");
        }
        return R.ok(wjyH5ServiceImpl.getgjUrl(project,moduleType,phone));
    }
}