package com.aurine.cloudx.wjy.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.runnable.DataToWjy;
import com.aurine.cloudx.wjy.vo.ProjectVo;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import com.aurine.cloudx.wjy.utils.RedisUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/project")
@Api(value = "project", tags = "项目配置接口")
@Slf4j
public class ProjectController {

    @Resource
    ProjectService projectService;
    /**
     * 功能描述: 配置项目参数
     *
     * @author huangjj
     * @date 2021/4/15
     * @param projectVo 项目参数对象
     * @return 返回添加结果
    */
    @PostMapping("/config")
    @Inner(value = false)
    @ApiOperation(value = "项目配置", tags = "项目配置")
    public R projectConfig(@RequestBody ProjectVo projectVo){
        log.info("项目配置"+JSONObject.toJSONString(projectVo));
        if(projectVo != null && StringUtils.isNotBlank(projectVo.getPid())){
            Project project = projectService.getByPid(projectVo.getPid());
            if(project == null){
                project = new Project();
                project.setEnable(0);
            }
            project.setProjectId(projectVo.getProjectId());
            project.setOrgName(projectVo.getOrgName());
            project.setPid(projectVo.getPid());
            project.setPhone(projectVo.getPhone());
            project.setWyAppid(projectVo.getWyAppid());
            project.setWyAppkey(projectVo.getWyAppkey());
            project.setWyAppsecret(projectVo.getWyAppsecret());
            project.setWjAppkey(projectVo.getWyAppkey());
            project.setWjAppsecret(projectVo.getWjAppsecret());
            project.setWjAppid(projectVo.getWjAppid());
            project.setWjAppkey(projectVo.getWjAppkey());
            project.setWjAppsecret(projectVo.getWjAppsecret());
            project.setGjAppid(projectVo.getGjAppid());
            project.setGjAppkey(projectVo.getGjAppkey());
            project.setGjAppsecret(projectVo.getGjAppsecret());
            projectService.saveOrUpdate(project);

            //判断是否要插入缓存中
            /*if(!RedisUtil.sExistValue("wjy_project_set",project.getProjectId().toString())){//判断该项目是否需要同步我家云
                RedisUtil.sSet("wjy_project_set",project.getProjectId().toString());
                DataToWjy.setQueue(project.getProjectId());
                projectService.initProjectToWjy(projectVo.getProjectId());
            }*/

            return R.ok();
        }
        return R.failed();
    }
    @PostMapping("/enable")
    @Inner(value = false)
    @ApiOperation(value = "开关我家云同步", tags = "开关我家云同步")
    public R projectEnable(@RequestParam("projectId") Integer projectId, @RequestParam("enable") boolean enable){

        Project project = projectService.getByProjectId(projectId);
        if(project == null){
            return R.failed("未找到项目信息");
        }
        if(enable){
            project.setEnable(1);
            //判断是否要插入缓存中
            if(!RedisUtil.sExistValue("wjy_project_set",project.getProjectId().toString())){//判断该项目是否需要同步我家云
                RedisUtil.sSet("wjy_project_set",project.getProjectId().toString());
                DataToWjy.setQueue(project.getProjectId());
                projectService.initProjectToWjy(project.getProjectId());
            }
        }else{
            project.setEnable(0);
            //判断是否要移出缓存
            if(RedisUtil.sExistValue("wjy_project_set",project.getProjectId().toString())){//判断该项目是否需要同步我家云
                RedisUtil.setRemove("wjy_project_set",project.getProjectId().toString());
                RedisUtil.unlink("wjy_project"+projectId);
            }
        }
        projectService.saveOrUpdate(project);

        return R.ok();
    }

    @GetMapping("/info")
    @Inner(value = false)
    @ApiOperation(value = "查询项目数据", tags = "查询项目数据")
    public R projectInfo(@RequestParam("projectId") Integer projectId){
        Project project = projectService.getByProjectId(projectId);
        if(project == null){
            return R.failed("未找到项目信息");
        }
        ProjectVo projectVo = new ProjectVo();
        BeanUtil.copyProperties(project,projectVo);

        return R.ok(projectVo);
    }
}
