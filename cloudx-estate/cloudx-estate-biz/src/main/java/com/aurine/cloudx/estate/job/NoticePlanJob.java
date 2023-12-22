package com.aurine.cloudx.estate.job;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonNoticePlanService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * 冠林云平台增值服务执行器(NoticePlanJob)
 *
 * @author guhl@aurine
 * @since 2020-12-16 16:43:48
 */
@Slf4j
@Component
@AllArgsConstructor
public class NoticePlanJob {

    @Resource
    private ProjectPersonNoticePlanService projectPersonNoticePlanService;
    @Resource
    ProjectInfoService projectInfoService;

    @XxlJob("executeNoticePlan")
    public ReturnT<String> projectServiceHandler(String data) {
        List<ProjectInfo> projectList = projectInfoService.listProject();
        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            ProjectContextHolder.setProjectId(projectInfo.getProjectId());
            TenantContextHolder.setTenantId(1);
            projectPersonNoticePlanService.executeNoticePlan();
        }
        return SUCCESS;
    }
}
