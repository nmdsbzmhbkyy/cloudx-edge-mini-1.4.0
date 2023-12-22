package com.aurine.cloudx.estate.config;

import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestMasterService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.thirdparty.module.edge.cloud.remote.IntoCloudRemoteService;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.DriverManagerReq;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.SubCfgInfo;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ConfigMainProjectUUID implements ApplicationRunner {
    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    IntoCloudRemoteService intoCloudRemoteService;
    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void run(ApplicationArguments args) {
        init();
    }

    private void init() {
        TaskUtil.hashedWheelTimer.newTimeout(timeout -> {
            taskExecutor.execute(() -> {
                try {
                    // 配置驱动主社区ID（驱动topic前缀）
                    Integer originProjectId = edgeCascadeRequestMasterService.getOriginProjectId();
                    String masterUUID = projectInfoService.getProjectUUID(originProjectId);
                    boolean b = intoCloudRemoteService.confDriverCommunityId(new DriverManagerReq(masterUUID));
                    boolean c = intoCloudRemoteService.pubSubCfg(new SubCfgInfo("0", masterUUID));
                    if (!b) {
                        this.init();
                    }
                } catch (Exception e) {
                    log.error("驱动主社区ID配置失败,2分钟后重试");
                    this.init();
                    e.printStackTrace();
                }
            });
        }, 2, TimeUnit.MINUTES);
    }
}
