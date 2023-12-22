package com.aurine.cloudx.estate.job;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectBillDayConf;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.ProjectBillDayConfService;
import com.aurine.cloudx.estate.service.ProjectBillingInfoService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * <p>
 * 巡检任务
 * </p>
 * @author : 王良俊
 * @date : 2021-08-02 16:08:16
 */
@Slf4j
@Component
public class InspectTask {
    @Autowired
    ProjectInspectTaskService projectInspectTaskService;

    @Resource
    ProjectInfoService projectInfoService;


    @XxlJob("taskJobHandle")
    public ReturnT<String> taskJobHandle(String data) {
        log.info("准备生成巡检任务");
        List<ProjectInfo> projectList = projectInfoService.listProject();

        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            ProjectContextHolder.setProjectId(projectInfo.getProjectId());
            TenantContextHolder.setTenantId(1);
            try {
                projectInspectTaskService.initTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }
}