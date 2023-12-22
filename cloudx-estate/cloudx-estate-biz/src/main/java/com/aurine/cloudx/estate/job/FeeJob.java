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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * 冠林云平台每日账单执行器(FeeJob)
 *
 * @author xull@aurine
 * @since 2020-07-20 16:43:48
 */
@Slf4j
@Component
public class FeeJob {
    @Resource
    ProjectBillingInfoService projectBillingInfoService;

    @Autowired
    ProjectInspectTaskService projectInspectTaskService;

    @Resource
    ProjectInfoService projectInfoService;

    @Resource
    ProjectBillDayConfService projectBillDayConfService;


    /**
     * 按照配置自动催缴
     * @param data
     * @return
     */
    @XxlJob("feeJobCall")
    public ReturnT<String> feeJobCall(String data) {
        List<ProjectInfo> projectList = projectInfoService.listProject();
        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            try {
                ProjectContextHolder.setProjectId(projectInfo.getProjectId());
                TenantContextHolder.setTenantId(1);
                List<ProjectBillDayConf> list = projectBillDayConfService.list();
                if(CollectionUtil.isNotEmpty(list)){
                  projectBillingInfoService.automaticCall(list.get(0));
                }
            } catch (Exception e) {
                log.error("催缴出现异常 {} ，已跳过",e.getMessage());
                e.printStackTrace();
            }
        }

        return SUCCESS;
    }
    @XxlJob("feeJobHandler")
    public ReturnT<String> feeJobHandler(String data) {
        log.info("开始自动生成账单{}",data);
        List<ProjectInfo> projectList = projectInfoService.listProject();
        for (ProjectInfo projectInfo : projectList) {
            //为线程注入项目id参数
            try {
                ProjectContextHolder.setProjectId(projectInfo.getProjectId());
                TenantContextHolder.setTenantId(1);
                List<ProjectBillDayConf> list = projectBillDayConfService.list();
                if(CollectionUtil.isNotEmpty(list)){
                    String billDay=list.get(0).getBillDay();
                    String status=list.get(0).getStatus();
                     String dayOfMonth=String.valueOf(LocalDate.now().getDayOfMonth());
                    if (billDay.equals(dayOfMonth)&&status.equals("1")){
                        projectBillingInfoService.resentBillingInfoBatch();
                    }
                }
            } catch (Exception e) {
                log.error("生成账单出现异常 {} ，已跳过",e.getMessage());
                e.printStackTrace();
            }
        }

        return SUCCESS;
    }
}