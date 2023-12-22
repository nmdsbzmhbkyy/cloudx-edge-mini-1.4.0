package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenHouseDesignService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
import com.aurine.cloudx.open.origin.entity.ProjectHouseDesign;
import com.aurine.cloudx.open.origin.service.ProjectHouseBatchAddTemplateService;
import com.aurine.cloudx.open.origin.service.ProjectHouseDesignService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-户型管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class OpenHouseDesignServiceImpl implements OpenHouseDesignService {

    @Resource
    private ProjectHouseDesignService projectHouseDesignService;


    @Override
    public R<HouseDesignVo> getById(String id) {
        ProjectHouseDesign po = projectHouseDesignService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        HouseDesignVo vo = new HouseDesignVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<HouseDesignVo>> page(Page page, HouseDesignVo vo) {
        return R.ok(projectHouseDesignService.page(page, vo));
    }

    @Override
    public R<HouseDesignVo> save(HouseDesignVo vo) {
//        ProjectHouseDesign po = new ProjectHouseDesign();
//        BeanUtils.copyProperties(vo, po);
//
//        int disCount = projectHouseDesignService.count(new QueryWrapper<ProjectHouseDesign>().lambda()
//                .eq(ProjectHouseDesign::getProjectId, po.getProjectId())
//                .eq(ProjectHouseDesign::getDesginDesc, po.getDesginDesc())
//                .eq(ProjectHouseDesign::getArea, po.getArea())
//        );
//        if (disCount >= 1) {
//            return Result.fail(vo, CloudxOpenErrorEnum.SERVICE_ERROR.getCode(), "已存在该户型，请重新调整户型或户型面积");
//        }
//
//        boolean result = projectHouseDesignService.add(po);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.SYSTEM_ERROR);
//
//        HouseDesignVo resultVo = new HouseDesignVo();
//        BeanUtils.copyProperties(po, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<HouseDesignVo> update(HouseDesignVo vo) {
//        ProjectHouseDesign po = new ProjectHouseDesign();
//        BeanUtils.copyProperties(vo, po);
//
//        boolean result = projectHouseDesignService.update(po);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        HouseDesignVo resultVo = new HouseDesignVo();
//        BeanUtils.copyProperties(po, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
//        boolean isUsing = projectHouseBatchAddTemplateService.checkIsUsing(id);
//        if (isUsing) return Result.fail(false, CloudxOpenErrorEnum.SERVICE_ERROR.getCode(), "该户型正在被模板使用中无法删除");
//
//        boolean result = projectHouseDesignService.delete(id);
//        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        return R.ok(true);

        return R.ok();
    }
}
