package com.aurine.cloudx.wjy.controller;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import com.aurine.cloudx.wjy.service.WjyH5Service;
import com.aurine.cloudx.wjy.service.impl.WjyH5ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ： huangjj
 * @date ： 2021/4/16
 * @description： WEB后端h5页面
 */
@RestController
@RequestMapping("/web")
public class WebH5Controller {
    @Resource
    WjyH5Service wjyH5Service;
    @Resource
    ProjectService projectService;
    /**
     * 功能描述: WEB后端H5页面URL获取
     *
     * @author huangjj
     * @date 2021/4/16
     * @param projectId 项目id
     * @param moduleType 业务类型
     * @return 返回URL
     */
    @GetMapping("/module")
    @Inner(value = false)
    public R getModule(
            @RequestParam(value = "projectId") Integer projectId,
            @RequestParam(value = "moduleType") String moduleType){
        Project project = projectService.getByProjectId(projectId);
        if(project == null){
            return R.failed("未找到项目信息");
        }
        return R.ok(wjyH5Service.getWyUrl(project,moduleType));
    }
}