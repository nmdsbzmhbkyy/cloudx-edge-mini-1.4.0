package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.CallBackURIConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.HuaweiCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.factory.HuaweiCallbackServiceFactory;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service.AliEdgeCallbackDeviceService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service.AliEdgeCallbackParkingService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service.AliEdgeCallbackPerimeterAlarmService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.checker.AbstractChecker;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.checker.AliEdgeEventParkingDTOChecker;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.checker.AliEdgeEventPerimeterAlarmDTOChecker;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.dto.AliEdgeEventDTO;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.dto.AliEdgeEventParkingDTO;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.util.AliEdgeDTOUtil;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>阿里边缘 回调API</p>
 * 目前阿里边缘回调接口没有版本，暂定统一均为V1版
 *
 * @ClassName: AliEdgeCallbackController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 15:43
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping(CallBackURIConstant.EDGE_ALI)
@Slf4j
@Api(value = "AliEdgeCallbackController", tags = "[阿里边缘]回调API")
public class AliEdgeCallbackController {
    private static final String SN = "ALI_EDGE";

    @Resource
    private AliEdgeCallbackParkingService aliEdgeCallbackParkingService;

    @Resource
    private AliEdgeCallbackPerimeterAlarmService aliEdgeCallbackPerimeterAlarmService;
    @Resource
    private AliEdgeCallbackDeviceService aliEdgeCallbackDeviceService;


    /**
     * 事件 - 阿里边缘 -  事件
     * 新增设备等操作
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/event/report")
    public JSONObject event(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) {
        log.info("[阿里边缘] - 接收到 事件通知：{}", json);
//        HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceActive(json);

        //解析事件类型
        AliEdgeEventDTO eventDTO = JSONObject.parseObject(json.toJSONString(), AliEdgeEventDTO.class);
        JSONObject dataJson = eventDTO.getOnNotifyData();

        //分发调用
        if (check(dataJson, new AliEdgeEventPerimeterAlarmDTOChecker())) {
            aliEdgeCallbackPerimeterAlarmService.channelAlarm(dataJson);
        } else if (check(dataJson, new AliEdgeEventParkingDTOChecker())) {
            aliEdgeCallbackParkingService.dispatch(dataJson);
        } else {
            log.info("[阿里边缘] - 事件通知 未知的数据结构：{}", json);
        }


        return returnSuccess();
    }

    /**
     * 事件 - 啊里边缘 -  设备状态变化通知
     * 新增设备等操作
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/device/status/update")
    public JSONObject status(@RequestParam(value = "sn") String sn, @RequestParam(value = "ver") String ver, @RequestBody JSONObject json) {
        log.info("[阿里边缘] - 接收到 状态变化事件通知：{}", json);
        //设备状态事件转发给3.0统一处理
        AliEdgeEventDTO eventDTO = JSONObject.parseObject(json.toJSONString(), AliEdgeEventDTO.class);
        JSONObject dataJson = eventDTO.getOnNotifyData();

        aliEdgeCallbackDeviceService.deviceStatusUpdate(dataJson);
//        HuaweiCallbackServiceFactory.getInstance(getVersion(ver)).deviceActive(json);
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
        VersionEnum versionEnum = VersionEnum.getByCode(ver);
        return versionEnum == null ? VersionEnum.V1 : versionEnum;


    }

    private boolean check(JSONObject jsonObject, AbstractChecker checker) {
        return checker.check(jsonObject);
    }

}
