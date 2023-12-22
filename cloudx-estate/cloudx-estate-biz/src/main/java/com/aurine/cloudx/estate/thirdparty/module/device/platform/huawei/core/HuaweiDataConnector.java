package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.exception.ThirdPartyServiceErrorEnum;
import com.aurine.cloudx.common.core.exception.ThirdPartyServiceException;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiTokenUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiMethodConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 华为中台 数据连接 核心类
 *
 * @ClassName: HuaweiDataConnector
 * @author: 王伟 <wangwei@Huawei.cn>
 * @date: 2020-08-03 9:13
 * @Copyright:
 */
@Component
@Slf4j
public class HuaweiDataConnector {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    @Resource
    KafkaTemplate kafkaTemplate;

    private final int MAX_LOOP = 3;//最大尝试数

    /**
     * remote层统一调用方法
     *
     * @param requestObject
     * @return
     */
    public JSONObject send(HuaweiRequestObject requestObject) {
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
    public JSONObject sendSync(HuaweiRequestObject requestObject, String requestKey, String responseKey) {
        log.info("[华为中台] 开始异步转同步发送指令 requestKey:{},responseKey:{}，内容：{} ", requestKey, responseKey, requestObject);

        //缓存暂存

        RedisUtil.set(requestKey, JSONObject.toJSONString(requestObject), 20);

        this.send(requestObject);

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
            log.info("[华为中台] 异步转同步发送指令 请求超时！ requestKey:{},responseKey:{}，内容：{} ", requestKey, responseKey, requestObject);
            throw new RuntimeException("连接超时，请联系管理员");
        } else {
            String jsonObjectStr = RedisUtil.get(responseKey).toString();
            JSONObject result = JSON.parseObject(jsonObjectStr, JSONObject.class);
            log.info("[华为中台] 异步转同步发送指令 获取到结果！ requestKey:{},responseKey:{}，内容：{} ", requestKey, responseKey, result);
            return result;
        }
    }

    /**
     * 同步登录，失败不缓存kafka
     *
     * @return
     */
    public JSONObject sendData(HuaweiRequestObject requestObject, boolean isPost) {
        return this.sendData(requestObject, isPost, 0);
    }


