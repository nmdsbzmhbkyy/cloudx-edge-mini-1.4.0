package com.aurine.cloudx.estate.job;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.entity.SysProductService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectVisitorService;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.service.SysProductServiceService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * <p>
 *  设备相关的定时任务
 * </p>
 * @author : 王良俊
 * @date : 2021-05-24 09:22:35
 */
@Slf4j
@Component
public class DeviceJob {
    @Resource
    SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    ProjectInfoService projectInfoService;

    @XxlJob("syncDeviceProduct")
    public ReturnT<String> syncDeviceProduct(String data) {
        List<ProjectInfo> projectList = projectInfoService.listProject();
        projectList.forEach(projectInfo -> {
            try {
                DeviceFactoryProducer.getFactory(DeviceTypeEnum.GATE_DEVICE.getCode(), projectInfo.getProjectId(), 1)
                        .getDeviceService().syncProduces(projectInfo.getProjectId(), 1);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        return SUCCESS;
    }
}