package com.aurine.cloudx.estate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;

import com.aurine.cloudx.common.data.adater.MappingConverterAdapter;
import com.aurine.cloudx.common.data.mybatis.MybatisProjectConfiguration;
import com.aurine.cloudx.common.data.project.ProjectConfiguration;
import com.aurine.cloudx.common.log.LogAutoConfiguration;
import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.job.annotation.EnablePigxXxlJob;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lingang
 * @date 2020年03月30日
 * <p>
 * 冠林云-社区管理模块服务
 */
@EnablePigxSwagger2
@SpringCloudApplication
@EnablePigxFeignClients(basePackages = {"com.pig4cloud.pigx", "com.aurine.cloudx","com.aurine.center.frames.ms","com.aurine.parking"})
@EnablePigxResourceServer
@EnablePigxXxlJob
@EnableAsync
@Import({ProjectConfiguration.class,
        MybatisProjectConfiguration.class,
        MappingConverterAdapter.class,
        LogAutoConfiguration.class})
@Slf4j
// @EnableCaching  // 允许二级缓存
public class CloudxEstateApplication {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        SpringApplication.run(CloudxEstateApplication.class, args);
        log.info("############################## ############################### ##############################");
        log.info("##############################     Service Started Success     ##############################");
        log.info("##############################     Startup time: {}s",(System.currentTimeMillis() - startTime)/1000.0);
        log.info("############################## ############################### ##############################");
    }
}
