package com.aurine.cloudx.estate.job;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectVisitorService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * 冠林云平台每日访客迁签检查器(PassRightJob)
 *
 * @author 王伟
 * @since 2020-09-16
 */
@Slf4j
@Component
public class VisitorJob {
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    ProjectVisitorService projectVisitorService;

    @XxlJob("visitorJobHandler")
    public ReturnT<String> visitorJobHandler(String data) {
        List<ProjectInfo> projectList = projectInfoService.listProject();

        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            try {
                ProjectContextHolder.setProjectId(projectInfo.getProjectId());
                TenantContextHolder.setTenantId(1);
                //调用访客相关接口
                projectVisitorService.signOffAll();
            } catch (Exception e) {
                log.error("过期迁离任务发生异常 {} ，已跳过", e.getMessage());
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    @XxlJob("visitorSendCertJobHandler")
    public ReturnT<String> visitorSendCertJobHandler(String data) {
        List<ProjectInfo> projectList = projectInfoService.listProject();

        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            try {
                ProjectContextHolder.setProjectId(projectInfo.getProjectId());
                TenantContextHolder.setTenantId(1);
                //调用访客相关接口
                projectVisitorService.sendCertBatch();
            } catch (Exception e) {
                log.error("自动下发介质发生异常 {} ，已跳过", e.getMessage());
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    @XxlJob("initVisitorDelayTask")
    public ReturnT<String> initVisitorDelayTask(String data) {
        projectVisitorService.init();
        return SUCCESS;
    }
}