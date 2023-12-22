package com.aurine.cloudx.estate.job;

import com.aurine.cloudx.estate.service.ProjectVisitorHisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * (TimeOutJob)审核超时调度器
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/2 9:53
 */
@Slf4j
@Component
public class TimeOutJob {
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;



    @XxlJob("timeOutHandler")
    public ReturnT<String> projectServiceHandler(String data) {

        projectVisitorHisService.getTimeOutVisitor();
        return ReturnT.SUCCESS;
    }
}
