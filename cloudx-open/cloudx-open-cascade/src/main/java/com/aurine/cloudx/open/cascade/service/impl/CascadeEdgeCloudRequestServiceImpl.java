package com.aurine.cloudx.open.cascade.service.impl;

import com.aurine.cloudx.open.cascade.service.CascadeEdgeCloudRequestService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.EdgeCloudRequest;
import com.aurine.cloudx.open.origin.service.EdgeCloudRequestService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 边缘网关入云申请
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@Service
public class CascadeEdgeCloudRequestServiceImpl implements CascadeEdgeCloudRequestService {

    @Resource
    private EdgeCloudRequestService edgeCloudRequestService;


    @Override
    public R<EdgeCloudRequest> update(EdgeCloudRequest po) {
        boolean result = true;
        EdgeCloudRequest edgeCloudRequest = edgeCloudRequestService.getOne(Wrappers.lambdaQuery(EdgeCloudRequest.class)
                .eq(EdgeCloudRequest::getProjectCode, po.getProjectCode())
                .eq(EdgeCloudRequest::getCloudStatus,po.getCloudStatus()));
        if (po.getDelStatus().equals('1')) {
            edgeCloudRequest.setDelStatus(po.getDelStatus());
            result = edgeCloudRequestService.updateById(edgeCloudRequest);
        }
        if (po.getIsSync().equals('1')) {
            edgeCloudRequest.setIsSync(po.getIsSync());
            result = edgeCloudRequestService.updateById(edgeCloudRequest);
        }
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);
        return R.ok(po);
    }
}
