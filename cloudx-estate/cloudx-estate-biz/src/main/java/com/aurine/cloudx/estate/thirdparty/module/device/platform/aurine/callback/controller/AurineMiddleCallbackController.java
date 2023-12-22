package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.callback.controller;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.CallBackURIConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.callback.factory.AurineCallbackServiceFactory;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>冠林中台 回调API</p>
 * 目前冠林中台回调接口没有版本，暂定统一均为V1版
 *
 * @ClassName: AurineMiddleCallbackController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 15:43
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping(CallBackURIConstant.DEVICE_MIDDLE_AURINE)
@Slf4j
@Api(value = "AurineMiddleCallbackController", tags = "冠林中台回调API")
public class AurineMiddleCallbackController {



    /**
     * 事件 - 冠林中台 -  设备状态变化通知
     * 设备上下线等通知
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/status/update")
    public JSONObject deviceStatusUpdate(@RequestBody JSONObject json) {
//        log.info("冠林中台 - 接收到 设备状态变化通知：{}", json);
//        AurineCallbackServiceFactory.getInstance(getVersion(json)).deviceStatusUpdate(json);
        return returnSuccess();
    }



    /**
     * 响应 - 冠林中台 -  设备操作指令下发响应
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/command/response")
    public JSONObject deviceCommandResponse(@RequestBody JSONObject json) {
//        log.info("响应回调 - 冠林中台 - 设备操作指令下发响应 接收到数据：{}", json);
//        AurineCallbackServiceFactory.getInstance(getVersion(json)).deviceCommandResponse(json);
        return returnSuccess();
    }

    /**
     * 事件 - 设备事件 - 上报
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/event/report")
    public JSONObject eventDeviceNotice(@RequestBody JSONObject json, HttpServletRequest request) {
//        log.info("事件回调 - 冠林中台 - 设备事件 - 接收到数据：{}", json);
//        AurineCallbackServiceFactory.getInstance(getVersion(json)).deviceEventReport(json);
        return returnSuccess();
    }


    /**
     * 返回成功JSON数据
     *
     * @return
     */
    public JSONObject returnSuccess() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("errorCode", 0);
        resultJson.put("errorMsg", "操作成功");
        return resultJson;
    }


    /**
     * 获取处理的版本号
     * 中台回调接口无版本信息，默认给V1版
     *
     * @param json
     * @return
     */
    public VersionEnum getVersion(JSONObject json) {
//        String productId = json.getString("productId");//产品类型
//        String deviceId = json.getString("devId");//
//        String gatewayId = json.getString("gatewayId");

        return VersionEnum.V2_1;


    }

}
