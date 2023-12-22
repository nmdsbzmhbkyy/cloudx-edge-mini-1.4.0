package com.aurine.cloudx.edge.sync.biz.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * http请求工具类
 *
 * @ClassName: HttpConnectorUtil
 * @author: zouyu
 * @date: 2022-12-19 08:56:58
 * @Copyright:
 */
@Component
@Slf4j
public class HttpConnectorUtil {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    /**
     * post连接
     *
     * @param url         要推送的地址
     * @param requestBody 要推送的内容
     * @return
     */
    public JSONObject post(String url, JSONObject requestBody, String sign) {
        return conn(url, requestBody, sign, HttpMethod.POST);
    }


    /**
     * get连接
     *
     * @param url 要查询的地址
     * @return
     */
    public JSONObject get(String url, String sign) {
        return conn(url, null, sign, HttpMethod.GET);
    }

    /**
     * 推送连接
     *
     * @param url 要查询的地址
     * @return
     */
    private JSONObject conn(String url, JSONObject requestBody, String sign, HttpMethod httpMethod) throws HttpClientErrorException {

        log.info("[同步服务] 发送数据 发送方式：{} , 地址： {}", httpMethod.toString(), url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(StrUtil.isNotEmpty(sign)){
            headers.add("Authorization", sign);
        }
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JSONObject> result = restTemplate.exchange(url, httpMethod, requestEntity, JSONObject.class);
        log.info("[同步服务] 接口返回数据:{}",result.getBody());
        return result.getBody();
    }


}
