package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.CascadeStatusConstants;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestSlave;
import com.aurine.cloudx.estate.mapper.EdgeCascadeRequestSlaveMapper;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestMasterService;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestSlaveAspectService;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestSlaveService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.thirdparty.module.edge.cascade.service.EdgeCascadeService;
import com.aurine.cloudx.estate.vo.EdgeCascadeRequestSlaveVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>边缘网关级联申请管理（主）</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 10:03:20
 */
@Slf4j
@Service
public class EdgeCascadeRequestSlaveAspectServiceImpl extends ServiceImpl<EdgeCascadeRequestSlaveMapper, EdgeCascadeRequestSlave> implements EdgeCascadeRequestSlaveAspectService {

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE,serviceName = OpenApiServiceNameEnum.CASCADE_APPLY,cascadeType = OpenApiCascadeTypeEnum.REVOKE)
    public R<EdgeCascadeRequestSlave> revokeRequestUpdate(EdgeCascadeRequestSlave edgeCascadeRequestSlave) {
        this.updateById(edgeCascadeRequestSlave);
        return R.ok(edgeCascadeRequestSlave);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CONFIG, serviceName = OpenApiServiceNameEnum.CASCADE_APPLY, cascadeType = OpenApiCascadeTypeEnum.APPLY)
    public R<EdgeCascadeRequestSlave> requestCascadeSaveOrUpdate(EdgeCascadeRequestSlave requestSlave) {
        if (StrUtil.isNotEmpty(requestSlave.getRequestId())) {
            EdgeCascadeRequestSlave one = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestSlave>().eq(EdgeCascadeRequestSlave::getRequestId, requestSlave.getRequestId()));
            if (one != null) {
                this.updateById(requestSlave);
                return R.ok(requestSlave);
            }
        }
        this.save(requestSlave);
        return R.ok(requestSlave);
    }

    @Override
    @ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.CASCADE,serviceName = OpenApiServiceNameEnum.CASCADE_UNBIND,cascadeType = OpenApiCascadeTypeEnum.APPLY)
    public R<EdgeCascadeRequestSlave> requestUnbindUpdate(EdgeCascadeRequestSlave requestSlave) {
        this.updateById(requestSlave);
        return R.ok(requestSlave);
    }
}
