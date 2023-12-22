package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.constant.EdgePushConstant;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: wrm
 * @Date: 2021/12/13 19:08
 * @Package: com.aurine.cloudx.estate.feign
 * @Version: 1.0
 * @Remarks:
 **/
@FeignClient(contextId = "CloudXRemoteOpenApiService", value = "cloudx-open-biz")
@RequestMapping(value = "/v1/push/send")
public interface RemoteOpenApiService {

    /**
     * 发送配置类消息
     *
     * @param openApiEntityRequest
     * @return true/false
     */
    @PostMapping("/config")
    R sendConfigMessage(OpenApiEntity openApiEntityRequest, @RequestHeader(EdgePushConstant.FROM) String from);

    /**
     * 发送事件消息
     *
     * @param openApiEntityRequest
     * @return true/false
     */
    @PostMapping("/event")
    R sendEventMessage(OpenApiEntity openApiEntityRequest, @RequestHeader(EdgePushConstant.FROM) String from);

    /**
     * 发送操作消息
     *
     * @param openApiEntityRequest
     * @return true/false
     */
    @PostMapping("/operate")
    R sendOperateMessage(OpenApiEntity openApiEntityRequest, @RequestHeader(EdgePushConstant.FROM) String from);

    /**
     * 发送命令消息
     *
     * @param openApiEntityRequest
     * @return true/false
     */
    @PostMapping("/command")
    R sendCommandMessage(OpenApiEntity openApiEntityRequest, @RequestHeader(EdgePushConstant.FROM) String from);

    /**
     * 发送级联消息
     *
     * @param openApiEntityRequest
     * @return true/false
     */
    @PostMapping("/cascade")
    R sendCascadeMessage(OpenApiEntity openApiEntityRequest, @RequestHeader(EdgePushConstant.FROM) String from);

    /**
     * 发送其他消息
     *
     * @param openApiEntityRequest
     * @return true/false
     */
    @PostMapping("/other")
    R sendOtherMessage(OpenApiEntity openApiEntityRequest, @RequestHeader(EdgePushConstant.FROM) String from);
}
