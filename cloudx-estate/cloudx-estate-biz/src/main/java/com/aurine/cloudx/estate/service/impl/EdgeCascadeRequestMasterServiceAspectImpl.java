package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.mapper.EdgeCascadeRequestMasterMapper;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestMasterAspectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

@Service
public class EdgeCascadeRequestMasterServiceAspectImpl extends ServiceImpl<EdgeCascadeRequestMasterMapper, EdgeCascadeRequestMaster> implements EdgeCascadeRequestMasterAspectService {


    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CONFIG, serviceName = OpenApiServiceNameEnum.CASCADE_APPLY, cascadeType = OpenApiCascadeTypeEnum.APPLY)
    public R<EdgeCascadeRequestMaster> requestCascadeSaveOrUpdate(EdgeCascadeRequestMaster requestMaster) {
        if (StrUtil.isNotEmpty(requestMaster.getRequestId())) {
            EdgeCascadeRequestMaster one = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestMaster>().eq(EdgeCascadeRequestMaster::getRequestId, requestMaster.getRequestId()));
            if (one != null) {
                this.updateById(requestMaster);
                return R.ok(requestMaster);
            }
        }
        this.save(requestMaster);
        return R.ok(requestMaster);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE, serviceName = OpenApiServiceNameEnum.CASCADE_APPLY, cascadeType = OpenApiCascadeTypeEnum.ACCEPT)
    public R<EdgeCascadeRequestMaster> passRequestUpdate(EdgeCascadeRequestMaster requestMaster) {
        this.updateById(requestMaster);
        return R.ok(requestMaster);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE, serviceName = OpenApiServiceNameEnum.CASCADE_APPLY, cascadeType = OpenApiCascadeTypeEnum.REJECT)
    public R<EdgeCascadeRequestMaster> rejectRequestUpdate(EdgeCascadeRequestMaster requestMaster) {
        this.updateById(requestMaster);
        return R.ok(requestMaster);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE, serviceName = OpenApiServiceNameEnum.CASCADE_UNBIND, cascadeType = OpenApiCascadeTypeEnum.ACCEPT)
    public R<EdgeCascadeRequestMaster> removeEdgeUpdate(EdgeCascadeRequestMaster requestMaster) {
        this.updateById(requestMaster);
        return R.ok(requestMaster);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE, serviceName = OpenApiServiceNameEnum.CASCADE_APPLY, cascadeType = OpenApiCascadeTypeEnum.REVOKE)
    public R<EdgeCascadeRequestMaster> cancelRequestUpdate(EdgeCascadeRequestMaster requestMaster) {
        this.updateById(requestMaster);
        return R.ok(requestMaster);
    }
}
