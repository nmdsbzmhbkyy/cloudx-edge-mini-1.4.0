package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util.AurineEdgeDeviceDataUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiMethodConstant;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 冠林边缘网关 数据连接 核心类
 *
 * @ClassName: HuaweiDataConnector
 * @author: 王伟 <wangwei@Huawei.cn>
 * @date: 2020-08-03 9:13
 * @Copyright:
 */
@Component
@Slf4j
public class AurineEdgeDataConnector {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    /**
     * remote层统一调用方法
     *
     * @param requestObject
     * @return
     */
    public JSONObject send(AurineEdgeRequestObject requestObject) {
        return sendData(requestObject, requestObject.getMethod().equalsIgnoreCase(HuaweiMethodConstant.POST));
    }


    /**
     * 同步请求方法
     * 在redis中定义请求和响应数据，当监听到响应数据出现时，认为请求连接完成，返回响应数据。如请求数据超时消失，则认为请求超时。
     * 因此，requestObj对象中应包含requestKey和responseKey信息
     *
     * @param requestKey  请求id
     * @param responseKey 返回值id
     * @return
     */
    public JSONObject sendSync(AurineEdgeRequestObject requestObject, String requestKey, String responseKey) {
        log.info("[冠林边缘网关] 开始异步转同步发送指令 requestKey:{},responseKey:{}，内容：{} ", requestKey, responseKey, requestObject);

        //缓存暂存
        RedisUtil.set(requestKey, JSONObject.toJSONString(requestObject), 20);

        AurineEdgeRespondDTO aurineEdgeRespondDTO = AurineEdgeDeviceDataUtil.handelRespond(this.send(requestObject));
        if(aurineEdgeRespondDTO.getErrorEnum() == AurineEdgeErrorEnum.NO_DEVICE){
            log.error("[冠林边缘网关] 未找到指定设备:{}",requestObject.getRequestJson());
            return requestObject.getRequestJson();
        }

        //挂起,监听结果，直到key过期或处理完成
        boolean outOfTimeFlag = true;

        while (RedisUtil.hasKey(requestKey)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //获取到结果，中断等待
            if (RedisUtil.hasKey(responseKey)) {
                outOfTimeFlag = false;
                break;
            }
        }


        if (outOfTimeFlag) {
//            throw new ThirdPartyServiceException(ThirdPartyServiceErrorEnum.OUT_OF_TIME);
            log.info("[冠林边缘网关] 异步转同步发送指令 请求超时！ requestKey:{},responseKey:{}，内容：{} ", requestKey, responseKey, requestObject);
            throw new RuntimeException("连接超时，请联系管理员");
        } else {
            String jsonObjectStr = RedisUtil.get(responseKey).toString();
            JSONObject result = JSON.parseObject(jsonObjectStr, JSONObject.class);
            log.info("[冠林边缘网关] 异步转同步发送指令 获取到结果！ requestKey:{},responseKey:{}，内容：{} ", requestKey, responseKey, result);
            return result;
        }
    }


    /**
     * 发送请求，如果发送请求后token失败，以递归方式重新登录后继续发送
     *
     * @param requestObject
     * @param isPost
     * @return
     */
    private JSONObject sendData(AurineEdgeRequestObject requestObject, boolean isPost) {

        try {

            if (isPost) {
                return post(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), requestObject.getRequestJson());
            } else {
                return get(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion());
            }

        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("[冠林边缘网关] {}", "接口地址错误：" + requestObject.getUri());
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("[冠林边缘网关] 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
//            return null;
        } catch (ResourceAccessException rae) {
            log.error("[冠林边缘网关] 连接超时:{}", rae.getMessage());
            throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * post连接
     * 只用于kafka缓存
     *
     * @return
     */
    public JSONObject post(AurineEdgeRequestObject requestObject) {


        try {
            //执行对接业务
            return post(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), requestObject.getRequestJson());

        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("[冠林边缘网关] {}", "授权无效");
            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("[冠林边缘网关] {}", "接口地址错误：" + requestObject.getUri());
            } else {
                log.error("[冠林边缘网关] 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }


    /**
     * get连接
     * 只用于kafka缓存
     *
     * @return
     */
    public JSONObject get(AurineEdgeRequestObject requestObject) {

        try {
            //对接业务
            return get(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion());

        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("[冠林边缘网关] {}", "授权无效");
            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("[冠林边缘网关] {}", "接口地址错误：" + requestObject.getUri());
            }
            return null;
        }

    }


    /**
     * post连接
     *
     * @param uri         要推送的方法地址
     * @param requestBody 要推送的内容
     * @return
     */
    public JSONObject post(AurineEdgeConfigDTO config, String uri, String version, JSONObject requestBody) {
        //初始化通讯数据

        String url = config.getApiEndPoint() + "/" + version + "/" + config.getApiResUrl() + "/" + uri;

        //调用连接
//        String sign = AurineEdgeUtil.signGenerator(config.getAppKey(), config.getAppSecret(), token);
        return post(url, requestBody, "");

    }

    /**
     * get连接
     *
     * @param uri 要推送的方法地址
     * @return
     */
    public JSONObject get(AurineEdgeConfigDTO config, String uri, String version) {
        //初始化通讯数据
        String url = config.getApiEndPoint() + "/" + version + "/" + config.getApiResUrl() + "/" + uri;

        //调用连接
        return get(url, version, "");
    }


    /**
     * 推送连接
     *
     * @param url         要推送的地址
     * @param requestBody 要推送的内容
     * @return
     */
    private JSONObject post(String url, JSONObject requestBody, String sign) {
        return conn(url, requestBody, sign, HttpMethod.POST);
    }


    /**
     * get连接
     *
     * @param url 要查询的地址
     * @return
     */
    private JSONObject get(String url, String version, String sign) {
        return conn(url, null, sign, HttpMethod.GET);
    }

    /**
     * get连接
     *
     * @param url 要查询的地址
     * @return
     */
    private JSONObject conn(String url, JSONObject requestBody, String sign, HttpMethod httpMethod) throws HttpClientErrorException {

        log.info("[冠林边缘网关] 发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", sign);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<JSONObject> result;
//        try {
        result = restTemplate.exchange(url, httpMethod, requestEntity, JSONObject.class);
        log.info("[冠林边缘网关] 同步获取到响应：{}", result);
        JSONObject json = result.getBody();

        String code = json.getString("errorCode");
        if (StringUtil.isNotEmpty(code) && code.equals(AurineEdgeErrorEnum.WRONG_TOKEN.code)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);//授权异常
        }
        return json;
    }
}
