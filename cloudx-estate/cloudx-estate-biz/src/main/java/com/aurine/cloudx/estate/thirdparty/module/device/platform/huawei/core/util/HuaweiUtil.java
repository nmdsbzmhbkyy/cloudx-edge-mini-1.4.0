package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiMethodConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * 华为连接工具类
 *
 * @ClassName: HuaweiUtil
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 14:50
 * @Copyright:
 */
@Slf4j
public class HuaweiUtil {


    /**
     * Authorization生成
     * 生成规则：
     * http请求头Authorization部分，统一使用Basic方式，存放的Basic认证内容包括所有接口的4个公共参数appKey、timestamp（当前unix时间戳）、verify（校验字符串）、accesstoken（访问accesstoken）。
     * 内容格式为：“appKey:timestamp:verify:accesstoken”，各个参数位置固定不变，参数间用冒号“:”隔开，某项参数不传时对应位置留空（如“appKey:timestamp:verify::type”）,verify生成规则为MD5（timestamp+“|”+appSecret)。如下：
     * <p>
     * Authorization: Basic base64(10001:1408430177:verify验证串:accesstoken)
     *
     * @param appkey    AppKey
     * @param appSecret 授权码
     * @return
     */
    @SneakyThrows
    public static String signGenerator(String appkey, String appSecret, String token) {
        //verify 生成规则为 MD5（timestamp+“|”+appSecret)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String verify = timestamp + "|" + appSecret;
        verify = DigestUtils.md5DigestAsHex(verify.getBytes());

        String sign = appkey + ":" + timestamp + ":" + verify + ":" + token;
//        sign = "Basic " + DigestUtils.md5DigestAsHex(sign.getBytes());

        String asB64 = Base64.getEncoder().encodeToString(sign.getBytes("utf-8"));
        sign = "Basic " + asB64;

        return sign;
    }


    /**
     * 创建 Post Request对象
     *
     * @param uri
     * @param requestJson
     * @param version
     * @return
     */
    public static HuaweiRequestObject createPostRequestObject(HuaweiConfigDTO configDTO, String uri, JSONArray requestJson, String version) {
        return createRequestObject(configDTO, uri, null, requestJson, version, HuaweiMethodConstant.POST);
    }

    /**
     * 创建 Post Request对象
     *
     * @param uri
     * @param requestJson
     * @param version
     * @return
     */
    public static HuaweiRequestObject createPostRequestObject(HuaweiConfigDTO configDTO, String uri, JSONArray requestJson, String version, JSONObject otherParams) {
        return createRequestObject(configDTO, uri, null, requestJson, version, HuaweiMethodConstant.POST, otherParams);
    }

    /**
     * 创建 Post Request对象
     *
     * @param uri
     * @param requestJson
     * @param version
     * @return
     */
    public static HuaweiRequestObject createPostRequestObject(HuaweiConfigDTO configDTO, String uri, JSONObject requestJson, String version) {
        return createRequestObject(configDTO, uri, requestJson, null, version, HuaweiMethodConstant.POST);
    }

    /**
     * 创建 Post Request对象
     *
     * @param uri
     * @param requestJson
     * @param version
     * @return
     */
    public static HuaweiRequestObject createPostRequestObject(HuaweiConfigDTO configDTO, String uri, JSONObject requestJson, String version, JSONObject otherParams) {
        return createRequestObject(configDTO, uri, requestJson, null, version, HuaweiMethodConstant.POST, otherParams);
    }

    /**
     * 创建 Get Request对象
     *
     * @param uri
     * @param requestJson
     * @param version
     * @return
     */
    public static HuaweiRequestObject createGetRequestObject(HuaweiConfigDTO configDTO, String uri, JSONObject requestJson, String version) {
        return createRequestObject(configDTO, uri, requestJson, null, version, HuaweiMethodConstant.GET);
    }

    /**
     * 创建 Get Request对象
     *
     * @param uri
     * @param requestJson
     * @param version
     * @return
     */
    public static HuaweiRequestObject createGetRequestObject(HuaweiConfigDTO configDTO, String uri, JSONObject requestJson, String version, JSONObject otherParams) {
        return createRequestObject(configDTO, uri, requestJson, null, version, HuaweiMethodConstant.GET, otherParams);
    }

    private static HuaweiRequestObject createRequestObject(HuaweiConfigDTO configDTO, String uri, JSONObject requestJson, JSONArray requestJsonArray, String version, String method) {


        String uid = UUID.randomUUID().toString().replaceAll("-", "");
//        String uid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 19);

        //针对post指令，设置msgId
        if ((requestJson != null || requestJsonArray != null) && HuaweiMethodConstant.POST.equalsIgnoreCase(method)) {
            requestJson.put("msgId", uid);
        }

        HuaweiRequestObject obj = new HuaweiRequestObject();
        obj.setMethod(method);
        obj.setUri(uri);
        obj.setVersion(version);
        obj.setRequestJson(requestJson);
        obj.setRequestJsonArray(requestJsonArray);
        obj.setMessageId(uid);
        obj.setConfig(configDTO);
//        obj.setProjectId(ProjectContextHolder.getProjectId());
        //配置到缓存中
        RedisUtil.set(HuaweiCacheConstant.HUAWEI_REQUEST_PER + uid, JSON.toJSONString(obj), HuaweiCacheConstant.HUAWEI_REQUEST_MSG_TTL);

        return obj;
    }

