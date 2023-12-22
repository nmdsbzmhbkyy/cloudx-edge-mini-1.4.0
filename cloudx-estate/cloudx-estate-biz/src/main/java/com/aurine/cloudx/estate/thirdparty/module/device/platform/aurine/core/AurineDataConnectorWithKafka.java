package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util.AurineKafkaUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util.AurineTokenUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util.AurineUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineMethodConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineRequestObject;
import com.aurine.cloudx.estate.util.JsonUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 冠林中台 数据连接 核心类
 *
 * @ClassName: AurineDataConnector
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03 9:13
 * @Copyright:
 */
@Component
@Slf4j
public class AurineDataConnectorWithKafka {
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
    public JSONObject send(AurineRequestObject requestObject) {

        //请求进入sRedis缓存，等待异步回应

        RedisUtil.set(requestObject.getMessageId(), JSONObject.toJSONString(requestObject), 30);

        if (useMq) {
            //直接进入MQ缓存 方案
            kafkaTemplate.send(AurineConstant.KAFKA_TOPIC_REQUEST, JSONObject.toJSONString(requestObject));
            return null;
        } else {
            if (requestObject.getMethod().equalsIgnoreCase(AurineMethodConstant.POST)) {
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
    public JSONObject post(AurineRequestObject requestObject) {

        //无Token
        if (StringUtils.isEmpty(AurineTokenUtil.getToken(requestObject.getConfig().getProjectId(),requestObject.isProject()))) {
            log.error("冠林中台{}： {}", requestObject.getProjectId(), "缺失授权数据");
//            this.sendToMq(requestObject);
            login(requestObject);
            return null;

        } else {
            try {
                //执行对接业务
                return post(requestObject.getDataType(), requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), requestObject.getRequestJson(), AurineTokenUtil.getToken(requestObject.getConfig().getProjectId(),requestObject.isProject()));

            } catch (HttpClientErrorException e) {
                //未授权，进行重授权机制
                if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                    log.error("冠林中台 {}： {}", requestObject.getProjectId(), "授权无效");
//                    this.sendToMq(requestObject);
                    //调用登录流程，并结束当前业务
                    login(requestObject);
                } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    log.error("冠林中台{}： {}", requestObject.getProjectId(), "接口地址错误：" + requestObject.getUri());
                    throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
                } else {
                    log.error("冠林中台{}： 异常代码：{} 发生异常：{}", requestObject.getProjectId(), e.getRawStatusCode(), e.getMessage());
                    throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
                }
                return null;
            } catch (ResourceAccessException rae) {
                log.error("冠林中台{}： 连接超时:{}", requestObject.getProjectId(), rae.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
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
    public JSONObject get(AurineRequestObject requestObject) {
        //无Token
        if (StringUtils.isEmpty(AurineTokenUtil.getToken(requestObject.getConfig().getProjectId(),requestObject.isProject()))) {
            log.error("冠林中台 {}： {}", requestObject.getProjectId(), "缺失授权数据");
//            this.sendToMq(requestObject);
            login(requestObject);
            return null;

        } else {
            try {
                //对接业务
                return get(requestObject.getDataType(), requestObject.getConfig(), requestObject.getUri(), requestObject.getVersion(), AurineTokenUtil.getToken(requestObject.getConfig().getProjectId(),requestObject.isProject()));

            } catch (HttpClientErrorException e) {
                //未授权，进行重授权机制
                if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                    log.error("冠林中台{}： {}", requestObject.getProjectId(), "授权无效");
//                    this.sendToMq(requestObject);
                    //调用登录流程，并结束当前业务
                    login(requestObject);
                } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    log.error("冠林中台{}： {}", requestObject.getProjectId(), "接口地址错误：" + requestObject.getUri());
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
    public JSONObject post(String dataType, AurineConfigDTO config, String uri, String version, JSONObject requestBody, String token) {
        //初始化通讯数据
        String url = config.getApiEndPoint() + "/" + version + "/" + uri;

        //调用连接
        String sign = AurineUtil.signGenerator(config.getAppKey(), config.getAppType(), config.getAppSecret(), token);
        return doPost(dataType, url, requestBody, token, sign);

    }

    /**
     * get连接
     *
     * @param uri 要推送的方法地址
     * @return
     */
    public JSONObject get(String dataType, AurineConfigDTO config, String uri, String version, String token) {
        //初始化通讯数据
        String url = config.getApiEndPoint() + "/" + version + "/" + uri;

        //调用连接
        String sign = AurineUtil.signGenerator(config.getAppKey(), config.getAppType(), config.getAppSecret(), token);
        return doGet(dataType, url, version, token, sign);
    }


    /**
     * 登录业务
     */
    @Async
    public void login(AurineRequestObject requestObject) {
        //检查登录锁，不存在则上锁，存在忽略该操作
        log.info("冠林中台 项目ID:{} {}", requestObject.getProjectId(), "开始执行登录业务");
        if (!AurineTokenUtil.hasLogin(requestObject.getProjectId(),requestObject.isProject())) {
            //1 登录锁
            AurineTokenUtil.loginLock(requestObject.getProjectId(),requestObject.isProject());
            log.info("冠林中台： {} {}", requestObject.getProjectId(), "挂登陆锁");

            //2 暂停kafka消费
            AurineKafkaUtil.stop();
            log.info("冠林中台：{} {}", requestObject.getProjectId(), "停止MQ消费者");

            //3 执行登录
            // 3.1 初始化通讯数据
            String ticketUri = "ticket/get";
            String loginUri = "accesstoken/get ";
            String ticketUrl = requestObject.getConfig().getApiEndPoint() + "/" + requestObject.getConfig().getVersion().number + "/" + ticketUri;
            String loginUrl = requestObject.getConfig().getApiEndPoint() + "/" + requestObject.getConfig().getVersion().number + "/" + loginUri;

            // 3.2 调用连接，获取票据
            String sign = AurineUtil.signGenerator(requestObject.getConfig().getAppKey(), requestObject.getConfig().getAppType(), requestObject.getConfig().getAppSecret(), "");
            JSONObject ticketResult = doGet(MediaType.MULTIPART_FORM_DATA_VALUE, ticketUrl, "", "", sign);
            log.info("冠林中台 {}： 调用登录请求，结果为 {}", requestObject.getProjectId(), ticketResult);
            if (ticketResult != null) {

                if (ticketResult.getString("errorCode").equalsIgnoreCase(AurineConstant.STATE_SUCCESS)) {
                    String ticket = ticketResult.getJSONObject("body").getString("ticket");
                    log.info("获取到TicketUrl{}", ticket);
                    String params = "?ticket=" + ticket + "&username=" + requestObject.getConfig().getUserName();

                    //执行登录
                    JSONObject tokenResult = doGet(MediaType.MULTIPART_FORM_DATA_VALUE, loginUrl + params, "", "", sign);

                    //4 登录成功，重设Token
                    log.info("获取到Token:{}", tokenResult.getJSONObject("body").getString("accessToken"));
                    AurineTokenUtil.setToken(tokenResult.getJSONObject("body").getString("accessToken"), requestObject.getProjectId(),requestObject.isProject());
                }

                //5 执行成功， 解锁, 恢复Kafka，否则按照默认设定时间自动解锁
                AurineTokenUtil.loginUnlock();
                log.info("冠林中台 {}： {}", requestObject.getProjectId(), "登录完成，解除登陆锁");
                AurineKafkaUtil.start();
                log.info("冠林中台 {}： {}", requestObject.getProjectId(), "登录完成，恢复MQ消费者");
            } else {
                //对方服务器无法连接，登录取消，恢复kafka
                AurineKafkaUtil.start();
                log.error("冠林中台 {}： {}", requestObject.getProjectId(), "登录失败，恢复MQ消费者");
            }

        } else {
            log.info("冠林中台{}： {}", requestObject.getProjectId(), "其他线程正在登录，取消登录");
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
    private JSONObject doPost(String dataType, String url, JSONObject requestBody, String token, String sign) {
        if (dataType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            return connWithJson(url, requestBody, sign, token, HttpMethod.POST);
        } else {
            return connWithDataForm(url, requestBody, sign, token, HttpMethod.POST);
        }
    }


    /**
     * get连接
     *
     * @param url 要查询的地址
     * @return
     */
    private JSONObject doGet(String dataType, String url, String version, String token, String sign) {
        if (dataType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            return connWithJson(url, null, sign, token, HttpMethod.GET);
        } else {
            return connWithDataForm(url, null, sign, token, HttpMethod.GET);
        }
    }

    /**
     * 使用JSON数据传输
     *
     * @param url 要查询的地址
     * @return
     */
    private JSONObject connWithJson(String url, JSONObject requestBody, String sign, String token, HttpMethod httpMethod) throws HttpClientErrorException {
        MultiValueMap<String, String> dataMap = new LinkedMultiValueMap<>();
        if (requestBody != null) {
            requestBody.put("accesstoken", token);
        }
        log.info("向冠林中台，发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", sign);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> result;
        result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);

        JSONObject json = JSONObject.parseObject(result.getBody());
        log.info("resopnse: {}", json);

        String code = json.getString("errorCode");
        if (StringUtil.isNotEmpty(code) && code.equals(AurineErrorEnum.WRONG_TOKEN.code)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }

        return json;

    }

    /**
     * 使用Form数据传输
     *
     * @param url 要查询的地址
     * @return
     */
    private JSONObject connWithDataForm(String url, JSONObject requestBody, String sign, String token, HttpMethod httpMethod) throws HttpClientErrorException {
        MultiValueMap<String, String> dataMap = new LinkedMultiValueMap<>();
        if (requestBody != null) {
            requestBody.put("accesstoken", token);
            dataMap = JsonUtil.toMultiValueMap(requestBody);
        }
        log.info("向冠林中台，发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", sign);

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(dataMap, headers);
        ResponseEntity<String> result;
        result = restTemplate.exchange(url, httpMethod, requestEntity, String.class);

        JSONObject json = JSONObject.parseObject(result.getBody());
        log.info("resopnse: {}", json);

        String code = json.getString("errorCode");
        if (StringUtil.isNotEmpty(code) && code.equals(AurineErrorEnum.WRONG_TOKEN.code)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        return json;
    }


    /**
     * 当连接方式为直接连接时候，将请求对象缓存到MQ
     *
     * @param requestObject
     */
    private void sendToMq(AurineRequestObject requestObject) {
        if (!useMq) {
            kafkaTemplate.send(AurineConstant.KAFKA_TOPIC_REQUEST, JSONObject.toJSONString(requestObject));
        }
    }


}
