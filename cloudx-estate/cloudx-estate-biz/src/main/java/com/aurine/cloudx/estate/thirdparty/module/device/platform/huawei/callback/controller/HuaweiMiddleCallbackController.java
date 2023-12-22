package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.controller;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.CallBackURIConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.HuaweiCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.factory.HuaweiCallbackServiceFactory;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * <p>华为中台 回调API</p>
 * 目前华为中台回调接口没有版本，暂定统一均为V1版
 *
 * @ClassName: HuaweiMiddleCallbackController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 15:43
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping(CallBackURIConstant.DEVICE_MIDDLE_HUAWEI)
@Slf4j
@Api(value = "HuaweiMiddleCallbackController", tags = "[华为中台]回调API")
public class HuaweiMiddleCallbackController {


    private static final String SN = "HUAWEI_MIDDLE";
    @Resource
    private HuaweiCallbackService huaweiCallbackService;


    /**
     * 事件 - 华为中台 -  设备激活通知
     * 新增设备等操作
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/active")
    public JSONObject deviceActive(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) {
        log.info("[华为中台] - 接收到 设备激活通知：{}", json);
        HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceActive(json);
        return returnSuccess();
    }

    /**
     * 事件 - 华为中台 -  设备状态变化通知
     * 设备上下线等通知
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/status/update")
    public JSONObject deviceStatusUpdate(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) {
        log.info("[华为中台] - 接收到 设备状态变化通知：{}", json);
        HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceStatusUpdate(json);
        return returnSuccess();
    }

    /**
     * 事件 - 华为中台 -  设备属性变化通知
     * 设备IP变更等操作
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/data/update")
    public JSONObject deviceDataUpdate(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) throws InterruptedException {
        //延后执行
//        Thread.sleep(900);
        TaskUtil.hashedWheelTimer.newTimeout(timeout -> {
            log.info("[华为中台] - 接收到 设备属性数据 - 回调数据：{}", json);
            HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceDataUpdate(json);
        }, 900, TimeUnit.MILLISECONDS);
        return returnSuccess();
    }


    /**
     * 响应 - 华为中台 - 设备对象 - 响应
     * 设备的业务数据响应，如WR20数据响应、直连设备添加卡片返回第三方ID响应
     * 该方法通常为某个操作的异步响应，因此需要校验msgId在系统缓存中是否存在
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/objmanager/report")
    public JSONObject deviceObjManagerReport(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) throws InterruptedException {
//        Thread.sleep(1000);
        TaskUtil.hashedWheelTimer.newTimeout(timeout -> {
            log.info("[华为中台] - 接收到设备对象 - 回调数据：{}", json);
            JSONObject onNotifyDataJson = json.getJSONObject("onNotifyData");
            String msgId = onNotifyDataJson.getString("msgId");
            HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceObjManagerReport(json);
        }, 1, TimeUnit.SECONDS);

        //校验是否为系统提交的异步响应
//        if (RedisUtil.hasKey(msgId)) {
//        }
        return returnSuccess();
    }

    /**
     * 响应 - 华为中台 -  设备操作指令下发响应
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/command/response")
    public JSONObject deviceCommandResponse(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) {
        log.info("[华为中台] - 接收到下发指令响应 -  回调数据：{}", json);
        HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceCommandResponse(json);
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
    public JSONObject eventDeviceNotice(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) throws InterruptedException {
//        Thread.sleep(2000);
        TaskUtil.hashedWheelTimer.newTimeout(timeout -> {
            log.info("[华为中台] - 接收到设备事件 - 回调数据：{}", json);
            TenantContextHolder.setTenantId(1);
            HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceEventReport(json);
        }, 2, TimeUnit.SECONDS);
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
     * @param ver
     * @return
     */
    public VersionEnum getVersion(String ver) {
//        String productId = json.getString("productId");//产品类型
//        String deviceId = json.getString("devId");//
//        String gatewayId = json.getString("gatewayId");
        VersionEnum versionEnum = VersionEnum.getByCode(ver);
        return versionEnum == null ? VersionEnum.V1 : versionEnum;


    }

}
