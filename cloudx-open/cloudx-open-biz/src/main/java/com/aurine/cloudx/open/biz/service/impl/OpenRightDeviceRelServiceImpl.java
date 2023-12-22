package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenRightDeviceRelService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.RightDeviceRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import com.aurine.cloudx.open.origin.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-权限设备关系管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class OpenRightDeviceRelServiceImpl implements OpenRightDeviceRelService {

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;


    @Override
    public R<RightDeviceRelVo> getById(String id) {
        ProjectRightDevice po = projectRightDeviceService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        RightDeviceRelVo vo = new RightDeviceRelVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<RightDeviceRelVo>> page(Page page, RightDeviceRelVo vo) {
        return R.ok(projectRightDeviceService.page(page, vo));
    }

    @Override
    public R<RightDeviceRelVo> save(RightDeviceRelVo vo) {
//        ProjectRightDevice po = new ProjectRightDevice();
//        BeanUtils.copyProperties(vo, po);
//
//        boolean result = projectRightDeviceService.save(po);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.SYSTEM_ERROR);
//
//        RightDeviceRelVo resultVo = new RightDeviceRelVo();
//        BeanUtils.copyProperties(po, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<RightDeviceRelVo> update(RightDeviceRelVo vo) {
//        ProjectRightDevice po = new ProjectRightDevice();
//        BeanUtils.copyProperties(vo, po);
//
//        boolean result = projectRightDeviceService.updateById(po);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        RightDeviceRelVo resultVo = new RightDeviceRelVo();
//        BeanUtils.copyProperties(po, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
//        boolean result = projectRightDeviceService.removeById(id);
//        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT.getMsg());

        return R.ok();
    }
}
