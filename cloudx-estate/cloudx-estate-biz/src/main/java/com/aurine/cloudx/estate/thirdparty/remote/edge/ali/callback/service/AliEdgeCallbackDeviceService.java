

package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Async;


/**
 * 华为中天 回调函数 数据清洗
 *
 * @ClassName: AliEdgeCallbackDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-23 15:53
 * @Copyright:
 */
public interface AliEdgeCallbackDeviceService {

//    /**
//     * 设备激活通知 数据清洗
//     *
//     * @param requestJson
//     * @return
//     */
//    @Async
//    void deviceActive(JSONObject requestJson);

    /**
     * 设备状态变化通知 数据清洗
     *
     * @param requestJson
     */
    @Async
    void deviceStatusUpdate(JSONObject requestJson);

//    /**
//     * 设备属性变化通知 数据清洗
//     *
//     * @param requestJson
//     */
//    @Async
//    void deviceDataUpdate(JSONObject requestJson);
//
//    /**
//     * 设备对象管理信息回应 数据清洗
//     *
//     * @param requestJson
//     */
//    @Async
//    void deviceObjManagerReport(JSONObject requestJson);
//
//    /**
//     * 设备操作指令的响应 数据清洗
//     *
//     * @param requestJson
//     */
//    @Async
//    void deviceCommandResponse(JSONObject requestJson);
//
//    /**
//     * 设备事件上报 数据清洗
//     *
//     * @param requestJson
//     */
//    @Async
//    void deviceEventReport(JSONObject requestJson);

}
