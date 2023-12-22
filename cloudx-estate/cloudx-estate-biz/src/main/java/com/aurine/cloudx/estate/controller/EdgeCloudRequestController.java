package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.EdgeCloudRequestService;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>边缘网关入云申请控制器</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:34:59
 */
@RestController
@RequestMapping("/edgeCloudRequest")
public class EdgeCloudRequestController {

    @Resource
    EdgeCloudRequestService edgeCloudRequestService;

    @PostMapping("/requestIntoCloud")
    public R requestIntoCloud(@RequestBody RequestInfo requestInfo) {
        String key = "requestIntoCloud_" + requestInfo.getProjectId() + requestInfo.getConnectCode();
        if (RedisUtil.hasKey(key)) {
            return R.failed("请勿频繁提交入云申请");
        } else {
            RedisUtil.set(key, "1", 5);
        }
        return edgeCloudRequestService.requestIntoCloud(requestInfo.getSyncType(), requestInfo.getConnectCode(), requestInfo.getProjectId());
    }

    @GetMapping("/revokeIntoCloudRequest/{requestId}")
    public R revokeIntoCloudRequest(@PathVariable String requestId) {
        return R.ok(edgeCloudRequestService.revokeIntoCloudRequest(requestId));
    }

    @GetMapping("/revokeUnbindRequest/{requestId}")
    public R revokeUnbindRequest(@PathVariable String requestId) {
        return R.ok(edgeCloudRequestService.revokeUnbindRequest(requestId));
    }

    @GetMapping("/requestUnbind/{requestId}")
    public R requestUnbind(@PathVariable String requestId) {
        return edgeCloudRequestService.requestUnbind(requestId);
    }

    /**
     * <p>入云申请</p>
     * @author : 王良俊
     * @date : 2021-12-17 14:50:24
     */
    @Data
    public static class RequestInfo {

        /**
         * 项目ID
         */
        private Integer projectId;

        /**
         * 入云码
         */
        private String connectCode;

        /**
         * 同步方式
         */
        private Character syncType;

        public RequestInfo() {
        }

        public RequestInfo(Integer projectId, String connectCode, Character syncType) {
            this.projectId = projectId;
            this.connectCode = connectCode;
            this.syncType = syncType;
        }
    }

}
