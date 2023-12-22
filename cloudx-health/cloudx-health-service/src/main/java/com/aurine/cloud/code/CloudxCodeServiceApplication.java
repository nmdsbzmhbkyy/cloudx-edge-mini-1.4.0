package com.aurine.cloud.code;

import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnablePigxSwagger2
public class CloudxCodeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudxCodeServiceApplication.class, args);
    }
}
