package com.aurine.cloudx.wjy;

import com.aurine.cloudx.wjy.config.AppInit;
import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author pigx archetype
 * <p>
 * 项目启动类
 */
@EnablePigxSwagger2
@SpringCloudApplication
@EnablePigxFeignClients(basePackages = {"com.pig4cloud.pigx", "com.aurine.cloudx"})
@EnablePigxResourceServer
public class App {
    static SpringApplication springApplication;
    public static AppInit appInit = null;
    public static void main(String[] args) {
        appInit = new AppInit();
        springApplication = new SpringApplication(App.class);
        springApplication.addListeners(appInit);
        springApplication.run(args);
        //SpringApplication.run(App.class, args);
    }
}
