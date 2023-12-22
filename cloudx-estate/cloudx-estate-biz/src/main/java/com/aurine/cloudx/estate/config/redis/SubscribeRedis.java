package com.aurine.cloudx.estate.config.redis;

import com.aurine.cloudx.common.data.project.ProjectConfig;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DecidedEnum;
import com.aurine.cloudx.estate.entity.SysDecided;
import com.aurine.cloudx.estate.service.SysDecidedService;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.wjy.entity.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
        RedisUtil.delPattenKey("subscribe:*:*");
        List<SysDecided> sysDecidedList = sysDecidedService.list();
        for (SysDecided sysDecided : sysDecidedList) {
            RedisUtil.lSet("subscribe:" + DecidedEnum.getEnum(sysDecided.getType()) + ":" + sysDecided.getProjectid(), sysDecided.getAddr());
        }

        log.info("sys_decided表缓存更新成功");
    }

}
