package com.aurine.cloudx.push;


import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author lingang
 * @date 2020年03月30日
 * <p>
 * 冠林云-社区管理模块服务
 */
@EnablePigxSwagger2
@SpringCloudApplication
@EnablePigxFeignClients(basePackages = {"com.pig4cloud.pigx", "com.aurine.cloudx"})
@EnablePigxResourceServer
public class CloudxPushApplication {
	public static void main(String[] args) {
		SpringApplication.run(CloudxPushApplication.class, args);
	}
}
