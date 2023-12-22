package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiTokenUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.config.AliEdgeConfig;
import lombok.SneakyThrows;
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
public class AliEdgeDataConnector {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    public JSONObject get(String url, Map<String, Object> queryParamMap) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return sendData(url + "?" + queryUrl, null, HttpMethod.GET, 0);
    }


    public JSONObject post(String url, JSONObject requestBody) {
        return sendData(url, requestBody, HttpMethod.POST, 0);
    }

    public JSONObject post(String url, Map<String, Object> queryParamMap, JSONObject requestBody) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return sendData(url + "?" + queryUrl, requestBody, HttpMethod.POST, 0);
    }

    public JSONObject put(String url, JSONObject requestBody) {
        return sendData(url, requestBody, HttpMethod.PUT, 0);
    }

    public JSONObject put(String url, Map<String, Object> queryParamMap, JSONObject requestBody) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return sendData(url + "?" + queryUrl, requestBody, HttpMethod.PUT, 0);
    }


    public JSONObject delete(String url, Map<String, Object> queryParamMap) {
        String queryUrl = getRequestBodyFromMap(queryParamMap, false);
        return sendData(url + "?" + queryUrl, null, HttpMethod.DELETE, 0);

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


    /**
     * 登录业务
     */
    @SneakyThrows
    public void login() {
        //检查登录锁，不存在则上锁，存在忽略该操作
        log.info("[阿里边缘-华为TOKEN] {}", "开始执行登录业务");
        if (!HuaweiTokenUtil.hasLogin()) {
            //1 登录锁
            HuaweiTokenUtil.loginLock();
            log.info("[阿里边缘-华为TOKEN] {}", "挂登陆锁");


            //3 执行登录
            // 3.1 初始化通讯数据
            String loginUri = "access_token/get";

            String url = AliEdgeConfig.tokenEndPoint + "/" + AliEdgeConfig.tokenVersion + "/" + AliEdgeConfig.tokenResUrl + "/" + loginUri;

            // 3.2 调用连接
            String sign = HuaweiUtil.signGenerator(AliEdgeConfig.tokenKey, AliEdgeConfig.tokenSecret, "");
            JSONObject tokenResult = conn(url, null, sign, HttpMethod.GET);

            log.info("[阿里边缘-华为TOKEN] 调用登录请求，结果为 {}", tokenResult);
            if (tokenResult != null) {

                if (tokenResult.getString("errorCode").equalsIgnoreCase(HuaweiConstant.STATE_SUCCESS)) {
                    //4 登录成功，重设Token
                    log.info("[阿里边缘-华为TOKEN] 获取到Token:{}", tokenResult.getJSONObject("body").getString("access_token"));
                    HuaweiTokenUtil.setToken(tokenResult.getJSONObject("body").getString("access_token"));

                    //因阿里边缘网关token使用中台3.0主备同步机制，因此需要一定时间同步
                    Thread.sleep(300);
                }

                //5 执行成功， 解锁, 恢复Kafka，否则按照默认设定时间自动解锁
                HuaweiTokenUtil.loginUnlock();
                log.info("[阿里边缘-华为TOKEN] {}", "登录完成，解除登陆锁");
            } else {
                log.error("[阿里边缘-华为TOKEN] {}", "连接异常");
            }

        } else {
            log.info("[阿里边缘-华为TOKEN] {}", "其他线程正在登录");
        }
    }


    /**
     * 发送请求，如果发送请求后token失败，以递归方式重新登录后继续发送
     *
     * @param loop
     * @return
     */
    private JSONObject sendData(String uri, JSONObject requestJsonBody, HttpMethod httpMethod, int loop) {
        if (loop >= 3) {
            log.error("[阿里边缘-华为TOKEN] {}", "凭证获取失败次数过多");
            throw new RuntimeException("凭证获取失败，请联系管理员");
        }

        try {
            int count = 1;

            while (StringUtils.isEmpty(HuaweiTokenUtil.getToken()) && (count <= 5)) {
                log.error("[阿里边缘-华为TOKEN] 尝试第{}次登录", count);
                login();
                count++;
            }

            if (count <= 0) {
                throw new RuntimeException("连接超时，请联系管理员");
            }


            return connToAliEdge(uri, requestJsonBody, HuaweiTokenUtil.getToken(), httpMethod);

        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("[阿里边缘-华为TOKEN] {}", "授权无效");
                HuaweiTokenUtil.removeToken();

                return this.sendData(uri, requestJsonBody, httpMethod, loop + 1);

            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("[阿里边缘-华为TOKEN] {}", "接口地址错误：" + uri);
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("[阿里边缘-华为TOKEN] 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
        } catch (ResourceAccessException rae) {
            log.error("[阿里边缘-华为TOKEN] 连接超时:{}", rae.getMessage());
            throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    //连接到阿里
    public JSONObject connToAliEdge(String uri, JSONObject requestBody, String token, HttpMethod httpMethod) {
        //初始化通讯数据
        String url;
        if (!uri.contains("http://dev.miligc.com")){
            url = AliEdgeConfig.apiEndPoint + "/" + AliEdgeConfig.apiVersion + "/" + AliEdgeConfig.apiResUrl + "/" + uri;
        }else {
            url = uri;
        }
        //调用连接
        String sign = HuaweiUtil.signGenerator(AliEdgeConfig.appKey, AliEdgeConfig.appSecret, token);
        log.info("[阿里边缘] 连接参数：token:{},httpMethod:{},appKey:{},appSecret:{}",token,httpMethod,AliEdgeConfig.appKey,AliEdgeConfig.appSecret);
        return conn(url, requestBody, sign, httpMethod);
    }

    /**
     * get连接
     *
     * @param url 要查询的地址
     * @return
     */
    private JSONObject conn(String url, JSONObject requestBody, String sign, HttpMethod httpMethod) throws HttpClientErrorException {

        log.info("[阿里边缘] 发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", sign);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> result;
//        try {
        result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        JSONObject json = JSONObject.parseObject(result.getBody());
        log.info("[阿里边缘] 同步获取到响应：{}", json);

        String code = json.getString("errorCode");
        if (StringUtil.isNotEmpty(code) && code.equals(HuaweiErrorEnum.WRONG_TOKEN.code)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);//授权异常
        }

        return json;
//
    }

}