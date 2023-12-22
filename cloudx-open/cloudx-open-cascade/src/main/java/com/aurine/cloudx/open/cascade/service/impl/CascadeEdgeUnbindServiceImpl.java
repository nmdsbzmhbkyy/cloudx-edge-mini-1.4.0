package com.aurine.cloudx.open.cascade.service.impl;

import com.aurine.cloudx.open.cascade.service.CascadeEdgeUnbindService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.service.EdgeCascadeRequestMasterService;
import com.aurine.cloudx.open.origin.service.EdgeCascadeRequestSlaveService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 级联申请业务实现
 *
 * @author : Qiu
 * @date : 2021 12 27 15:06
 */

@Service
public class CascadeEdgeUnbindServiceImpl implements CascadeEdgeUnbindService {

    @Resource
    private EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;

    @Resource
    private EdgeCascadeRequestSlaveService edgeCascadeRequestSlaveService;


    @Override
    public R<Boolean> apply(String projectCode) {
//        boolean result = edgeCascadeRequestMasterService.removeEdge(projectCode);
//        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }

    @Override
    public R<Boolean> revoke(String projectCode) {
        return null;
    }

    @Override
    public R<Boolean> accept(String projectCode){
        boolean result = edgeCascadeRequestSlaveService.removeSlave(projectCode);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }

    @Override
    public R<Boolean> reject(String projectCode){
        return null;
    }
}
