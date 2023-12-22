package com.aurine.cloudx.open.cascade.service.impl;

import com.aurine.cloudx.open.cascade.service.CascadeCloudEdgeRequestService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.CloudEdgeRequest;
import com.aurine.cloudx.open.origin.service.CloudEdgeRequestService;
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
public class CascadeCloudEdgeRequestServiceImpl implements CascadeCloudEdgeRequestService {

    @Resource
    private CloudEdgeRequestService cloudEdgeRequestService;


    @Override
    public R<CloudEdgeRequest> update(CloudEdgeRequest po) {
        boolean result = true;
        CloudEdgeRequest cloudEdgeRequest = cloudEdgeRequestService.getOne(Wrappers.lambdaQuery(CloudEdgeRequest.class)
                .eq(CloudEdgeRequest::getProjectCode, po.getProjectCode())
                .eq(CloudEdgeRequest::getCloudStatus, po.getCloudStatus()));
        if ("1".equals(po.getDelStatus())) {
            cloudEdgeRequest.setDelStatus(po.getDelStatus());
            result = cloudEdgeRequestService.updateById(cloudEdgeRequest);
        }
        if (po.getIsSync().equals('1')) {
            cloudEdgeRequest.setIsSync(po.getIsSync());
            result = cloudEdgeRequestService.updateById(cloudEdgeRequest);
        }
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);
        return R.ok(po);
    }
}
