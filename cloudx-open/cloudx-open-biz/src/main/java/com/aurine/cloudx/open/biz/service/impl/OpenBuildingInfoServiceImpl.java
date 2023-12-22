package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenBuildingInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.BuildingInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectBuildingInfo;
import com.aurine.cloudx.open.origin.service.ProjectBuildingInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-楼栋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenBuildingInfoServiceImpl implements OpenBuildingInfoService {

    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;


    @Override
    public R<BuildingInfoVo> getById(String id) {
        ProjectBuildingInfo po = projectBuildingInfoService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        BuildingInfoVo vo = new BuildingInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<BuildingInfoVo>> page(Page page, BuildingInfoVo vo) {
        return R.ok(projectBuildingInfoService.page(page, vo));
    }

    @Override
    public R<BuildingInfoVo> save(BuildingInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<BuildingInfoVo> update(BuildingInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
