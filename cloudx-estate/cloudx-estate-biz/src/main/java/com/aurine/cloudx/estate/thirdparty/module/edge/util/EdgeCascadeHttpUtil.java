package com.aurine.cloudx.estate.thirdparty.module.edge.util;

import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>边缘侧入云http工具</p>
 *
 * @author : 王良俊
 * @date : 2021-12-13 16:34:24
 */
@Slf4j
@Component
public class EdgeCascadeHttpUtil implements ApplicationContextAware {

    static RestTemplate restTemplate;

    static ObjectMapper objectMapper = ObjectMapperUtil.instance();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EdgeCascadeHttpUtil.restTemplate = applicationContext.getBean("proxyRestTemplate", RestTemplate.class);
    }

    @SneakyThrows
    public static RequestResponse post(String url, Object data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = data != null ? objectMapper.writeValueAsString(data).toString() : "";
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        log.info("发送POST请求：URL:{} DATA:{}", url, json);
        try {
            ResponseEntity<JsonNode> intoCloudResponseResponseEntity = restTemplate.postForEntity(url, requestEntity, JsonNode.class);
            if (intoCloudResponseResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                JsonNode body = intoCloudResponseResponseEntity.getBody();
                log.info("POST请求 结果：{}", body);
                return new RequestResponse(true, body.toString());
            } else if (intoCloudResponseResponseEntity.getStatusCode().equals(HttpStatus.REQUEST_TIMEOUT)) {
                throw new RuntimeException("连接超时");
            } else {
//            log.error("post请求失败 URL:{}, DATA:{}", url, data);
                log.error("POST请求失败 URL：{} DATA：{}", url, data);
                return new RequestResponse(false, null);
            }
        } catch (ResourceAccessException e) {
            throw new RuntimeException("连接超时");
        }
    }

    public static RequestResponse get(String url, Map<String, ?> uriVariables) {
        if (uriVariables == null) {
            uriVariables = new HashMap<>();
        }
        log.info("GET请求 URL：{} 参数：{}", url, uriVariables);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, uriVariables);
        if (forEntity.getStatusCode() != HttpStatus.OK) {
            log.error("GET请求失败 URL：{} 参数：{}", url, uriVariables);
            return new RequestResponse(false, null);
        }
        log.info("GET请求 结果：{}", forEntity.getBody());
        return new RequestResponse(true, forEntity.getBody());
//        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestResponse {

        private boolean isSuccess;

        private String data;
    }

}
