package com.aurine.cloudx.wjy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WjyConfig {

    @Value("${wjy.base-url}")
    private String baseUrl;
    @Value("${wjy.wy-web-url}")
    private String wyWebUrl;
    @Value("${wjy.wj_web_url}")
    private String wjWebUrl;
    @Value("${wjy.gj_web_url}")
    private String gjWebUrl;
    @Value("${wjy.product_name}")
    private String productName;
}
