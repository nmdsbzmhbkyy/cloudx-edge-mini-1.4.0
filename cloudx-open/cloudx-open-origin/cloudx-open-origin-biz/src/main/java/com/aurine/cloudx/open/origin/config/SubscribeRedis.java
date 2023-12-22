package com.aurine.cloudx.open.origin.config;

import com.aurine.cloudx.common.data.project.ProjectConfig;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.service.SysDecidedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订阅表数据在项目启动的时候更新redis  sys_decided
 *
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-08-19 17:19
 */
@Component
@Slf4j
public class SubscribeRedis implements CommandLineRunner {

    @Resource
    private SysDecidedService sysDecidedService;
    @Resource
    ProjectConfig projectConfig;

    @Override
    public void run(String... strings) throws Exception {
        ProjectContextHolder.setProjectConfig(projectConfig);
    }

}
