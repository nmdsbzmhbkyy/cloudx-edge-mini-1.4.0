package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.core;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.request.YushiConnectDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.respond.YushiResponse;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums.RespondEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums.YushiApiEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * 博西尼 数据连接 核心类
 */
@Component
@Slf4j
public class YushiDataConnector {

    /**
     * 发送请求
     *
     * @return
     */
    public JSONObject send(YushiConnectDTO connectDTO, String api, JSONObject requestBody, HttpMethod httpMethod) throws Exception {
        try {
            YushiResponse response = this.conn(connectDTO, api, requestBody, httpMethod)
                    .getObject("Response", YushiResponse.class);
            return handleResult(response);

        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
//                log.error("[冠林边缘网关] {}", "接口地址错误：" + config.getUrl());
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("[冠林边缘网关] 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
        } catch (ResourceAccessException rae) {
            log.error("[冠林边缘网关] 连接超时:{}", rae.getMessage());
            throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 连接
     *
     * @param api
     * @return
     */
    private JSONObject conn(YushiConnectDTO connectDTO, String api, JSONObject requestBody, HttpMethod httpMethod) throws HttpClientErrorException {
//        String uri = "http://" + connectDTO.getIp() + ":" + connectDTO.getPort() + url;
        String uri = new StringBuilder("http://").append(connectDTO.getIp())
                .append(connectDTO.getPort() == null ? "" : ":" + connectDTO.getPort()).append(api).toString();
        log.info("[冠林边缘网关] 发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), uri, requestBody);
        RestTemplate restTemplate = this.getRestTemplate(connectDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> result;
        result = restTemplate.exchange(uri, httpMethod, requestEntity, String.class);
        JSONObject json = JSONObject.parseObject(result.getBody());
        log.info("[冠林边缘网关] 同步获取到响应：{}", json);

        return json;
    }

    private RestTemplate getRestTemplate(YushiConnectDTO connectDTO) {
        int port =connectDTO.getPort() == null ? AuthScope.ANY_PORT : connectDTO.getPort();
        //摘要认证时使用
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(connectDTO.getIp(), port, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(connectDTO.getUserName(), connectDTO.getPassword()));
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

        //设置超时时间
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpclient);
        httpRequestFactory.setConnectionRequestTimeout(3000);
        httpRequestFactory.setConnectTimeout(3000);
        httpRequestFactory.setReadTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        //支持中文编码
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    private JSONObject handleResult(YushiResponse response) throws Exception {
        if (!RespondEnum.SUCCEED.ResponseCode.equals(response.getResponseCode())) {
            throw new RuntimeException("请求失败，错误码：" + response.getResponseCode() + "，错误信息：" + response.getResponseString());
        }
        return response.getData();
    }
}
