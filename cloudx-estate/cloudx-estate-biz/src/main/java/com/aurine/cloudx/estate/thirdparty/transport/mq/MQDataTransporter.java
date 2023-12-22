package com.aurine.cloudx.estate.thirdparty.transport.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aurine.cloudx.common.core.exception.ThirdPartyServiceErrorEnum;
import com.aurine.cloudx.common.core.exception.ThirdPartyServiceException;
import com.aurine.cloudx.estate.thirdparty.transport.config.TransportConfig;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;

/**
 * MQ数据传输器
 * 用于统一处理基于MQ的异步传输、异步转同步传输操作
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-15
 * @Copyright:
 */
@Component
@Slf4j
public class MQDataTransporter<V, T> {
    @Resource
    private TransportConfig transportConfig;


    /**
     * 同步请求方法
     * 在redis中定义请求和响应数据，当监听到响应数据出现时，认为请求连接完成，返回响应数据。如请求数据超时消失，则认为请求超时。
     * 因此，requestObj对象中应包含requestKey和responseKey信息
     *
     * @param topic       请求主题
     * @param requestKey  请求id
     * @param responseKey 返回值id
     * @param requestObj  请求对象
     * @param returnClass 返回值类型
     * @return
     */
    public <T> T syncRequest(String topic, String requestKey, String responseKey, V requestObj, Class<T> returnClass) {


        //发送请求到MQT
        KafkaProducer.sendMessage(topic, requestObj);

        log.info("Kafka同步推送 请求已推送到主题：{} 内容:{}", topic, requestObj);

        //缓存暂存

        RedisUtil.set(requestKey, JSONObject.toJSONString(requestObj), transportConfig.timeout);


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
            throw new ThirdPartyServiceException(ThirdPartyServiceErrorEnum.OUT_OF_TIME);
        } else {
            String jsonObjectStr = RedisUtil.get(responseKey).toString();
            T result =  JSON.parseObject(jsonObjectStr,returnClass);
            log.info("Kafka同步推送 获取返回值：{}", result);
            return result;
        }
    }

    /**
     * 异步请求方法
     *
     * @param topic
     * @param requestObj
     * @return
     */
    public T asyncRequest(String topic, V requestObj) {

        //发送请求到MQT
        KafkaProducer.sendMessage(topic, requestObj);

        //缓存暂存
        String key = "";
        String resultKey = "";
        Long ttl = 30L;
        RedisUtil.set(key, requestObj, ttl);

        return null;
    }

}
