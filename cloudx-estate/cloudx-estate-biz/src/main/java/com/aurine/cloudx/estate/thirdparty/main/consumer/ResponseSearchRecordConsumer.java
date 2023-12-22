package com.aurine.cloudx.estate.thirdparty.main.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseSearchRecordConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseSearchRecordDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.DeviceService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;

/**
 * <p>查询结果消费者</p>
 * @ClassName: ResponseSearchRecordConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-01 16:03
 * @Copyright:
 */
@Slf4j
public class ResponseSearchRecordConsumer {
    @Resource
    private DeviceService deviceService;


//    @KafkaListener(groupId = "THIRD_GROUP", topics = TopicConstant.SDI_RESPONSE_SEARCH_RECORD, errorHandler = "")
    @KafkaListener(topics = TopicConstant.SDI_RESPONSE_SEARCH_RECORD, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("查询结果响应 统一接口 接口接收到数据：{}", message);
        dispatch(message);
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        try {
            ResponseSearchRecordDTO recordDTO = JSONObject.parseObject(message, ResponseSearchRecordDTO.class);

            switch (recordDTO.getAction()) {
                case ResponseSearchRecordConstant.ACTION_SYNC_GATE:
                    deviceService.syncDevice(recordDTO);
                    break;

                default:
                    //其他类型
//                    eventService.passGateOther(gatePassDto);
                    break;
            }

        } catch (Exception e) {
            //JSON解析失败，抛弃处理
        }
    }
}
