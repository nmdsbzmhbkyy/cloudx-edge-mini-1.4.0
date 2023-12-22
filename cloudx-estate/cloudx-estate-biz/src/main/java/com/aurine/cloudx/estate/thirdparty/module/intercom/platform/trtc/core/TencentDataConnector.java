package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.dto.TencentRespondDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @program: cloudx
 * @description: 腾讯云对讲 数据连接 核心类
 * @author: 谢泽毅
 * @create: 2021-08-19 15:04
 **/
@Slf4j
@Component
public class TencentDataConnector {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    /**
     * get连接
     *
     * @param url 要查询的地址
     * @return
     */
    public JSONObject conn(String url, TencentRespondDTO tencentRespondDTO, HttpMethod httpMethod) throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> result;
        if (tencentRespondDTO.getRespondObj() != null) {
            log.info("[腾讯云对讲] 发送信息 方法：{}，地址：{}， 数据：{}", httpMethod, url, tencentRespondDTO.getRespondObj());
            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(tencentRespondDTO.getRespondObj(), headers);
            result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        } else if (tencentRespondDTO.getRespondArray() != null) {
            log.info("[腾讯云对讲] 发送信息 方法：{}，地址：{}， 数据：{}", httpMethod, url, tencentRespondDTO.getRespondArray());
            HttpEntity<JSONArray> requestEntity = new HttpEntity<>(tencentRespondDTO.getRespondArray(), headers);
            result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        } else {
            log.error("[腾讯云对讲] 发送信息 方法：{}，地址：{}， 数据为空", httpMethod, url);
            return null;
        }
        JSONObject json = JSONObject.parseObject(result.getBody());
        String code = json.getString("errorCode");
        if (StringUtil.isNotEmpty(code) && code.equals(HuaweiErrorEnum.WRONG_TOKEN.code)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);//授权异常
        }
        return json;
//
    }





//    /**
//     * get连接
//     *
//     * @param url 要查询的地址
//     * @return
//     */
//    private JSONObject conn(String url, JSONObject requestBody, String sign, HttpMethod httpMethod) throws HttpClientErrorException {
//
//        log.info("[阿里边缘] 发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        headers.add("Authorization", sign);
//
//        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<String> result;
////        try {
//        result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
//        JSONObject json = JSONObject.parseObject(result.getBody());
//        log.info("[阿里边缘] 同步获取到响应：{}", json);
//
//        String code = json.getString("errorCode");
//        if (StringUtil.isNotEmpty(code) && code.equals(HuaweiErrorEnum.WRONG_TOKEN.code)) {
//            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);//授权异常
//        }
//
//        return json;
////
//    }
}
