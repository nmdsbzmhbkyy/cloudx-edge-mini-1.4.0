package com.aurine.cloudx.edge.sync.biz;

import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.ChainManager;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: wrm
 * @Date: 2021/12/09 9:39
 * @Package: PACKAGE_NAME
 * @Version: 1.0
 * @Remarks:
 **/
@EnableAsync
@SpringCloudApplication
@EnablePigxResourceServer
@EnablePigxFeignClients(basePackages = {"com.pig4cloud.pigx", "com.aurine.cloudx", "com.aurine.center","com.aurine.parking"})
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan(basePackages = {"com.aurine.cloudx.edge.sync.biz","com.aurine.cloudx.edge.sync.common"})
public class CloudxEdgeSyncPlatformMasterApplication {
    /**
     * @author wrm
     * @date 2020年03月30日
     * <p>`
     * 冠林云-级联入云服务
     */
    public static void main(String[] args) {
        SpringApplication.run(CloudxEdgeSyncPlatformMasterApplication.class, args);
    }

}
