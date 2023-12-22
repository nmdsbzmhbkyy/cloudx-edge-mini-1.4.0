package com.aurine.cloudx.estate.job;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectShiftPlanService;
import com.aurine.cloudx.estate.util.HolidayUtil;
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
 * 冠林云平台获取节假日执行器(HolidayJob)
 *
 * @author guhl@aurine
 * @since 2021-3-11 16:43:48
 */
@Slf4j
@Component
@AllArgsConstructor
public class HolidayJob {
    @Resource
    private ProjectShiftPlanService projectShiftPlanService;
    @Resource
    private ProjectInfoService projectInfoService;

    @XxlJob("executeHoliday")
    public ReturnT<String> holidayHandler(String data) {
        HolidayUtil.setHoliday();
        //由于更新了当前年的节假日安排，所以需要对当前年的排班做调整
        List<ProjectInfo> projectList = projectInfoService.listProject();
        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            ProjectContextHolder.setProjectId(projectInfo.getProjectId());
            TenantContextHolder.setTenantId(1);
            projectShiftPlanService.refreshHoliday();
        }
        return SUCCESS;
    }
}
