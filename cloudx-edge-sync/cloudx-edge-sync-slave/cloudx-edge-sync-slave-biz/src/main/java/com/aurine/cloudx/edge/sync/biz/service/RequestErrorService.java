package com.aurine.cloudx.edge.sync.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @Author: wrm
 * @Date: 2022/03/09 17:24
 * @Package: com.aurine.cloudx.edge.sync.biz.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface RequestErrorService {

    /**
     * R<JSONObject>类型 message
     * @param taskInfoDto 返回taskInfo对象
     * @param message 消息内容
     */
    void dealRequestError(TaskInfoDto taskInfoDto, R<JSONObject> message);

    /**
     * String类型 message
     *
     * @param taskInfoDto 返回taskInfo对象
     * @param message 消息内容
     * @param success 返回类型，成功失败用于response处理
     */
    void dealRequestError(TaskInfoDto taskInfoDto, String message, Boolean success);

    /**
     * 异常信息处理，默认类型failed
     * String类型 message
     * @param taskInfoDto 返回taskInfo对象
     * @param message 消息内容
     */
    void dealRequestError(TaskInfoDto taskInfoDto, String message);

    void dealRequestError(TaskInfoDto taskInfoDto, CloudxOpenErrorEnum cloudxOpenErrorEnum);
}
