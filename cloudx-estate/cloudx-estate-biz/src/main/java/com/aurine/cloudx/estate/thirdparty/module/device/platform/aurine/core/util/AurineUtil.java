package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineMethodConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * 冠林中台连接工具类
 *
 * @ClassName: AurineUtil
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-11 14:50
 * @Copyright:
 */
@Slf4j
public class AurineUtil {


    private String appKey;
    private String appSecret;
    private String appType;
    public static final String HEAD_NAME = "Authorization";

    public String makeAuthorization() {
//        String time = String.valueOf(new Date().getTime());
        String time = String.valueOf(System.currentTimeMillis());
        String verify = SecureUtil.md5(time + "|" + this.appType + "|" + this.appSecret);
        String token = "";
        String base64 = this.appKey + ":" + time + ":" + verify + ":" + token + ":" + this.appType;
        return "Base64 " + cn.hutool.core.codec.Base64.encode(base64, "UTF-8");
    }

    /**
     * Authorization生成
     * 生成规则：
     * ，存 放的Basic认证内容包括所有接口的5个公共参数
     * appId、timestamp （当前unix时间戳）、verify（请求内容校验字符串）、token（访问token）、 type（参见附录” 接口请求端类型”）。
     * 内容格式为： “appId:timestamp:verify:token:type”，各个参数位置固定不变，参 数间用冒号“:”隔开，
     * 某项参数不传时对应位置留空（如 “appId:timestamp:verify::type”）,
     * verify生成规则为MD5(MD5（GET 请求参数串）+“|”+timestamp+“|”+type+“|”+appSecret)。
     * <p>
     * 如 下： Authorization: Basic base64(10001:1408430177:verify 验证 串::3)
     *
     * @param appkey    AppKey
     * @param appSecret 授权码
     * @return
     */
    @SneakyThrows
    public static String signGenerator(String appkey, String appType, String appSecret, String token) {
//        log.info("生成sign，appKey:{},appType:{},appSecret:{},token:{}", appkey, appType, appSecret, token);
        if (StringUtils.isEmpty(appkey) || StringUtils.isEmpty(appType) || StringUtils.isEmpty(appSecret)) {
            throw new RuntimeException("连接参数缺失，请检查对接配置");
        }
        String timestamp = String.valueOf(System.currentTimeMillis());

        String verify = SecureUtil.md5(timestamp + "|" + appType + "|" + appSecret);
        String base64 = appkey + ":" + timestamp + ":" + verify + ":" + token + ":" + appType;
        return "Base64 " + cn.hutool.core.codec.Base64.encode(base64, "UTF-8");
    }


    /**
     * 创建request对象
     *
     * @param config
     * @param uri
     * @param requestJson
     * @param version
     * @param dataType    MediaType.MULTIPART_FORM_DATA_VALUE/MediaType.APPLICATION_JSON
     * @return
     */
    public static AurineRequestObject createPostRequestObject(AurineConfigDTO config, String uri, JSONObject requestJson, String version, String dataType) {
        return createRequestObject(config, uri, requestJson, version, AurineMethodConstant.POST, dataType);
    }

    /**
     * 创建 Get Request对象
     *
     * @param uri
     * @param requestJson
     * @param version
     * @return
     */
    public static AurineRequestObject createGetRequestObject(AurineConfigDTO config, String uri, JSONObject requestJson, String version, String dataType) {
        return createRequestObject(config, uri, requestJson, version, AurineMethodConstant.GET, dataType);
    }

    private static AurineRequestObject createRequestObject(AurineConfigDTO config, String uri, JSONObject requestJson, String version, String method, String dataType) {


        String uid = UUID.randomUUID().toString().replaceAll("-", "");

//        //针对post指令，设置msgId
//        if (requestJson != null && AurineMethodConstant.POST.equalsIgnoreCase(method)) {
//            requestJson.put("msgId", uid);
//        }

        AurineRequestObject obj = new AurineRequestObject();
        obj.setMethod(method);
        obj.setUri(uri);
        obj.setVersion(version);
        obj.setRequestJson(requestJson);
        obj.setMessageId(uid);
        obj.setConfig(config);
        obj.setDataType(dataType);
        return obj;
    }

    /**
     * 创建deviceVo
     *
     * @param deviceInfoProxyVo
     * @return
     */
    public static AurineDeviceVo createDeviceVo(ProjectDeviceInfoProxyVo deviceInfoProxyVo, String modelid) {
        AurineDeviceVo vo = new AurineDeviceVo();
        vo.setName(deviceInfoProxyVo.getDeviceName());
        vo.setMac(deviceInfoProxyVo.getMac());
        vo.setDevsn(deviceInfoProxyVo.getSn());
        vo.setModelid(modelid);
        vo.setCommunityId(deviceInfoProxyVo.getProjectId());

        return vo;
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
//        log.info("send msg to aurine middle platform:{}", requestBody);
//        ResponseEntity<String> result = null;
//
//        try {
//            result = restTemplate.postForEntity(url, requestEntity, String.class);
//
//            //判断返回状态
////            if (result.getStatusCode() == HttpStatus.OK) {
////            } else if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
////                log.error("unauthorized aurine middle platform, reloging.....");//未授权
////            } else {
////                log.error("Fail to send msg to aurine middle platform");//网络等原因
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
//        log.info("send msg to aurine middle platform:{}", param);
//
//        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
//
//        //判断返回状态
//        if (result.getStatusCode() == HttpStatus.OK) {
//        } else if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//            log.error("unauthorized aurine middle platform, reloging.....");//未授权
//        } else {
//            log.error("Fail to send msg to aurine middle platform");//网络等原因
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
//        String sign = AurineUtil.signGenerator(appKey, appsecret, token);
//
//
//        String url = "http://220.250.30.50:11111/v1/gwiot/api/device/5ebb8cf6d4152802c59b9708_7000ABF6-98D1-4B7E-91E3-EFB9981C8FEF_000000002/commands/down";
//
//        JSONObject result = AurineUtil.post(url, json, sign);
//        System.out.println(result);
//
//    }


}
