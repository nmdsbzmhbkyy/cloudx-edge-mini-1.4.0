

package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Async;

/**
 * @ClassName: AliEdgeEventPerimeterAlarmService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22 17:09
 * @Copyright:
 */
public interface AliEdgeCallbackPerimeterAlarmService {


    /**
     * 告警事件
     *
     * @param jsonObject
     * @return
     */
    @Async
    void channelAlarm(JSONObject jsonObject);



}
