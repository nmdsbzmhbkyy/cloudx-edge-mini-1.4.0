package com.aurine.cloudx.estate.job;

import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectHouseServiceService;
import com.aurine.cloudx.estate.service.ProjectServiceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * 冠林云平台增值服务执行器(FeeJob)
 *
 * @author guhl@aurine
 * @since 2020-11-20 16:43:48
 */
@Slf4j
@Component
@AllArgsConstructor
public class ServiceJob {
    @Resource
    private ProjectServiceService projectServiceService;
    @Resource
    private ProjectHouseServiceService projectHouseServiceService;

    @Resource
    ProjectInfoService projectInfoService;

    @XxlJob("expireServiceHandler")
    public ReturnT<String> projectServiceHandler(String data) {
/*        List<ProjectInfo> projectList = projectInfoService.listProject();
        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            ProjectContextHolder.setProjectId(projectInfo.getProjectId());
            TenantContextHolder.setTenantId(1);
            webProjectServiceService.removeExpireProjectService();
            webProjectHouseServiceService.removeExpireHouseService();
        }*/
        return SUCCESS;
    }

}
