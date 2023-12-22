package com.aurine.cloudx.estate;

import com.aurine.cloudx.common.data.adater.MappingConverterAdapter;
import com.aurine.cloudx.common.data.mybatis.MybatisProjectConfiguration;
import com.aurine.cloudx.common.data.project.ProjectConfiguration;
import com.aurine.cloudx.common.log.LogAutoConfiguration;
import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * (WeChatEstateApplication)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/8/31 16:15
 */
@EnablePigxSwagger2
@SpringCloudApplication
@EnablePigxFeignClients(basePackages = {"com.pig4cloud.pigx", "com.aurine.cloudx"})
@EnablePigxResourceServer
@EnableAsync
@Import({ProjectConfiguration.class,
        MybatisProjectConfiguration.class,
        MappingConverterAdapter.class,
        LogAutoConfiguration.class})
public class WeChatEstateApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeChatEstateApplication.class, args);
    }
}
