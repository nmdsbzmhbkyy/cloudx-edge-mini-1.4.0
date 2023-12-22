package com.aurine.cloudx.edge.sync.biz.controller;

import com.aurine.cloudx.edge.sync.biz.service.biz.DealRequestService;
import com.aurine.cloudx.edge.sync.biz.service.biz.DealResponseService;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/01/20 21:48
 * @Package: com.aurine.cloudx.edge.sync.biz.controller
 * @Version: 1.0
 * @Remarks:
 **/
@RestController
@RequestMapping("/edge/sync/receive")
public class TestController {

    @Resource
    private DealRequestService dealRequestService;

    @Resource
    private DealResponseService dealResponseService;

    /**
     * 收到mqtt请求事件处理
     * @param requestObj
     * @return
     */
    @PostMapping(value = "/mqttRequest")
    public void mqttRequest(@RequestBody TaskInfoDto requestObj) {
        dealRequestService.dealRequest(requestObj);
    }

    /**
     * 收到mqtt响应事件处理
     * @param requestObj
     * @return
     */
    @PostMapping(value = "/mqttResponse")
    public void mqttResponse(@RequestBody TaskInfoDto requestObj) {
        dealResponseService.dealResponse(requestObj);
    }
}
