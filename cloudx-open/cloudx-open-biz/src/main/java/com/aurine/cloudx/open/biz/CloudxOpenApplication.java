package com.aurine.cloudx.open.biz;

import com.aurine.cloudx.common.data.adater.MappingConverterAdapter;
import com.aurine.cloudx.common.data.mybatis.MybatisProjectConfiguration;
import com.aurine.cloudx.common.data.project.ProjectConfiguration;
import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.log.LogAutoConfiguration;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author : Qiu
 * @date : 2021 12 07 9:18
 */

@EnablePigxSwagger2
@ComponentScan(basePackages = {"com.aurine.cloudx.open"})
@MapperScan(basePackages = {"com.aurine.cloudx.open.*.mapper", "com.aurine.cloudx.open.*.mongodb"})
@SpringCloudApplication
@EnablePigxFeignClients(basePackages = {"com.pig4cloud.pigx", "com.aurine.cloudx"})
@EnablePigxResourceServer
@EnableAsync
@Import({ProjectConfiguration.class,
        MybatisProjectConfiguration.class,
        MappingConverterAdapter.class,
        LogAutoConfiguration.class})
//@EnableCaching  //允许二级缓存
public class CloudxOpenApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudxOpenApplication.class, args);
    }
}
