package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenHouseInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.HouseInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectHouseInfo;
import com.aurine.cloudx.open.origin.service.ProjectHouseInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-房屋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenHouseInfoServiceImpl implements OpenHouseInfoService {

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;


    @Override
    public R<HouseInfoVo> getById(String id) {
        ProjectHouseInfo po = projectHouseInfoService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        HouseInfoVo vo = new HouseInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<HouseInfoVo>> page(Page page, HouseInfoVo vo) {
        return R.ok(projectHouseInfoService.page(page, vo));
    }

    @Override
    public R<HouseInfoVo> save(HouseInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<HouseInfoVo> update(HouseInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
