package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiTokenUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <p>[阿里边缘]  数据连接 核心类</p>
 *
 * @ClassName: SfirmDataConnector
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-25 14:11
 * @Copyright:
 */
@Component
@Slf4j
public class AliEdgeDataConnector_BACK {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    private String baseUrl = "http://dev.miligc.com:18088";

    public JSONObject get(String url, Map<String, Object> queryParamMap) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return getConn(url + "?" + queryUrl);
    }


    public JSONObject post(String url, JSONObject requestBody) {
        return postConn(url, requestBody);
    }

    public JSONObject post(String url, Map<String, Object> queryParamMap, JSONObject requestBody) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return postConn(url + "?" + queryUrl, requestBody);
    }

    public JSONObject put(String url, JSONObject requestBody) {
        return postConn(url, requestBody);
    }

    public JSONObject put(String url, Map<String, Object> queryParamMap, JSONObject requestBody) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return postConn(url + "?" + queryUrl, requestBody);
    }


    public JSONObject delete(String url, Map<String, Object> queryParamMap) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return getConn(url + "?" + queryUrl);
    }

    /**
     * 推送连接
     *
     * @param url 接口地址（/v1/sinking/parking）
     * @return
     */
    public JSONObject getConn(String url) {
        //获取到平台配置


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info("[阿里边缘]发送信息 方法：GET，地址：{}， 数据：{}", baseUrl + url);


        try {
            //执行对接业务
            ResponseEntity<String> result = restTemplate.getForEntity(baseUrl + url, String.class);

            //判断返回状态
            if (result.getStatusCode() == HttpStatus.OK) {
                log.info(result.toString());
            } else {
                log.error("Fail to send msg to sfirm car parking");//网络不同等原因
            }
            JSONObject json = JSONObject.parseObject(result.getBody());
            log.info("resopnse: {}", json);

            return json;
        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("[阿里边缘]： {}", "授权无效");
            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("[阿里边缘]：  {}", "接口地址错误：" + baseUrl + url);
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("[阿里边缘]： 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
            return null;
        } catch (ResourceAccessException rae) {
            log.error("[阿里边缘]： 连接超时:{}", rae.getMessage());
            throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }

    /**
     * 推送连接
     *
     * @param url         接口地址（/v1/sinking/parking）
     * @param requestBody 要推送的内容
     * @return
     */
    public JSONObject postConn(String url, JSONObject requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info("[阿里边缘]发送信息 方法：POST，地址：{}， 数据：{}", baseUrl + url, requestBody);


        try {
            //执行对接业务
            ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + url, requestEntity, String.class);

            //判断返回状态
            if (result.getStatusCode() == HttpStatus.OK) {
                log.info(result.toString());
            } else {
                log.error("Fail to send msg to sfirm car parking");//网络不同等原因
            }
            JSONObject json = JSONObject.parseObject(result.getBody());
            log.info("resopnse: {}", json);

            return json;
        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("[阿里边缘]： {}", "授权无效");
            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("[阿里边缘]：  {}", "接口地址错误：" + baseUrl + url);
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("[阿里边缘]： 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
            return null;
        } catch (ResourceAccessException rae) {
            log.error("[阿里边缘]： 连接超时:{}", rae.getMessage());
            throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }


    private String getRequestBodyFromMap(Map<String, Object> parametersMap, boolean isUrlEncoding/*,String charset*/) {
        StringBuffer sbuffer = new StringBuffer();
        for (String key : parametersMap.keySet()) {
            String value = parametersMap.get(key).toString();
            if (isUrlEncoding) {
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                    if (StringUtils.isNotEmpty(value)) {
                        parametersMap.put(key, value);
                    }
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
            sbuffer.append(key).append("=").append(value).append("&");
        }
        return sbuffer.toString().replaceAll("&$", "");
    }


}