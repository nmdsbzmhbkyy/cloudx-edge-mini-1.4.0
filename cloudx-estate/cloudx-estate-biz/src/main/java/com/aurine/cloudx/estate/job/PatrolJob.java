package com.aurine.cloudx.estate.job;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectPatrolInfoService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * 冠林云平台每日巡更记录(PatrolJob)
 *
 * @author 王伟
 * @since 2020-09-16
 */
@Slf4j
@Component
public class PatrolJob {
    @Resource
    ProjectInfoService projectInfoService;

    @Resource
    private ProjectPatrolInfoService projectPatrolInfoService;

    @XxlJob("patrolJobHandler")
    public ReturnT<String> patrolJobHandler(String data) {
        List<ProjectInfo> projectList = projectInfoService.listProject();

        for (ProjectInfo projectInfo : projectList) {

            try{
                //为线程注入项目id参数
                ProjectContextHolder.setProjectId(projectInfo.getProjectId());
                TenantContextHolder.setTenantId(1);

                projectPatrolInfoService.saveNextDayPatrolInfo();
            }catch (Exception e){
                log.error("巡更定时任务任务发生异常 {} ，已跳过",e.getMessage());
                e.printStackTrace();
            }

        }

        return SUCCESS;
    }
}