package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.aurine.cloudx.estate.mapper.EdgeCloudRequestMapper;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.EdgeCloudRequestVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>边缘网关入云申请表</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 09:21:16
 */
@Slf4j
@Service
public class EdgeCloudRequestAspectServiceImpl extends ServiceImpl<EdgeCloudRequestMapper, EdgeCloudRequest> implements EdgeCloudRequestAspectService {

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CONFIG, serviceName = OpenApiServiceNameEnum.CASCADE_APPLY, cascadeType = OpenApiCascadeTypeEnum.APPLY)
    public R<EdgeCloudRequestVo> requestIntoCloudSaveOrUpdate(EdgeCloudRequest edgeCloudRequest, String sn) {
        EdgeCloudRequestVo edgeCloudRequestVo = new EdgeCloudRequestVo();
        edgeCloudRequestVo.setEdgeDeviceId(sn);
        BeanUtil.copyProperties(edgeCloudRequest, edgeCloudRequestVo);
        if (StrUtil.isNotEmpty(edgeCloudRequest.getRequestId())) {
            EdgeCloudRequest one = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, edgeCloudRequest.getRequestId()));
            if (one != null) {
                this.updateById(edgeCloudRequest);
                log.info("保存/更新入云申请：{}", edgeCloudRequest);
                return R.ok(edgeCloudRequestVo);
            }
        }
        this.save(edgeCloudRequest);
        log.info("保存/更新入云申请：{}", edgeCloudRequest);
        return R.ok(edgeCloudRequestVo);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE, serviceName = OpenApiServiceNameEnum.CASCADE_APPLY, cascadeType = OpenApiCascadeTypeEnum.REVOKE)
    public R<EdgeCloudRequest> cancelRequestUpdate(EdgeCloudRequest request) {
        this.updateById(request);
        String requestId = request.getRequestId();
        EdgeCloudRequest edgeCloudRequest = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId));
        return R.ok(edgeCloudRequest);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE, serviceName = OpenApiServiceNameEnum.CASCADE_UNBIND, cascadeType = OpenApiCascadeTypeEnum.APPLY)
    public R<EdgeCloudRequest> requestUnbindUpdate(String requestId, IntoCloudStatusEnum unbinding) {
        EdgeCloudRequest request = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId));
        request.setRequestId(requestId);
        request.setCloudStatus(unbinding.code);
        this.updateById(request);
        EdgeCloudRequest edgeCloudRequest = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId));
        return R.ok(edgeCloudRequest);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE, serviceName = OpenApiServiceNameEnum.CASCADE_UNBIND, cascadeType = OpenApiCascadeTypeEnum.REVOKE)
    public R<EdgeCloudRequest> revokeUnbindRequestUpdate(String requestId, IntoCloudStatusEnum intoCloud) {
        EdgeCloudRequest request = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId));
        request.setRequestId(requestId);
        request.setCloudStatus(intoCloud.code);
        this.updateById(request);
        EdgeCloudRequest edgeCloudRequest = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId));
        return R.ok(edgeCloudRequest);
    }
}
