package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.EdgeCascadeRequestSlave;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestMasterService;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestSlaveService;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>边缘网关级联申请控制器</p>
 *
 * @author : 王良俊
 * @date : 2021-12-02 17:34:59
 */
@RestController
@RequestMapping("/edgeCascadeRequestSlave")
public class EdgeCascadeRequestSlaveController {

    @Resource
    EdgeCascadeRequestSlaveService edgeCascadeRequestSlaveService;

    @ApiOperation(value = "申请级联", notes = "申请级联")
    @PostMapping("/requestCascade")
    public R requestCascade(@RequestBody EdgeCascadeRequestSlave request) {
        String key = "requestCascade" + request.getParentEdgeIp() + request.getConnectionCode();
        if (RedisUtil.hasKey(key)) {
            return R.failed("请勿频繁提交级联申请");
        } else {
            RedisUtil.set(key, "1", 5);
        }
        return edgeCascadeRequestSlaveService.requestCascade(request);
    }

    @ApiOperation(value = "撤销级联申请", notes = "撤销级联申请")
    @GetMapping("/revokeRequest/{requestId}")
    public R<String> revokeRequest(@PathVariable String requestId) {
        return edgeCascadeRequestSlaveService.revokeRequest(requestId);
    }

    @ApiOperation(value = "申请解绑边缘网关", notes = "申请解绑边缘网关")
    @GetMapping("/requestUnbind/{requestId}")
    public R<String> requestUnbind(@PathVariable String requestId) {
        return edgeCascadeRequestSlaveService.requestUnbind(requestId);
    }


}
