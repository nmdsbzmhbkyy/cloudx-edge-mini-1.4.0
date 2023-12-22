package com.aurine.cloudx.open.cascade.controller;

import com.aurine.cloudx.open.cascade.service.CascadeCloudEdgeRequestService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.CloudEdgeRequest;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 边缘网关入云申请（云平台）
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@RestController
@RequestMapping("v1/cascade/cloud-edge-request")
@Api(value = "cascadeCloudEdgeRequest", tags = {"v1", "级联入云相关", "边缘网关入云申请（云平台）"})
@Inner
@Slf4j
public class CascadeCloudEdgeRequestController {

    @Resource
    private CascadeCloudEdgeRequestService cascadeCloudEdgeRequestService;


    /**
     * 修改入云申请
     *
     * @param model 入云申请
     * @return R 返回修改后的入云申请
     */
    @AutoInject
    @ApiOperation(value = "修改入云申请", notes = "修改入云申请", hidden = true)
    @SysLog("修改入云申请")
    @PutMapping
    public R<CloudEdgeRequest> update(@RequestBody OpenApiModel<CloudEdgeRequest> model) {
        log.info("[CascadeCloudEdgeRequestController - update]: 修改入云申请, model={}", JSONConvertUtils.objectToString(model));
        return cascadeCloudEdgeRequestService.update(model.getData());
    }
}
