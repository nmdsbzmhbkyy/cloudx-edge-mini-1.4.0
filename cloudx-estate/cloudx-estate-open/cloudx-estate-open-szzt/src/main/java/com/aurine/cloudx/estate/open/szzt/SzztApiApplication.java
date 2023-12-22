package com.aurine.cloudx.estate.open.house;

import com.aurine.cloudx.common.data.adater.MappingConverterAdapter;
import com.aurine.cloudx.common.data.project.ProjectConfiguration;
import com.aurine.cloudx.estate.open.core.OpenGetAuthorities;
import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.log.LogAutoConfiguration;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;

/**
 * @author lingang
 * @date 2020年03月30日
 * <p>
 * 冠林云-社区管理模块服务
 */
@EnablePigxSwagger2
@SpringCloudApplication
@EnablePigxFeignClients(basePackages = {"com.pig4cloud.pigx", "com.aurine.cloudx", "com.aurine.open"})
@EnablePigxResourceServer
@Import({ProjectConfiguration.class,
        MappingConverterAdapter.class,
        LogAutoConfiguration.class,
        OpenGetAuthorities.class})
public class SzztApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SzztApiApplication.class, args);
    }
}
