package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfig;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiKafkaUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiTokenUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiMethodConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 华为中台 数据连接 核心类
 *
 * @ClassName: HuaweiDataConnector
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03 9:13
 * @Copyright:
 */
//@Component
@Slf4j
@Deprecated
public class HuaweiDataConnectorWithKafka {
    @Resource(name="proxyRestTemplate")
    private RestTemplate restTemplate;

    @Resource
    KafkaTemplate kafkaTemplate;

    /**
     * 连接机制
     * 直接使用队列缓存请求历史，通过消费队列
     * 直接http连接，只在登陆时缓存请求数据到队列
     */
    private final boolean useMq = false;


    /**
     * remote层统一调用方法
     *
     * @param requestObject
     * @return
     */
    public JSONObject send(HuaweiRequestObject requestObject) {

        //请求进入sRedis缓存，等待异步回应

        RedisUtil.set(requestObject.getMessageId(), JSONObject.toJSONString(requestObject), 30);

        if (useMq) {
            //直接进入MQ缓存 方案
            kafkaTemplate.send(HuaweiConstant.KAFKA_TOPIC_REQUEST, JSONObject.toJSONString(requestObject));
            return null;
        } else {
            if (requestObject.getMethod().equalsIgnoreCase(HuaweiMethodConstant.POST)) {
                return post(requestObject);

            } else {
                return get(requestObject);
            }

        }
    }


    /**
     * post连接
     *
     * @return
     */
    public JSONObject post(HuaweiRequestObject requestObject) {

        //无Token
        if (StringUtils.isEmpty(HuaweiTokenUtil.getToken())) {
            log.error("华为中台： {}", "缺失授权数据");
//            this.sendToMq(requestObject);
            login(requestObject);
            return null;

        } else {
            try {
                //执行对接业务
                return post(requestObject.getUri(), requestObject.getVersion(), requestObject.getRequestJson());

            } catch (HttpClientErrorException e) {
                //未授权，进行重授权机制
                if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                    log.error("华为中台： {}", "授权无效");
//                    this.sendToMq(requestObject);
                    //调用登录流程，并结束当前业务
                    login(requestObject);
                } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    log.error("华为中台： {}", "接口地址错误：" + requestObject.getUri());
                } else {
                    log.error("华为中台： 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
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
     *
     * @return
     */
    public JSONObject get(HuaweiRequestObject requestObject) {
        //无Token
        if (StringUtils.isEmpty(HuaweiTokenUtil.getToken())) {
            log.error("华为中台： {}", "缺失授权数据");
//            this.sendToMq(requestObject);
            login(requestObject);
            return null;

        } else {
            try {
                //对接业务
                return get(requestObject.getUri(), requestObject.getVersion());

            } catch (HttpClientErrorException e) {
                //未授权，进行重授权机制
                if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                    log.error("华为中台： {}", "授权无效");
//                    this.sendToMq(requestObject);
                    //调用登录流程，并结束当前业务
                    login(requestObject);
                } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    log.error("华为中台： {}", "接口地址错误：" + requestObject.getUri());
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
    public JSONObject post(String uri, String version, JSONObject requestBody) {
        //初始化通讯数据
        String url = HuaweiConfig.apiEndPoint + "/" + version + "/" + HuaweiConfig.apiResUrl + "/" + uri;
        String token = HuaweiTokenUtil.getToken();

        //调用连接
        String sign = HuaweiUtil.signGenerator(HuaweiConfig.appKey, HuaweiConfig.appSecret, token);
        return post(url, requestBody, sign);

    }

    /**
     * get连接
     *
     * @param uri 要推送的方法地址
     * @return
     */
    public JSONObject get(String uri, String version) {
        //初始化通讯数据
        String url = HuaweiConfig.apiEndPoint + "/" + version + "/" + HuaweiConfig.apiResUrl + "/" + uri;
        String token = HuaweiTokenUtil.getToken();

        //调用连接
        String sign = HuaweiUtil.signGenerator(HuaweiConfig.appKey, HuaweiConfig.appSecret, token);
        return get(url, version, sign);
    }


    /**
     * 登录业务
     */
    @Async
    public void login(HuaweiRequestObject requestObject) {
        //检查登录锁，不存在则上锁，存在忽略该操作
        log.info("华为中台： {}", "开始执行登录业务");
        if (!HuaweiTokenUtil.hasLogin()) {
            //1 登录锁
            HuaweiTokenUtil.loginLock();
            log.info("华为中台： {}", "挂登陆锁");

            //2 暂停kafka消费
            HuaweiKafkaUtil.stop();
            log.info("华为中台： {}", "停止MQ消费者");

            //3 执行登录
            // 3.1 初始化通讯数据
            String loginUri = "access_token/get";
            String url = HuaweiConfig.apiEndPoint + "/" + HuaweiConfig.apiVersion + "/" + HuaweiConfig.apiResUrl + "/" + loginUri;

            // 3.2 调用连接
            String sign = HuaweiUtil.signGenerator(HuaweiConfig.appKey, HuaweiConfig.appSecret, "");
            JSONObject tokenResult = get(url, "", sign);
            log.info("华为中台： 调用登录请求，结果为 {}", tokenResult);
            if (tokenResult != null) {

                if (tokenResult.getString("errorCode").equalsIgnoreCase(HuaweiConstant.STATE_SUCCESS)) {
                    //4 登录成功，重设Token
                    log.info("获取到Token:{}", tokenResult.getJSONObject("body").getString("access_token"));
                    HuaweiTokenUtil.setToken(tokenResult.getJSONObject("body").getString("access_token"));
                }

                //5 执行成功， 解锁, 恢复Kafka，否则按照默认设定时间自动解锁
                HuaweiTokenUtil.loginUnlock();
                log.info("华为中台： {}", "登录完成，解除登陆锁");
                HuaweiKafkaUtil.start();
                log.info("华为中台： {}", "登录完成，恢复MQ消费者");
            } else {
                //对方服务器无法连接，登录取消，恢复kafka
                HuaweiKafkaUtil.start();
                log.error("华为中台： {}", "登录失败，恢复MQ消费者");
            }

        } else {
            log.info("华为中台： {}", "其他线程正在登录，取消登录");
        }

        this.sendToMq(requestObject);

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

        log.info("向华为中台，发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", sign);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> result;
//        try {
        result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        JSONObject json = JSONObject.parseObject(result.getBody());
        log.info("resopnse: {}", json);

        return json;
//        } catch (HttpClientErrorException e) {
//            //未授权，进行重授权机制
//            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
//                log.error("华为中台： {}", "授权无效");
//                //调用登录流程，并结束当前业务
//                login();
//            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
//                log.error("华为中台： {}", "接口地址错误：" + url);
//            }
//            return null;
//        }
    }


    /**
     * 当连接方式为直接连接时候，将请求对象缓存到MQ
     *
     * @param requestObject
     */
    private void sendToMq(HuaweiRequestObject requestObject) {
        if (!useMq) {
            kafkaTemplate.send(HuaweiConstant.KAFKA_TOPIC_REQUEST, JSONObject.toJSONString(requestObject));
        }
    }


}
