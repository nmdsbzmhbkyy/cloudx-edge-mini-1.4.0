package com.aurine.cloudx.edge.sync.biz.config;

import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wrm
 * @Date: 2022/01/07 13:34
 * @Package: com.aurine.cloudx.edge.sync.biz.config
 * @Version: 1.0
 * @Remarks:
 **/
@Configuration
@Slf4j
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("from", PublicConstants.FROM_IN);
        requestTemplate.header("VERSION", Constants.GRAYSCALE_ROUTING_VERSION);
        requestTemplate.removeHeader("Content-Type");
        requestTemplate.header("Content-Type","application/json;charset=UTF-8");
        log.info("requestTemplate：{}", requestTemplate);
    }
}
