package com.aurine.cloudx.estate.thirdparty.module.wr20.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20PersonCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20TopicConstant;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * WR20 住户信息 消费者
 *
 * @ClassName: WR20PersonConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-07 14:46
 * @Copyright:
 */
@Component
@Slf4j
public class WR20PersonConsumer {
    @Resource
    private WR20PersonCallbackService wr20PersonCallbackService;

    //    @KafkaListener(groupId = "THIRD_GROUP", topics = WR20TopicConstant.WR20_PERSON, errorHandler = "")
    @KafkaListener(topics = WR20TopicConstant.WR20_PERSON, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("WR20 住户消费者 接收到信息：{}", message);
        dispatch(message);

    }

    void dispatch(String message) {
        try {

            JSONObject requestJson = JSONObject.parseObject(message);
            JSONObject onNotifyDataJson = requestJson.getJSONObject("onNotifyData");

            String msgId = onNotifyDataJson.getString("msgId");
            JSONObject objManagerData = onNotifyDataJson.getJSONObject("objManagerData");
            JSONObject objInfoData = objManagerData.getJSONObject("objInfo");

            HuaweiRequestObject requestObject = JSONObject.parseObject(RedisUtil.get(msgId).toString(), HuaweiRequestObject.class);

            //根据方法和类型，分类主题
            JSONObject actionInfoObj = requestObject.getRequestJson().getJSONObject("actionInfo");
            String serviceId = actionInfoObj.getString("serviceId");
            String action = actionInfoObj.getString("action");


            switch (action) {
                case "ADD":
//                    wr20PersonCallbackService.addPerson(objInfoData, requestObject.getUid());
                    break;

                default:
                    //抛弃处理
                    break;
            }

        } catch (Exception e) {
            //JSON解析失败，抛弃处理
        }
    }
}
