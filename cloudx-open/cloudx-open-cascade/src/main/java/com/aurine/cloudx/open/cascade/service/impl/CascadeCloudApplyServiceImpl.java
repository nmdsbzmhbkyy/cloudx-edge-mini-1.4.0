package com.aurine.cloudx.open.cascade.service.impl;

import com.aurine.cloudx.open.cascade.service.CascadeCloudApplyService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.service.CloudEdgeRequestService;
import com.aurine.cloudx.open.origin.service.EdgeCloudRequestService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 入云申请业务实现
 *
 * @author : Qiu
 * @date : 2021 12 27 15:06
 */

@Service
public class CascadeCloudApplyServiceImpl implements CascadeCloudApplyService {

    @Resource
    private CloudEdgeRequestService cloudEdgeRequestService;

    @Resource
    private EdgeCloudRequestService edgeCloudRequestService;


    @Override
    public R<Boolean> revoke(String projectUUID, String projectCode) {
        Boolean result = cloudEdgeRequestService.cancelRequest(projectUUID, projectCode);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }

    @Override
    public R<Boolean> accept(String projectCode) {
        Boolean result = edgeCloudRequestService.passRequest(projectCode);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }

    @Override
    public R<Boolean> reject(String projectCode) {
        Boolean result = edgeCloudRequestService.rejectRequest(projectCode);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
