package com.aurine.cloudx.open.cascade.service.impl;

import com.aurine.cloudx.open.cascade.service.CascadeProcessMasterService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeProcessMaster;
import com.aurine.cloudx.open.origin.service.EdgeCascadeProcessMasterService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 级联入云对接进度
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@Service
public class CascadeProcessMasterServiceImp implements CascadeProcessMasterService {

    @Resource
    private EdgeCascadeProcessMasterService edgeCascadeProcessMasterService;


    @Override
    public R<EdgeCascadeProcessMaster> save(EdgeCascadeProcessMaster po) {
        boolean result = edgeCascadeProcessMasterService.save(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<EdgeCascadeProcessMaster> update(EdgeCascadeProcessMaster po) {
        boolean result = edgeCascadeProcessMasterService.updateById(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }
}
