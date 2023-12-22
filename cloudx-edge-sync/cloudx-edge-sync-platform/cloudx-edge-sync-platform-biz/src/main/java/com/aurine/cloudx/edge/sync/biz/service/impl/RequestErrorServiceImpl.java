package com.aurine.cloudx.edge.sync.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.service.RequestErrorService;
import com.aurine.cloudx.edge.sync.biz.transfer.mqtt.MqttConnector;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/3/8 14:26
 * @Version: 1.0
 * @Remarks:
 **/
@Service
@Slf4j
public class RequestErrorServiceImpl implements RequestErrorService {

    @Resource
    private MqttConnector mqttConnector;

    /**
     * R<JSONObject>类型数据
     */
    @Override
    public void dealRequestError(TaskInfoDto taskInfoDto, R<JSONObject> message) {
        publishRequestError(taskInfoDto, JSONObject.toJSONString(message));
    }
    /**
     * String类型数据
     */
    @Override
    public void dealRequestError(TaskInfoDto taskInfoDto, String message, Boolean success) {
        if (success) {
            publishRequestError(taskInfoDto, JSONObject.toJSONString(R.ok(message)));
        } else {
            publishRequestError(taskInfoDto, JSONObject.toJSONString(R.failed(message)));
        }
    }

    /**
     * 异常信息处理，默认类型failed
     */
    @Override
    public void dealRequestError(TaskInfoDto taskInfoDto, String message) {
        publishRequestError(taskInfoDto, JSONObject.toJSONString(R.failed(message)));
    }

    /**
     * 异常信息保存为errorMsg，发出mqtt响应返回异常
     */
    private void publishRequestError(TaskInfoDto taskInfoDto, String message) {
        log.info("走到了异常处理器");
        taskInfoDto.setErrMsg(message);
        mqttConnector.publishResponseMessage(taskInfoDto);
        log.error("responseMessage ={} ", JSONObject.toJSONString(taskInfoDto));
    }

    @Override
    public void dealRequestError(TaskInfoDto taskInfoDto, CloudxOpenErrorEnum cloudxOpenErrorEnum) {
        publishRequestError(taskInfoDto, JSONObject.toJSONString(Result.fail(taskInfoDto,cloudxOpenErrorEnum)));
    }
}
