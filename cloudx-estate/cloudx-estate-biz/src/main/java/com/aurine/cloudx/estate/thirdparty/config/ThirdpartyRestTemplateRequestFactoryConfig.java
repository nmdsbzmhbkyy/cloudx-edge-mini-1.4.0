/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.estate.thirdparty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * <p>RestTemplateFactory配置</p>
 *
 * @ClassName: RestTemplateConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-13 8:50
 * @Copyright:
 */
@Configuration
@RefreshScope
public class ThirdpartyRestTemplateRequestFactoryConfig {

    /**
     * 是否启用代理
     */
    @Value("${thirdparty.rest.proxy.enabled}")
    private Boolean enabledProxy;
    /**
     * 代理主机地址
     */
    @Value("${thirdparty.rest.proxy.host}")
    private String proxyHost;
    /**
     * 代理端口
     */
    @Value("${thirdparty.rest.proxy.port}")
    private Integer proxyPort;

    @Value("${thirdparty.rest.connection.read-timeout}")
    private int readTimeout;

    @Value("${thirdparty.rest.connection.connect-timeout}")
    private int connectionTimeout;


    @Bean
    public SimpleClientHttpRequestFactory httpClientFactory() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(readTimeout);
        httpRequestFactory.setConnectTimeout(connectionTimeout);

        if (enabledProxy) {
            SocketAddress address = new InetSocketAddress(proxyHost, proxyPort);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            httpRequestFactory.setProxy(proxy);
        }

        return httpRequestFactory;
    }

    @Bean("proxyRestTemplate")
    public RestTemplate proxyRestTemplate(SimpleClientHttpRequestFactory httpClientFactory) {
        RestTemplate restTemplate = new RestTemplate(httpClientFactory);
        return restTemplate;
    }
}
