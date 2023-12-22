package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.core;

import cn.hutool.core.net.URLDecoder;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.config.DongdongConfig;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.core.util.DongDongUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-13
 * @Copyright:
 */
@Slf4j
@Component
public class DongdongDataConnector {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    /**
     * 推送连接
     *
     * @param method   要推送的方法
     * @param paramMap 要推送的内容
     * @return
     */

    public JSONObject post(String method, Map<String, String> paramMap) {
        String url = DongdongConfig.baseUrl;

        //添加对接参数
        paramMap.put("apikey", DongdongConfig.appKey);
        paramMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        //添加sign信息
        try {
            paramMap.put("sign", DongDongUtil.getSign(method, DongdongConfig.baseUrl, DongdongConfig.secretKey, paramMap));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", DongdongConfig.CONTENT_TYPE);
        headers.set("User-Agent", DongdongConfig.USER_AGENT);

//        log.info("[咚咚云对讲] 发送信息 Head:  Content-Type= {} ,User-Agent = {}", DongdongConfig.CONTENT_TYPE, DongdongConfig.USER_AGENT);
        HttpEntity<Object> requestEntity = null;
        try {
            requestEntity = new HttpEntity<>(this.getMessage(paramMap), headers);
            log.info("[咚咚云对讲] 发送信息（加密前） 方法：POST，地址：{}， 数据：{}", url, paramMap);
        } catch (UnsupportedEncodingException e) {
            log.error("[咚咚云对讲] 参数转换错误 方法：POST，地址：{}， 数据：{}", url, paramMap);
            e.printStackTrace();
            throw new RuntimeException("云对讲参数转换错误");
        }

        try {
            //执行对接业务

            ResponseEntity<String> result = restTemplate.postForEntity(url, requestEntity, String.class);
//            ResponseEntity<String> result = restTemplate.postForLocation(url, String.class);

            //判断返回状态
            if (result.getStatusCode() == HttpStatus.OK) {
//                log.info(result.toString());
            } else {
                log.error("咚咚云对讲： {}", "网络连接失败");//网络不通等原因
            }
            JSONObject json = JSONObject.parseObject(DongDongUtil.unicodeToString(URLDecoder.decode(result.getBody(), Charset.forName("UTF-8"))));

            log.info("[咚咚云对讲] 返回结果： {}", json);


            return json;


        } catch (HttpClientErrorException e) {
            //未授权，进行重授权机制
            if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("咚咚云对讲： {}", "授权无效");
            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("咚咚云对讲：  {}", "接口地址错误：" + url);
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("咚咚云对讲： 异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
            return null;
        } catch (ResourceAccessException rae) {
            log.error("咚咚云对讲： 连接超时:{}", rae.getMessage());
            throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * 将map参数凭借为 utf-8 编码格式 url query数据
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getMessage(Map<String, String> params) throws UnsupportedEncodingException {

        List<String> keys = new ArrayList<String>(params.keySet());

        String query_string = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = URLEncoder.encode(keys.get(i), "UTF-8");
            String value = URLEncoder.encode(params.get(key) + "", "UTF-8");

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                query_string = query_string + key + "=" + value;
            } else {
                query_string = query_string + key + "=" + value + "&";
            }
        }

        return query_string;
    }
}