    /**
     * 发送请求，如果发送请求后token失败，以递归方式重新登录后继续发送
     *
     * @param requestObject
     * @param isPost
     * @param loop
     * @return
     */
    private JSONObject sendData(HuaweiRequestObject requestObject, boolean isPost, int loop) {
        if (loop >= MAX_LOOP) {
            log.error("[华为中台] {}", "凭证获取失败次数过多");
            throw new RuntimeException("凭证获取失败，请联系管理员");
        }

        try {
            int count = 1;

            while (StringUtils.isEmpty(HuaweiTokenUtil.getToken()) && (count <= 5)) {
                log.error("[华为中台] 尝试第{}次登录", count);
                login(requestObject);
                count++;
            }

            if (count <= 0) {
                throw new RuntimeException("连接超时，请联系管理员");
            }


            if (isPost) {
                return post(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), requestObject.getRequestJson(), HuaweiTokenUtil.getToken());
            } else {
                return get(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), HuaweiTokenUtil.getToken());
            }

        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("[华为中台] {}", "授权无效");
                HuaweiTokenUtil.removeToken();
                /**
                 * @since: 2020-09-21 首次连接发现授权无效后，进行二次登录
                 */
                return this.sendData(requestObject, isPost, loop + 1);

            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("[华为中台] {}", "接口地址错误：" + requestObject.getUri());
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("[华为中台] 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
//            return null;
        } catch (ResourceAccessException rae) {
            log.error("[华为中台] 连接超时:{}", rae.getMessage());
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
    public JSONObject post(HuaweiRequestObject requestObject) {

        //无Token
        if (StringUtils.isEmpty(HuaweiTokenUtil.getToken())) {
            log.error("[华为中台] {}", "缺失授权数据");
//            this.sendToMq(requestObject);
            login(requestObject);
            return null;

        } else {
            try {
                //执行对接业务
                return post(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), requestObject.getRequestJson(), HuaweiTokenUtil.getToken());

            } catch (HttpClientErrorException e) {
                //未授权，进行重授权机制
                if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                    log.error("[华为中台] {}", "授权无效");
//                    this.sendToMq(requestObject);
                    //调用登录流程，并结束当前业务
                    login(requestObject);
                } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    log.error("[华为中台] {}", "接口地址错误：" + requestObject.getUri());
                } else {
                    log.error("[华为中台] 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                }
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

    }


    /**
     * get连接
     * 只用于kafka缓存
     *
     * @return
     */
    public JSONObject get(HuaweiRequestObject requestObject) {
        //无Token
        if (StringUtils.isEmpty(HuaweiTokenUtil.getToken())) {
            log.error("[华为中台] {}", "缺失授权数据");
//            this.sendToMq(requestObject);
            login(requestObject);
            return null;

        } else {
            try {
                //对接业务
                return get(requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), HuaweiTokenUtil.getToken());

            } catch (HttpClientErrorException e) {
                //未授权，进行重授权机制
                if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                    log.error("[华为中台] {}", "授权无效");
//                    this.sendToMq(requestObject);
                    //调用登录流程，并结束当前业务
                    login(requestObject);
                } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    log.error("[华为中台] {}", "接口地址错误：" + requestObject.getUri());
                }
                return null;
            }
        }

    }


    /**
     * post连接
     *
     * @param uri         要推送的方法地址
     * @param requestBody 要推送的内容
     * @return
     */
    public JSONObject post(HuaweiConfigDTO config, String uri, String version, JSONObject requestBody, String token) {
        //初始化通讯数据

        String url = config.getApiEndPoint() + "/" + version + "/" + config.getApiResUrl() + "/" + uri;

        //调用连接
        String sign = HuaweiUtil.signGenerator(config.getAppKey(), config.getAppSecret(), token);
        return post(url, requestBody, sign);

    }

    /**
     * get连接
     *
     * @param uri 要推送的方法地址
     * @return
     */
    public JSONObject get(HuaweiConfigDTO config, String uri, String version, String token) {
        //初始化通讯数据
        String url = config.getApiEndPoint() + "/" + version + "/" + config.getApiResUrl() + "/" + uri;

        //调用连接
//        String sign = HuaweiUtil.signGenerator(HuaweiConfig.appKey, HuaweiConfig.appSecret, token);
        String sign = HuaweiUtil.signGenerator(config.getAppKey(), config.getAppSecret(), token);
        return get(url, version, sign);
    }


    /**
     * 登录业务
     */
    @Async
    public void login(HuaweiRequestObject requestObject) {
        //检查登录锁，不存在则上锁，存在忽略该操作
        log.info("[华为中台] {}", "开始执行登录业务");
        if (!HuaweiTokenUtil.hasLogin()) {
            //1 登录锁
            HuaweiTokenUtil.loginLock();
            log.info("[华为中台] {}", "挂登陆锁");

//            //2 暂停kafka消费
//            HuaweiKafkaUtil.stop();
//            log.info("[华为中台] {}", "停止MQ消费者");

            //3 执行登录
            // 3.1 初始化通讯数据
            String loginUri = "access_token/get";
            String url = requestObject.getConfig().getApiEndPoint() + "/" + requestObject.getConfig().getApiVersion() + "/" + requestObject.getConfig().getApiResUrl() + "/" + loginUri;

            // 3.2 调用连接
            String sign = HuaweiUtil.signGenerator(requestObject.getConfig().getAppKey(), requestObject.getConfig().getAppSecret(), "");
            JSONObject tokenResult = get(url, "", sign);
            log.info("[华为中台] 调用登录请求，结果为 {}", tokenResult);
            if (tokenResult != null) {

                if (tokenResult.getString("errorCode").equalsIgnoreCase(HuaweiConstant.STATE_SUCCESS)) {
                    //4 登录成功，重设Token
                    log.info("[华为中台] 获取到Token:{}", tokenResult.getJSONObject("body").getString("access_token"));
                    HuaweiTokenUtil.setToken(tokenResult.getJSONObject("body").getString("access_token"));
                }

                //5 执行成功， 解锁, 恢复Kafka，否则按照默认设定时间自动解锁
                HuaweiTokenUtil.loginUnlock();
                log.info("[华为中台] {}", "登录完成，解除登陆锁");
//                HuaweiKafkaUtil.start();
//                log.info("[华为中台] {}", "登录完成，恢复MQ消费者");
            } else {
                //对方服务器无法连接，登录取消，恢复kafka
//                HuaweiKafkaUtil.start();
//                log.error("[华为中台] {}", "登录失败，恢复MQ消费者");
                log.error("[华为中台] {}", "连接异常");
            }

        } else {
            log.info("[华为中台] {}", "其他线程正在登录");
        }
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

        log.info("[华为中台] 发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", sign);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> result;
//        try {
        result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        JSONObject json = JSONObject.parseObject(result.getBody());
        log.info("[华为中台] 同步获取到响应：{}", json);

        String code = json.getString("errorCode");
        if (StringUtil.isNotEmpty(code) && code.equals(HuaweiErrorEnum.WRONG_TOKEN.code)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);//授权异常
        }
//
//        else if (StringUtil.isNotEmpty(code) && !code.equals(HuaweiErrorEnum.SUCCESS.code)) {
//            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, json.getString("errorMsg"));//其他异常
//        }


//        return json.getJSONObject("body");
        return json;
//        } catch (HttpClientErrorException e) {
//            //未授权，进行重授权机制
//            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
//                log.error("[华为中台] {}", "授权无效");
//                //调用登录流程，并结束当前业务
//                login();
//            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
//                log.error("[华为中台] {}", "接口地址错误：" + url);
//            }
//            return null;
//        }
    }


}
