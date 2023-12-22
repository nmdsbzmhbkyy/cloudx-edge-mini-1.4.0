package com.aurine.cloudx.estate.job;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonLiftRelService;
import com.aurine.cloudx.estate.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectPersonDeviceService;
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
 * 冠林云平台增通信凭证执行器()
 *
 * @author 王伟
 * @since 2021-02-03 11:09
 */
@Slf4j
@Component
@AllArgsConstructor
public class PassRightJob {

    @Resource
    private AbstractProjectPersonDeviceService abstractWebProjectPersonDeviceService;

    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private ProjectPersonLiftRelService projectPersonLiftRelService;

    /**
     * 通行凭证过期转为禁用
     *
     * @param data
     * @return
     */
    @XxlJob("passRightExpireServiceHandler")
    public ReturnT<String> passRightExpireServiceHandler(String data) {
        List<ProjectInfo> projectList = projectInfoService.listProject();

        for (ProjectInfo projectInfo : projectList) {
            try {
                //为线程注入项目id参数
                ProjectContextHolder.setProjectId(projectInfo.getProjectId());
                TenantContextHolder.setTenantId(1);

                //获取当前项目已过期的人员列表
                List<ProjectPersonPlanRel> personPlanRelList = projectPersonPlanRelService.listPersonPlanRelExpDate();
                if (CollUtil.isNotEmpty(personPlanRelList)) {
                    for (ProjectPersonPlanRel personPlanRel : personPlanRelList) {
                        //projectPersonLiftRelService.disablePassRight(personPlanRel.getPersonType(), personPlanRel.getPersonId());
                        abstractWebProjectPersonDeviceService.disablePassRight(personPlanRel.getPersonType(), personPlanRel.getPersonId());
                    }
                }
            } catch (Exception e) {
                log.error("通信时间过期禁用定时任务任务发生异常 {} ，已跳过", e.getMessage());
                e.printStackTrace();
            }

        }

        return SUCCESS;
    }

}
