package com.aurine.cloudx.edge.sync.biz.controller;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.biz.OpenApiPushService;
import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2021/12/14 14:34
 * @Package: com.aurine.cloudx.open.controller
 * @Version: 1.0
 * @Remarks:
 **/
@RestController
@RequestMapping("/edge/sync/receive")
@Slf4j
public class OpenApiPushController {
    @Resource
    private OpenApiPushService cloudxOpenApiService;


    /**
     * 处理config订阅收到消息内容
     */
    @PostMapping(value = "/config")
    public R dealConfig(@RequestBody OpenApiEntity requestObj) {
        // 判断是否为需要处理的消息请求
        if (!requestObj.getData().containsKey(Constants.REQUEST_STATUS_NAME)) {
            return R.ok();
        }
        log.info("callback dealConfig, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
        return R.ok(cloudxOpenApiService.dealConfigRequest(requestObj));
    }

    /**
     * 处理事件
     *
     * @param requestObj
     * @return
     */
    @PostMapping(value = "/event")
    public R dealEvent(@RequestBody OpenApiEntity requestObj) {
        log.info("callback dealEvent, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
        return R.ok(cloudxOpenApiService.dealEventRequest(requestObj));
    }

    @PostMapping(value = "/operate")
    public R dealOperate(@RequestBody OpenApiEntity requestObj) {
        log.info("callback dealOperate, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
        return R.ok(cloudxOpenApiService.dealOperateRequest(requestObj));
    }

    @PostMapping(value = "/command")
    public R dealCommand(@RequestBody OpenApiEntity requestObj) {
        log.info("callback dealCommand, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
        return R.ok(cloudxOpenApiService.dealCommandRequest(requestObj));
    }

    @PostMapping(value = "/cascade")
    public R dealCascade(@RequestBody OpenApiEntity requestObj) {
        log.info("callback dealCascade, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
        return R.ok(cloudxOpenApiService.dealCascadeRequest(requestObj));
    }

    @PostMapping(value = "/other")
    public R dealOther(@RequestBody OpenApiEntity requestObj) {
        log.info("callback dealOther, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
        return R.ok(cloudxOpenApiService.dealOtherRequest(requestObj));
    }
}