    /**
     * 创建 Get Request对象
     *
     * @param configDTO
     * @param uri
     * @param requestJson
     * @param version
     * @param method
     * @param otherParams 数据来源：WR20等
     * @return
     */
    private static HuaweiRequestObject createRequestObject(HuaweiConfigDTO configDTO, String uri, JSONObject requestJson, JSONArray requestJsonArray, String version, String method, JSONObject otherParams) {


        String uid = UUID.randomUUID().toString().replaceAll("-", "");
//        String uid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 19);

        //针对post指令，设置msgId
        if ((requestJson != null || requestJsonArray != null) && HuaweiMethodConstant.POST.equalsIgnoreCase(method)) {
            requestJson.put("msgId", uid);
        }

        HuaweiRequestObject obj = new HuaweiRequestObject();
        obj.setMethod(method);
        obj.setUri(uri);
        obj.setVersion(version);
        obj.setRequestJson(requestJson);
        obj.setRequestJsonArray(requestJsonArray);
        obj.setMessageId(uid);
        obj.setConfig(configDTO);
        obj.setProjectId(ProjectContextHolder.getProjectId());

        if (otherParams != null) {
            if (StringUtils.isNotEmpty(otherParams.getString("source"))) {
                obj.setSource(otherParams.getString("source"));
            }

            if (StringUtils.isNotEmpty(otherParams.getString("sourceVersion"))) {
                obj.setSourceVersion(otherParams.getString("sourceVersion"));
            }

            if (StringUtils.isNotEmpty(otherParams.getString("wr20DeviceType"))) {
                obj.setWr20DeviceType(otherParams.getString("wr20DeviceType"));
            }

            if (StringUtils.isNotEmpty(otherParams.getString("uuid"))) {
                obj.setUuid(otherParams.getString("uuid"));
            }
        }

        //配置到缓存中
        RedisUtil.set(HuaweiCacheConstant.HUAWEI_REQUEST_PER + uid, JSON.toJSONString(obj), HuaweiCacheConstant.HUAWEI_REQUEST_MSG_TTL);

        return obj;
    }


//    /**
//     * 推送连接
//     *
//     * @param url         要推送的地址
//     * @param requestBody 要推送的内容
//     * @return
//     */
//    public static JSONObject post(String url, JSONObject requestBody, String sign) {
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        headers.add("Authorization", sign);
//
//        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
//        log.info("send msg to huawei middle platform:{}", requestBody);
//        ResponseEntity<String> result = null;
//
//        try {
//            result = restTemplate.postForEntity(url, requestEntity, String.class);
//
//            //判断返回状态
////            if (result.getStatusCode() == HttpStatus.OK) {
////            } else if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
////                log.error("unauthorized huawei middle platform, reloging.....");//未授权
////            } else {
////                log.error("Fail to send msg to huawei middle platform");//网络等原因
////            }
//
//
//            JSONObject json = JSONObject.parseObject(result.getBody());
//            log.info("resopnse: {}", json);
//
//            return json;
//        } catch (HttpClientErrorException e) {
//
//            //未授权，进行重授权机制
//            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
//                //调用授权服务
//
//                //验证登录锁
//
//                //请求回归
//
//            } else {
//
//            }
//
//
//            return null;
//        }
//
////        ResponseEntity<String> result = restTemplate.exchange(url,HttpMethod.POST,requestEntity, String.class);
//
//
//    }

//    /**
//     * 推送连接
//     *
//     * @param url   要查询的地址
//     * @param param 要查询的内容
//     * @return
//     */
//    public static JSONObject get(String url, String param) {
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        log.info("send msg to huawei middle platform:{}", param);
//
//        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
//
//        //判断返回状态
//        if (result.getStatusCode() == HttpStatus.OK) {
//        } else if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//            log.error("unauthorized huawei middle platform, reloging.....");//未授权
//        } else {
//            log.error("Fail to send msg to huawei middle platform");//网络等原因
//        }
//        JSONObject json = JSONObject.parseObject(result.getBody());
//        log.info("resopnse: {}", json);
//
//        return json;
//    }

//    public static void main(String[] args) {
//        JSONObject json = new JSONObject();
//        json.put("devId", "5ebb8cf6d4152802c59b9708_7000ABF6-98D1-4B7E-91E3-EFB9981C8FEF_000000002");
//        json.put("msgId", "12312312");
//
//        JSONObject dataJson = new JSONObject();
//        dataJson.put("serviceId", "DeviceControl");
//        dataJson.put("commandName", "OpenDoorAction");
//
//        json.put("commandInfo", dataJson);
//
//        String token = "2664eeb2-77e2-4a1b-b4a9-256ca03a13f0";
//        String appKey = "HVzOtlZN9iWoCuQV";
//        String appsecret = "Muppm50raHPoSsocGpSXgZuHUpEaTTw1";
//
//        String sign = HuaweiUtil.signGenerator(appKey, appsecret, token);
//
//
//        String url = "http://220.250.30.50:11111/v1/gwiot/api/device/5ebb8cf6d4152802c59b9708_7000ABF6-98D1-4B7E-91E3-EFB9981C8FEF_000000002/commands/down";
//
//        JSONObject result = HuaweiUtil.post(url, json, sign);
//        System.out.println(result);
//
//    }


}
