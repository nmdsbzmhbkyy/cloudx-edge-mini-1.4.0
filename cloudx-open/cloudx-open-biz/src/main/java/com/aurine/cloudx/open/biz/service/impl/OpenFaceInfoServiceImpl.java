package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenFaceInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.FaceInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectFaceResources;
import com.aurine.cloudx.open.origin.service.ProjectFaceResourcesService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人脸信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenFaceInfoServiceImpl implements OpenFaceInfoService {

    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;


    @Override
    public R<FaceInfoVo> getById(String id) {
        ProjectFaceResources po = projectFaceResourcesService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        FaceInfoVo vo = new FaceInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<FaceInfoVo>> page(Page page, FaceInfoVo vo) {
        return R.ok(projectFaceResourcesService.page(page, vo));
    }

    @Override
    public R<FaceInfoVo> save(FaceInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<FaceInfoVo> update(FaceInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
