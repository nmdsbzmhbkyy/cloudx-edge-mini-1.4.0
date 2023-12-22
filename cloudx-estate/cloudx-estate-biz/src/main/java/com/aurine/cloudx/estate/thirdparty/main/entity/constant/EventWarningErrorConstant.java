package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

import lombok.Data;

/**
 * @description: 异常告警
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */

@Data
public class EventWarningErrorConstant  {


    /**
     * 异常事件
     */
    public static final String ACTION_ERROR = "ERROR";
    /**
     * 周界异常事件
     */
    public static final String ACTION_CHANNEL_ALARM = "CHANNEL_ALARM";
}
