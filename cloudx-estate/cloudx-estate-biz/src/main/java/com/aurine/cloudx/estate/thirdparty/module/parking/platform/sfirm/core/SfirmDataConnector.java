package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.core;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * <p>赛菲姆 数据连接 核心类</p>
 *
 * @ClassName: SfirmDataConnector
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-25 14:11
 * @Copyright:
 */
@Component
@Slf4j
public class SfirmDataConnector {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;


    /**
     * 推送连接
     *
     * @param url         要推送的地址
     * @param requestBody 要推送的内容
     * @return
     */
    public JSONObject post(String url, JSONObject requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info("给赛菲姆平台发送信息 方法：POST，地址：{}， 数据：{}", url, requestBody);


        try {
            //执行对接业务
            ResponseEntity<String> result = restTemplate.postForEntity(url, requestEntity, String.class);

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
                log.error("赛菲姆车场： {}", "授权无效");
            } else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("赛菲姆车场：  {}", "接口地址错误：" + url);
                throw new RuntimeException(ErrorMessageConstant.ERROR_URL_WRONG);
            } else {
                log.error("赛菲姆车场： 异常代码：{} 发生异常：{}",  e.getRawStatusCode(), e.getMessage());
                throw new RuntimeException(ErrorMessageConstant.ERROR_OTHER + e.getRawStatusCode());
            }
            return null;
        } catch (ResourceAccessException rae) {
            log.error("赛菲姆车场： 连接超时:{}",  rae.getMessage());
            throw new RuntimeException(ErrorMessageConstant.ERROR_IO_TIME_OUT);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }



    }


}
