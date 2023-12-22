package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenHousePersonInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.HousePersonInfoVo;
import com.aurine.cloudx.open.origin.constant.enums.DataOriginEnum;
import com.aurine.cloudx.open.origin.constant.enums.DataOriginExEnum;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonRel;
import com.aurine.cloudx.open.origin.service.ProjectHousePersonRelService;
import com.aurine.cloudx.open.origin.vo.ProjectHousePersonRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * open平台-人员信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class OpenHousePersonInfoServiceImpl implements OpenHousePersonInfoService {

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;


    @Override
    public R<HousePersonInfoVo> getById(String id) {
        ProjectHousePersonRel po = projectHousePersonRelService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        HousePersonInfoVo vo = new HousePersonInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<HousePersonInfoVo>> page(Page page, HousePersonInfoVo vo) {
        return R.ok(projectHousePersonRelService.page(page, vo));
    }

    @Override
    public R<HousePersonInfoVo> save(HousePersonInfoVo vo) {
//        if (vo.getRentStopTime() != null) {
//            LocalDateTime time = vo.getRentStopTime();
//            vo.setRentStopTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
//        }
//        vo.setOrigin(DataOriginEnum.WEB.code);
//        vo.setOriginEx(DataOriginExEnum.WY.code);
//        vo.setCheckInTime(LocalDateTime.now());
//
//        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();
//        BeanUtils.copyProperties(vo, projectHousePersonRelVo);
//
//        ProjectHousePersonRel resultPo = projectHousePersonRelService.saveRel(projectHousePersonRelVo);
//        if (resultPo == null) return Result.fail(vo, CloudxOpenErrorEnum.SYSTEM_ERROR);
//
//        HousePersonInfoVo resultVo = new HousePersonInfoVo();
//        BeanUtils.copyProperties(resultPo, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<HousePersonInfoVo> update(HousePersonInfoVo vo) {
//        if (vo.getRentStopTime() != null) {
//            LocalDateTime time = vo.getRentStopTime();
//            vo.setRentStopTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
//        }
//
//        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();
//        BeanUtils.copyProperties(vo, projectHousePersonRelVo);
//
//        boolean result = projectHousePersonRelService.updateById(projectHousePersonRelVo);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        HousePersonInfoVo resultVo = new HousePersonInfoVo();
//        BeanUtils.copyProperties(projectHousePersonRelVo, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
//        boolean result = projectHousePersonRelService.removeById(id);
//        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        return R.ok(true);

        return R.ok();
    }
}
