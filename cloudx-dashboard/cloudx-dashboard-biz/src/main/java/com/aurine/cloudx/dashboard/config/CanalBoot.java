package com.aurine.cloudx.dashboard.config;

import com.aurine.cloudx.dashboard.canal.CanalClient;
import com.aurine.cloudx.dashboard.handler.chain.DashboardHandleChain;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @description:
 * @ClassName: CanalConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24 8:37
 * @Copyright:
 */
@Component
public class CanalBoot implements ApplicationRunner {

    @Resource
    private CanalClient canalClient;
    @Resource
    private DashboardHandleChain dashboardHandleChain;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        canalClient.connection(dashboardHandleChain);
    }
}
